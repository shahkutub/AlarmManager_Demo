package com.alarmmanager_demo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //Pending intent instance
    private PendingIntent pendingIntent,pendingIntent2;
    Intent alarmIntent;
    Intent alarmIntent2;
    private RadioButton secondsRadioButton, minutesRadioButton, hoursRadioButton;

    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    private static final int ALARM_REQUEST_CODE2 = 134;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find id of all radio buttons
        secondsRadioButton = (RadioButton) findViewById(R.id.seconds_radio_button);
        minutesRadioButton = (RadioButton) findViewById(R.id.minutes_radio_button);
        hoursRadioButton = (RadioButton) findViewById(R.id.hours_radio_button);

        /* Retrieve a PendingIntent that will perform a broadcast */
        alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent2 = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
        pendingIntent2 = PendingIntent.getBroadcast(MainActivity.this, ALARM_REQUEST_CODE2, alarmIntent2, 2);

        //Find id of Edit Text
        final EditText editText = (EditText) findViewById(R.id.input_interval_time);

        //Set On CLick over start alarm button
        findViewById(R.id.start_alarm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getInterval = editText.getText().toString().trim();//get interval from edittext

                //check interval should not be empty and 0
               // if (!getInterval.equals("") && !getInterval.equals("0"))
                    //finally trigger alarm manager
                    //triggerAlarmManager(getTimeInterval(getInterval));
                    startAlarm();
                startAlarm2();

            }
        });

        //set on click over stop alarm button
        findViewById(R.id.stop_alarm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Stop alarm manager
                stopAlarmManager();
            }
        });

    }

    //get time interval to trigger alarm manager
    private int getTimeInterval(String getInterval) {
        int interval = Integer.parseInt(getInterval);//convert string interval into integer

        //Return interval on basis of radio button selection
        if (secondsRadioButton.isChecked())
            return interval;
        if (minutesRadioButton.isChecked())
            return interval * 60;//convert minute into seconds
        if (hoursRadioButton.isChecked()) return interval * 60 * 60;//convert hours into seconds

        //else return 0
        return 0;
    }


    //Trigger alarm manager with entered time interval
    public void triggerAlarmManager(int alarmTriggerTime) {
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

        Toast.makeText(this, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();
    }


    private void startAlarm(){
        //Intent myIntent = new Intent(MainActivity.this , AlarmNotificationService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 42);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
        Toast.makeText(this, "Alarm Set for " + "11" + " seconds.", Toast.LENGTH_SHORT).show();

    }


    private void startAlarm2(){
        //Intent myIntent = new Intent(MainActivity.this , AlarmNotificationService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 44);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent2);
        Toast.makeText(this, "Alarm Set for " + "11" + " seconds.", Toast.LENGTH_SHORT).show();

    }
    //Stop/Cancel alarm manager
    public void stopAlarmManager() {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent


        //Stop the Media Player Service to stop sound
        stopService(new Intent(MainActivity.this, AlarmSoundService.class));

        //remove the notification from notification tray
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);

        Toast.makeText(this, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
    }
}
