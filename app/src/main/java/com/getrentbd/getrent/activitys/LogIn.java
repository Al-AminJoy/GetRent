package com.getrentbd.getrent.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.getrentbd.getrent.helper.Apis;
import com.getrentbd.getrent.helper.Common;
import com.getrentbd.getrent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.getrentbd.getrent.helper.Common.spEmail;
import static com.getrentbd.getrent.helper.Common.spGender;
import static com.getrentbd.getrent.helper.Common.spId;
import static com.getrentbd.getrent.helper.Common.spName;
import static com.getrentbd.getrent.helper.Common.spNumber;

public class LogIn extends AppCompatActivity {
    private EditText mobileNumber, pass;
    private Button loginbutton;
    private TextView textmessage, tvSignUp;
    private RequestQueue requestQueue;
    private LinearLayout layoutKeyboardHide;
    private ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        idFinder();

        //Request Queue
        requestQueue = Volley.newRequestQueue(this);


        //Intent Operation
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String inputnum = mobileNumber.getText().toString();
                String inputpass = pass.getText().toString().trim();
                if (inputnum.isEmpty() && inputpass.isEmpty()) {
                    mobileNumber.setError("Enter Number");
                    pass.setError("Enter Password");
                }

                if (inputnum.length() != 11) {
                    mobileNumber.setError("Number Should 11 Digit");
                }
                if (!inputnum.isEmpty() && !inputpass.isEmpty() && inputnum.length() == 11) {
                    loginbutton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    jsonparse(inputnum, inputpass);

                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        // Keyboard Hide
        layoutKeyboardHide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

    }



    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void idFinder() {
        layoutKeyboardHide = findViewById(R.id.layoutKeyboardHide);
        mobileNumber = findViewById(R.id.etmobilenumberid);
        pass = findViewById(R.id.etpasswordid);
        loginbutton = findViewById(R.id.btloginbuttonid);
        textmessage = findViewById(R.id.tvloginmessageid);
        tvSignUp = findViewById(R.id.tvLogInSignUp);
        progressBar = findViewById(R.id.pbLogInProgressBar);
    }

    public void jsonparse(final String inputnum, final String inputpass) {
        String url = Apis.LogIn + inputnum + "&pass=" + inputpass;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("login");

                    if (jsonArray.length() == 0) {
                        textmessage.setText("Invalid Number \n Please, Sign Up First !");
                        mobileNumber.setError("");
                        progressBar.setVisibility(View.GONE);
                        loginbutton.setVisibility(View.VISIBLE);

                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject receive = jsonArray.getJSONObject(i);

                            String pnumber = receive.getString("mobile_number");
                            if (pnumber.equals("Wrong Password")) {
                                textmessage.setText(pnumber);
                                pass.setError("");
                                loginbutton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                String email = receive.getString("email");
                                String name = receive.getString("name");
                                String id = receive.getString("id");
                                String Gender = receive.getString("gender");
                                //Put Shared Pref Value
                                SharedPreferences sharedPreferences = getSharedPreferences(Common.AppPra, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Common.SpId, id);
                                editor.putString(Common.SpPass, inputpass);
                                editor.putString(Common.SpName, name);
                                editor.putString(Common.SpEmail, email);
                                editor.putString(Common.SpNumber, pnumber);
                                editor.putString(Common.SpGender, Gender);
                                editor.apply();

                                // Intent in home page
                                Intent intent = new Intent(LogIn.this, HomePage.class);
                                startActivity(intent);
                                finish();


                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
