package com.getrentbd.getrent.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.getrentbd.getrent.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class SignUp extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText etName, etEmail, etNumber, etPass, etConfirmPass;
    private Button btSignUp;
    private TextView tvSelectGender;
    private TextView tvOverAllErrorShower;
    private TextView tvSignUpToLogin;
    private ProgressBar progressBar;
    private String selectGender = "Select Gender";
    private LinearLayout layoutKeyboardHide;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy-hh:mm:ss",Locale.getDefault());
    private String submi_date ;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        idFinder();

        layoutKeyboardHide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });


        //Request Queue
        requestQueue = Volley.newRequestQueue(this);
        //Intent Operation
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                tvOverAllErrorShower.setText("");
                String Number = etNumber.getText().toString().trim();
                String Email = etEmail.getText().toString().trim();
                String Name = etName.getText().toString().trim();
                String Password = etPass.getText().toString().trim();
                String ConfirmPass = etConfirmPass.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    etEmail.setError("Invalid Email");
                }
                if (Name.isEmpty()) {
                    etName.setError("Enter Name");
                }
                if (Name.length() > 80) {
                    etName.setError("Too Long Name");
                }

                if (Email.isEmpty()) {
                    etEmail.setError("Enter Email");
                }

                if (Email.length() > 80) {
                    etEmail.requestFocus();
                    etEmail.setError("Too Large Email");
                }
                if (Number.isEmpty()) {
                    etNumber.requestFocus();
                    etNumber.setError("Enter Your 11 Digit Phone Number");
                }

                if ((Password.isEmpty())) {
                    etPass.requestFocus();
                    etPass.setError("Enter Password");
                }else if (Password.length() < 6){
                    etPass.requestFocus();
                    etPass.setError("To short, minimum 6 Digits");
                }else if (Password.length() > 11){
                    etPass.requestFocus();
                    etPass.setError("To large, maximum 11 Digits");
                }

                if (ConfirmPass.isEmpty()) {
                    etPass.requestFocus();
                    etConfirmPass.setError("Retype Password");
                }
                if (!Password.equals(ConfirmPass)) {
                    etConfirmPass.setError("Did Not Match");
                } else {
                    etConfirmPass.setError(null);
                }
                if (Number.length() != 11) {
                    etNumber.setError("Number Should 11 Digit. Please Check Your Number.");
                }


                if (!Name.isEmpty() && !Email.isEmpty() && !Number.isEmpty() && !Password.isEmpty() && (Password.equals(ConfirmPass)) && Number.length() == 11 && ((Password.length() > 3 && Password.length() < 9)) && Patterns.EMAIL_ADDRESS.matcher(Email).matches() && Name.length() <= 80 && Email.length() <= 80) {
                    if ((Number.substring(0, 3).equals("017") || Number.substring(0, 3).equals("013") || Number.substring(0, 3).equals("019") || Number.substring(0, 3).equals("014") || Number.substring(0, 3).equals("018") || Number.substring(0, 3).equals("016") || Number.substring(0, 3).equals("015"))) {
                        postdata(Name, Email, Number, Password, selectGender);
                        submi_date = formatter.format(Calendar.getInstance().getTime());
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        etNumber.requestFocus();
                        etNumber.setError("Invalid Number");
                    }
                }

            }
        });


        /**  click this button for select Gender*/
        tvSelectGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                tvSelectGender.setText(selectGender);
            }
        });


        /** Have an already account then he back the login page on click this button */
        tvSignUpToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
                finish();
            }
        });

    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void postdata(final String name, final String email, final String number, final String password, final String gender) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.SignUp, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                if (response.equals("Inserted Successfully")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SignUp.this, LogIn.class);
                            Toast.makeText(getApplicationContext(), "Successfully Registered. Login Now", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                    }, 5000);
                } else {
                    tvOverAllErrorShower.setVisibility(View.VISIBLE);
                    tvOverAllErrorShower.setText(response);
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley_Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("mobile_number", number);
                pars.put("name", name);
                pars.put("email", email);
                pars.put("password", password);
                pars.put("gender", gender);
                pars.put("timeDate", submi_date);
                return pars;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void idFinder() {

        layoutKeyboardHide = findViewById(R.id.layoutKeyboardHide);
        etName = findViewById(R.id.etSignUpName);
        etNumber = findViewById(R.id.etSignUpNumber);
        etEmail = findViewById(R.id.etSignUpEmail);
        tvSelectGender = findViewById(R.id.tvSelectGender);
        etPass = findViewById(R.id.etSignUpPass);
        etConfirmPass = findViewById(R.id.etSignUpPassConfirm);
        btSignUp = findViewById(R.id.btSignUpId);
        tvSignUpToLogin = findViewById(R.id.tvSignUpToLogin);
        progressBar = findViewById(R.id.pbSignUpProgressBar);
        tvOverAllErrorShower = findViewById(R.id.tvSignUpErrorMessage);
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_gender_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        final ImageView ivDialogMale = dialog.findViewById(R.id.ivDialogMale);
        final ImageView ivDialogFemale = dialog.findViewById(R.id.ivDialogFemale);
        final ImageView ivDialogCancel = dialog.findViewById(R.id.ivDialogCancel);


        // Click for the male select form dialog
        ivDialogMale.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                selectGender = "Male";
                tvSelectGender.setText(selectGender);
                dialog.dismiss();
            }
        });

        // Click for the female select form dialog
        ivDialogFemale.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                selectGender = "Female";
                tvSelectGender.setText(selectGender);
                dialog.dismiss();
            }
        });


        // dialog cancel button
        ivDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tvSelectGender.setText(selectGender);
            }
        });

        dialog.show();
    }

}
