package com.example.remindme.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.remindme.user.SharedPrefManager;

public class BootReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (sharedPrefManager.getEmail().equals("OFFLINE-USER")) {
                AlarmReceiver.offlineAlarms(context);
            } else {
                AlarmReceiver.onlineAlarms(context);
            }
        }
    }
}
