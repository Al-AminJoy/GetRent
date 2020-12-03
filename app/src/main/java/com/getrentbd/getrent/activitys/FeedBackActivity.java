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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getrentbd.getrent.helper.Common;
import com.getrentbd.getrent.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spName;

public class FeedBackActivity extends AppCompatActivity {
    EditText etFeedbackMessage;
    Button btFeedbackButton;
    private DrawerLayout drawerLayout;
    private ImageView navigationimage;
    private ActionBarDrawerToggle mToggle;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView  tvFeedback, tvLogOut, tvName, tvEmail, tvTitle;
    private CircleImageView ivProfileImage;
    private LinearLayout layoutProfile,layoutOwnPost,layoutCreatepost,layoutFilterSearch,layoutFavorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        idFinder();
        drawerOperation();
        bottomNavOperation();
        //Set Value
        tvTitle.setText("Feedback");
        //Intent Operation
        btFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivHome.setImageResource(R.drawable.button_huver_home);
                    Intent intent = new Intent(FeedBackActivity.this, HomePage.class);
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
                    Intent intent = new Intent(FeedBackActivity.this, ProfileActivity.class);
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
                    Intent intent = new Intent(FeedBackActivity.this, OwnPost.class);
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
                Intent intent = new Intent(FeedBackActivity.this, ProfileActivity.class);

                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });


        layoutOwnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackActivity.this, OwnPost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackActivity.this, FilterSearch.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackActivity.this, FeedBackActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackActivity.this, LogIn.class);
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
                Intent intent = new Intent(FeedBackActivity.this, CreatePost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackActivity.this, FavoriteList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
    }

    private void idFinder() {
        etFeedbackMessage = findViewById(R.id.etUserMessage);
        btFeedbackButton = findViewById(R.id.btFeedbackId);
        tvTitle = findViewById(R.id.tvTitleId);
        drawerLayout = findViewById(R.id.DrawerFeedbackId);
        navigationimage = findViewById(R.id.ivToolBarIcon);
        tvName = findViewById(R.id.tvNavFeedNameId);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvEmail = findViewById(R.id.tvNavFeedEmailId);
        tvFeedback = findViewById(R.id.tvNavFeedfeedbackid);
        tvLogOut = findViewById(R.id.tvNavFeedLogOutId);
        ivProfile = findViewById(R.id.ivprofileid);
        ivPost = findViewById(R.id.icpostid);
        ivHome = findViewById(R.id.ivHomeId);

        layoutProfile = findViewById(R.id.navProfile);
        layoutOwnPost = findViewById(R.id.navOwnPost);
        layoutCreatepost = findViewById(R.id.navCreatePost);
        layoutFilterSearch = findViewById(R.id.navFilterSearch);
        layoutFavorites = findViewById(R.id.navFavorites);
    }

    private void send() {
        String receiver = "getrentbd@gmail.com";
        String[] recevername = receiver.split(",");
        String subject = "Get-Rent";
        String message = etFeedbackMessage.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recevername);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose App For Send"));

    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(FeedBackActivity.this, HomePage.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }
}
