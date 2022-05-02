package kanten.Workout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import kanten.R;

import static android.content.Context.MODE_PRIVATE;


public class WorkoutClosedFragment extends Fragment {

    TextView textName, textDescription;
    Integer id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_closed, container, false);
        textName = (TextView) view.findViewById(R.id.text_name);
        textDescription = (TextView) view.findViewById(R.id.button_text2);

        SharedPreferences sh = getActivity().getSharedPreferences("Fragment" + id.toString(), MODE_PRIVATE);
        System.out.println("HIER: create Workout: " + sh.getString("Name", ""));
        textName.setText(sh.getString("Name", ""));

        //SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
        String workout1 = sh.getString("workout1", "");
        String workout2 = sh.getString("workout2", "");
        String workout3 = sh.getString("workout3", "");
        textDescription.setText(workout1+"\n"+workout2+"\n"+workout3);

        ConstraintLayout c = (ConstraintLayout) view.findViewById(R.id.constraintlayout);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != null) {
                    WorkoutActivity notification = (WorkoutActivity) getActivity();
                    if (notification.weekdayFragmentOPEN == true) {
                        WorkoutClosedFragment newFragment = new WorkoutClosedFragment();
                        newFragment.setID(notification.IDoldFragment);
                        FragmentTransaction ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(notification.IDoldFragment, newFragment);
                        //ft.addToBackStack(null);
                        ft.commit();
                    }
                    WorkoutOpenedFragment newFragment = new WorkoutOpenedFragment();
                    newFragment.setID(id);
                    FragmentTransaction ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(id, newFragment, id.toString());
                    //ft.addToBackStack(null);
                    ft.commit();
                    notification.IDoldFragment = id;
                    notification.weekdayFragmentOPEN = true;

                }
            }

        });

        return view;
    }

    public void setID(int id) {
        this.id = id;
    }

}

