package com.getrentbd.getrent.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getrentbd.getrent.helper.Common;
import com.getrentbd.getrent.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spName;

public class ProfileActivity extends AppCompatActivity {
    private String Name, Number, Email, Gender;
    private Button btEdite;
    private TextView tvTitle;
    private TextView name, email, number, gender;
    private DrawerLayout drawerLayout;
    private ImageView navigationimage;
    private ActionBarDrawerToggle mToggle;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView  tvFeedback, tvLogOut, tvName, tvEmail;
    private LinearLayout layoutProfile,layoutOwnPost,layoutCreatepost,layoutFilterSearch,layoutFavorites;
    private CircleImageView ivProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        idFinder();
        drawerOperation();
        bottomNavOperation();
        takeSharedPrefValue();
        //Set Value
        tvTitle.setText("Profile");
        name.setText(Name);
        email.setText(Email);
        number.setText(Number);
        gender.setText(Gender);
        //Intent Operation
        btEdite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileUpdate.class);
                startActivity(intent);
            }
        });

        ivProfile.setImageResource(R.drawable.ic_select_profile);
        ivProfile.setPadding(0,0,0,0);

    }

    private void takeSharedPrefValue() {
        SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
        Number = sharedPreferences.getString(Common.SpNumber, "");
        Name = sharedPreferences.getString(Common.SpName, "");
        Email = sharedPreferences.getString(Common.SpEmail, "");
        Gender = sharedPreferences.getString(Common.SpGender, "");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivHome.setImageResource(R.drawable.button_huver_home);
                    Intent intent = new Intent(ProfileActivity.this, HomePage.class);
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
                    Intent intent = new Intent(ProfileActivity.this, OwnPost.class);
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
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);

                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });

        layoutOwnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, OwnPost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FavoriteList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FilterSearch.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FeedBackActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LogIn.class);
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
        layoutCreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CreatePost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
    }

    private void idFinder() {
        name = findViewById(R.id.tvprofilenameid);
        email = findViewById(R.id.tvprofileemailid);
        number = findViewById(R.id.tvprofilenumberid);
        gender = findViewById(R.id.tvprofilegenderid);
        btEdite = findViewById(R.id.btProfileEditeId);
        tvTitle = findViewById(R.id.tvTitleId);
        drawerLayout = findViewById(R.id.DrawerProfileId);
        navigationimage = findViewById(R.id.ivToolBarIcon);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvNavProfileNameId);
        tvEmail = findViewById(R.id.tvNavProfileEmailId);
        tvFeedback = findViewById(R.id.tvNavProfilefeedbackid);
        tvLogOut = findViewById(R.id.tvNavProfileLogOutId);
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
            startActivity(new Intent(ProfileActivity.this, HomePage.class));
            finish();
            super.onBackPressed();
        }
    }

}
