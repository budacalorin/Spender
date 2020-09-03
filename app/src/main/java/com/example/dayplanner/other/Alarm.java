package com.example.dayplanner.other;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.dayplanner.R;

import java.text.DateFormat;
import java.util.Calendar;

public class Alarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    static MediaPlayer player;
    public TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mTextView = findViewById(R.id.textView);

        Button buttonTimePicker = findViewById(R.id.alarmButton);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new AlarmFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        Button buttonCancelAlarma = findViewById(R.id.button_cancel);
        buttonCancelAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        Toast.makeText(this,"ALARM SET WORKS", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(hourOfDay + ":" + minute);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startAlarm(c);
    }

    private void updateTimeText(Calendar c)
    {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        mTextView.setText(timeText);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startAlarm(Calendar c)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if(c.before(Calendar.getInstance()))
        {
            c.add(Calendar.DATE,1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);
        //soundStart();
    }

    private void cancelAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarm canceled");
        Toast.makeText(this,"ALARM CANCEL WORKS", Toast.LENGTH_SHORT).show();
        soundStop();
    }

    public void soundStart()
    {
        if(player == null)
        {
            player = MediaPlayer.create(this, R.raw.song);
        }
        player.start();

        Toast.makeText(this,"SOUND WORKS", Toast.LENGTH_SHORT).show();
    }

    public void soundStop()
    {
        if(player!=null)
        {
            player.release();
            player=null;
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        soundStop();
    }

}
