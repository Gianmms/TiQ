package com.example.proyecto_dam_gian_monacelli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {


    DataBaseHelper dataBaseHelper;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView passwordTextView = findViewById(R.id.passwordTextView);

        Button showCredentialsButton = findViewById(R.id.ShowCredentialsButton);

        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton calendarButton = findViewById(R.id.calendarButton);
        dataBaseHelper = new DataBaseHelper(this);

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





        //METODO PARA OBTENER PASSWORD Y USERNAME


        showCredentialsButton.setOnClickListener(new View.OnClickListener() {
            boolean isInformationDisplayed = false;

            @Override
            public void onClick(View v) {
                if (isInformationDisplayed) {
                    usernameTextView.setText("");
                    passwordTextView.setText("");
                    showCredentialsButton.setText(R.string.ShowCredentialsButtonText);
                    isInformationDisplayed = false;
                } else {
                    String usernameRetrieved = dataBaseHelper.getUsername();
                    usernameTextView.setText(usernameRetrieved);
                    String passwordRetrieved = dataBaseHelper.getPassword();
                    passwordTextView.setText(passwordRetrieved);
                    showCredentialsButton.setText(R.string.HideCredentialsButtonText);
                    isInformationDisplayed = true;
                }
            }
        });


    }
}