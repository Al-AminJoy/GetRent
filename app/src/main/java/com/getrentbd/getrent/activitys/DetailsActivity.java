package com.getrentbd.getrent.activitys;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getrentbd.getrent.R;
import com.squareup.picasso.Picasso;

import static com.getrentbd.getrent.helper.Apis.baseurl;

public class DetailsActivity extends AppCompatActivity {
    private TextView name, catagory, seat, from, location, rent, details,
            number, address, email, tvFloor_no;
    private String Name, Catagory, Seat, From, Location, Rent, Details,
            Number, Address, Email, Floor_no,Image;
    private ImageView ivCall;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivProfile, ivPost, ivHome;
    private TextView tvRentTitle;
    private LinearLayout linearLayout;
    private LinearLayout layoutViewImage;
    private ImageView ivImage;
    private TextView tvOwnerDetails;
    private LinearLayout layoutOwnerDetails;
    private boolean isShow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        idFinder();
        bottomNavOperation();
        takeIntentValue();
        setTextOperations();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       //Applying Conditions on Details
        if (!Details.isEmpty()) {
            details.setText(Details);
            linearLayout.setVisibility(View.VISIBLE);
        }
        //Applying Conditions on Seat
        if (Seat.equals("1")){
                tvRentTitle.setText("Rent");
                seat.setText(Seat+" "+Catagory);
        }else {
            if (Catagory.equals("Mess") || Catagory.equals("Hostel")) {
                seat.setText(Seat+" Seat");
                tvRentTitle.setText("Rent Per Seat");
            }else {
                tvRentTitle.setText("Rent Per " + Catagory);
                seat.setText(Seat+" "+Catagory);
            }
        }

        if (!Image.equals("null")){
            Picasso.get().load(baseurl+Image).fit().placeholder(R.drawable.loading_text).into(ivImage);
        }else {
            layoutViewImage.setVisibility(View.GONE);
        }

        layoutOwnerDetails.setVisibility(View.GONE);
        final Drawable imageUp = getApplicationContext().getResources().getDrawable( R.drawable.ic_arrow_up );
        final Drawable imagedown = getApplicationContext().getResources().getDrawable( R.drawable.ic_arrow_down );
        imageUp.setBounds( 0, 0, imageUp.getIntrinsicWidth(), imageUp.getIntrinsicHeight() );
        imagedown.setBounds( 0, 0, imagedown.getIntrinsicWidth(), imagedown.getIntrinsicHeight() );
        tvOwnerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow){
                    layoutOwnerDetails.setVisibility(View.VISIBLE);
                    tvOwnerDetails.setCompoundDrawables(null,null,imageUp,null);
                    isShow = false;

                }else{
                    layoutOwnerDetails.setVisibility(View.GONE);
                    tvOwnerDetails.setCompoundDrawables(null,null,imagedown,null);
                    isShow = true;
                }

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + Number));
                startActivity(callIntent);
            }
        });



    }

    private void setTextOperations() {
        tvTitle.setText("Details");
        catagory.setText("About The "+Catagory);
        tvFloor_no.setText(Floor_no);
        rent.setText(Rent);
        name.setText(Name);
        from.setText(From);
        location.setText(Location);
        number.setText(Number);
        address.setText(Address);
        email.setText(Email);
    }

    private void takeIntentValue() {

        Catagory = getIntent().getStringExtra("Catagory");
        Floor_no = getIntent().getStringExtra("Floor_no");
        Seat = getIntent().getStringExtra("Seat");
        From = getIntent().getStringExtra("From");
        Location = getIntent().getStringExtra("Location");
        Rent = getIntent().getStringExtra("Rent");
        Details = getIntent().getStringExtra("Description");
        Number = getIntent().getStringExtra("Number");
        Address = getIntent().getStringExtra("Address");
        Email = getIntent().getStringExtra("Email");
        Name = getIntent().getStringExtra("Name");
        Image = getIntent().getStringExtra("Image");
    }

    private void bottomNavOperation() {
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, HomePage.class);

                startActivity(intent);
                finish();
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, OwnPost.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void idFinder() {

        layoutViewImage = findViewById(R.id.layoutViewImage);
        ivImage = findViewById(R.id.ivImage);
        catagory = findViewById(R.id.tvdetailscatagoryid);
        tvFloor_no = findViewById(R.id.tvDetailsFloorId);
        seat = findViewById(R.id.tvdetailsseatid);
        from = findViewById(R.id.tvdetailsmonthid);
        location = findViewById(R.id.tvdetailslocationid);
        rent = findViewById(R.id.tvdetailsrentid);
        details = findViewById(R.id.tvdetailshomedetailsid);
        name = findViewById(R.id.tvdetailsnameid);
        email = findViewById(R.id.tvdetailsemailid);
        number = findViewById(R.id.tvdetailsnumberid);
        address = findViewById(R.id.tvdetailsaddressid);
        ivCall = findViewById(R.id.ivCall);

        tvOwnerDetails = findViewById(R.id.tvOwnerDetails);
        layoutOwnerDetails = findViewById(R.id.layoutOwnerDetails);

        tvRentTitle = findViewById(R.id.tvDetailsRentTitleId);
        tvTitle = findViewById(R.id.tvTitleId);
        tvTitle = findViewById(R.id.tvTitleId);
        linearLayout = findViewById(R.id.linearDesp);
        ivBack = findViewById(R.id.ivBack);
        ivProfile = findViewById(R.id.ivprofileid);
        ivPost = findViewById(R.id.icpostid);
        ivHome = findViewById(R.id.ivHomeId);
    }

}
