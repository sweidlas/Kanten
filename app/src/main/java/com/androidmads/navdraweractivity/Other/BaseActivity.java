package com.androidmads.navdraweractivity.Other;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.androidmads.navdraweractivity.Module.WorkoutModuleActivity;
import com.androidmads.navdraweractivity.R;
import com.androidmads.navdraweractivity.Workout.WorkoutActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    public FloatingActionButton fab;
    public NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //createNotificationChannel();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        /*
        if (id == R.id.nav_activity1) {
            startAnimatedActivity(new Intent(getApplicationContext(), TimerActivity.class));
        } else if (id == R.id.nav_activity2) {
            startAnimatedActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        } else if (id == R.id.nav_activity3) {
            startAnimatedActivity(new Intent(getApplicationContext(), ThirdActivity.class));
        } else */if (id == R.id.nav_activity_workoutmodules) {
            /*
            Intent intent = new Intent(getApplicationContext(), WorkoutModuleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startAnimatedActivity(intent);*/
            startAnimatedActivity(new Intent(getApplicationContext(), WorkoutModuleActivity.class));
            this.finish();
        } else if (id == R.id.nav_activity_workouts) {
            /*
            Intent intent = new Intent(getApplicationContext(), WorkoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startAnimatedActivity(intent);*/
            startAnimatedActivity(new Intent(getApplicationContext(), WorkoutActivity.class));
            this.finish();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startAnimatedActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    public void startAnimatedReverseActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

}
