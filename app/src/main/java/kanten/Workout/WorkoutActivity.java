package kanten.Workout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import kanten.Other.BaseActivity;
import kanten.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class WorkoutActivity extends BaseActivity {

    public boolean weekdayFragmentOPEN = false;
    public Integer IDoldFragment, id;
    public ArrayList workoutnames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Workouts");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_workout, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_activity_workouts);

        workoutnames = getWorkoutNames();
        setupFragments();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWorkout();
            }
        });
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_notifications) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                super.onBackPressed();
            }
        }
    }

    public void selectTime(View view) {

        final Dialog d = new Dialog(WorkoutActivity.this); //oder getActivity().getApplicationContext();
        d.setTitle("TimePicker");
        d.setContentView(R.layout.dialog_timepicker);
        Button b1 = (Button) d.findViewById(R.id.dialogN_button1);
        Button b2 = (Button) d.findViewById(R.id.dialogN_button2);
        final TimePicker tp = (TimePicker) d.findViewById(R.id.dialog_selectTime);
        tp.setIs24HourView(true);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = tp.getCurrentHour();
                int minute = tp.getCurrentMinute();
                Toast.makeText(WorkoutActivity.this, "Selected time is "+hour+"."+minute, Toast.LENGTH_LONG).show();  //oder getActivity().getApplicationContext();
                d.dismiss();

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

    public int addFragment(Integer id) {

        FrameLayout aLayout = new FrameLayout(this);
        aLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        aLayout.setId(id);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainLayout);
        linearLayout.addView(aLayout);

        WorkoutClosedFragment newFragment = new WorkoutClosedFragment();
        newFragment.setID(id);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(aLayout.getId(), newFragment);
        fragmentTransaction.commit();

        SharedPreferences sh = getSharedPreferences("Fragments",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putInt("Fragment"+id.toString(), id);
        myEdit.commit();

        return id;
    }

    public void setupFragments(){

        SharedPreferences sh1 = getSharedPreferences("Fragments",MODE_PRIVATE);
        Integer wid = sh1.getInt("workoutID", 0);

        for (Integer i = 0; i<=wid; i++) {
            SharedPreferences sharedPreferences = getSharedPreferences("Fragments",MODE_PRIVATE);
            int id = sharedPreferences.getInt("Fragment"+i.toString(),0);
            if (id != 0) {
                FrameLayout aLayout = new FrameLayout(this);
                aLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                aLayout.setId(id);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainLayout);
                linearLayout.addView(aLayout);

                WorkoutClosedFragment newFragment = new WorkoutClosedFragment();
                newFragment.setID(id);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(aLayout.getId(), newFragment);
                fragmentTransaction.commit();
            }
        }
    }


    public void createWorkout() {

        final Dialog d = new Dialog(this);
        //d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_nameworkout);
        Button b1 = (Button) d.findViewById(R.id.dialogN_button1);
        Button b2 = (Button) d.findViewById(R.id.dialogN_button2);
        final EditText editText = (EditText) d.findViewById(R.id.workoutName);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().length() > 20) {
                    Snackbar.make(v, "maximum name length is 20 characters", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    SharedPreferences sh1 = getSharedPreferences("Fragments", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit1 = sh1.edit();
                    Integer wid = sh1.getInt("workoutID", 0);       //workout mit größter nummer zähler
                    wid++;
                    myEdit1.putInt("workoutID", wid);
                    myEdit1.commit();

                    SharedPreferences sh = getSharedPreferences("Fragment" + wid.toString(), MODE_PRIVATE);

                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putString("Name", editText.getText().toString());
                    System.out.println("HIER: create Workout: " + editText.getText().toString());
                    myEdit.commit();

                    addFragment(wid);

                    d.dismiss();
                }

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

    public String changeWorkoutName(Integer id) {
        final Integer ID = id;
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_nameworkout);
        Button b1 = (Button) d.findViewById(R.id.dialogN_button1);
        Button b2 = (Button) d.findViewById(R.id.dialogN_button2);
        TextView text = (TextView) d.findViewById(R.id.textView2);
        text.setText("new workout name:");
        final EditText editText = (EditText) d.findViewById(R.id.workoutName);
        SharedPreferences sh = getSharedPreferences("Fragment"+id.toString(), MODE_PRIVATE);
        editText.setText(sh.getString("Name", ""));
        if (editText.getText().length() > 0 ) {
            editText.setSelection(editText.getText().length());
        }


        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().length() > 20) {
                    Snackbar.make(v, "maximum name length is 20 characters", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    SharedPreferences sh = getSharedPreferences("Fragment"+ID.toString(),MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putString("Name", editText.getText().toString());
                    myEdit.commit();

                    WorkoutOpenedFragment fragment = (WorkoutOpenedFragment) getFragmentManager().findFragmentByTag(ID.toString());
                    fragment.setNewName(editText.getText().toString());
                    d.dismiss();
                }
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

        return editText.getText().toString();
    }

    public ArrayList<String> getWorkoutNames() {
        ArrayList<String> workoutNames = new ArrayList<String>();
        SharedPreferences sh1 = getSharedPreferences("FragmentsModule", MODE_PRIVATE);
        Integer wid = sh1.getInt("workoutID", 0);

        for (Integer i = 0; i<=wid; i++) {
            String s = "workout"+i.toString();
            SharedPreferences sh = getSharedPreferences(s, MODE_PRIVATE);
            s = sh.getString("Name", "");
            if (!s.equals("")) {
                workoutNames.add(s);
            }
        }
        return workoutNames;
    }
}