package com.getrentbd.getrent.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getrentbd.getrent.R;
import com.getrentbd.getrent.activitys.MyPostDetails;
import com.getrentbd.getrent.modelClass.OwnPostList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnPostAdapter extends RecyclerView.Adapter<OwnPostAdapter.PostViewHolder> {
    private Context context;
    private ArrayList<OwnPostList> ownLists;

    public OwnPostAdapter(Context context, ArrayList<OwnPostList> list ) {
        this.context = context;
       this.ownLists = list;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.mini_ad_layout, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, final int i) {

        final OwnPostList ad = ownLists.get(i);
        final String p_id = ad.getId();
        final String Seat = ad.getSeat();
        final String floor_no = ad.getNum_floor();
        final String Location = ad.getLocation();
        final String From = ad.getMonth();
        final String Rent = ad.getRent();
        final String HouseCatagory = ad.getCatagory();
        final String timeDate = ad.getTimeDate();
        final String image_url = ad.getUrl();
        final String address = ad.getAddress();
        final String facilities = ad.getDescription();
        final String Name = ad.getName();
        final String phn_num = ad.getNumber();
        final String email = ad.getEmail();
        final String gender = ad.getGender();
        long seconds = Long.parseLong(timeDate);

        postViewHolder.ivFavorite.setVisibility(View.INVISIBLE);

        try {
            //Catagory Wise Show Operation
            double SeatNumber = Double.parseDouble(Seat);
            if (HouseCatagory.equals("Mess") || HouseCatagory.equals("Hostel")) {
                if (SeatNumber > 1) {
                    postViewHolder.seat.setText(Seat + " Seat's Available");
                } else {
                    postViewHolder.seat.setText(Seat + " Seat Available");
                }

            } else {
                if (SeatNumber > 1) {
                    postViewHolder.seat.setText(Seat + " " + HouseCatagory + "'s Available");
                } else {
                    postViewHolder.seat.setText(Seat + " " + HouseCatagory + " Available");
                }

            }
        } catch (Exception e) {
            Log.e("ADDAPTER TRYCATCH",e.toString());
        }
        postViewHolder.location.setText("Location : " + Location);
        postViewHolder.from.setText("From : " + From);
        postViewHolder.rent.setText(Rent);
        postViewHolder.name.setText(Name);
        postViewHolder.tvDateTime.setText(getTimeAgoDate(seconds));

        //User image setting gender wise
        if (gender.toLowerCase().equals("male")){
            postViewHolder.userImage.setImageResource(R.drawable.profile);
        }else {
            postViewHolder.userImage.setImageResource(R.drawable.femalepng);
        }


        //Intent Value To MyPostDetails Activity
        postViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyPostDetails.class);
                intent.putExtra("Position", String.valueOf(i));
                intent.putExtra("Id", p_id);
                intent.putExtra("Seat", Seat);
                intent.putExtra("Floor_no", floor_no);
                intent.putExtra("Location", Location);
                intent.putExtra("From", From);
                intent.putExtra("Rent", Rent);
                intent.putExtra("Catagory", HouseCatagory);
                intent.putExtra("TimeDate", timeDate);
                intent.putExtra("Url", image_url);
                intent.putExtra("Address", address);
                intent.putExtra("Description", facilities);
                intent.putExtra("Name", Name);
                intent.putExtra("Number", phn_num);
                intent.putExtra("Email", email);
                intent.putExtra("Gender", gender);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });
    }

    private String getTimeAgoDate(long pastTimeStamp) {

        // for 2 min ago   use  DateUtils.MINUTE_IN_MILLIS
        // for 2 sec ago   use  DateUtils.SECOND_IN_MILLIS
        // for 1 hours ago use  DateUtils.HOUR_IN_MILLIS

        long now = System.currentTimeMillis();

        if (now - pastTimeStamp < 1000) {
            pastTimeStamp = pastTimeStamp + 1000;
        }
        CharSequence ago =
                DateUtils.getRelativeTimeSpanString(pastTimeStamp, now, DateUtils.SECOND_IN_MILLIS);
        return ago.toString();
    }

    @Override
    public int getItemCount() {
        return ownLists.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView seat, location, from, rent, name, det;
        private TextView tvDateTime;
        private ImageView ivFavorite;
        private LinearLayout linearLayout;
        private CircleImageView userImage;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            seat = itemView.findViewById(R.id.tvminiavailableid);
            location = itemView.findViewById(R.id.tvminilocationid);
            from = itemView.findViewById(R.id.tvminimonthfromid);
            rent = itemView.findViewById(R.id.tvminirentid);
            userImage = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.tvminiusernameid);
            linearLayout = itemView.findViewById(R.id.linearminiadid);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }
    }
}
