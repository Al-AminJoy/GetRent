package com.getrentbd.getrent.activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.getrentbd.getrent.R;
import com.getrentbd.getrent.modelClass.OwnPostList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Apis.baseurl;
import static com.getrentbd.getrent.helper.Common.ownLists;
import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spName;

public class MyPostDetails extends AppCompatActivity {
    private TextView name, catagory, tvFloor, seat, from, location, rent, details, number, address, email, gender;
    private String position;
    private String Id, Name, Catagory, Floor_no, Seat, From, Location,
            Rent,TimeDate,Url, Details, Number, Address, Email, Gender;
    private RequestQueue requestQueue;
    private ProgressBar pbDelete;
    private TextView tvTitle;
    private DrawerLayout drawerLayout;
    private ImageView navigationimage;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView  tvFeedback, tvLogOut, tvName, tvEmail, tvRentTitle;
    private LinearLayout layoutProfile,layoutOwnPost,layoutCreatepost,layoutFilterSearch,layoutFavorites;
    private Button btUpdate, btDelete;
    private LinearLayout layoutFacilities;
    private CircleImageView ivProfileImage;
    private LinearLayout layoutViewImage;
    private ImageView ivImage;
    private ArrayList<OwnPostList> ownItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_details);
        idFinder();
        drawerOperation();
        bottomNavOperation();
        takeIntentValue();
        //Request Queue
        requestQueue = Volley.newRequestQueue(this);

        ownItems = ownLists;

        //Applying Conditions on Seat
        if (Seat.equals("1")) {
            tvRentTitle.setText("Rent");
            seat.setText(Seat + " " + Catagory);
        } else {
            if (Catagory.equals("Mess") || Catagory.equals("Hostel")) {
                seat.setText(Seat + " Seat's");
                tvRentTitle.setText("Rent Per Seat");
            } else {
                tvRentTitle.setText("Rent Per " + Catagory);
                seat.setText(Seat + " " + Catagory + "'s");
            }
        }

        if (!Url.equals("null")){
            Picasso.get().load(baseurl+Url).fit().placeholder(R.drawable.upload_image).into(ivImage);
        }else {
            layoutViewImage.setVisibility(View.GONE);
        }


        setTextOperations();

        // Intent Operation
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyPostDetails.this, PostUpdate.class);
                intent.putExtra("Position", position);
                intent.putExtra("Id", Id);
                intent.putExtra("Seat", Seat);
                intent.putExtra("Floor_no",Floor_no );
                intent.putExtra("Location", Location);
                intent.putExtra("From", From);
                intent.putExtra("Rent", Rent);
                intent.putExtra("Catagory", Catagory);
                intent.putExtra("TimeDate", TimeDate);
                intent.putExtra("Url", Url);
                intent.putExtra("Address", Address);
                intent.putExtra("Description", Details);
                startActivity(intent);
                finish();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btDelete.setVisibility(View.GONE);
                btUpdate.setVisibility(View.GONE);
                DeletePost(Id);
            }
        });

    }

    private void setTextOperations() {
        tvTitle.setText("Details");
        catagory.setText("About The " + Catagory);
        tvFloor.setText(Floor_no);
        from.setText(From);
        rent.setText(Rent);
        location.setText(Location);
        address.setText(Address);
        //Applying Conditions on Details
        if (!Details.isEmpty()) {
            details.setText(Details);
            layoutFacilities.setVisibility(View.VISIBLE);
        }
        name.setText(Name);
        number.setText(Number);
        email.setText(Email);
        gender.setText(Gender);
    }

    private void takeIntentValue() {
        position = getIntent().getStringExtra("Position");
        Id = getIntent().getStringExtra("Id");
        Seat = getIntent().getStringExtra("Seat");
        Floor_no = getIntent().getStringExtra("Floor_no");
        Catagory = getIntent().getStringExtra("Catagory");
        From = getIntent().getStringExtra("From");
        Location = getIntent().getStringExtra("Location");
        Rent = getIntent().getStringExtra("Rent");
        TimeDate = getIntent().getStringExtra("TimeDate");
        Url = getIntent().getStringExtra("Url");
        Details = getIntent().getStringExtra("Description");
        Address = getIntent().getStringExtra("Address");
        Name = getIntent().getStringExtra("Name");
        Number = getIntent().getStringExtra("Number");
        Email = getIntent().getStringExtra("Email");
        Gender = getIntent().getStringExtra("Gender");
    }


    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivHome.setImageResource(R.drawable.button_huver_home);
                    Intent intent = new Intent(MyPostDetails.this, HomePage.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    finish();

                } else {
                    //finger was lifted
                    ivHome.setImageResource(R.drawable.ic_home);
                }
                return true;
            }
        });

        ivProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivProfile.setImageResource(R.drawable.button_huver_profile);
                    Intent intent = new Intent(MyPostDetails.this, ProfileActivity.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    finish();
                } else {
                    //finger was lifted
                    ivProfile.setImageResource(R.drawable.ic_profile);
                }
                return true;
            }
        });

        ivPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivPost.setImageResource(R.drawable.button_huver_post);
                    Intent intent = new Intent(MyPostDetails.this, OwnPost.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    finish();

                } else {
                    //finger was lifted
                    ivPost.setImageResource(R.drawable.my_post);
                }
                return true;
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
                Intent intent = new Intent(MyPostDetails.this, ProfileActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });

        layoutOwnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetails.this, OwnPost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutCreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetails.this, CreatePost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetails.this, FavoriteList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetails.this, FilterSearch.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetails.this, FeedBackActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetails.this, LogIn.class);
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

    private void idFinder() {
        // details show ids

        layoutViewImage = findViewById(R.id.layoutViewImage);
        ivImage = findViewById(R.id.ivImage);
        catagory = findViewById(R.id.tvmydetailscatagoryid);
        tvFloor = findViewById(R.id.tvMyPostDetailsFloorId);
        seat = findViewById(R.id.tvmydetailsseatid);
        from = findViewById(R.id.tvmydetailsmonthid);
        tvRentTitle = findViewById(R.id.tvDetailsRentTitleId);
        rent = findViewById(R.id.tvmydetailsrentid);
        location = findViewById(R.id.tvmydetailslocationid);
        address = findViewById(R.id.tvmydetailsaddressid);
        layoutFacilities = findViewById(R.id.linearDesp);
        details = findViewById(R.id.tvmydetailshomedetailsid);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        name = findViewById(R.id.tvmydetailsnameid);
        email = findViewById(R.id.tvmydetailsemailid);
        number = findViewById(R.id.tvmydetailsnumberid);
        gender = findViewById(R.id.tvmydetailsGenderid);
        btUpdate = findViewById(R.id.btMyPostUpdate);
        btDelete = findViewById(R.id.btMyPostDelete);
        pbDelete = findViewById(R.id.pbDeleteId);

        // toolbar ids
        tvTitle = findViewById(R.id.tvTitleId);

        // navigation Drawer ids
        drawerLayout = findViewById(R.id.DrawerMyPostDetailsId);
        navigationimage = findViewById(R.id.ivToolBarIcon);
        tvName = findViewById(R.id.tvNavMyPostDetailsNameId);
        tvEmail = findViewById(R.id.tvNavMyPostDetailsEmailId);
        tvFeedback = findViewById(R.id.tvNavMyPostDetailsfeedbackid);
        tvLogOut = findViewById(R.id.tvNavMyPostDetailsLogOutId);

        layoutProfile = findViewById(R.id.navProfile);
        layoutOwnPost = findViewById(R.id.navOwnPost);
        layoutCreatepost = findViewById(R.id.navCreatePost);
        layoutFilterSearch = findViewById(R.id.navFilterSearch);
        layoutFavorites = findViewById(R.id.navFavorites);

        // bottom navigation ids
        ivHome = findViewById(R.id.ivHomeId);
        ivProfile = findViewById(R.id.ivprofileid);
        ivPost = findViewById(R.id.icpostid);

    }

    private void DeletePost(final String id) {

        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Delete !");
        alertbuilder.setIcon(R.drawable.ic_delete);
        alertbuilder.setMessage("Are You Sure Want To Delete?");
        alertbuilder.setCancelable(false);
        alertbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pbDelete.setVisibility(View.VISIBLE);
                String url = Apis.PostDelete + "?id=" + id;
                requestQueue.getCache().clear();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (response.equals("Delete Successfully")) {
                                    Intent intent = new Intent(MyPostDetails.this, OwnPost.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Common.SpDeletePostId, id);
                                    editor.apply();
                                    ownItems.remove(Integer.parseInt(position));
                                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }

                            }


                        }, 5000);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volly_Error", error.getMessage());
                    }
                });
                requestQueue.add(stringRequest);

            }
        });
        alertbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btDelete.setVisibility(View.VISIBLE);
                btUpdate.setVisibility(View.VISIBLE);
                pbDelete.setVisibility(View.GONE);
            }
        });
        AlertDialog alertDialog = alertbuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(MyPostDetails.this, OwnPost.class));
            finish();
            super.onBackPressed();
        }
    }
}
