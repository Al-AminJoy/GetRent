package com.getrentbd.getrent.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.LocaleData;
import android.util.Log;

import com.getrentbd.getrent.modelClass.FavoriteItemsList;
import com.getrentbd.getrent.modelClass.RentItemsList;

import java.util.ArrayList;
import java.util.List;


public class SqliteDB extends SQLiteOpenHelper {


    private final static String DATABASE_NAME = "ItemList.db";
    private final static int VERSION = 1;
    private final static String TABLE_NAME = "listofItem";
    private final static String ID = "id";
    private final static String POST_ID = "postid";
    private final static String USER_NUMBER = "user_number";
    private final static String NUMBEROFSEAT = "seat";
    private final static String FLOORNO = "floorno";
    private final static String LOCATION = "location";
    private final static String MONTH_NAME = "month_name";
    private final static String RENT = "house_rent";
    private final static String CATEGORY = "category";
    private final static String TIME_DATE = "timedate";
    private final static String IMAGE_URL = "image_url";
    private final static String ADDRESS = "address";
    private final static String FACILITIES = "facilities";
    private final static String NAME = "name";
    private final static String MOBILE_NO = "mobile_no";
    private final static String EMAIL = "email";
    private final static String GENDER = "gender";

    private Context context;

    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    private final static String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + POST_ID + " VARCHAR(255),"
            + USER_NUMBER + " VARCHAR(255),"
            + NUMBEROFSEAT + " VARCHAR(255),"
            + FLOORNO + " VARCHAR(255),"
            + LOCATION + " VARCHAR(255),"
            + MONTH_NAME + " VARCHAR(255),"
            + RENT + " VARCHAR(255),"
            + CATEGORY + " VARCHAR(255),"
            + TIME_DATE + " VARCHAR(255),"
            + IMAGE_URL + " VARCHAR(255),"
            + ADDRESS + " VARCHAR(255),"
            + FACILITIES + " VARCHAR(5000),"
            + NAME + " VARCHAR(255),"
            + MOBILE_NO + " VARCHAR(255),"
            + EMAIL + " VARCHAR(255),"
            + GENDER + " VARCHAR(255));";



    private final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SqliteDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public long itemInsert(String post_id,String user_num,String no_seat, String floor_no, String loca, String from, String rent,
                           String category, String timeDate,String url, String address,
                           String facilities,String name,String phn_no,String email,String gender) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, post_id);
        contentValues.put(USER_NUMBER, user_num);
        contentValues.put(NUMBEROFSEAT, no_seat);
        contentValues.put(FLOORNO, floor_no);
        contentValues.put(LOCATION, loca);
        contentValues.put(MONTH_NAME, from);
        contentValues.put(RENT, rent);
        contentValues.put(CATEGORY, category);
        contentValues.put(TIME_DATE, timeDate);
        contentValues.put(IMAGE_URL, url);
        contentValues.put(ADDRESS, address);
        contentValues.put(FACILITIES, facilities);
        contentValues.put(NAME, name);
        contentValues.put(MOBILE_NO, phn_no);
        contentValues.put(EMAIL, email);
        contentValues.put(GENDER, gender);
        long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return rowId;
    }


    public List<FavoriteItemsList> favRentList(String number) {
        List<FavoriteItemsList> data = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from listofItem where "+ USER_NUMBER +"=?", new String[]{number});
        while (cursor.moveToNext()) {
            data.add(new FavoriteItemsList(
                    cursor.getString(1),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getString(14),
                    cursor.getString(15),
                    cursor.getString(16)));
        }
        return data;
    }


    public long favoriteCheck(String postID,String number) {
        long id = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("select * from listofItem where " + POST_ID + "=?"+"AND "+ USER_NUMBER + "=?"  , new String[]{postID,number});
        while (cursor.moveToNext()) {
            id = cursor.getLong(1);
        }
        return id;
    }

    public boolean productDelete(String id) {
        return sqLiteDatabase.delete(TABLE_NAME, POST_ID + "=?", new String[]{id}) > 0;
    }


}