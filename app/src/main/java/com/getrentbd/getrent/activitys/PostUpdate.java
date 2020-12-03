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
import com.getrentbd.getrent.R;
import com.getrentbd.getrent.helper.Apis;
import com.getrentbd.getrent.helper.Common;
import com.getrentbd.getrent.modelClass.OwnPostList;
import com.getrentbd.getrent.modelClass.RentItemsList;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.getrentbd.getrent.helper.Apis.baseurl;
import static com.getrentbd.getrent.helper.Common.ownLists;
import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spName;
import static com.getrentbd.getrent.helper.Common.spNumber;

public class PostUpdate extends AppCompatActivity {
    private LinearLayout layoutKeyboardHide;
    private String Position;
    private String Id,  Catagory, Seat, floor_no, From, Location,
            Rent, TimeDate,Url,Details,  Address;
    private String[] MonthNames, CatagoryNames;
    private Spinner MonthSpinner, CatagorySpinner;
    private EditText etAddress, etRent, etSeat, etFloor, etDescription;
    private Button btUpdatePost;
    private ProgressBar pbUpdate;
    private AutoCompleteTextView actvLocation;
    private ArrayList<RentItemsList> datalist = new ArrayList<>();
    private RequestQueue requestQueue;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivProfile, ivPost, ivHome;
    private ImageView ivSelectImage;
    private ArrayList<OwnPostList> modelClasses = new ArrayList<>();
    private Bitmap bitmap;
    private JSONObject jsonObject;
    private String type = "";
    private String imgename;
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_post);
        idFinder();
        bottomNavOperation();
        takeIntentValue();
        SpinnerOperations();
        modelClasses = ownLists;

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutKeyboardHide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

        //Volly
        requestQueue = Volley.newRequestQueue(this);
        //Set Value
        tvTitle.setText("Update Post");
        etSeat.setText(Seat);
        etFloor.setText(floor_no);
        etRent.setText(Rent);
        actvLocation.setText(Location);
        etAddress.setText(Address);
        etDescription.setText(Details);

        if (!Url.equals("null")){
            Picasso.get().load(baseurl+Url).fit().placeholder(R.drawable.upload_image).into(ivSelectImage);
        }else {
            ivSelectImage.setImageResource(R.drawable.upload_image);
        }

        ivSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(PostUpdate.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    requestMultiplePermissions();
                } else {
                    openGallery();
                }
            }

        });

        //Auto Complete Text
        datalist = Common.spList;
        int sz = datalist.size();

        String[] location = new String[sz];
        for (int i = 0; i < sz; i++) {
            location[i] = datalist.get(i).getLocation();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.suggetion_layout, R.id.tvSuggetionId, location);
        actvLocation.setAdapter(adapter);
        //Intent Operations

        btUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String UpdatedMonth = MonthSpinner.getSelectedItem().toString();
                String UpdatedCatagory = CatagorySpinner.getSelectedItem().toString();
                String UpdatedLocation = actvLocation.getText().toString().trim();
                String UpdatedSeat = etSeat.getText().toString().trim();
                String UpdatedRent = etRent.getText().toString().trim();
                String UpdatedAddress = etAddress.getText().toString();
                String updatedFloor = etFloor.getText().toString();
                String UpdatedDetails = etDescription.getText().toString();
                if (UpdatedLocation.isEmpty()) {
                    actvLocation.requestFocus();
                    actvLocation.setError("");
                }
                if (UpdatedLocation.length() > 50) {
                    actvLocation.requestFocus();
                    actvLocation.setError("Too Many Charecter");
                }
                if (UpdatedAddress.isEmpty()) {
                    etAddress.requestFocus();
                    etAddress.setError("");
                }
                if (UpdatedAddress.length() >= 250) {
                    etAddress.requestFocus();
                    etAddress.setError("Too Many Charecter");
                }
                if (UpdatedRent.isEmpty()) {
                    etRent.requestFocus();
                    etRent.setError("");
                }

                if (UpdatedRent.length() > 8) {
                    etRent.requestFocus();
                    etRent.setError("Max <100000000");
                }
                if (UpdatedSeat.isEmpty()) {
                    etSeat.requestFocus();
                    etSeat.setError("");
                }
                if (updatedFloor.isEmpty()) {
                    etFloor.requestFocus();
                    etFloor.setError("");
                }
                if (UpdatedSeat.length() > 2) {
                    etSeat.requestFocus();
                    etSeat.setError("Max <100");
                }
                if (UpdatedDetails.length() > 5000) {
                    etDescription.requestFocus();
                    etDescription.setError("Too Many Charecter");
                }

                try {
                    double InputSeat = Double.parseDouble(UpdatedSeat);
                    double InputRent = Double.parseDouble(UpdatedRent);

                    if (InputRent <= 0) {
                        etSeat.requestFocus();
                        etRent.setError("Enter Valid Rent");
                    }

                    if (InputSeat <= 0) {
                        etSeat.requestFocus();
                        etSeat.setError("Enter Valid Digit");
                    }
                    if (!updatedFloor.isEmpty() && !UpdatedLocation.isEmpty() && !UpdatedAddress.isEmpty() && !UpdatedRent.isEmpty() && UpdatedRent.length() <= 8 && !UpdatedSeat.isEmpty() && UpdatedSeat.length() <= 2 && (UpdatedLocation.length() <= 50) && (UpdatedDetails.length() <= 5000) && (UpdatedAddress.length() <= 250)) {
                        if (InputRent > 0 && InputSeat > 0) {
                            if (type.isEmpty()){
                                UpdatePost(UpdatedMonth, UpdatedCatagory, UpdatedLocation, UpdatedAddress, UpdatedRent, UpdatedSeat, UpdatedDetails, updatedFloor,Url);
                                btUpdatePost.setVisibility(View.GONE);
                                pbUpdate.setVisibility(View.VISIBLE);
                            }else {
                                btUpdatePost.setVisibility(View.GONE);
                                pbUpdate.setVisibility(View.VISIBLE);
                                uploadImage(bitmap,UpdatedMonth, UpdatedCatagory, UpdatedLocation, UpdatedAddress, UpdatedRent, UpdatedSeat, UpdatedDetails, updatedFloor,imgename);
                            }

                        }
                    }

                } catch (Exception e) {

                }

            }
        });
    }

    private void idFinder() {
        ivSelectImage = findViewById(R.id.ivSelectImage);
        layoutKeyboardHide = findViewById(R.id.layoutKeyboardHide);
        MonthSpinner = findViewById(R.id.spUpdatePostAdMonthId);
        CatagorySpinner = findViewById(R.id.spUpdatPostAdCatagoryId);
        actvLocation = findViewById(R.id.actvUpdatPostAdLocationid);
        etAddress = findViewById(R.id.etUpdatPostAdAddressId);
        etRent = findViewById(R.id.etUpdatPostAdRentId);
        etSeat = findViewById(R.id.etUpdatPostAdSeatId);
        etFloor = findViewById(R.id.etUpdatePostAdFloorId);
        etDescription = findViewById(R.id.etUpdatPostAdDescriptionId);
        btUpdatePost = findViewById(R.id.btUpdatPostButtonId);
        pbUpdate = findViewById(R.id.pbUpdatPostAdProgressBar);
        tvTitle = findViewById(R.id.tvTitleId);
        ivBack = findViewById(R.id.ivBack);
        ivProfile = findViewById(R.id.ivprofileid);
        ivPost = findViewById(R.id.icpostid);
        ivHome = findViewById(R.id.ivHomeId);
    }

    private void SpinnerOperations() {
        MonthNames = getResources().getStringArray(R.array.post_monthname);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spiner_sample, R.id.tvspinersample, MonthNames);
        MonthSpinner.setAdapter(monthAdapter);
        int selectMonth = monthAdapter.getPosition(From);
        MonthSpinner.setSelection(selectMonth);

        CatagoryNames = getResources().getStringArray(R.array.post_catagory);
        ArrayAdapter<String> catagoryAdapter = new ArrayAdapter<>(this, R.layout.spiner_sample, R.id.tvspinersample, CatagoryNames);
        CatagorySpinner.setAdapter(catagoryAdapter);
        int selectCategory = catagoryAdapter.getPosition(Catagory);
        CatagorySpinner.setSelection(selectCategory);
    }

    private void takeIntentValue() {
        Position = getIntent().getStringExtra("Position");
        Id = getIntent().getStringExtra("Id");
        From = getIntent().getStringExtra("From");
        Catagory = getIntent().getStringExtra("Catagory");
        Seat = getIntent().getStringExtra("Seat");
        floor_no = getIntent().getStringExtra("Floor_no");
        Location = getIntent().getStringExtra("Location");
        Rent = getIntent().getStringExtra("Rent");
        TimeDate = getIntent().getStringExtra("TimeDate");
        Url = getIntent().getStringExtra("Url");
        Details = getIntent().getStringExtra("Description");
        Address = getIntent().getStringExtra("Address");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bottomNavOperation() {
        ivHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Button Pressed
                    ivHome.setImageResource(R.drawable.button_huver_home);
                    Intent intent = new Intent(PostUpdate.this, HomePage.class);
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
                    Intent intent = new Intent(PostUpdate.this, ProfileActivity.class);
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
                    Intent intent = new Intent(PostUpdate.this, OwnPost.class);
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


    private void UpdatePost(final String updatedMonth, final String updatedCatagory,
                            final String updatedLocation, final String updatedAddress,
                            final String updatedRent, final String updatedSeat,
                            final String updatedDetails, final String updatedFloor,
                            final String imgename) {
        requestQueue.getCache().clear();
        String url = Apis.PostUpdate;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (response.equals("Update Successfully")) {
                            Intent intent = new Intent(PostUpdate.this, OwnPost.class);
                            Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Common.SpUpdatePostId, Id);
                            editor.putString(Common.SpUpdatedMonth, updatedMonth);
                            editor.putString(Common.SpUpdatedCatagory, updatedCatagory);
                            editor.putString(Common.SpUpdatedLocation, updatedLocation);
                            editor.putString(Common.SpUpdatedAddress, updatedAddress);
                            editor.putString(Common.SpUpdatedRent, updatedRent);
                            editor.putString(Common.SpUpdatedSeat, updatedSeat);
                            editor.putString(Common.SpUpdatedDetails, updatedDetails);
                            editor.apply();

                            listUpdate(Integer.parseInt(Position),Id,spNumber,updatedSeat,updatedFloor,updatedLocation
                            ,updatedMonth,updatedRent,updatedCatagory,TimeDate,"noUrl",spGender,updatedAddress,updatedDetails
                            ,spName,spEmail);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 5000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
                String UserId = sharedPreferences.getString(Common.SpId, "");
                params.put("id", Id);
                params.put("user_id", UserId);
                params.put("number_sit", updatedSeat);
                params.put("floor_num", updatedFloor);
                params.put("location", updatedLocation);
                params.put("month_name", updatedMonth);
                params.put("house_rent", updatedRent);
                params.put("catagory", updatedCatagory);
                params.put("timeDate", TimeDate);
                params.put("image_url", imgename);
                params.put("address", updatedAddress);
                params.put("description", updatedDetails);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public ArrayList<OwnPostList> listUpdate(int pos, String id, String phn_num, String seat,
                                             String floorNo, String loca, String month, String rent,
                                             String category, String tDate, String url, String gender,
                                             String address, String description, String name,
                                             String email ){

        modelClasses.set(pos, new OwnPostList(id,phn_num,seat,floorNo,loca,month,rent,category,tDate,
                url,gender,address,description,name,email));
        return modelClasses;
    }
    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

                            Toast.makeText(PostUpdate.this, "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PostUpdate.this, "Some Error! ", Toast.LENGTH_SHORT).show();
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
            imgename = String.valueOf(Calendar.getInstance().getTimeInMillis());

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

    private void uploadImage(Bitmap bitmap, final String updatedMonth, final String updatedCatagory,
                             final String updatedLocation, final String updatedAddress,
                             final String updatedRent, final String updatedSeat, final String updatedDetails,
                             final String updatedFloor, final String imgename) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", imgename);
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
                            UpdatePost(updatedMonth,updatedCatagory,updatedLocation,updatedAddress,updatedRent,updatedSeat,updatedDetails,updatedFloor,imgename+".jpg");
                        }
                        requestQueue.getCache().clear();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btUpdatePost.setVisibility(View.VISIBLE);
                pbUpdate.setVisibility(View.GONE);
                Log.d("VolleyError",volleyError.toString());
                Toast.makeText(PostUpdate.this, "Timeout.Your Net is slow. Plz try aging", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);

    }


}
