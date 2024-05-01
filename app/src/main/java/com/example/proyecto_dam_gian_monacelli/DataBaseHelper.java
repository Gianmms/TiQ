package com.example.proyecto_dam_gian_monacelli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String SIGNUP_DATABASE_NAME ="Signup.db";
    public static final String TIME_STAMPS_TABLE_NAME = "timestamps";

    public DataBaseHelper(@Nullable Context context) {
        super(context, SIGNUP_DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDataBase) {
        MyDataBase.execSQL("Create Table allusers(email Text primary key, password TEXT)");
        MyDataBase.execSQL("Create Table " + TIME_STAMPS_TABLE_NAME + "(email Text, time_in TEXT, time_out TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDataBase, int i, int i1) {
        MyDataBase.execSQL("drop Table if exists allusers");
    }

    public Boolean insertData (String email, String password){
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result =MyDataBase.insert("allusers", null, contentValues );
                if(result == -1) {
                    return false;
                }else {
                    return true;

                }

    }

    public Boolean checkEmail (String email){

        SQLiteDatabase MyDataBase  = this.getWritableDatabase();
        Cursor cursor = MyDataBase.rawQuery("Select * from allusers where email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            return true;


        }else {
            return false;

        }
    }

public Boolean checkEmailPassword(String email, String password){
    SQLiteDatabase MyDataBase  = this.getWritableDatabase();
    Cursor cursor = MyDataBase.rawQuery("Select * from allusers where email = ? and password = ? ", new String[]{email, password});

    if (cursor.getCount() > 0) {
        return true;


    }else {
        return false;

    }

}

    public Boolean stampTimeIn(String email, String timeIn){
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("time_in", timeIn);
        long result = MyDataBase.insert(TIME_STAMPS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Boolean stampTimeOut(String email, String timeOut){
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("time_out", timeOut);
        int result = MyDataBase.update(TIME_STAMPS_TABLE_NAME, contentValues, "email = ?", new String[]{email});
        return result > 0;
    }

    public String getCurrentTimestamp() { //obtiene el tiempo actual.
        return String.valueOf(System.currentTimeMillis());
    }




    // Method to retrieve the password for a given username
    public  String getPasswordForUsername(String username) {
        SQLiteDatabase MyDataBase = this.getReadableDatabase();
        String[] columns = {"password"};
        String selection = "email=?";
        String[] selectionArgs = {username};
        Cursor cursor = MyDataBase.query("allusers", columns, selection, selectionArgs, null, null, null);
        String password = "";
        if (cursor.moveToFirst()) {
            password = cursor.getString(0); // Assuming password is stored in the first column
        }
        cursor.close();
        return password;
    }

    // Method to retrieve the username for a given password
    public  String getUsernameForPassword(String password) {
        SQLiteDatabase MyDataBase = this.getReadableDatabase();
        String[] columns = {"email"};
        String selection = "password=?";
        String[] selectionArgs = {password};
        Cursor cursor = MyDataBase.query("allusers", columns, selection, selectionArgs, null, null, null);
        String username = "";
        if (cursor.moveToFirst()) {
            username = cursor.getString(0); // Assuming username is stored in the first column
        }
        cursor.close();
        return username;
    }


}
