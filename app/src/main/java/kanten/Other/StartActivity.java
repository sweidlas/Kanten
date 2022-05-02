package kanten.Other;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kanten.R;
import kanten.Workout.WorkoutActivity;

import static android.content.Intent.FLAG_FROM_BACKGROUND;

public class StartActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //moveTaskToBack(true);
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3500);  //Delay of 3.5 seconds
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(StartActivity.this,
                            WorkoutActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
