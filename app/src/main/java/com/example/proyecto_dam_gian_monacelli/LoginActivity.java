package com.example.proyecto_dam_gian_monacelli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_dam_gian_monacelli.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String loginTimestamp = sharedPreferences.getString("loginTimestamp", ""); // consigue el timestamp del login guardado
        long currentTime = System.currentTimeMillis();// obtiene el tiempo actual en milisegundos
        long sessionDuration = 7 * 24 * 60 * 60 * 1000;  // 7 dias en milisegundos

        //Chequea si la sesión es válida
        if (!loginTimestamp.equals("") && currentTime - Long.parseLong(loginTimestamp) <= sessionDuration) {
            // Sesión es válida, redirige automáticamente al MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();  // Finaliza el  LoginActivity  para prevenir volver allí, sin desloguearse
        }

        dataBaseHelper = new DataBaseHelper(this);

        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailLogin.getText().toString();
                String password = binding.passwordLogin.getText().toString();

                if (email.equals("") || password.equals(""))
                    Toast.makeText(LoginActivity.this, R.string.AllFieldsAreMandatory, Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkCredentials = dataBaseHelper.checkPassword(email, password);
                    if (checkCredentials) {
                        Toast.makeText(LoginActivity.this, R.string.LoginSuccessful, Toast.LENGTH_SHORT).show();

                        // Almacena el tiempo del login en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loginTimestamp", dataBaseHelper.getCurrentLoginTimestamp());
                        editor.apply();
                        // Procede a la  MainActivity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.IncorrectCredentials, Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
        binding.SignUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


}