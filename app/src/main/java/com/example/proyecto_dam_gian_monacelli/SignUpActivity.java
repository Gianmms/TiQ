package com.example.proyecto_dam_gian_monacelli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_dam_gian_monacelli.databinding.ActivityRegistroBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivityRegistroBinding binding;
    DataBaseHelper dataBaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding =ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataBaseHelper = new DataBaseHelper(this);


        binding.createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =binding.emailRegister.getText().toString();
                String password =binding.passwordRegister.getText().toString();
                String confirmPassword =binding.confirmPassword.getText().toString();

                if (email.equals("") || password.equals("") || confirmPassword.equals(""))
                    Toast.makeText(SignUpActivity.this, R.string.AllFieldsAreMandatory, Toast.LENGTH_SHORT).show();
                else  {
                    if (password.equals(confirmPassword)){
                        Boolean checkUserEmail = dataBaseHelper.checkEmail(email);

                        if (!checkUserEmail){
                            Boolean insert = dataBaseHelper.insertData(email, password);

                            if (insert){
                                Toast.makeText(SignUpActivity.this, R.string.AccountCreatedSuccessfully, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(SignUpActivity.this, R.string.AccountCreationFailed, Toast.LENGTH_SHORT).show();
                            }

                            } else {
                                Toast.makeText(SignUpActivity.this, R.string.UserAlreadyExists, Toast.LENGTH_SHORT).show();

                            }
                        } else  {
                        Toast.makeText(SignUpActivity.this, R.string.PasswordIncorrect, Toast.LENGTH_SHORT).show();

                    }

                    }
                }


        });

        binding.loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


          }


  }