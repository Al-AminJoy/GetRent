package com.getrentbd.getrent.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getrentbd.getrent.helper.Apis;
import com.getrentbd.getrent.helper.Common;
import com.getrentbd.getrent.modelClass.RentItemsList;
import com.getrentbd.getrent.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spList;
import static com.getrentbd.getrent.helper.Common.spName;

public class CreatePost extends AppCompatActivity {
    private String[] MonthNames, CatagoryNames;
    private Spinner MonthSpinner, CatagorySpinner;
    private EditText etAddress, etRent, etSeat, etDescription,etPostAdFloorId;
    private AutoCompleteTextView actvLocation;
    private ArrayList<RentItemsList> datalist = new ArrayList<>();
    private Button btPost;
    private TextView tvTitle;
    private TextView tvErrorMessage;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ImageView navigationimage;
    private ActionBarDrawerToggle mToggle;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView tvFeedback, tvLogOut,  tvName, tvEmail;
    private LinearLayout layoutProfile,layoutOwnPost,layoutCreatepost,layoutFilterSearch,layoutFavorites;
    private RequestQueue requestQueue;
    private long timeDate;
    private LinearLayout layoutKeyboardHide;
    private CircleImageView ivProfileImage;
    private ImageView ivSelectImage;

    private Bitmap bitmap;
    private JSONObject jsonObject;
    private String type = "";
    private String imgname = "null";
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);
        idFinder();
        drawerOperation();
        bottomNavOperation();
        SpinnerOperations();

        layoutKeyboardHide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

        //Set Value
        tvTitle.setText("Post Ad");
        //Request Queue
        requestQueue = Volley.newRequestQueue(this);
        //Auto Complete Text
        datalist = spList;
        int sz = datalist.size();
        String[] location = new String[sz];
        for (int i = 0; i < sz; i++) {
            location[i] = datalist.get(i).getLocation();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.suggetion_layout, R.id.tvSuggetionId, location);
        actvLocation.setAdapter(adapter);
        //Intent Operation
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Month = MonthSpinner.getSelectedItem().toString();
                String Catagory = CatagorySpinner.getSelectedItem().toString();
                String Location = actvLocation.getText().toString().trim();
                String Address = etAddress.getText().toString().trim();
                String Rent = etRent.getText().toString();
                String Seat = etSeat.getText().toString();
                String etFloor = etPostAdFloorId.getText().toString().trim();
                String Description = etDescription.getText().toString().trim();

                if (Location.isEmpty()) {
                    actvLocation.requestFocus();
                    actvLocation.setError("Empty");
                }else if (Location.length() > 50) {
                    actvLocation.requestFocus();
                    actvLocation.setError("You Can Input Max 50 Character");
                }

                if (Address.isEmpty()) {
                    etAddress.requestFocus();
                    etAddress.setError("Empty");
                }else if (Address.length() >= 250) {
                    etAddress.requestFocus();
                    etAddress.setError("You Can Input Max 250 Character");
                }

                if (Rent.isEmpty()) {
                    etRent.requestFocus();
                    etRent.setError("");
                }else if (Rent.length() > 8) {
                    etRent.requestFocus();
                    etRent.setError("Max < 8 Digits");
                }

                if (Seat.isEmpty()) {
                    etSeat.requestFocus();
                    etSeat.setError("");
                }else if (Seat.length() > 2) {
                    etSeat.requestFocus();
                    etSeat.setError("Max < 2 Digits(Ex:1 to 99)");
                }
                if (etFloor.isEmpty()) {
                    etPostAdFloorId.requestFocus();
                    etPostAdFloorId.setError("");
                }else if (etFloor.length() > 10) {
                    etPostAdFloorId.requestFocus();
                    etPostAdFloorId.setError("You Can Input Max 10 Character");
                }

                if (Description.length() > 5000) {
                    etDescription.requestFocus();
                    etDescription.setError("You Can Input Max 5000 Character");
                }

                try {
                    double InputRent = Double.parseDouble(Rent);
                    double InputSeat = Double.parseDouble(Seat);
                    if (InputRent <= 0) {
                        etRent.requestFocus();
                        etRent.setError("Enter Valid Rent");
                    }
                    if (InputSeat <= 0) {
                        etSeat.requestFocus();
                        etSeat.setError("Enter Valid Digit");
                    }

                    if (!etFloor.isEmpty() && !Location.isEmpty() && !Address.isEmpty() && !Rent.isEmpty() && Rent.length() <= 8 && !Seat.isEmpty() && Seat.length() <= 2 && (Location.length() <= 50) && (Description.length() <= 5000) && (Address.length() <= 250)) {

                        if (InputRent > 0 && InputSeat > 0) {
                            timeDate = System.currentTimeMillis();
                            if (type.isEmpty()){
                                postdata(etFloor,Month, Catagory, Location, Address, Rent, Seat, Description,imgname);
                                btPost.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                            }else {
                                btPost.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                uploadImage(bitmap,etFloor,Month, Catagory, Location, Address, Rent, Seat, Description,imgname);
                            }
                        }

                    }

                } catch (Exception e) {
                    Log.e("TRYCATCH",e.toString());
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ivSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(CreatePost.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    requestMultiplePermissions();
                } else {
                    openGallery();
                }
            }

        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void SpinnerOperations() {
        MonthNames = getResources().getStringArray(R.array.post_monthname);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spiner_sample, R.id.tvspinersample, MonthNames);
        MonthSpinner.setAdapter(monthAdapter);

        CatagoryNames = getResources().getStringArray(R.array.post_catagory);
        ArrayAdapter<String> catagoryAdapter = new ArrayAdapter<>(this, R.layout.spiner_sample, R.id.tvspinersample, CatagoryNames);
        CatagorySpinner.setAdapter(catagoryAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivHome.setImageResource(R.drawable.button_huver_home);
                    Intent intent = new Intent(CreatePost.this, HomePage.class);
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
                    Intent intent = new Intent(CreatePost.this, ProfileActivity.class);
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
                    Intent intent = new Intent(CreatePost.this, OwnPost.class);
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
                if (spGender.equals("male")){
                    ivProfileImage.setImageResource(R.drawable.profile);
                }else {
                    ivProfileImage.setImageResource(R.drawable.femalepng);
                }

            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, ProfileActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });

        layoutOwnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, OwnPost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutCreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, CreatePost.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, FilterSearch.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        layoutFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, FavoriteList.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, FeedBackActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this, LogIn.class);
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


    private void postdata(final String floorNo, final String month, final String catagory, final String location,
                          final String address, final String rent, final String seat, final String description,
                          final String imgname) {
        requestQueue.getCache().clear();
        String url = Apis.PostAd;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (response.equals("Inserted Successfully")) {
                            Intent intent = new Intent(CreatePost.this, HomePage.class);
                            Toast.makeText(getApplicationContext(), "Post Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }

                    }
                }, 5000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btPost.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
                String ID = sharedPreferences.getString(Common.SpId, "");
                params.put("user_id", ID);
                params.put("month_name", month);
                params.put("catagory", catagory);
                params.put("location", location);
                params.put("floor_num", floorNo);
                params.put("timeDate", String.valueOf(timeDate));
                params.put("image_url", imgname);
                params.put("address", address);
                params.put("house_rent", rent);
                params.put("number_sit", seat);
                params.put("description", description);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void idFinder() {
        ivSelectImage = findViewById(R.id.tvSelectImage);
        layoutKeyboardHide = findViewById(R.id.layoutKeyboardHide);
        MonthSpinner = findViewById(R.id.spPostAdMonthId);
        CatagorySpinner = findViewById(R.id.spPostAdCatagoryId);
        actvLocation = findViewById(R.id.actvPostAdLocationid);
        etAddress = findViewById(R.id.etPostAdAddressId);
        etRent = findViewById(R.id.etPostAdRentId);
        etSeat = findViewById(R.id.etPostAdSeatId);
        etPostAdFloorId = findViewById(R.id.etPostAdFloorId);
        etDescription = findViewById(R.id.etPostAdDescriptionId);
        btPost = findViewById(R.id.btPostAdButtonId);
        linearLayout = findViewById(R.id.linearPostAdSeatId);
        tvErrorMessage = findViewById(R.id.tvPostAdErrorMessage);
        progressBar = findViewById(R.id.pbPostAdProgressBar);
        tvTitle = findViewById(R.id.tvTitleId);
        drawerLayout = findViewById(R.id.DrawerPostAdId);
        navigationimage = findViewById(R.id.ivToolBarIcon);
        tvName = findViewById(R.id.tvNavPostAdNameId);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvEmail = findViewById(R.id.tvNavPostAdEmailId);
        tvFeedback = findViewById(R.id.tvNavPostAdfeedbackid);
        tvLogOut = findViewById(R.id.tvNavPostAdLogOutId);
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
            Intent intent = new Intent(CreatePost.this, HomePage.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }


    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            Toast.makeText(CreatePost.this, "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(CreatePost.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            Cursor cursor = MediaStore.Images.Media.query(getApplicationContext().getContentResolver(), imageUri, new String[]{MediaStore.Images.Media.DATA});

            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //Create ImageCompressTask and execute with Executor.
                bitmap = decodeImageFromFiles(path, /* your desired width*/300, /*your desired height*/ 300);
            }
            ivSelectImage.setImageBitmap(bitmap);

            type = "image";
            imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());

        }
    }


    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        // decode with the sample size
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }

    private void uploadImage(Bitmap bitmap, final String etFloor, final String month, final String catagory,
                             final String location, final String address, final String rent, final String seat,
                             final String description, final String imgname) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", imgname);
            jsonObject.put("imageType", "itemsPost");
            jsonObject.put("image", encodedImage);

        } catch (JSONException e) {
            Log.e("JSONObjectHere", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Apis.img_url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (jsonObject.toString().toLowerCase().contains("1")) {
                            postdata(etFloor,month, catagory, location, address, rent, seat, description,imgname+".jpg");
                        }
                        requestQueue.getCache().clear();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btPost.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.d("VolleyError",volleyError.toString());
                Toast.makeText(CreatePost.this, "Timeout.Your Net is slow. Plz try aging", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);

    }


}
