package kanten.Module;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.view.GravityCompat;
import kanten.Other.BaseActivity;
import kanten.R;


public class WorkoutModuleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Workout Modules");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_workout_module, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_activity_workoutmodules);


        setupFragments();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateIntent = new Intent(getApplicationContext(), WorkoutModuleCreateActivity.class);
                CreateIntent.putExtra(WorkoutModuleCreateActivity.newBool, true);
                startAnimatedActivity(CreateIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
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

    public void setupFragments(){

        SharedPreferences sh1 = getSharedPreferences("FragmentsModule",MODE_PRIVATE);
        Integer wid = sh1.getInt("workoutID", 0);

        for (Integer i = 0; i<=wid; i++) {
            SharedPreferences sharedPreferences = getSharedPreferences("FragmentsModule",MODE_PRIVATE);
            int id = sharedPreferences.getInt("workout"+i.toString(),0);
            if (id != 0) {
                FrameLayout aLayout = new FrameLayout(this);
                aLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                aLayout.setId(id);
                LinearLayout linearLayout = findViewById(R.id.mainLayout);
                linearLayout.addView(aLayout);

                WorkoutModuleFragment newFragment = new WorkoutModuleFragment();
                newFragment.setID(id);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(aLayout.getId(), newFragment);
                fragmentTransaction.commit();
            }
        }
    }
}