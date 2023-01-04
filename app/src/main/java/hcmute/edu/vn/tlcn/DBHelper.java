package hcmute.edu.vn.tlcn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //Initialize Variable

    private static final String DATABASE_NAME = "flight_database";
    private static final String USER_TABLE = "users";
    private static final String PROFILE_TABLE = "profiles";
    private static final String INFOR_TABLE = "infors";
    public static final String BOOKING_TABLE = "bookings";
    public static final String NOTICE_TABLE = "notices";
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Tables
        String users = "CREATE TABLE "+ USER_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, username TEXT, password TEXT, email TEXT, phone String)";
        String profiles = "CREATE TABLE "+ PROFILE_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, uid INTEGER, fullname TEXT, bob TEXT, gender TEXT)";
        String infors = "CREATE TABLE "+ INFOR_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,uid INTERGER, tid TEXT, name TEXT, CMND String, phone String)";
        String tickets = "CREATE TABLE "+ BOOKING_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "Iid INTERGER, startpoint TEXT, endpoint TEXT, date TEXT, time TEXT,type TEXT," +
                "adult TEXT, " + "children TEXT, " + "baby TEXT, hangbay TEXT)";
        String notices = "CREATE TABLE "+ NOTICE_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,uid INTERGER, content TEXT, day Text, time TEXT)";

        db.execSQL(users);
        db.execSQL(profiles);
        db.execSQL(infors);
        db.execSQL(tickets);
        db.execSQL(notices);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ INFOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ BOOKING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ NOTICE_TABLE);
        onCreate(db);
    }

    //Insert Method
    public boolean insertUser(String username, String password, String email, String phone){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        long result = myDB.insert(USER_TABLE, null, contentValues);
        if(result == -1){
            return  false;
        }
        else {
            return true;
        }
    }

    //Truy van khong tra ket qua: Create, Insert, Update, Delete..
    public void  QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    //Truy van co tra ket qua: Select
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public boolean insertProfile(Integer uid, String fullname, String bob, String gender){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", uid);
        contentValues.put("fullname", fullname);
        contentValues.put("bob", bob);
        contentValues.put("gender", gender);
        long result = myDB.insert(PROFILE_TABLE, null, contentValues);
        if(result == -1){
            return  false;
        }
        else {
            return true;
        }
    }

    //check Username Password
    public boolean checkUserName(String username){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from "+ USER_TABLE + " where username = ?", new String[] {username});
        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }
    public boolean checkUserNamePassword(String username, String password){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from "+ USER_TABLE + " where username = ? and password = ?", new String[] {username,password});
        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    // Get method
    public int getUIDbyName(String name){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("Select id from "+ USER_TABLE+" where username LIKE \"%" + name + "%\"", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return -1;
        }
    }
    public Cursor getProfilebyUID(Integer id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("Select fullname, bob, gender from "+ PROFILE_TABLE+" where uid = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        } else {
            return null;
        }
    }
    public Cursor getContactbyUID(Integer uid){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("Select email, phone from "+ USER_TABLE+" where id = ?", new String[]{String.valueOf(uid)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        } else {
            return null;
        }
    }
    //Update
    public boolean UpdateProfile(Integer id, String name, String bob, String gender){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", name);
        contentValues.put("bob", bob);
        contentValues.put("gender", gender);

        long result = myDB.update(PROFILE_TABLE, contentValues, "uid =?", new String[]{String.valueOf(id)});
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean UpdateContact(Integer id, String email, String numphone){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("phone", numphone);

        long result = myDB.update(USER_TABLE, contentValues, "id =?", new String[]{String.valueOf(id)});
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    // INFOR
    public boolean insertInfor(Integer Uid, String name, String CMND, String phone, String tid){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", Uid);
        contentValues.put("name", name);
        contentValues.put("CMND", CMND);
        contentValues.put("phone", phone);
        contentValues.put("tid", tid);
        long result = myDB.insert(INFOR_TABLE, null, contentValues);
        if(result == -1){
            return  false;
        }
        else {
            return true;
        }
    }
    public int getIidbyName(String ticketid){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("Select id from "+ INFOR_TABLE+" where tid LIKE \"%" + ticketid + "%\"", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return -1;
        }
    }
    //BOOKING
    public boolean insertBooking(Integer Iid, String startpoint, String endpoint, String date, String time, String type,
                                 String nguoilon, String treem, String embe, String airline){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Iid", Iid);
        contentValues.put("startpoint", startpoint);
        contentValues.put("endpoint", endpoint);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("type", type);
        contentValues.put("adult", nguoilon);
        contentValues.put("children", treem);
        contentValues.put("baby", embe);
        contentValues.put("hangbay", airline);
        long result = myDB.insert(BOOKING_TABLE, null, contentValues);
        if(result == -1){
            return  false;
        }
        else {
            return true;
        }
    }
    public Cursor getBookbyUid(Integer uid){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("Select bookings.startpoint, bookings.endpoint, bookings.date, bookings.time, bookings.type,  bookings.hangbay \n" +
                "from bookings INNER JOIN  (Select * from infors where infors.uid = ?) as ifs\n" +
                "on bookings.Iid = ifs.id ORDER BY bookings.id DESC", new String[]{String.valueOf(uid)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        } else {
            return null;
        }
    }
    //NOTICE
    public boolean insertNotice(String content, String day, String time, int uid){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        contentValues.put("day", day);
        contentValues.put("time", time);
        contentValues.put("uid", uid);
        long result = myDB.insert(NOTICE_TABLE, null, contentValues);
        if(result == -1){
            return  false;
        }
        else {
            return true;
        }
    }
    public Cursor getNoticebyUID(Integer uid){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("Select * from "+ NOTICE_TABLE +" where uid = ? ORDER BY id DESC", new String[]{String.valueOf(uid)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        } else {
            return null;
        }
    }
}
