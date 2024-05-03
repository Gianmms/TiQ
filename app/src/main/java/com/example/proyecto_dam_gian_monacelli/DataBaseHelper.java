package com.example.proyecto_dam_gian_monacelli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String SIGNUP_DATABASE_NAME = "Signup.db";
    public static final String TIME_STAMPS_TABLE_NAME = "timestamps";

    public DataBaseHelper(@Nullable Context context) {
        super(context, SIGNUP_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDataBase) {
        MyDataBase.execSQL("Create Table allusers(username Text primary key, password TEXT)");
        MyDataBase.execSQL("Create Table " + TIME_STAMPS_TABLE_NAME + "(username Text, tiq_in_start TEXT, tiq_break TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDataBase, int i, int i1) {
        MyDataBase.execSQL("drop Table if exists allusers");
    }

    public Boolean insertData(String username, String password) {
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDataBase.insert("allusers", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;

        }

    }

    public Boolean checkUsername(String username) {

        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        Cursor cursor = MyDataBase.rawQuery("Select * from allusers where username = ?", new String[]{username});

        if (cursor.getCount() > 0) {
            return true;


        } else {
            return false;

        }
    }

    public Boolean checkEmailPassword(String username, String password) {
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        Cursor cursor = MyDataBase.rawQuery("Select * from allusers where username = ? and password = ? ", new String[]{username, password});

        if (cursor.getCount() > 0) {
            return true;


        } else {
            return false;

        }

    }

    public Boolean stampTiqInStart(String username, String timestamp) {
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("tiq_in_start", timestamp);
        long result = MyDataBase.insert(TIME_STAMPS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Boolean stampTiqBreak(String username, String timestamp) {
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tiq_break", timestamp);
        int result = MyDataBase.update(TIME_STAMPS_TABLE_NAME, contentValues, "username = ?", new String[]{username});
        return result > 0;
    }


    public String getCurrentTimestamp() { //obtiene el tiempo actual.
        return String.valueOf(System.currentTimeMillis());
    }


    // Method to retrieve the password for a given username
    public String getPasswordForUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String password = null;

        try {
            Cursor cursor = db.rawQuery("SELECT password FROM allusers WHERE username = ?", new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                password = cursor.getString(0);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            // Log any exceptions that occur
            Log.e("DataBaseHelper", "Error retrieving password for username: " + e.getMessage());
        }

        return password;
    }

    // Method to retrieve the username for a given password
    public String getUsernameForPassword(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = null;

        try {
            Cursor cursor = db.rawQuery("SELECT username FROM allusers WHERE password = ?", new String[]{password});

            if (cursor != null && cursor.moveToFirst()) {
                username = cursor.getString(0);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            // Log any exceptions that occur
            Log.e("DataBaseHelper", "Error retrieving username for password: " + e.getMessage());
        }

        return username;
    }

}