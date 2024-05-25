package com.example.proyecto_dam_gian_monacelli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String SIGNUP_DATABASE_NAME = "Signup.db";
    public static final String TIME_STAMPS_TABLE_NAME = "timestamps";
    private static final String COLUMN_DATE = "date";
    public static final int DATABASE_VERSION = 5;

    public DataBaseHelper(@Nullable Context context) {
        super(context, SIGNUP_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDataBase) {
        MyDataBase.execSQL("Create Table allusers(username Text primary key, password TEXT)");
        MyDataBase.execSQL("Create Table " + TIME_STAMPS_TABLE_NAME + "(username Text, tiq_in_start TEXT, tiq_break TEXT, " + COLUMN_DATE + " DATE)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            // Drop the existing allusers table
            db.execSQL("DROP TABLE IF EXISTS allusers");

            // Create the allusers table with the modified schema
            db.execSQL("CREATE TABLE allusers (username TEXT PRIMARY KEY, tiq_in_start TEXT, tiq_break TEXT)");

            // Increment the version to the latest
            db.setVersion(6);
        }
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

    public Boolean checkPassword(String username, String password) {
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        Cursor cursor = MyDataBase.rawQuery("Select * from allusers where username = ? and password = ? ", new String[]{username, password});

        if (cursor.getCount() > 0) {
            return true;


        } else {
            return false;

        }

    }

    //Obtiene el nombre de usuario

    public String getUsername() {
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        Cursor cursor = MyDataBase.rawQuery("Select username from allusers", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            return null;
        }
    }

    //Obtiene la contraseña
    public String getPassword() {

        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        Cursor cursor = MyDataBase.rawQuery("Select password from allusers", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            return null;
        }
    }

    public Boolean stampTiqInStart(String username, String timestamp, String date) {
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("tiq_in_start", timestamp);
        contentValues.put(COLUMN_DATE, date);  // Guarda la fecha
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


    public String getCurrentLoginTimestamp() { //obtiene el tiempo actual.
        return String.valueOf(System.currentTimeMillis());
    }


    public String getTimestampForDate(String selectedDate) {
        SQLiteDatabase MyDataBase = this.getReadableDatabase();
        String[] columns = {"tiq_in_start", "tiq_break"};
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {selectedDate};

        // Logging de selectedDate
        Log.d("DataBaseHelper", "Selected Date: " + selectedDate);

        Cursor cursor = MyDataBase.query(TIME_STAMPS_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount(); // Get the number of rows returned by the cursor
        Log.d("DataBaseHelper", "Number of rows returned: " + count);

        if (cursor.moveToFirst()) {
            // Obtiene timestamps del cursor
            String tiqInStartMillis = cursor.getString(cursor.getColumnIndexOrThrow("tiq_in_start"));
            String tiqBreakMillis = cursor.getString(cursor.getColumnIndexOrThrow("tiq_break"));

            // Convert timestamps to human-readable format
            String tiqInStart = formatTimestamp(Long.parseLong(tiqInStartMillis));
            String tiqBreak = formatTimestamp(Long.parseLong(tiqBreakMillis));

            Log.d("DataBaseHelper", "Fetched data: " + tiqInStart + ", " + tiqBreak);
            return "Time In: " + tiqInStart + "\nTime Out: " + tiqBreak;
        } else {
            Log.d("DataBaseHelper", "No data found for date: " + selectedDate);
            return "Timestamps not found for the selected date";
        }
    }

    // Método para formatear el timestamp de milisegundos a un formato de fecha legible
    private String formatTimestamp(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(millis));
    }


}
