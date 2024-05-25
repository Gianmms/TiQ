package com.example.proyecto_dam_gian_monacelli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView Tiq_In_Time, Tiq_Break_Time;
    Button Tiq_In_Start, Tiq_Stop, Tiq_Break;
    CountDownTimer countDownTimer, breakCountDownTimer;
    boolean isTimerRunning = false; //Flag para seguir el estado del contador  principal
    boolean isBreakTimerRunning = false; // Flag para seguir el estado del contador de break
    long timeCounter = 0; // for time in milliseconds
    long breakTimeCounter = 0;
    DataBaseHelper dataBaseHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tiq_In_Time = findViewById(R.id.Tiq_In_Time);
        Tiq_Break_Time = findViewById(R.id.Tiq_Break_Time);
        Tiq_In_Start = findViewById(R.id.Tiq_In_Start);
        Tiq_Stop = findViewById(R.id.Tiq_Stop);
        Tiq_Break = findViewById(R.id.Tiq_Break);
        ImageButton calendarButton = findViewById(R.id.calendarButton);
        ImageButton profileButton = findViewById(R.id.profileButton);
        dataBaseHelper = new DataBaseHelper(this);


        Context context;


        //Abre CalendarActivity
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning || isBreakTimerRunning) {
                    // Arroja un mensaje si el contador está activo, y se quiere cambiar de Activity.
                    Toast.makeText(MainActivity.this, R.string.ToastContadorActivo, Toast.LENGTH_SHORT).show();
                } else {
                    // Crea un Intent para abrir CalendarActivity
                    Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
            }
        });

//Abre ProfileActivity
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning || isBreakTimerRunning) {
                    // Arroja un mensaje si el contador está activo, y se quiere cambiar de Activity.
                    Toast.makeText(MainActivity.this, R.string.ToastContadorActivo, Toast.LENGTH_SHORT).show();
                } else {
                    // Crea un Intent para abrir ProfileActivity
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });


        //Inicia el contador principal
        Tiq_In_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    isTimerRunning = true;
                    Tiq_In_Start.setVisibility(View.GONE);
                    Tiq_Stop.setVisibility(View.VISIBLE);
                    Tiq_Break.setVisibility(View.VISIBLE);
                    Tiq_Break_Time.setVisibility(View.VISIBLE);

                    // Obtiene username y timestamp
                    String usernameRetrieved = dataBaseHelper.getUsername();
                    String timestamp = String.valueOf(System.currentTimeMillis());

                    // Obtiene fecha actual
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDate = sdf.format(new Date());

                    // guarda un stamp del tiempo de inicio
                    dataBaseHelper.stampTiqInStart(usernameRetrieved, timestamp, currentDate);  // Pass the date

                    // Iniciar el temporizador  si no está en marcha
                    if (timeCounter == 0) {
                        startTimer(8 * 60 * 60 * 1000); // Iniciar el temporizador con una duración fija de 8 horas
                    } else {
                        startTimer(timeCounter); // Continúa el temporizador con el tiempo restante
                    }
                }
                dataBaseHelper.close();
            }
        });

        //Detiene el contador
        Tiq_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    isTimerRunning = false;
                    Tiq_In_Start.setVisibility(View.VISIBLE);
                    Tiq_In_Start.setText(R.string.ContinueTiqInCounter);
                    Tiq_Stop.setVisibility(View.GONE);
                    Tiq_Break.setVisibility(View.GONE);
                    Tiq_Break_Time.setVisibility(View.GONE);
                    stopTimer();


                    // Obtiene username y timestamp
                    String usernameRetrieved = dataBaseHelper.getUsername();
                    String timestamp = String.valueOf(System.currentTimeMillis());

                    // guarda un stamp del tiempo de inicio
                    dataBaseHelper.stampTiqBreak(usernameRetrieved, timestamp);
                    dataBaseHelper.close();

                }
            }
        });


        //Boton que inicia y detiene el contador de Break
        Tiq_Break.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (!isBreakTimerRunning) {
                    if (breakTimeCounter <= 0) {
                        startBreakTimer(30 * 60 * 1000);
                    } else {
                        startBreakTimer(breakTimeCounter);
                    }
                    stopTimer();
                    Tiq_Break.setText(R.string.PauseBreak);
                    isBreakTimerRunning = true;
                } else {
                    pauseBreakTimer();
                    startTimer(timeCounter);
                    Tiq_Break.setText(R.string.TiqBreakText);
                    isBreakTimerRunning = false;
                }
            }
        });


    }//FINAL DEL METODO ON CREATE


    //Inicia el contador principal
    private void startTimer(long durationInMillis) {
        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeCounter = millisUntilFinished;
                updateTimerDisplay();// Actualiza el contador con el tiempo restante




            }

            @Override
            public void onFinish() {

                Toast.makeText(MainActivity.this, R.string.ToastShiftOver, Toast.LENGTH_SHORT).show();


            }
        };
        countDownTimer.start();
    }

    //Detiene el contador principal
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    //Actualiza el contador
    private void updateTimerDisplay() {
        long hours = timeCounter / 3600000;
        long minutes = (timeCounter % 3600000) / 60000;
        long seconds = (timeCounter % 60000) / 1000;
        String timeString = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        Tiq_In_Time.setText(timeString);
    }


    private void startBreakTimer(long durationInMillis) {
        if (breakCountDownTimer != null) {
            breakCountDownTimer.cancel();
        }


        breakCountDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breakTimeCounter = millisUntilFinished;
                updateBreakTimerDisplay();
            }

            @Override
            public void onFinish() {

                Toast.makeText(MainActivity.this, R.string.ToastBreakOver, Toast.LENGTH_SHORT).show();// Display a toast message
                startTimer(timeCounter); // Resume the Tiq_In_Time countdown
                Tiq_Break.setText("---");


            }

        };
        breakCountDownTimer.start();
    }


    private void pauseBreakTimer() {
        if (breakCountDownTimer != null) {
            breakCountDownTimer.cancel(); // Pausa el temporizador de cuenta atrás de break

        }
    }

    // Método para actualizar la interfaz de usuario con el tiempo restante de la cuenta atrás de la interrupción
    private void updateBreakTimerDisplay() {


        long minutes = (breakTimeCounter / 60000) % 60;
        long seconds = (breakTimeCounter / 1000) % 60;
        String timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        Tiq_Break_Time.setText(timeString);
    }


}




