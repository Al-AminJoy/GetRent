package com.getrentbd.getrent.activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getrentbd.getrent.helper.Apis;
import com.getrentbd.getrent.helper.Common;
import com.getrentbd.getrent.adapter.Mini_Ad_Adapter;
import com.getrentbd.getrent.modelClass.OwnPostList;
import com.getrentbd.getrent.modelClass.RentItemsList;
import com.getrentbd.getrent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spId;
import static com.getrentbd.getrent.helper.Common.spName;
import static com.getrentbd.getrent.helper.Common.spNumber;


public class HomePage extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView navigationimage;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView tvFeedback, tvLogOut,  tvName, tvEmail;
    private LinearLayout layoutProfile,layoutOwnPost,layoutCreatepost,layoutFilterSearch,layoutFavorites;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<RentItemsList> dataItem = new ArrayList<>();
    private ArrayList<OwnPostList> ownItems = new ArrayList<>();
    private RecyclerView rvminiad;
    private RequestQueue requestQueue;
    private Mini_Ad_Adapter mini_ad_adapter;
    private ProgressDialog progressDialog;
    private EditText etSearchbar;
    private CircleImageView ivProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        idfinder();

        requestQueue = Volley.newRequestQueue(this);

        //Recycler View
        rvminiad.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        rvminiad.setLayoutManager(layoutManager);
        mini_ad_adapter = new Mini_Ad_Adapter(HomePage.this, dataItem);
        rvminiad.setAdapter(mini_ad_adapter);

        //Progress Dialogue
        progressDialog = new ProgressDialog(this, R.style.ProgressColor);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        ivHome.setImageResource(R.drawable.ic_home);
        ivHome.setPadding(0,0,0,0);

        drawerOperation();
        bottomNavOperation();

        // SharedPreferences value get and set in common class in this method
        getSharedPrefValue();

        loaddata();
        //Search Bar Operation
        etSearchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        //Refresh Operation
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        loaddata();
                    }
                }, 2000);

            }
        });
    }

    private void drawerOperation() {
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        navigationimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                tvName.setText(spName);
                tvEmail.setText(spEmail);
                if (spGender.toLowerCase().equals("male")){
                    ivProfileImage.setImageResource(R.drawable.profile);
                }else {
                    ivProfileImage.setImageResource(R.drawable.femalepng);
                }
            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ProfileActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutOwnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, OwnPost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutCreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, CreatePost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });

        layoutFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, FilterSearch.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, FavoriteList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });

        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, FeedBackActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, LogIn.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Common.SpNumber, "");
                editor.putString(Common.SpPass, "");
                editor.apply();

                startActivity(intent);
                finish();
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ivHome.setImageResource(R.drawable.button_huver_home);

                    //Button Pressed
                }else {
                    //finger was lifted
                    ivHome.setImageResource(R.drawable.ic_home);
                }
                return true;
            }
        });

        ivProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ivProfile.setImageResource(R.drawable.button_huver_profile);
                    Intent intent = new Intent(HomePage.this, ProfileActivity.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    finish();
                    //Button Pressed
                }else {
                    //finger was lifted
                    ivProfile.setImageResource(R.drawable.ic_profile);
                }
                return true;
            }
        });

        ivPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ivPost.setImageResource(R.drawable.button_huver_post);
                    Intent intent = new Intent(HomePage.this, OwnPost.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    finish();
                    //Button Pressed
                }else {
                    //finger was lifted
                    ivPost.setImageResource(R.drawable.my_post);
                }
                return true;
            }
        });
    }

    private void getSharedPrefValue() {
        SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
        spId = sharedPreferences.getString(Common.SpId, "");
        spName = sharedPreferences.getString(Common.SpName, "");
        spEmail = sharedPreferences.getString(Common.SpEmail, "");
        spNumber = sharedPreferences.getString(Common.SpNumber, "");
        spGender = sharedPreferences.getString(Common.SpGender, "");
    }


    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
            alertbuilder.setTitle("Exit !");
            alertbuilder.setIcon(R.drawable.ic_exit);
            alertbuilder.setMessage("Are You Sure Want To Exit ?");
            alertbuilder.setCancelable(false);
            alertbuilder.setPositiveButton(Html.fromHtml("<font color='#413752'>Yes</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });
            alertbuilder.setNegativeButton(Html.fromHtml("<font color='#413752'>No</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alertDialog = alertbuilder.create();
            alertDialog.show();
        }
    }

    private void filter(String toString) {
        try {
            ArrayList<RentItemsList> searchList = new ArrayList<>();
            for (RentItemsList item : dataItem) {
                if (item.getLocation().toLowerCase().contains(toString.toLowerCase())) {
                    searchList.add(item);
                }
            }
            mini_ad_adapter.filterList(searchList);
        } catch (Exception e) {

        }

    }

    private void idfinder() {
        navigationView = findViewById(R.id.navigid);

        navigationimage = findViewById(R.id.ivtoolbarimgid);
        tvFeedback = findViewById(R.id.tvNavHomefeedbackid);
        tvLogOut = findViewById(R.id.tvNavHomeLogOutId);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvNavHomeNameId);
        tvEmail = findViewById(R.id.tvNavHomeEmailId);
        drawerLayout = findViewById(R.id.drawerid);
        ivProfile = findViewById(R.id.ivprofileid);
        ivPost = findViewById(R.id.icpostid);
        ivHome = findViewById(R.id.ivHomeId);
        rvminiad = findViewById(R.id.rvhomeminidataid);
        etSearchbar = findViewById(R.id.etsearchbar);
        swipeRefreshLayout = findViewById(R.id.srefHomeId);

        layoutProfile = findViewById(R.id.navProfile);
        layoutOwnPost = findViewById(R.id.navOwnPost);
        layoutCreatepost = findViewById(R.id.navCreatePost);
        layoutFilterSearch = findViewById(R.id.navFilterSearch);
        layoutFavorites = findViewById(R.id.navFavorites);

    }


    private void loaddata() {
        progressDialog.show();
        dataItem.clear();
        ownItems.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Apis.GetPostData + spNumber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String floor_num;
                    JSONObject jsonObject = new JSONObject(response);
                    String st = jsonObject.getString("staus");
                    if (st.equals("1")) {
                        Toast.makeText(HomePage.this, "You are Blocked, Please contact admin.", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("itemsList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject receive = jsonArray.getJSONObject(i);
                            String ID = receive.getString("id");
                            String TotalSeat = receive.getString("number_sit");
                            if ("null".equals(receive.getString("floor_num"))){
                                floor_num = "N/A";
                            }else {
                                floor_num = receive.getString("floor_num");
                            }
                            String HouseLocation = receive.getString("location");
                            String MonthFrom = receive.getString("month_name");
                            String HouseRent = receive.getString("house_rent");
                            String Catagory = receive.getString("catagory");
                            String timedate = receive.getString("timedate");
                            String url = receive.getString("url");
                            String HouseAddress = receive.getString("address");
                            String HouseDescription = receive.getString("description");
                            JSONArray userdetails = receive.getJSONArray("user_info");

                            for (int j = 0; j < userdetails.length(); j++) {
                                JSONObject object = userdetails.getJSONObject(j);

                                String MobileNumber = object.getString("mobile_number");
                                String ClientName = object.getString("name");
                                String Email = object.getString("email");
                                String Gender = object.getString("gender");
                                RentItemsList data = new RentItemsList(ID, MobileNumber, TotalSeat, floor_num, HouseLocation, MonthFrom, HouseRent, Catagory, timedate, url, Gender, HouseAddress, HouseDescription, ClientName, Email);

                                if (spNumber.equals(MobileNumber)) {
                                    ownItems.add(new OwnPostList(ID, MobileNumber, TotalSeat, floor_num, HouseLocation, MonthFrom, HouseRent, Catagory, timedate, url, Gender, HouseAddress, HouseDescription, ClientName, Email));
                                }
                                dataItem.add(data);

                            }
                        }
                        mini_ad_adapter.notifyDataSetChanged();
                        Common.ownLists = ownItems;
                        Common.spList = dataItem;

                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }


}
