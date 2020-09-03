package com.example.dayplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dayplanner.other.Alarm;
import com.example.dayplanner.other.Notification;
import com.example.dayplanner.selectbudget.SelectDatabaseSpenderActivity;
import com.example.dayplanner.test.TestActivity;

public class MainActivity extends AppCompatActivity {

    private Button alarmButton;
    private Button notificationButton;
    private Button spenderButton;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_main);

        alarmButton= (Button)findViewById(R.id.setAlarmButton);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlarm();
            }
        });

        notificationButton = (Button)findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationScript();
            }
        });

        spenderButton = (Button)findViewById(R.id.spenderButton);
        spenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpenderScript();
            }
        });

        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTestActivity();
            }
        });



    }

    public void openAlarm()
    {
        Intent intent = new Intent(this, Alarm.class);
        startActivity(intent);
    }

    public void openNotificationScript()
    {
        Intent intent = new Intent(this, Notification.class);;
        startActivity(intent);
    }

    public void openSpenderScript()
    {
        Intent intent = new Intent(this, SelectDatabaseSpenderActivity.class);
        startActivity(intent);
    }

    public void openTestActivity()
    {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

}
