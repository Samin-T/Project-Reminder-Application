package com.example.remindme.online;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.MainActivity;
import com.example.remindme.PHPLinks;
import com.example.remindme.alarm.AlarmReceiver;
import com.example.remindme.databinding.AddReminderBinding;
import com.example.remindme.user.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AddOnlineReminder extends AppCompatActivity {
    private final String URL_ADD = PHPLinks.setReminder;
    AddReminderBinding binding;
    TextInputEditText medName, medDesc, reminderDate, reminderTime, btnDatePicker, btnTimePicker;
    private SharedPrefManager sharedPrefManager;
    private int mYear, mMonth, mDay, mHour, mMin;
    private RequestQueue requestQueue;
    private boolean isRepeating;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new SharedPrefManager(binding.getRoot().getContext());

        btnDatePicker = binding.date;
        btnTimePicker = binding.time;
        medName = binding.name;
        medDesc = binding.desc;
        reminderDate = binding.date;
        reminderTime = binding.time;

        // Add Reminder Button
        binding.addReminder.setOnClickListener(view -> {
            final String name = Objects.requireNonNull(medName.getText()).toString();
            final String desc = Objects.requireNonNull(medDesc.getText()).toString();
            final String date = Objects.requireNonNull(reminderDate.getText()).toString();
            final String time = Objects.requireNonNull(reminderTime.getText()).toString();

            // CHECK DATE & TIME
            Calendar mCalendar = Calendar.getInstance();

            // Format Time
            String[] t = time.split(":");
            mHour = Integer.parseInt(t[0].trim());
            mMin = Integer.parseInt(t[1].trim());

            // Format Date
            String[] d = date.split("-");
            mDay = Integer.parseInt(d[0].trim());
            mMonth = Integer.parseInt(d[1].trim());
            mYear = Integer.parseInt(d[2].trim());

            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMin);
            mCalendar.set(Calendar.SECOND, 0);

            if (System.currentTimeMillis() > mCalendar.getTimeInMillis()) {
                Snackbar snackbar = Snackbar
                        .make(binding.getRoot(), "Input time has past", Snackbar.LENGTH_LONG);
                snackbar.show();

            } else {
                addReminder(name, desc, date, time, String.valueOf(isRepeating));
            }
        });

        // Cancel Button
        binding.cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        });

        // Pick Date Button
        btnDatePicker.setOnClickListener(view -> {
            if (view == btnDatePicker) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        (view1, year, monthOfYear, dayOfMonth) -> reminderDate.setText(checkDigit(dayOfMonth) + "-" + checkDigit(monthOfYear + 1) + "-" + checkDigit(year)), mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Pick Time Button
        btnTimePicker.setOnClickListener(view -> {
            if (view == btnTimePicker) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMin = c.get(Calendar.MINUTE);

                @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        (view12, hourOfDay, minute) -> reminderTime.setText(checkDigit(hourOfDay) + ":" + checkDigit(minute)), mHour, mMin, true);
                timePickerDialog.show();
            }
        });
    }

    // Fix Missing Zero
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    // Add Reminder
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addReminder(String medName, String medDesc, String medDate, String medTime, String isRepeating) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD,
                response -> {
                    Log.i("SET ONLINE RESPONSE: ", response);

                    requestQueue.getCache().clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.optString("status").equals("true")) {
                            // Set Alarm
                            AlarmReceiver.cancelAll(this);
                            AlarmReceiver.onlineAlarms(this);

                            //Toast.makeText(AddOnlineReminder.this, "Reminder set", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddOnlineReminder.this, MainActivity.class);
                            startActivity(intent);
                            this.finish();
                        } else {
                            Toast.makeText(AddOnlineReminder.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("ADD REMINDER:", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("MedName", medName);
                params.put("MedDesc", medDesc);
                params.put("ReminderDate", medDate);
                params.put("ReminderTime", medTime);
                params.put("isRepeating", isRepeating);
                params.put("UserEmail", sharedPrefManager.getEmail());
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(AddOnlineReminder.this);
        requestQueue.add(stringRequest);
    }

    public void onCheckboxClicked(View view) {
        isRepeating = binding.checkboxRepeating.isChecked();
    }
}
