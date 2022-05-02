package kanten.Module;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import kanten.R;
import kanten.Other.TimerActivity;


import static android.content.Context.MODE_PRIVATE;


public class WorkoutModuleFragment extends Fragment {

    TextView textName, textDescription, textDescription2;
    Integer id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_module, container, false);
        textName = view.findViewById(R.id.text_name);
        textDescription = view.findViewById(R.id.button_text2);
        textDescription2 = view.findViewById(R.id.button_text3);

        SharedPreferences sh = getActivity().getSharedPreferences("workout" + id.toString(), MODE_PRIVATE);
        try {
            textName.setText(sh.getString("Name", ""));
        } catch (Exception e){}

        int hangTime = (sh.getInt("HangTime_min", 0)*60+ sh.getInt("HangTime_sec", 1));
        int restTime = (sh.getInt("RestTime_min", 0)*60+ sh.getInt("RestTime_sec", 1));
        int sh_repetitions = (sh.getInt("Repetitions", 1));
        int sh_sets = (sh.getInt("Sets", 1));
        int breaks = (sh.getInt("Breaks_min", 0)*60+ sh.getInt("Breaks_sec", 1));

        textDescription.setText("Hang: "+hangTime+"\nRepetitions: "+sh_repetitions+"\nBreaks: "+breaks);
        textDescription2.setText("Rest: "+restTime+"\nSets: "+sh_sets);

        ConstraintLayout c = view.findViewById(R.id.constraintlayout);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TimerIntent = new Intent(getActivity(), TimerActivity.class);
                TimerIntent.putExtra(TimerActivity.WORKOUT_ID, id);
                ((WorkoutModuleActivity)getActivity()).startAnimatedActivity(TimerIntent);
            }
        });

        c.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Intent CreateIntent = new Intent(getActivity().getApplicationContext(), WorkoutModuleCreateActivity.class);
                CreateIntent.putExtra(WorkoutModuleCreateActivity.WORKOUT_ID, id);
                ((WorkoutModuleActivity)getActivity()).startAnimatedActivity(CreateIntent);
                return true;
            }
        });
        return view;
    }


    public void setID(int id) {
        System.out.println("HIER: setID: "+id);
        this.id = id;
    }





}


