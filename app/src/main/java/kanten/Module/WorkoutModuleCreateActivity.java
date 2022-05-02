package kanten.Module;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.GravityCompat;

import kanten.Other.BaseActivity;
import kanten.R;
import kanten.Other.TimerActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

public class WorkoutModuleCreateActivity extends BaseActivity {

    TextView text1, text_resttime, text_breaktime;
    Button button, button_hangtime, button_resttime, button_repetitions, button_sets, button_breaks, button_name;
    Integer HangTime_min, HangTime_sec, RestTime_min, RestTime_sec, Breaks_min, Breaks_sec, Repetitions, Sets;
    String WorkoutModuleName;

    public static String WORKOUT_ID = "com.androidmads.navdraweractivity.Module.WorkoutModuleCreateActivity.WORKOUT_ID" ;
    public static String newBool = "com.androidmads.navdraweractivity.Module.WorkoutModuleCreateActivity.newBool" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Edit Workout");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_create_workout, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.GONE);

        setupButtons();

    }

    public void setupButtons() {
        text1 = findViewById(R.id.textView_hang);
        button = findViewById(R.id.button_hangtime);

        SharedPreferences sh = getSharedPreferences(getWorkout(), MODE_PRIVATE);

        HangTime_min = sh.getInt("HangTime_min", 0);
        HangTime_sec = sh.getInt("HangTime_sec", 0);
        RestTime_min = sh.getInt("RestTime_min", 0);
        RestTime_sec = sh.getInt("RestTime_sec", 0);
        Breaks_min = sh.getInt("Breaks_min", 0);
        Breaks_sec = sh.getInt("Breaks_sec", 0);
        Repetitions = sh.getInt("Repetitions", 1);
        Sets = sh.getInt("Sets", 1);
        WorkoutModuleName = sh.getString("Name", "");

        button_hangtime = findViewById(R.id.button_hangtime);
        button_hangtime.setText(HangTime_min+" min "+HangTime_sec+" sec");
        text_resttime = findViewById(R.id.textView_rest);
        button_resttime = findViewById(R.id.button_resttime);
        if (Repetitions == 1) {
            button_resttime.setVisibility(View.GONE);
            text_resttime.setVisibility(View.GONE);
        } else {
            button_resttime.setText(RestTime_min + " min " + RestTime_sec + " sec");
        }
        button_repetitions = findViewById(R.id.button_repetitions);
        button_repetitions.setText(Repetitions+" Times");
        button_sets = findViewById(R.id.button_sets);
        button_sets.setText(Sets+" Times");
        text_breaktime = findViewById(R.id.textView_breaks);
        button_breaks = findViewById(R.id.button_breaks);
        if (Sets == 1) {
            button_breaks.setVisibility(View.GONE);
            text_breaktime.setVisibility(View.GONE);
        } else {
            button_breaks.setText(RestTime_min + " min " + RestTime_sec + " sec");
        }
        button_breaks.setText(Breaks_min+" min "+Breaks_sec+" sec");
        button_name = findViewById(R.id.select_name);
        button_name.setText(WorkoutModuleName);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_create_workout_Delete) {
            deleteDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

           // Intent TimerIntent = new Intent(getApplicationContext(), WorkoutModuleActivity.class);
           // startAnimatedReverseActivity(TimerIntent);
            //startAnimatedActivity(new Intent(getApplicationContext(), WorkoutModuleActivity.class));*/

            this.finish();
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
        }
    }

    public String getWorkout() {
        Integer workout_int = getIntent().getIntExtra( WORKOUT_ID , 0 );
        String s = "workout"+workout_int.toString();
        System.out.println("HIER: Workout_ID: "+workout_int);
        return(s);
    }

    public void selectName(View view) {

        final Dialog d = new Dialog(WorkoutModuleCreateActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_namepicker);
        final Button b1 = d.findViewById(R.id.dialogN_button1);
        Button b2 = d.findViewById(R.id.dialogN_button2);
        final EditText ed = d.findViewById(R.id.dialog_selectName);

        SharedPreferences sh = getSharedPreferences(getWorkout(), MODE_PRIVATE);
        ed.setText(sh.getString("Name", ""));
        if (ed.getText().length() > 0 ) {
            ed.setSelection(ed.getText().length());
        }


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed.getText().toString().length() > 20) {
                    Snackbar.make(v, "maximum name length is 20 characters", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    button_name.setText(ed.getText().toString());
                    d.dismiss();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
    }

    public void selectHangTime(View view) {

        final Dialog d = new Dialog(WorkoutModuleCreateActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_numberpicker_two);
        Button b1 = d.findViewById(R.id.dialog2_button1);
        Button b2 = d.findViewById(R.id.dialog2_button2);
        final NumberPicker np = d.findViewById(R.id.dialog2_numberPicker1);

        np.setMaxValue(59);
        np.setValue(HangTime_min);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        final NumberPicker np2 = d.findViewById(R.id.dialog2_numberPicker2);
        np2.setMaxValue(59);
        np2.setValue(HangTime_sec);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((np.getValue() + np2.getValue()) == 0) {
                    Toast.makeText(WorkoutModuleCreateActivity.this, "Selected time has to be more than 0 seconds", Toast.LENGTH_LONG).show();
                } else {
                    HangTime_min = np.getValue();
                    HangTime_sec = np2.getValue();
                    button_hangtime.setText(HangTime_min+" min "+HangTime_sec+" sec");
                    d.dismiss();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();

    }

    public void selectRestTime(View view) {

        final Dialog d = new Dialog(WorkoutModuleCreateActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_numberpicker_two);
        Button b1 = d.findViewById(R.id.dialog2_button1);
        Button b2 = d.findViewById(R.id.dialog2_button2);
        final NumberPicker np = d.findViewById(R.id.dialog2_numberPicker1);

        np.setMaxValue(59);
        np.setValue(RestTime_min);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        final NumberPicker np2 = d.findViewById(R.id.dialog2_numberPicker2);
        np2.setMaxValue(59);
        np2.setValue(RestTime_sec);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((np.getValue() + np2.getValue()) == 0) {
                    Toast.makeText(WorkoutModuleCreateActivity.this, "Selected time has to be more than 0 seconds", Toast.LENGTH_LONG).show();
                } else {
                    RestTime_min = np.getValue();
                    RestTime_sec = np2.getValue();
                    button_resttime.setText(RestTime_min+" min "+RestTime_sec+" sec");
                    d.dismiss();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();

    }

    public void selectBreaks(View view) {

        final Dialog d = new Dialog(WorkoutModuleCreateActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_numberpicker_two);
        Button b1 = d.findViewById(R.id.dialog2_button1);
        Button b2 = d.findViewById(R.id.dialog2_button2);
        final NumberPicker np = d.findViewById(R.id.dialog2_numberPicker1);

        np.setMaxValue(59);
        np.setValue(Breaks_min);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        final NumberPicker np2 = d.findViewById(R.id.dialog2_numberPicker2);
        np2.setMaxValue(59);
        np2.setValue(Breaks_sec);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((np.getValue() + np2.getValue()) == 0) {
                    Toast.makeText(WorkoutModuleCreateActivity.this, "Selected time has to be more than 0 seconds", Toast.LENGTH_LONG).show();
                } else {
                    Breaks_min = np.getValue();
                    Breaks_sec = np2.getValue();
                    button_breaks.setText(Breaks_min+" min "+Breaks_sec+" sec");
                    d.dismiss();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();

    }

    public void selectRepetitions(View view) {

        final Dialog d = new Dialog(WorkoutModuleCreateActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_numberpicker);
        Button b1 = d.findViewById(R.id.dialogN_button1);
        Button b2 = d.findViewById(R.id.dialogN_button2);
        final NumberPicker np = d.findViewById(R.id.dialog_selectName);

        np.setMaxValue(10);
        np.setValue(Repetitions);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (np.getValue()== 1) {
                    button_resttime.setVisibility(View.GONE);
                    text_resttime.setVisibility(View.GONE);
                } else {
                    button_resttime.setVisibility(View.VISIBLE);
                    text_resttime.setVisibility(View.VISIBLE);
                }
                Repetitions = np.getValue();
                button_repetitions.setText(Repetitions+" times");
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void selectSet(View view) {

        final Dialog d = new Dialog(WorkoutModuleCreateActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_numberpicker);
        Button b1 = d.findViewById(R.id.dialogN_button1);
        Button b2 = d.findViewById(R.id.dialogN_button2);
        final NumberPicker np = d.findViewById(R.id.dialog_selectName);

        np.setMaxValue(10);
        np.setValue(Sets);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (np.getValue()== 1) {
                    button_breaks.setVisibility(View.GONE);
                    text_breaktime.setVisibility(View.GONE);
                } else {
                    button_breaks.setVisibility(View.VISIBLE);
                    text_breaktime.setVisibility(View.VISIBLE);
                }
                Sets = np.getValue();
                button_sets.setText(Sets+" times");
                d.dismiss();

            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

    public void save (View view) {
        if (button_name.getText().toString().equals("")) {
            Toast.makeText(WorkoutModuleCreateActivity.this, "Please give your workout a name", Toast.LENGTH_LONG).show();
        } else if (HangTime_min == 0 && HangTime_sec == 0){
            Toast.makeText(WorkoutModuleCreateActivity.this, "Hang time has to be at least 1 second", Toast.LENGTH_LONG).show();
        } else if (Repetitions > 1 && RestTime_min == 0 && RestTime_sec == 0) {
            Toast.makeText(WorkoutModuleCreateActivity.this, "Rest time has to be at least 1 second", Toast.LENGTH_LONG).show();
        }
        else {

            boolean newIsTrue = getIntent().getBooleanExtra( newBool , false);
            if (newIsTrue) {
                Integer newWorkout = appendNewWorkout();
                savePreferences(newWorkout);
                Intent TimerIntent = new Intent(getApplicationContext(), WorkoutModuleActivity.class);
                TimerIntent.putExtra(TimerActivity.WORKOUT_ID, newWorkout);
                startAnimatedReverseActivity(TimerIntent);
            } else {
                savePreferences(getIntent().getIntExtra( WORKOUT_ID , 0 ));
                Intent TimerIntent = new Intent(getApplicationContext(), WorkoutModuleActivity.class);
                startAnimatedReverseActivity(TimerIntent);
                this.finish();

            }
        }

    }

    public void savePreferences(Integer workoutID) {
        SharedPreferences sh1 = getSharedPreferences("Fragments",MODE_PRIVATE);
        Integer max_id = sh1.getInt("workoutID", 0);
        SharedPreferences sharedPreferences = getSharedPreferences("workout"+workoutID.toString(),MODE_PRIVATE);
        if (!sharedPreferences.getString("Name", "").equals(button_name.getText().toString())){
            for (Integer i = 0; i<=max_id; i++) {
                SharedPreferences sh = getSharedPreferences("Fragment" + i.toString(), MODE_PRIVATE);
                SharedPreferences.Editor ed = sh.edit();
                if (sh.getString("workout1", "").equals(sharedPreferences.getString("Name", "")) && !sh.getString("workout1", "").equals("")) {
                    ed.putString("workout1", button_name.getText().toString());
                    ed.commit();
                }
                if (sh.getString("workout2", "").equals(sharedPreferences.getString("Name", "")) && !sh.getString("workout2", "").equals("")) {
                    ed.putString("workout2", button_name.getText().toString());
                    ed.commit();
                }
                if (sh.getString("workout3", "").equals(sharedPreferences.getString("Name", "")) && !sh.getString("workout3", "").equals("")) {
                    ed.putString("workout3", button_name.getText().toString());
                    ed.commit();
                }

            }
        }

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("Name", button_name.getText().toString());
        myEdit.putInt("HangTime_min", HangTime_min);
        myEdit.putInt("HangTime_sec", HangTime_sec);
        myEdit.putInt("RestTime_min", RestTime_min);
        myEdit.putInt("RestTime_sec", RestTime_sec);
        myEdit.putInt("Breaks_min", Breaks_min);
        myEdit.putInt("Breaks_sec", Breaks_sec);
        myEdit.putInt("Repetitions", Repetitions);
        myEdit.putInt("Sets", Sets);

        myEdit.commit();

    }

    public Integer appendNewWorkout() { //append new Workout to the lowest free Button
        SharedPreferences sh1 = getSharedPreferences("FragmentsModule", MODE_PRIVATE);
        SharedPreferences.Editor myEdit1 = sh1.edit();
        Integer wid = sh1.getInt("workoutID", 0);       //workout mit größter nummer zähler
        wid++;
        myEdit1.putInt("workoutID", wid);
        myEdit1.commit();


        SharedPreferences sh = getSharedPreferences("FragmentsModule",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putInt("workout"+wid.toString(), wid);
        myEdit.commit();

        return wid;
    }

    public void deleteDialog() {
        final Dialog d = new Dialog(WorkoutModuleCreateActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_delete);
        Button b1 = d.findViewById(R.id.dialogN_button1);
        Button b2 = d.findViewById(R.id.dialogN_button2);


        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Integer w_id = getIntent().getIntExtra( WORKOUT_ID , 13);
                System.out.println("HIER: Delete: string: "+w_id);
                deleteWorkoutModul(getWorkout());

                SharedPreferences sh = getSharedPreferences("FragmentsModule",MODE_PRIVATE);
                SharedPreferences.Editor myEdit1 = sh.edit();
                myEdit1.remove(getWorkout());
                myEdit1.commit();

                SharedPreferences sharedPreferences = getSharedPreferences(getWorkout(),MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.clear();
                myEdit.commit();



                d.dismiss();
                startAnimatedActivity(new Intent(getApplicationContext(), WorkoutModuleActivity.class));
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void deleteWorkoutModul(String workoutID) {
        SharedPreferences h = getSharedPreferences(workoutID, MODE_PRIVATE);
        String workoutToDelete = h.getString("Name", "");
        System.out.println("HIER: workoutToDelete: "+workoutToDelete);

        SharedPreferences fragments = getSharedPreferences("Fragments", MODE_PRIVATE);
        Map<String,?> keys = fragments.getAll();

        System.out.println("HIER: Map");

        for(Map.Entry<String,?> entry : keys.entrySet()){
            System.out.println("HIER: Map"+entry.getKey());
            SharedPreferences sh = getSharedPreferences(entry.getKey(), MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sh.edit();

            //workouts aus der Liste löschen
            if (sh.getString("workout1", "").equals(workoutToDelete)) {
                myEdit.putString("workout1", "");
                System.out.println("HIER: Map: workout1");
            }
            if (sh.getString("workout2", "").equals(workoutToDelete)) {
                myEdit.putString("workout2", "");
                System.out.println("HIER: Map: workout2");
            }
            if (sh.getString("workout3", "").equals(workoutToDelete)) {
                myEdit.putString("workout3", "");
                System.out.println("HIER: Map: workout3");
            }
            myEdit.commit();
            //workouts auf Liste zusammenschieben
            if (sh.getString("workout1", "").equals("")) {
                if (!sh.getString("workout2", "").equals("")) {
                    myEdit.putString("workout1", sh.getString("workout2", ""));
                    myEdit.remove("workout2");
                    myEdit.commit();
                } else if (!sh.getString("workout3", "").equals("")) {
                    myEdit.putString("workout1", sh.getString("workout3", ""));
                    myEdit.remove("workout3");
                    myEdit.commit();
                }
            }
            if (sh.getString("workout2", "").equals("")) {
                myEdit.putString("workout2", sh.getString("workout3", ""));
                myEdit.remove("workout3");
                myEdit.commit();
            }

        }
    }
}
