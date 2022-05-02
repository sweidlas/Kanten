package kanten.Other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import kanten.Module.WorkoutModuleActivity;
import kanten.R;
import kanten.Workout.WorkoutActivity;

public class TimerActivity extends BaseActivity {

    public static String WORKOUT_ID = "com.androidmads.navdraweractivity.Other.TimerActivity.WORKOUT_ID";
    public static String WORKOUTSET_ID = "com.androidmads.navdraweractivity.Other.TimerActivity.WORKOUT_SETID";

    CountDownTimer countdown, timer_hang, timer_rest, timer_break;
    private static int timer_semaphor_start_stop = 1;
    private static int timer_semaphor_initial_start = 1;
    private static int semaphor_hang_or_rest = 1;
    private static int hangTime, restTime, current_rest, countdownTime, repetitions, sh_repetitions, sets, sh_sets, breaks, breaktime_modules;
    String workout1, workout2, workout3;
    static Integer timercounter;
    static boolean exitToModulesOrWorkout = true;
    Button button2;
    TextView NameView, countdownView, hangRestView, repetitionsView, setsView, nameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("Timer");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_first, null, false);
        contentView.setKeepScreenOn(true);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.GONE);

        System.out.println("HIER: beim Timercreate: "+getIntent().getStringExtra(WORKOUTSET_ID));

        getWorkout2(); //Get the Workoutnames on the Variables
        countdownView = findViewById(R.id.countdownView);
        hangRestView = findViewById(R.id.workoutState);
        button2 = findViewById(R.id.button1);
        repetitionsView = findViewById(R.id.Repetitions);
        setsView = findViewById(R.id.Sets);
        nameView = findViewById(R.id.workoutname);

        initializeNumbers();

    }

    public void initializeNumbers(){
        SharedPreferences sh = getSharedPreferences(workout1, MODE_PRIVATE);

        hangRestView.setText("");

        String name = sh.getString("Name", "");
        nameView.setText("Modul: " + name);

        Integer i = sh.getInt("Repetitions", 0);
        repetitionsView.setText("Repetitions: " + i.toString());

        Integer i2 = sh.getInt("Sets", 0);
        setsView.setText("Sets: " + i2.toString());
        timer_semaphor_initial_start = 1;
        semaphor_hang_or_rest = 1;

        Integer i3 = sh.getInt("HangTime_min", 0)*60+ sh.getInt("HangTime_sec", 0);
        Integer i4 = (sh.getInt("RestTime_min", 0)*60+ sh.getInt("RestTime_sec", 0));
        countdownView.setTextSize(30);
        countdownView.setText("Hang: "+i3+"\nRest: "+i4);

        timercounter = 1;
    }

    public String getWorkout() {
        String s = "";
        Integer workout_int = getIntent().getIntExtra( WORKOUT_ID , 13 );
        if (workout_int != 13) {
            s = "workout" + workout_int.toString();
        }
        return(s);
    }

    public void getWorkout2() {
        String workout_str;
        Integer workout_int = getIntent().getIntExtra( WORKOUT_ID , 0 );
        if (workout_int != 0) {
            workout1 = "workout" + workout_int.toString();
            exitToModulesOrWorkout = true;
        }
        if ((workout_str = getIntent().getStringExtra(WORKOUTSET_ID)) != null) {
            exitToModulesOrWorkout = false;
            System.out.println("HIER: Fragment von WeekdayActivity: " + workout_str);
            SharedPreferences sh = getSharedPreferences(workout_str, MODE_PRIVATE);
            String workout1name = sh.getString("workout1", "");
            String workout2name = sh.getString("workout2", "");
            String workout3name = sh.getString("workout3", "");
            for (Integer i = 0; i<10; i++) {
                SharedPreferences sh2 = getSharedPreferences("workout"+i.toString(), MODE_PRIVATE);
                if (sh2.getString("Name", "nichtInitialisiert").equals(workout1name)) {workout1 = "workout"+i.toString();}
                if (sh2.getString("Name", "nichtInitialisiert").equals(workout2name)) {workout2 = "workout"+i.toString();}
                if (sh2.getString("Name", "nichtInitialisiert").equals(workout3name)) {workout3 = "workout"+i.toString();}        //bekommt workout dass noch nicht initialisiert ist
            }
            System.out.println("HIER: workout1 von WeekdayActivity: " + workout1);
            System.out.println("HIER: workout2 von WeekdayActivity: " + workout2);
            System.out.println("HIER: workout3 von WeekdayActivity: " + workout3);
        }
    }

    public void resetTimer() {
        timer_semaphor_start_stop = 1;
        timer_semaphor_initial_start = 1;
        semaphor_hang_or_rest = 1;
        try {
            countdown.cancel();
            timer_hang.cancel();
            timer_rest.cancel();
            timer_break.cancel();
        } catch (Exception e) {}
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            resetTimer();

            this.finish();
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_activity_workoutmodules) {
            resetTimer();
            this.finish();
            startAnimatedActivity(new Intent(getApplicationContext(), WorkoutModuleActivity.class));
        } else if (id == R.id.nav_activity_workouts) {
            resetTimer();
            this.finish();
            startAnimatedActivity(new Intent(getApplicationContext(), WorkoutActivity.class));
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
    }*/

    public void startTimer(View view) {
        if (timercounter == 1) {timer(workout1);}
        else if (timercounter == 2) {timer(workout2);}
        else if (timercounter == 3) {timer(workout3);}
    }

    public void timer(final String workoutID) {

        if (timer_semaphor_start_stop == 1) {   //Start
            timer_semaphor_start_stop = 0;
            button2.setBackground(getResources().getDrawable(R.drawable.pause_grau));
            if (timer_semaphor_initial_start == 1) {    //Zeiten für den Start
                timer_semaphor_initial_start = 0;

                SharedPreferences sh = getSharedPreferences(workoutID, MODE_PRIVATE);
                String name = sh.getString("Name", "");
                NameView.setText("Modul: " + name);

                hangTime = (sh.getInt("HangTime_min", 0)*60+ sh.getInt("HangTime_sec", 1));
                restTime = (sh.getInt("RestTime_min", 0)*60+ sh.getInt("RestTime_sec", 1));
                sh_repetitions = (sh.getInt("Repetitions", 1));
                sh_sets = (sh.getInt("Sets", 1));
                breaks = (sh.getInt("Breaks_min", 0)*60+ sh.getInt("Breaks_sec", 1));
                SharedPreferences sh2 = getSharedPreferences(getIntent().getStringExtra(WORKOUTSET_ID), MODE_PRIVATE);
                breaktime_modules = (sh2.getInt("breaktime_min", 0)*60+ sh2.getInt("breaktime_sec", 1));

                repetitions = sh_repetitions;
                sets = sh_sets;
                repetitionsView.setText("Repetitions: " + repetitions);
                setsView.setText("Sets: " + sets);
                if (timercounter > 1) {
                    countdownTime = breaktime_modules;
                    hangRestView.setText("Break");
                } else {
                    countdownTime = 5;
                    hangRestView.setText("Countdown");
                }
                semaphor_hang_or_rest = 1;
            } else {  //Zeiten nach einem Stop
                if (semaphor_hang_or_rest == 0) {
                    countdownTime = current_rest;
                } else {
                    countdownTime = 3;
                    hangRestView.setText("Countdown");
                }
            }


            countdown = new CountDownTimer(countdownTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countdownView.setText("" + (millisUntilFinished + 100) / 1000);
                    current_rest = (int) ((millisUntilFinished + 100) / 1000);
                }

                public void onFinish() {
                    timer_hang.start();
                    semaphor_hang_or_rest = 1; //now hang
                    hangRestView.setText("Hang");
                }
            };

            timer_hang = new CountDownTimer(hangTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countdownView.setText("" + (millisUntilFinished + 100) / 1000);
                }

                public void onFinish() {
                    timer_hang.cancel();
                    repetitions--;
                    repetitionsView.setText("Repetitions: " + repetitions);
                    if (repetitions > 0) {
                        timer_rest.start();
                        semaphor_hang_or_rest = 0; //now rest
                        hangRestView.setText("Rest");
                    } else {
                        sets--;
                        setsView.setText("Sets: " + sets);
                        if (sets == 0) {
                            button2.setBackground(getResources().getDrawable(R.drawable.start_grau));
                            timer_semaphor_start_stop = 1;
                            timer_semaphor_initial_start = 1;
                            timercounter++;
                            if (timercounter == 2) {
                                if (workout2 == null) {                 //nur ein Workout Module
                                    countdownView.setText("Finished");
                                    hangRestView.setText("Finished");
                                    timercounter = 1;
                                } else { timer(workout2);}}
                            else if (timercounter == 3) {
                                if (workout3 == null) {
                                    countdownView.setText("Finished");
                                    hangRestView.setText("Finished");
                                    timercounter = 1;
                                } else { timer(workout3);}}
                            else if (timercounter == 4) {
                                countdownView.setText("Finished");
                                hangRestView.setText("Finished");
                                timercounter = 1;
                            }
                        } else {
                            semaphor_hang_or_rest = 0; //rest gilt auch für break
                            repetitions = sh_repetitions;
                            hangRestView.setText("Break");
                            repetitionsView.setText("Repetitions: " + repetitions);
                            timer_break.start();

                        }


                    }
                }
            };

            timer_rest = new CountDownTimer(restTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countdownView.setText("" + (millisUntilFinished + 100) / 1000);
                    current_rest = (int) ((millisUntilFinished + 100) / 1000);
                }

                public void onFinish() {
                    timer_rest.cancel();
                    timer_hang.start();
                    semaphor_hang_or_rest = 1; //now hang
                    hangRestView.setText("Hang");
                }
            };

            timer_break = new CountDownTimer(breaks * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countdownView.setText("" + (millisUntilFinished + 100) / 1000);
                    current_rest = (int) ((millisUntilFinished + 100) / 1000);
                }

                public void onFinish() {
                    timer_break.cancel();
                    timer_hang.start();
                    semaphor_hang_or_rest = 1; //now hang
                    hangRestView.setText("Hang");
                }
            };
            countdownView.setText("");
            countdownView.setTextSize(70);
            countdown.start();


        } else {      //STOP
            timer_semaphor_start_stop = 1;

            button2.setBackground(getResources().getDrawable(R.drawable.start_grau));

            countdown.cancel();
            timer_hang.cancel();
            timer_rest.cancel();
            timer_break.cancel();


        }
    }

    public void reset(View view) {
        resetTimer();
        final Button button2 = findViewById(R.id.button1);
        button2.setBackground(getResources().getDrawable(R.drawable.start_grau));
        initializeNumbers();
    }
}
