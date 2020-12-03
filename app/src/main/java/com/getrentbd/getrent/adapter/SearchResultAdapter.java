package com.getrentbd.getrent.adapter;

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

import com.getrentbd.getrent.activitys.DetailsActivity;
import com.getrentbd.getrent.R;
import com.getrentbd.getrent.helper.SqliteDB;
import com.getrentbd.getrent.modelClass.FilterSearchModelClass;
import com.getrentbd.getrent.modelClass.RentItemsList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getrentbd.getrent.helper.Common.spNumber;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private Context context;
    private ArrayList<FilterSearchModelClass> itemslist;
    private SqliteDB sqliteDB;

    public SearchResultAdapter(Context context, ArrayList<FilterSearchModelClass> list) {
        this.context = context;
        this.itemslist = list;

    }

    @NonNull
    @Override
    public SearchResultAdapter.SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        sqliteDB = new SqliteDB(context);
        View view = layoutInflater.inflate(R.layout.mini_ad_layout, viewGroup, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultAdapter.SearchResultViewHolder searchResultViewHolder, final int i) {
        final FilterSearchModelClass ad = itemslist.get(i);
        final String p_id = ad.getId();
        final String seat = ad.getSeat();
        final String floor_no = ad.getNum_floor();
        final String location = ad.getLocation();
        final String from = ad.getMonth();
        final String rent = ad.getRent();
        final String catagory = ad.getCatagory();
        final String timeDate = ad.getTimeDate();
        final String imge_url = ad.getUrl();
        final String address = ad.getAddress();
        final String facilities = ad.getDescription();
        final String name = ad.getName();
        final String phn_num = ad.getNumber();
        final String email = ad.getEmail();
        final String gender = ad.getGender();

        long seconds = Long.parseLong(timeDate);

        if (sqliteDB.favoriteCheck(p_id,spNumber) != Integer.parseInt(p_id)) {
            searchResultViewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_border);
        }else {
            //Log.e("POSTID",sqliteDB.favoriteCheck(p_id,spNumber)+" "+p_id);
            searchResultViewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_full);
        }

        try {
            //Catagory Wise Show Operation
            double SeatNumber = Double.parseDouble(seat);
            if (catagory.equals("Mess") || catagory.equals("Hostel")) {
                if (SeatNumber > 1) {
                    searchResultViewHolder.seat.setText(seat + " Seat's Available");
                } else {
                    searchResultViewHolder.seat.setText(seat + " Seat Available");
                }

            } else {
                if (SeatNumber > 1) {
                    searchResultViewHolder.seat.setText(seat + " " + catagory + "'s Available");
                } else {
                    searchResultViewHolder.seat.setText(seat + " " + catagory + " Available");
                }

            }
        } catch (Exception e) {
            Log.e("ADDAPTER TRYCATCH",e.toString());
        }
        searchResultViewHolder.location.setText("Location : " + location);
        searchResultViewHolder.from.setText("From : " + from);
        searchResultViewHolder.rent.setText(rent);
        searchResultViewHolder.name.setText(name);
        searchResultViewHolder.tvDateTime.setText(getTimeAgoDate(seconds));

        //User image setting gender wise
        if (gender.toLowerCase().equals("male")){
            searchResultViewHolder.userImage.setImageResource(R.drawable.profile);
        }else {
            searchResultViewHolder.userImage.setImageResource(R.drawable.femalepng);
        }

        /** Favorite Button click bellow */
        searchResultViewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sqliteDB.favoriteCheck(p_id,spNumber) == 0){
                    searchResultViewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_full);
                    sqliteDB.itemInsert(p_id,spNumber,seat,floor_no,location,from,rent,catagory,timeDate,imge_url,address,facilities,name,phn_num,email,gender);
                    sqliteDB.close();
                }else {
                    sqliteDB.productDelete(String.valueOf(sqliteDB.favoriteCheck(p_id,spNumber)));
                    searchResultViewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        });

        //Intent Value To Details Activity
        searchResultViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("Floor_no", floor_no);
                intent.putExtra("Seat", seat);
                intent.putExtra("Location", location);
                intent.putExtra("From", from);
                intent.putExtra("Rent", rent);
                intent.putExtra("Name", name);
                intent.putExtra("Number", phn_num);
                intent.putExtra("Catagory", catagory);
                intent.putExtra("Address", address);
                intent.putExtra("Description", facilities);
                intent.putExtra("Email", email);
                intent.putExtra("Image", imge_url);
                context.startActivity(intent);
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
        return itemslist.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView seat, location, from, rent, name, tvDateTime;
        LinearLayout linearLayout;
        ImageView ivFavorite;
        CircleImageView userImage;
        public SearchResultViewHolder(@NonNull View itemView) {
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
