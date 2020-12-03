package com.getrentbd.getrent.activitys;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.getrentbd.getrent.R;
import com.getrentbd.getrent.adapter.SearchResultAdapter;
import com.getrentbd.getrent.modelClass.FilterSearchModelClass;
import com.getrentbd.getrent.modelClass.RentItemsList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.getrentbd.getrent.helper.Common.AppPra;
import static com.getrentbd.getrent.helper.Common.SpDeletePostId;
import static com.getrentbd.getrent.helper.Common.SpEmail;
import static com.getrentbd.getrent.helper.Common.SpGender;
import static com.getrentbd.getrent.helper.Common.SpName;
import static com.getrentbd.getrent.helper.Common.SpNumber;
import static com.getrentbd.getrent.helper.Common.SpPass;
import static com.getrentbd.getrent.helper.Common.SpUpdatePostId;
import static com.getrentbd.getrent.helper.Common.spList;

public class FilterSearchResult extends AppCompatActivity {
    private ArrayList<RentItemsList> dataItem = new ArrayList<>();
    private ArrayList<FilterSearchModelClass> fsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tvNoSearchResult;
    private SearchResultAdapter myAdapter;
    private String location, month, catagory, gender, rentFrom, rentTo, seat;
    private DrawerLayout drawerLayout;
    private ImageView navigationimage;
    private ActionBarDrawerToggle mToggle;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView tvFeedback, tvLogOut, tvName, tvEmail;
    private LinearLayout layoutProfile, layoutOwnPost, layoutCreatepost, layoutFilterSearch, layoutFavorites;
    private TextView tvTitle;
    private int rentFromValue, rentToValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        idFinder();
        drawerOperation();
        bottomNavOperation();
        //Set Value
        tvTitle.setText("Search Result");

        //Getting Value From ArrayList
        dataItem = spList;

        location = getIntent().getStringExtra("Location");
        month = getIntent().getStringExtra("Month");
        catagory = getIntent().getStringExtra("Catagory");
        gender = getIntent().getStringExtra("Gender");
        rentFrom = getIntent().getStringExtra("RentFrom");
        rentTo = getIntent().getStringExtra("RentTo");
        seat = getIntent().getStringExtra("Seat");

        if (rentFrom.isEmpty() && rentTo.isEmpty()) {
            rentFromValue = minOfNumList();
            rentToValue = maxOfNumList();
        } else if (!rentFrom.isEmpty() && rentTo.isEmpty()) {
            rentFromValue = Integer.parseInt(rentFrom);
            rentToValue = maxOfNumList();
        } else if (rentFrom.isEmpty() && !rentTo.isEmpty()) {
            rentFromValue = minOfNumList();
            rentToValue = Integer.parseInt(rentTo);
        } else {
            rentFromValue = Integer.parseInt(rentFrom);
            rentToValue = Integer.parseInt(rentTo);
        }

        for (RentItemsList itemsList : dataItem) {
            if (gender.isEmpty() || gender.toLowerCase().contains(itemsList.getGender().toLowerCase())) {

                if (month.toLowerCase().contains(itemsList.getMonth().toLowerCase())) {

                    if (catagory.toLowerCase().contains(itemsList.getCatagory().toLowerCase())) {

                        if (location.isEmpty() || location.toLowerCase().contains(itemsList.getLocation().toLowerCase())) {

                            if (Integer.parseInt(itemsList.getRent()) >= rentFromValue && Integer.parseInt(itemsList.getRent()) < rentToValue) {

                                if (seat.isEmpty() || seat.contains(itemsList.getSeat())) {

                                    final String p_id = itemsList.getId();
                                    final String seat = itemsList.getSeat();
                                    final String floor_no = itemsList.getNum_floor();
                                    final String location = itemsList.getLocation();
                                    final String from = itemsList.getMonth();
                                    final String rent = itemsList.getRent();
                                    final String category = itemsList.getCatagory();
                                    final String timeDate = itemsList.getTimeDate();
                                    final String url = itemsList.getUrl();
                                    final String address = itemsList.getAddress();
                                    final String facilities = itemsList.getDescription();
                                    final String name = itemsList.getName();
                                    final String phn_num = itemsList.getNumber();
                                    final String email = itemsList.getEmail();
                                    final String gender = itemsList.getGender();

                                    fsList.add(new FilterSearchModelClass(p_id, phn_num, seat, floor_no, location, from
                                            , rent, category, timeDate, url, gender, address, facilities, name, email));

                                }
                            }
                        }
                    }
                }
            }
        }

        if (fsList.size() == 0) {
            tvNoSearchResult.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // Filter data add in array list
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            myAdapter = new SearchResultAdapter(this, fsList);
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            tvNoSearchResult.setVisibility(View.GONE);
        }

    }

    public int maxOfNumList() {
        List<Integer> list = new ArrayList();
        for (RentItemsList data : dataItem) {
            list.add(Integer.parseInt(data.getRent()));
        }
        return Collections.max(list);
    }

    public int minOfNumList() {

        List<Integer> list = new ArrayList();
        for (RentItemsList data : dataItem) {
            list.add(Integer.parseInt(data.getRent()));
        }
        return Collections.min(list);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivHome.setImageResource(R.drawable.button_huver_home);
                    Intent intent = new Intent(FilterSearchResult.this, HomePage.class);
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
                    Intent intent = new Intent(FilterSearchResult.this, ProfileActivity.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
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
                    Intent intent = new Intent(FilterSearchResult.this, OwnPost.class);
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
        mToggle.syncState();

        navigationimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                SharedPreferences sharedPreferences = getSharedPreferences(AppPra, Context.MODE_PRIVATE);
                String Number = sharedPreferences.getString(SpNumber, "");
                String Name = sharedPreferences.getString(SpName, "");
                String Email = sharedPreferences.getString(SpEmail, "");
                String Gender = sharedPreferences.getString(SpGender, "");

                tvName.setText(Name);
                tvEmail.setText(Email);

            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearchResult.this, ProfileActivity.class);

                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutOwnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearchResult.this, OwnPost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearchResult.this, FavoriteList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearchResult.this, FilterSearch.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearchResult.this, FeedBackActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearchResult.this, LogIn.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                SharedPreferences sharedPreferences = getSharedPreferences(AppPra, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SpNumber, "");
                editor.putString(SpPass, "");
                editor.apply();
                startActivity(intent);
                finish();
            }
        });
        layoutCreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearchResult.this, CreatePost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
    }

    private void idFinder() {
        recyclerView = findViewById(R.id.rvFilterResultId);
        tvNoSearchResult = findViewById(R.id.tvNoSearchResult);
        tvTitle = findViewById(R.id.tvTitleId);
        drawerLayout = findViewById(R.id.DrawerFilterResultId);
        navigationimage = findViewById(R.id.ivToolBarIcon);
        tvName = findViewById(R.id.tvNavFilterResultNameId);
        tvEmail = findViewById(R.id.tvNavFilterResultEmailId);
        tvFeedback = findViewById(R.id.tvNavFilterResultfeedbackid);
        tvLogOut = findViewById(R.id.tvNavFilterResultLogOutId);
        ivProfile = findViewById(R.id.ivprofileid);
        ivPost = findViewById(R.id.icpostid);
        ivHome = findViewById(R.id.ivHomeId);

        layoutProfile = findViewById(R.id.navProfile);
        layoutOwnPost = findViewById(R.id.navOwnPost);
        layoutCreatepost = findViewById(R.id.navCreatePost);
        layoutFilterSearch = findViewById(R.id.navFilterSearch);
        layoutFavorites = findViewById(R.id.navFavorites);
    }


    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(FilterSearchResult.this, FilterSearch.class));
            finish();
            super.onBackPressed();
        }
    }

}
