package kanten.Workout;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import kanten.Other.MyNotificationPublisher;
import kanten.R;
import kanten.Other.TimerActivity;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class WorkoutOpenedFragment extends Fragment {
    TextView textName, workout1, workout2, workout3;
    Button buttonTime, buttonAdd, deleteButton1, deleteButton2, deleteButton3,switchButton1, switchButton2, deleteWorkoutButton, startButton, button_breaktime;
    Spinner spinnerWeekday, spinnerWorkout;
    Integer id, breakTime_min, breakTime_sec;
    Switch switch1;
    ImageView divider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_opened, container, false);

        setupViews(view);
        setupTextViews();
        setupSpinners();
        setupListeners();




        return view;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setupViews(View view) {
        textName = view.findViewById(R.id.text_name);
        buttonTime = view.findViewById(R.id.button_time);
        spinnerWeekday = view.findViewById(R.id.spinner_weekday);
        spinnerWorkout = view.findViewById(R.id.spinner_workout);
        switch1 = view.findViewById(R.id.switch1);
        buttonAdd = view.findViewById(R.id.addButton);
        switchButton1 = view.findViewById(R.id.switchButton1);
        switchButton2 = view.findViewById(R.id.switchButton2);
        workout1 = view.findViewById(R.id.workout1);
        workout2 = view.findViewById(R.id.workout2);
        workout3 = view.findViewById(R.id.workout3);
        deleteButton1 = view.findViewById(R.id.deleteButton1);
        deleteButton2 = view.findViewById(R.id.deleteButton2);
        deleteButton3 = view.findViewById(R.id.deleteButton3);
        deleteWorkoutButton = view.findViewById(R.id.DeleteWorkoutButton);
        startButton = view.findViewById(R.id.startButton);
        button_breaktime = view.findViewById(R.id.button_breaktime);
        divider = view.findViewById(R.id.imageView5);


    }

    public void setupTextViews() {
        SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
        textName.setText(sh.getString("Name",""));
        boolean notifications = sh.getBoolean("Notifications", false);


        if (notifications) {
            buttonTime.setVisibility(View.VISIBLE);
            spinnerWeekday.setVisibility(View.VISIBLE);
            int hour = sh.getInt("hour", 0);
            int minute = sh.getInt("minute", 0);
            if (minute<10) {
                buttonTime.setText(hour + ":0" + minute);
            }else {
                buttonTime.setText(hour + ":" + minute);
            }
        }
        else {
            buttonTime.setVisibility(View.GONE);
            spinnerWeekday.setVisibility(View.GONE);
        }

        if (!sh.getString("workout1", "").equals("")) {
            workout1.setText(sh.getString("workout1", ""));
        } else {
            deleteButton1.setVisibility(View.INVISIBLE);
        }
        if (!sh.getString("workout2", "").equals("")) {
            workout2.setText(sh.getString("workout2", ""));
        } else  {
            deleteButton2.setVisibility(View.GONE);
            workout2.setVisibility(View.GONE);
            switchButton1.setVisibility(View.INVISIBLE);
        }
        if (!sh.getString("workout3", "").equals("")) {
            workout3.setText(sh.getString("workout3", ""));
        } else  {
            deleteButton3.setVisibility(View.GONE);
            workout3.setVisibility(View.GONE);
            switchButton2.setVisibility(View.INVISIBLE);
            divider.setVisibility(View.GONE);
        }

        breakTime_min = sh.getInt("breaktime_min",0);
        breakTime_sec = sh.getInt("breaktime_sec",0);
        button_breaktime.setText(breakTime_min+" min "+breakTime_sec+" sec");
        button_breaktime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    selectBreakTime();
            }
        });

    }

    public void setupSpinners() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Training_Days, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinnerWeekday.setAdapter(adapter);
        SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
        String weekday = sh.getString("weekday", "");
        spinnerWeekday.setSelection(adapter.getPosition(weekday));
        spinnerWeekday.setTag(adapter.getPosition(weekday));
        spinnerWeekday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                //((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#5b5b5b"));
                ((TextView) parent.getChildAt(0)).setTextSize(14);
                if(((Integer) spinnerWeekday.getTag()) != position) {
                    try {
                        scheduleWeekdayNotification();
                    } catch (Exception e){}
                    spinnerWeekday.setTag(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> workoutNames = ((WorkoutActivity)getActivity()).workoutnames;
        spinnerWorkout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#5b5b5b"));
                ((TextView) parent.getChildAt(0)).setTextSize(14);
                if(((Integer) spinnerWeekday.getTag()) != position) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, workoutNames);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinnerWorkout.setAdapter(adapter2);
    }

    public void setupListeners() {
        SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
        boolean notifications = sh.getBoolean("Notifications", false);
        switch1.setChecked(notifications);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {           //MAKE TRANSITION!!
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor myEdit = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE).edit();

                if (isChecked) {
                    buttonTime.setVisibility(View.VISIBLE);
                    spinnerWeekday.setVisibility(View.VISIBLE);
                    myEdit.putBoolean("Notifications", true);
                    //System.out.println("HIER: String: "+(String) spinnerWeekday.getSelectedItem());
                    try {
                        scheduleWeekdayNotification();
                    } catch (Exception e){
                        Toast.makeText(getActivity(), "Kanten notifications do not work on this device", Toast.LENGTH_LONG).show();
                    }
                    /*
                    SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                    String weekday = sh.getString("weekday", "");*/
                }
                else {
                    buttonTime.setVisibility(View.GONE);
                    spinnerWeekday.setVisibility(View.GONE);
                    myEdit.putBoolean("Notifications", false);
                    cancelWeekdayNotification();
                }
                myEdit.commit();
            }
        });

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != null) {

                    WorkoutClosedFragment newFragment = new WorkoutClosedFragment();
                    newFragment.setID(id);
                    FragmentTransaction ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(id, newFragment);
                    //ft.addToBackStack(null);
                    ft.commit();
                    WorkoutActivity notification = (WorkoutActivity)getActivity();
                    notification.weekdayFragmentOPEN = false;
                }
            }
        });

        textName.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ((WorkoutActivity)getActivity()).changeWorkoutName(id);
                return true;
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = spinnerWorkout.getSelectedItem().toString();
                SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                if (sh.getString("workout1", "").equals("")) {
                    workout1.setText(s);
                    myEdit.putString("workout1", s);
                    myEdit.commit();
                    deleteButton1.setVisibility(View.VISIBLE);
                } else if (sh.getString("workout2", "").equals("")) {
                    workout2.setText(s);
                    myEdit.putString("workout2", s);
                    myEdit.commit();
                    deleteButton2.setVisibility(View.VISIBLE);
                    workout2.setVisibility(View.VISIBLE);
                    switchButton1.setVisibility(View.VISIBLE);
                } else if (sh.getString("workout3", "").equals("")) {
                    workout3.setText(s);
                    myEdit.putString("workout3", s);
                    myEdit.commit();
                    deleteButton3.setVisibility(View.VISIBLE);
                    workout3.setVisibility(View.VISIBLE);
                    switchButton2.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "maximum number of workout modules added", Toast.LENGTH_LONG).show();
                }
            }
        });


        deleteButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();

                if (sh.getString("workout2", "").equals("")) {
                    myEdit.remove("workout1");
                    workout1.setText("");
                    deleteButton1.setVisibility(View.INVISIBLE);
                } else {
                    myEdit.putString("workout1",sh.getString("workout2", ""));
                    workout1.setText(sh.getString("workout2", ""));

                    if (!sh.getString("workout3", "").equals("")){
                        workout2.setText(sh.getString("workout3", ""));
                        myEdit.putString("workout2",sh.getString("workout3", ""));
                        workout3.setText("");
                        myEdit.remove("workout3");
                        deleteButton3.setVisibility(View.GONE);
                        workout3.setVisibility(View.GONE);
                        switchButton2.setVisibility(View.INVISIBLE);
                        divider.setVisibility(View.GONE);
                    } else {
                        myEdit.remove("workout2");
                        workout2.setText("");
                        deleteButton2.setVisibility(View.GONE);
                        workout2.setVisibility(View.GONE);
                        switchButton1.setVisibility(View.INVISIBLE);
                    }
                }
                myEdit.commit();

            }
        });

        deleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();

                if (sh.getString("workout3", "").equals("")) {
                    myEdit.remove("workout2");
                    workout2.setText("");
                    deleteButton2.setVisibility(View.GONE);
                    workout2.setVisibility(View.GONE);
                    switchButton1.setVisibility(View.INVISIBLE);
                } else {
                    myEdit.putString("workout2",sh.getString("workout3", ""));
                    workout2.setText(sh.getString("workout3", ""));
                    workout3.setText("");
                    myEdit.remove("workout3");
                    deleteButton3.setVisibility(View.GONE);
                    workout3.setVisibility(View.GONE);
                    switchButton2.setVisibility(View.INVISIBLE);
                    divider.setVisibility(View.GONE);
                }
                myEdit.commit();

            }
        });

        deleteButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();

                if (!sh.getString("workout3", "").equals("")) {
                    myEdit.remove("workout3");
                    workout3.setText("");
                    deleteButton3.setVisibility(View.GONE);
                    workout3.setVisibility(View.GONE);
                    switchButton2.setVisibility(View.INVISIBLE);
                    divider.setVisibility(View.GONE);
                }
                myEdit.commit();


            }
        });


        deleteWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(getActivity());
                d.setContentView(R.layout.dialog_delete);
                Button b1 = d.findViewById(R.id.dialogN_button1);
                Button b2 = d.findViewById(R.id.dialogN_button2);


                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        deleteWorkout();
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
        });

        switchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                String w1 = sh.getString("workout1", "");
                String w2 = sh.getString("workout2", "");
                workout1.setText(w2);
                workout2.setText(w1);
                myEdit.putString("workout1", w2);
                myEdit.putString("workout2", w1);
                myEdit.commit();
            }
        });

        switchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                String w2 = sh.getString("workout2", "");
                String w3 = sh.getString("workout3", "");
                workout2.setText(w3);
                workout3.setText(w2);
                myEdit.putString("workout2", w3);
                myEdit.putString("workout3", w2);
                myEdit.commit();
            }
        });



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
                String w1 = sh.getString("workout1", "");
                if (w1.equals("")) {
                    Toast.makeText(getActivity(), "There are no modules in this workout", Toast.LENGTH_LONG).show();
                } else {
                    //getActivity().finish();
                    Intent TimerIntent = new Intent(getActivity().getApplicationContext(), TimerActivity.class);
                    TimerIntent.putExtra(TimerActivity.WORKOUTSET_ID, "Fragment" + id.toString());
                    ((WorkoutActivity) getActivity()).startAnimatedActivity(TimerIntent);
                   // getActivity().finish();
                    //startActivity(TimerIntent);
                }
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(getView());
            }
        });
    }

    public void deleteWorkout() {
        //erase Data
        SharedPreferences.Editor myEdit = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE).edit();
        //myEdit.putString("Name", "");
        myEdit.clear();
        myEdit.commit();

        SharedPreferences.Editor myEdit2 = getActivity().getSharedPreferences("Fragments",MODE_PRIVATE).edit();
        myEdit2.remove("Fragment"+id.toString());
        myEdit2.commit();
        //delete Fragment
        getActivity().finish();
        startActivity(new Intent(getActivity().getApplicationContext(), WorkoutActivity.class));

    }

    public void selectBreakTime() {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_numberpicker_two);
        Button b1 = d.findViewById(R.id.dialog2_button1);
        Button b2 = d.findViewById(R.id.dialog2_button2);
        final NumberPicker np = d.findViewById(R.id.dialog2_numberPicker1);

        np.setMaxValue(59);
        np.setValue(breakTime_min);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        final NumberPicker np2 = d.findViewById(R.id.dialog2_numberPicker2);
        np2.setMaxValue(59);
        np2.setValue(breakTime_sec);
        np2.setMinValue(0);
        np2.setWrapSelectorWheel(false);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((np.getValue() + np2.getValue()) == 0) {
                    Toast.makeText(getActivity(), "Selected time has to be more than 0 seconds", Toast.LENGTH_LONG).show();
                } else {
                    breakTime_min = np.getValue();
                    breakTime_sec = np2.getValue();
                    SharedPreferences.Editor myEdit = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE).edit();
                    myEdit.putInt("breaktime_min", breakTime_min);
                    myEdit.putInt("breaktime_sec", breakTime_sec);
                    myEdit.commit();
                    button_breaktime.setText(breakTime_min+" min "+breakTime_sec+" sec");
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

    public void setNewName(String name) {
        textName.setText(name);
    }

    public void selectTime(View view) {

        final Dialog d = new Dialog(getActivity()); //oder getActivity().getApplicationContext();
        d.setTitle("TimePicker");
        d.setContentView(R.layout.dialog_timepicker);
        Button b1 = d.findViewById(R.id.dialogN_button1);
        Button b2 = d.findViewById(R.id.dialogN_button2);
        final TimePicker tp = d.findViewById(R.id.dialog_selectTime);
        tp.setIs24HourView(true);
        SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
        int hour = sh.getInt("hour", 0);
        int minute = sh.getInt("minute", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp.setHour(hour);
            tp.setMinute(minute);
        } else {
            tp.setCurrentHour(hour);
            tp.setCurrentMinute(minute);
        }



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = tp.getCurrentHour();
                int minute = tp.getCurrentMinute();
                if (minute<10) {
                    buttonTime.setText(hour + ":0" + minute);
                }else {
                    buttonTime.setText(hour + ":" + minute);
                }
                SharedPreferences.Editor myEdit = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE).edit();
                myEdit.putInt("hour", hour);
                myEdit.putInt("minute", minute);
                myEdit.commit();
                try {
                    scheduleWeekdayNotification();
                } catch (Exception e){}
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


    public void scheduleWeekdayNotification() {
        SharedPreferences sh = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE);
        int hour = sh.getInt("hour", 0);
        int minute = sh.getInt("minute", 0);
        String s = (String) spinnerWeekday.getSelectedItem();
        String toastText;
        if (minute<10) {
            toastText = "Selected time is "+s+" at "+hour+".0"+minute;
        }else {
            toastText = "Selected time is "+s+" at "+hour+"."+minute;
        }
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
        SharedPreferences.Editor myEdit = getActivity().getSharedPreferences("Fragment"+id.toString(),MODE_PRIVATE).edit();
        myEdit.putString("weekday", s);
        myEdit.commit();

        int Day_to_set = 1; //Friday

        if (s.equals("Monday")) {
            Day_to_set = 2;
        } else if (s.equals("Tuesday")) {
            Day_to_set = 3;
        } else if (s.equals("Wednesday")) {
            Day_to_set = 4;
        } else if (s.equals("Thursday")) {
            Day_to_set = 5;
        } else if (s.equals("Friday")) {
            Day_to_set = 6;
        } else if (s.equals("Saturday")) {
            Day_to_set = 7;
        } else if (s.equals("Sunday")) {
            Day_to_set = 1;
        }

        Intent intent = new Intent(getActivity()/*.getApplicationContext()*/, TimerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String fragmentName = "Fragment"+id.toString();
        System.out.println("HIER: vor Intent: "+fragmentName);
        intent.putExtra(TimerActivity.WORKOUTSET_ID, fragmentName);

        //Intent intent = new Intent(getActivity(), WorkoutModuleActivity.class);
        PendingIntent resultIntent = PendingIntent.getActivity(getActivity(), 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), id.toString());
        mBuilder.setSmallIcon(R.drawable.hangboard_pic);
        mBuilder.setContentTitle("Hangboard Reminder");
        mBuilder.setContentText("Heute ist eine Trainingssession angesetzt: "+textName.getText());
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setContentIntent(resultIntent);
        mBuilder.setAutoCancel(true);
        Notification notification = mBuilder.build();



        Intent notificationIntent = new Intent(getActivity(), MyNotificationPublisher.class ) ;
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, id );
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( getActivity(), 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT );


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Uhrzeit"+calendar.toString());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        if (calendar2.get(Calendar.DAY_OF_WEEK)>Day_to_set) {
            calendar2.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR)+1);
        }
        calendar2.set(Calendar.DAY_OF_WEEK, Day_to_set);
        calendar2.set(Calendar.HOUR_OF_DAY, hour);
        calendar2.set(Calendar.MINUTE, minute);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 1000 * 60 * 60 * 24 * 7, pendingIntent); //bei Zeitumstellung gibt es ein Problem, evtl noch mit Alarm Handler lösen
    }


    public void cancelWeekdayNotification() {

        Intent notificationIntent = new Intent( getActivity(), MyNotificationPublisher.class ) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast ( getActivity(), 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT );


        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent); //bei Zeitumstellung gibt es ein Problem, evtl noch mit Alarm Handler lösen

    }

}
