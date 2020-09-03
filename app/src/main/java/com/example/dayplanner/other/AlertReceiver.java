package com.example.dayplanner.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String title = "Alarma";
        String message = "Este ora: ";
        Calendar c = Calendar.getInstance();
        int hour = c.get(c.HOUR_OF_DAY);
        if(hour<10)
            message+="0";
        message+=hour;
        int minute = c.get(c.MINUTE);
        message+=":";
        if(minute<10)
            message+="0";
        message+=minute;
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title, message);
        notificationHelper.getManager().notify(1, nb.build());
    }

}
