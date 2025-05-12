package com.thinkbold.fwb.offlinedatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class MembersDb extends SQLiteOpenHelper {

    String TABLE_NAME_MEMBERS = "members";
    protected Context context;
    public MembersDb(Context context){
        super(context, "members.db",null, 1 );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_country;
        query_country = "CREATE TABLE " + TABLE_NAME_MEMBERS +"(id TEXT, first_name TEXT, middle_name TEXT, surname TEXT, phone_no TEXT)";
        db.execSQL(query_country);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query_country;
        query_country = "DROP TABLE IF EXISTS " + TABLE_NAME_MEMBERS;
        db.execSQL(query_country);
        onCreate(db);
    }

    public void InsertMembers(String id, String first_name, String middle_name, String surname, String phone_no){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues row = new ContentValues();
            row.put("id", id);
            row.put("first_name", first_name);
            row.put("middle_name", middle_name);
            row.put("surname", surname);
            row.put("phone_no", phone_no);

            long chk = db.insert(TABLE_NAME_MEMBERS, null, row);
            if (chk != 0){

            }else {
                Toast.makeText(context, "Syn Failed, Try Restart the App", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllMembers(){
        ArrayList<String> allcountries = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MEMBERS, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            allcountries.add(cursor.getString(cursor.getColumnIndex("surname")));
            cursor.moveToNext();
        }
        cursor.close();
        return allcountries;
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllMiD(){
        ArrayList<String> allcountries = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MEMBERS, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            allcountries.add(cursor.getString(cursor.getColumnIndex("id")));
            cursor.moveToNext();
        }
        cursor.close();
        return allcountries;
    }
    public void deleteAllDataMembers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_MEMBERS, null, null);
    }
}
