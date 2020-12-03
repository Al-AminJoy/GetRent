package com.getrentbd.getrent.helper;

import android.view.View;

import com.getrentbd.getrent.activitys.FavoriteList;
import com.getrentbd.getrent.modelClass.OwnPostList;
import com.getrentbd.getrent.modelClass.RentItemsList;

import java.util.ArrayList;

public class Common {
    public static final String AppPra="com.getrentbd.getrent";
    public static final String SpId="IdKey";
    public static final String SpName="NameKey";
    public static final String SpEmail="EmailKey";
    public static final String SpNumber="NumberKey";
    public static final String SpPass="PassKey";
    public static final String SpGender="GenderKey";
    public static final String SpTitle="Title";
    public static final String SpUpdatePostId="UpdateIdKey";
    public static final String SpDeletePostId="DeleteIdKey";
    public static final String SpUpdatedProfileNumber="UpdatedProfileNumberKey";
    public static final String SpUpdatedMonth="MonthKey";
    public static final String SpUpdatedCatagory="CatagoryKey";
    public static final String SpUpdatedLocation="LocationKey";
    public static final String SpUpdatedAddress="AddressKey";
    public static final String SpUpdatedRent="RentKey";
    public static final String SpUpdatedSeat="SeatKey";
    public static final String SpUpdatedDetails="DetailsKey";

    public static  String spId;
    public static  String spName;
    public static  String spEmail;
    public static  String spNumber;
    public static  String spGender;

    public static ArrayList<RentItemsList> spList;
    public static ArrayList<OwnPostList> ownLists;

    public static void noData(int size){
        if (size == 0){
            FavoriteList.tvNoFavorites.setVisibility(View.VISIBLE);
        }
    }

}
