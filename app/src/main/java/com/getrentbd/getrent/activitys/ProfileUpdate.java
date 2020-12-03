package com.getrentbd.getrent.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spName;

public class ProfileUpdate extends AppCompatActivity {
    private String Name, Email, Gender, Number, Id, Password;
    private EditText etName, etEmail;
    private RadioGroup rgGenders;
    private RadioButton rbGender, rbMale, rbFemale;
    private TextView tvTitle;
    private Button btUpdate;
    private RequestQueue requestQueue;
    private ProgressBar pbUpdate;
    private ImageView ivProfile, ivPost, ivHome,ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_profile);

        idFinder();
        bottomNavOperation();
        takeSharedPrefValue();
        //Set Value
        tvTitle.setText("Update Profile");
        etName.setText(Name);
        etEmail.setText(Email);
        //Volly
        requestQueue = Volley.newRequestQueue(this);
        //Setting Gender
        if (Gender.equals("Male")) {
            rbMale.setChecked(true);
        } else if (Gender.equals("Female")) {
            rbFemale.setChecked(true);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Intent Operation
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btUpdate.setVisibility(View.GONE);
                int selectId = rgGenders.getCheckedRadioButtonId();
                rbGender = findViewById(selectId);
                String SelectedGender = rbGender.getText().toString();
                String ChangedName = etName.getText().toString().trim();
                String ChangedEmail = etEmail.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(ChangedEmail).matches()) {
                    etEmail.requestFocus();
                    etEmail.setError("Invalid Email");
                }
                if (ChangedName.isEmpty()) {
                    etName.requestFocus();
                    etName.setError("Enter Name");
                }
                if (ChangedName.length() > 80) {
                    etName.requestFocus();
                    etName.setError("Too Long Name");
                }

                if (ChangedEmail.isEmpty()) {
                    etEmail.requestFocus();
                    etEmail.setError("Enter Email");
                }

                if (ChangedEmail.length() > 80) {
                    etEmail.requestFocus();
                    etEmail.setError("Too Long Email");
                }
                if (!ChangedName.isEmpty() && !ChangedEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(ChangedEmail).matches() && ChangedName.length() <= 80 && ChangedEmail.length() <= 80) {
                    pbUpdate.setVisibility(View.VISIBLE);
                    UpdateData(SelectedGender, ChangedName, ChangedEmail);
                } else {
                    btUpdate.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void takeSharedPrefValue() {
        SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
        Id = sharedPreferences.getString(Common.SpId, "");
        Password = sharedPreferences.getString(Common.SpPass, "");
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
                    Intent intent = new Intent(ProfileUpdate.this, HomePage.class);
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
                    Intent intent = new Intent(ProfileUpdate.this, ProfileActivity.class);
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
                    Intent intent = new Intent(ProfileUpdate.this, OwnPost.class);
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




    private void UpdateData(final String selectedGender, final String changedName, final String changedEmail) {
        String url = Apis.ProfileUpdate;
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (response.equals("Update Successfully")) {
                            Intent intent = new Intent(ProfileUpdate.this, ProfileActivity.class);
                            SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Common.SpName, changedName);
                            editor.putString(Common.SpEmail, changedEmail);
                            editor.putString(Common.SpGender, selectedGender);
                            editor.putString(Common.SpUpdatedProfileNumber, Number);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Successfully Updated !", Toast.LENGTH_SHORT).show();
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
                Map<String, String> pars = new HashMap<String, String>();

                pars.put("id", Id);
                pars.put("mobile_number", Number);
                pars.put("name", changedName);
                pars.put("email", changedEmail);
                pars.put("password", Password);
                pars.put("gender", selectedGender);

                return pars;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void idFinder() {
        etName = findViewById(R.id.etEditeProfileNameId);
        etEmail = findViewById(R.id.etEditeProfileEmailId);
        rgGenders = findViewById(R.id.rgEditeProfileGendersId);
        rbMale = findViewById(R.id.rbEditeProfileMaleId);
        rbFemale = findViewById(R.id.rbEditeProfileFeMaleId);
        btUpdate = findViewById(R.id.btEditeProfileUpdateId);
        pbUpdate = findViewById(R.id.pbUpdateProgressId);
        tvTitle = findViewById(R.id.tvTitleId);
        ivBack = findViewById(R.id.ivBack);

        ivProfile = findViewById(R.id.ivprofileid);
        ivPost = findViewById(R.id.icpostid);
        ivHome = findViewById(R.id.ivHomeId);

    }

    public void onBackPressed() {
            super.onBackPressed();
    }

}
