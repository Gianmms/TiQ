package com.example.proyecto_dam_gian_monacelli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView passwordTextView = findViewById(R.id.passwordTextView);

        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton calendarButton = findViewById(R.id.calendarButton);


        //Abre MainActivity(home)
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir CalendarActivity
                setContentView(R.layout.activity_main);
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        //Abre CalendarActivity
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir CalendarActivity
                setContentView(R.layout.activity_calendar);
                Intent intent = new Intent(ProfileActivity.this, CalendarActivity.class);
                startActivity(intent);

            }
        });


        String retrievedUsername;
        String retrievedPassword;

        try (DataBaseHelper dbHelper = new DataBaseHelper(ProfileActivity.this)) {


            // Assuming you have an instance of DataBaseHelper named dbHelper
            String username = ""; // Replace with the actual username
            String password = ""; // Replace with the actual password


            // Retrieve the username and password from the database using the methods in DataBaseHelper
            retrievedUsername = dbHelper.getUsernameForPassword(password);
            retrievedPassword = dbHelper.getPasswordForUsername(username);

            Log.d("ProfileActivity", "Retrieved Username: " + retrievedUsername);
            Log.d("ProfileActivity", "Retrieved Password: " + retrievedPassword);
        }

        // Set the retrieved username and password in the TextViews
        usernameTextView.setText(retrievedUsername);
        passwordTextView.setText(retrievedPassword); // Display asterisks instead of the actual password

        // Add an option to show the password when clicked
        passwordTextView.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // If the password is currently visible, hide it
                    passwordTextView.setText("Password: ********");
                    isPasswordVisible = false;
                } else {
                    // If the password is currently hidden, show it
                    passwordTextView.setText(retrievedPassword);
                    isPasswordVisible = true;
                }
            }
        });
    }
}