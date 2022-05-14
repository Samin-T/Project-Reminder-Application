package com.example.remindme.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.PHPLinks;
import com.example.remindme.history.History;
import com.example.remindme.history.HistoryDB;
import com.example.remindme.user.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DismissReceiver extends BroadcastReceiver {
    private RequestQueue requestQueue;
    private SharedPrefManager sharedPrefManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPrefManager = new SharedPrefManager(context);

        Bundle bundle = intent.getExtras();
        String medName = bundle.getString("mName");
        String medDate = bundle.getString("mDate");
        String medTime = bundle.getString("mTime");
        String isRepeating = bundle.getString("isRepeating");

        // Stop Ringing
        AlarmReceiver.alarmStop();

        // If alarm is not repeating, cancel
        if (isRepeating.equals("false"))
            AlarmReceiver.cancelAlarm(context);

        NotificationManagerCompat.from(context).cancel(null, 1);
        Toast.makeText(context.getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();

        // Online / Offline user
        if (sharedPrefManager.getEmail().equals("OFFLINE-USER")) {
            HistoryDB historyDB = new HistoryDB(context);
            History history = new History(medName, medDate, medTime, "Dismiss");
            historyDB.addHistory(history);
        } else {
            dismissOnline(context, medName, medDate, medTime);
        }
    }

    // Dismiss online alarm ( Log History )
    public void dismissOnline(Context context, String medName, String medDate, String medTime) {
        requestQueue = Volley.newRequestQueue(context);
        String url = PHPLinks.logHistory;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    requestQueue.getCache().clear();
                    Log.e("HISTORY:", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("status").equals("true")) {
                            Log.i("DISMISS", "ONLINE ALARM DISMISSED");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("medName", medName);
                params.put("medDate", medDate);
                params.put("medTime", medTime);
                params.put("status", "Dismissed");
                params.put("userEmail", sharedPrefManager.getEmail());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}



