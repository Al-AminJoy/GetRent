package com.getrentbd.getrent.activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getrentbd.getrent.helper.Common;
import com.getrentbd.getrent.modelClass.RentItemsList;
import com.getrentbd.getrent.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Common.AppPra;
import static com.getrentbd.getrent.helper.Common.SpNumber;
import static com.getrentbd.getrent.helper.Common.SpPass;
import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spName;

public class FilterSearch extends AppCompatActivity {
    private String[] MonthNames, CatagoryNames;
    private Spinner monthspinner, catagoryspiner;
    private ArrayList<RentItemsList> datalist = new ArrayList<>();
    private RadioGroup RadioGroup;
    private LinearLayout layoutSelectGender;
    private RadioButton RadioBtGender;
    private EditText etRentFrom, etRentTo, etSeat;
    private AutoCompleteTextView actvLocation;
    private Button btShow;
    private TextView tvTitle;
    private DrawerLayout drawerLayout;
    private ImageView navigationimage;
    private ActionBarDrawerToggle mToggle;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView  tvFeedback, tvLogOut, tvName, tvEmail;
    private LinearLayout layoutProfile,layoutOwnPost,layoutCreatepost,layoutFilterSearch,layoutFavorites;
    private CircleImageView ivProfileImage;
    private String Gender = "";
    private String selectGender="false";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        idfinder();
        drawerOperation();
        bottomNavOperation();
        SpinnerOperation();

        //Set Value
        tvTitle.setText("Filter Search");

        //Auto Complete Text
        datalist = Common.spList;
        int sz = datalist.size();
        String[] location = new String[sz];
        for (int i = 0; i < sz; i++) {
            location[i] = datalist.get(i).getLocation();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.suggetion_layout, R.id.tvSuggetionId, location);
        actvLocation.setAdapter(adapter);

        catagoryspiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Mess") || selectedItem.equals("Hostel") || selectedItem.equals("Room")||selectedItem.equals("Sub-Let")) {
                    // do your stuff
                    layoutSelectGender.setVisibility(View.VISIBLE);
                    selectGender = "true";
                }else {
                    layoutSelectGender.setVisibility(View.GONE);
                    selectGender = "flse";
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Intent Operation
        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String loc = actvLocation.getText().toString().trim();
                final String Location = loc.toLowerCase();
                final String Seat = etSeat.getText().toString();
                final String RentFrom = etRentFrom.getText().toString();
                final String RentTo = etRentTo.getText().toString();
                final String MonthSpinerValue = monthspinner.getSelectedItem().toString();
                final String CatagorySpinerValue = catagoryspiner.getSelectedItem().toString();

                if (selectGender.equals("true")){
                    try {
                        int selectid = RadioGroup.getCheckedRadioButtonId();
                        RadioBtGender = findViewById(selectid);
                        Gender = RadioBtGender.getText().toString();

                    } catch (Exception e) {
                        String message = "You must select the gender?" +
                                "\n\nAnd select others 2 items (Month and Category)" +
                                "If you are select this 3 items." +
                                "You will easily get the right information you are looking for.";
                        showDialog(message);
                    }
                }

                if (RentFrom.length() > 8) {
                    etRentFrom.requestFocus();
                    etRentFrom.setError("Max < 8 Digits");
                }
                if (RentTo.length() > 8) {
                    etRentTo.requestFocus();
                    etRentTo.setError("Max< 8 Digits");
                }

                if (MonthSpinerValue.contains("Select")) {
                    showDialog("From which month you want to stay, please select a month. ");
                } else {
                    if (CatagorySpinerValue.contains("Select")) {
                        showDialog("Please select a category. which type of environment you are looking for.");
                    } else {
                        if (RentFrom.length() <= 8 && RentTo.length() <= 8) {
                            Intent intent = new Intent(FilterSearch.this, FilterSearchResult.class);
                            intent.putExtra("Location", Location);
                            intent.putExtra("Month", MonthSpinerValue);
                            intent.putExtra("Catagory", CatagorySpinerValue);
                            intent.putExtra("Gender", Gender);
                            intent.putExtra("RentFrom", RentFrom);
                            intent.putExtra("RentTo", RentTo);
                            intent.putExtra("Seat", Seat);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            }
        });

    }

    private void showDialog(String message) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Information !");
        alertbuilder.setIcon(R.drawable.information);
        alertbuilder.setMessage(message);
        alertbuilder.setCancelable(false);

        alertbuilder.setNegativeButton(Html.fromHtml("<font color='#413752'>Ok</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertbuilder.create();
        alertDialog.show();
    }

    private void SpinnerOperation() {
        MonthNames = getResources().getStringArray(R.array.monthname);
        ArrayAdapter<String> monthadapter = new ArrayAdapter<>(this, R.layout.spiner_sample, R.id.tvspinersample, MonthNames);
        monthspinner.setAdapter(monthadapter);
        CatagoryNames = getResources().getStringArray(R.array.catagory);
        ArrayAdapter<String> catagoryadapter = new ArrayAdapter<>(this, R.layout.spiner_sample, R.id.tvspinersample, CatagoryNames);
        catagoryspiner.setAdapter(catagoryadapter);

    }


    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivHome.setImageResource(R.drawable.button_huver_home);
                    Intent intent = new Intent(FilterSearch.this, HomePage.class);
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
                    Intent intent = new Intent(FilterSearch.this, ProfileActivity.class);
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
                    Intent intent = new Intent(FilterSearch.this, OwnPost.class);
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
                if (spGender.toLowerCase().equals("male")) {
                    ivProfileImage.setImageResource(R.drawable.profile);
                } else {
                    ivProfileImage.setImageResource(R.drawable.femalepng);
                }


            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearch.this, ProfileActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });


        layoutOwnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearch.this, OwnPost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearch.this, FavoriteList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearch.this, FilterSearch.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearch.this, FeedBackActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterSearch.this, LogIn.class);
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
                Intent intent = new Intent(FilterSearch.this, CreatePost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
    }

    private void idfinder() {

        monthspinner = findViewById(R.id.spFilterMonthId);
        layoutSelectGender = findViewById(R.id.layoutSelectGender);
        RadioGroup = findViewById(R.id.rgGenderFilterId);
        catagoryspiner = findViewById(R.id.spFilterCatagoryId);
        btShow = findViewById(R.id.btShowFilterId);
        actvLocation = findViewById(R.id.actvFilterLocation);
        etRentFrom = findViewById(R.id.etFilterRentFrom);
        etRentTo = findViewById(R.id.etFilterRentTo);
        etSeat = findViewById(R.id.etFilterSeat);
        tvTitle = findViewById(R.id.tvTitleId);
        drawerLayout = findViewById(R.id.DrawerFilterSearchId);
        navigationimage = findViewById(R.id.ivToolBarIcon);
        tvName = findViewById(R.id.tvNavFilterSearchNameId);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvEmail = findViewById(R.id.tvNavFilterSearchEmailId);
        tvFeedback = findViewById(R.id.tvNavFilterSearchfeedbackid);
        tvLogOut = findViewById(R.id.tvNavFilterSearchLogOutId);
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
            startActivity(new Intent(FilterSearch.this, HomePage.class));
            finish();
            super.onBackPressed();
        }
    }
}
