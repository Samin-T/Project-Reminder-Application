package com.example.remindme.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.MainActivity;
import com.example.remindme.R;
import com.example.remindme.offline.Reminders;
import com.example.remindme.offline.reminderDB;
import com.example.remindme.user.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {
    static MediaPlayer mediaPlayer;
    static PendingIntent pendingIntent;
    static AlarmManager alarmManager;
    static int reminderID;
    static List<Integer> idList = new ArrayList<>();

    // Stop Ringing
    public static void alarmStop() {
        mediaPlayer.stop();
    }

    // Set Alarm
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    public static void setAlarm(Context context, Calendar calendar, int requestCode, String medName, String medDate, String medTime, String isRepeating) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("mName", medName);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("mDate", medDate);
        intent.putExtra("mTime", medTime);
        intent.putExtra("isRepeating", isRepeating);
        idList.add(requestCode);

        long AlarmTime;
        if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
            AlarmTime = calendar.getTimeInMillis() + 86400000;
        } else {
            AlarmTime = calendar.getTimeInMillis();
        }

        if (isRepeating.equals("true")) {
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, AlarmTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.i("REPEATING ALARM SET", String.valueOf(requestCode));
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, AlarmTime, pendingIntent);
            Log.i("ALARM SET", String.valueOf(requestCode));
        }
    }

    // Set Reminder ID
    public static void setReminderID(int reminderID) {
        AlarmReceiver.reminderID = reminderID;
    }

    // Cancel Alarm
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    public static void cancelAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, reminderID, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        Log.i("CANCELED", String.valueOf(reminderID));
        reminderID = 0;
    }

    // Cancel All
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    public static void cancelAll(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        for (int i = 0; i < idList.size(); i++) {
            pendingIntent = PendingIntent.getBroadcast(context, idList.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);
            Log.i("ALARM CANCELED", String.valueOf(idList.get(i)));
        }
    }

    // offlineAlarms
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void offlineAlarms(Context context) {
        reminderDB mDatabase = new reminderDB(context);
        ArrayList<Reminders> reminders = mDatabase.listReminders();

        for (int i = 0; i < reminders.size(); i++) {

            Calendar mCalendar = Calendar.getInstance();

            String mDate = reminders.get(i).getDate();
            String mTime = reminders.get(i).getTime();
            String mName = reminders.get(i).getName();
            int requestCode = reminders.get(i).getId();

            String isRepeating = reminders.get(i).getIsRepeating();

            // Format Date
            String[] d = mDate.split("-");
            int mDay = Integer.parseInt(d[0].trim());
            int mMonth = Integer.parseInt(d[1].trim());
            int mYear = Integer.parseInt(d[2].trim());

            // Format Time
            String[] t = mTime.split(":");
            int mHour = Integer.parseInt(t[0].trim());
            int mMin = Integer.parseInt(t[1].trim());

            Log.d("TEST-DATE", mDay + "-" + mMonth + "-" + mYear);
            Log.d("TEST-TIME", "Hour : " + mHour + " Minute : " + mMin);

            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMin);
            mCalendar.set(Calendar.SECOND, 0);

            setAlarm(context, mCalendar, requestCode, mName, mDate, mTime, isRepeating);
            Log.e("offlineAlarms", "Alarm Set: " + requestCode);
        }
    }

    // onlineAlarms
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void onlineAlarms(Context context) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String URL_REMINDERS = "http://192.168.0.81/remindme/api/listreminders.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REMINDERS,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject reminders = jsonArray.getJSONObject(i);

                            int requestCode = reminders.getInt("reminder_id");
                            String mName = reminders.getString("med_name");
                            String mDate = reminders.getString("date");
                            String mTime = reminders.getString("time");
                            String isRepeating = reminders.getString("isRepeating");

                            // ADD ALARM
                            Calendar mCalendar = Calendar.getInstance();

                            // Format Time
                            String[] t = mTime.split(":");
                            int mHour = Integer.parseInt(t[0].trim());
                            int mMin = Integer.parseInt(t[1].trim());

                            // Format Date
                            String[] d = mDate.split("-");
                            int mDay = Integer.parseInt(d[0].trim());
                            int mMonth = Integer.parseInt(d[1].trim());
                            int mYear = Integer.parseInt(d[2].trim());

                            Log.d("TEST-DATE", mDay + "-" + mMonth + "-" + mYear);
                            Log.d("TEST-TIME", "Hour : " + mHour + " Minute : " + mMin);

                            mCalendar.set(Calendar.MONTH, --mMonth);
                            mCalendar.set(Calendar.YEAR, mYear);
                            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                            mCalendar.set(Calendar.MINUTE, mMin);
                            mCalendar.set(Calendar.SECOND, 0);

                            setAlarm(context, mCalendar, requestCode, mName, mDate, mTime, isRepeating);
                            Log.e("onlineAlarms", "Alarm Set: " + requestCode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("ERROR:", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", sharedPrefManager.getEmail());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void createNotification(Context context, String medName, String
            medDate, String medTime, int requestCode, String isRepeating) {

        // Take Button
        Intent intentTake = new Intent(context, TakeReceiver.class);
        intentTake.putExtra("mName", medName);
        intentTake.putExtra("requestCode", requestCode);
        intentTake.putExtra("mDate", medDate);
        intentTake.putExtra("mTime", medTime);
        intentTake.putExtra("isRepeating", isRepeating);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingTakeIntent = PendingIntent.getBroadcast(context, 1, intentTake, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Skip/Dismiss Button
        Intent intentDismiss = new Intent(context, DismissReceiver.class);
        intentDismiss.putExtra("mName", medName);
        intentDismiss.putExtra("requestCode", requestCode);
        intentDismiss.putExtra("mDate", medDate);
        intentDismiss.putExtra("mTime", medTime);
        intentDismiss.putExtra("isRepeating", isRepeating);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingDismissIntent = PendingIntent.getBroadcast(context, 1, intentDismiss, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Notification https://developer.android.com/training/notify-user/build-notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ID")
                .setSmallIcon(R.drawable.add_button)
                .setContentTitle(medName)
                .setContentText(medDate + " " + medTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Open MainActivity when notification clicked
                .addAction(0, "Take", pendingTakeIntent)
                .addAction(0, "Dismiss", pendingDismissIntent)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ID", "RemindME", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String medName = bundle.getString("mName");
        String medDate = bundle.getString("mDate");
        String medTime = bundle.getString("mTime");
        reminderID = bundle.getInt("requestCode");

        // Repeating Alarm
        String isRepeating = bundle.getString("isRepeating");

        // Ring
        mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        Log.i("ALARM RINGING", String.valueOf(reminderID));

        // Create Notification
        createNotification(context, medName, medDate, medTime, reminderID, isRepeating);
    }
}
