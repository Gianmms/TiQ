package com.example.proyecto_dam_gian_monacelli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    TextView usernameTextView = findViewById(R.id.usernameTextView);
    TextView passwordTextView = findViewById(R.id.passwordTextView);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);





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














        dbHelper = new DataBaseHelper(this);

        // Assuming you have an instance of DataBaseHelper named dbHelper
        String username = "user123"; // Replace with the actual username
        String password = "password123"; // Replace with the actual password



        // Retrieve the username and password from the database using the methods in DataBaseHelper
        String retrievedUsername = dbHelper.getUsernameForPassword(password);
        String retrievedPassword = dbHelper.getPasswordForUsername(username);

        // Set the retrieved username and password in the TextViews
        usernameTextView.setText("Username: " + retrievedUsername);
        passwordTextView.setText("Password: " + retrievedPassword ); // Display asterisks instead of the actual password

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
                    passwordTextView.setText("Password: " + retrievedPassword);
                    isPasswordVisible = true;
                }
            }
        });
    }
}