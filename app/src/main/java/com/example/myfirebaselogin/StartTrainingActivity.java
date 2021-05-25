package com.example.myfirebaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StartTrainingActivity extends AppCompatActivity {
    private TextView timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_training);

        timer = (TextView) findViewById(R.id.timer_starttraining);
        long duration = TimeUnit.MINUTES.toMillis(200);
        new CountDownTimer(duration, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                String stringDuration = String.format(Locale.ENGLISH,"%02d : %02d",TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timer.setText(stringDuration);

            }

            @Override
            public void onFinish() {
                timer.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Training beendet", Toast.LENGTH_LONG).show();
            }
        }.start();

    }
}