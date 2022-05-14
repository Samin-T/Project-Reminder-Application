package com.example.remindme.offline;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remindme.MainActivity;
import com.example.remindme.R;
import com.example.remindme.alarm.AlarmReceiver;
import com.example.remindme.databinding.AddReminderBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

public class EditReminder extends AppCompatActivity {
    TextInputEditText btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMin;
    private AddReminderBinding binding;
    private boolean isRepeating;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addReminder.setText("Save");

        reminderDB mDatabase = new reminderDB(this);
        btnDatePicker = binding.date;
        btnTimePicker = binding.time;

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        int editMedID = bundle.getInt("ID");
        String editMedName = bundle.getString("medName");
        String editMedDesc = bundle.getString("medDesc");
        String editMedDate = bundle.getString("medDate");
        String editMedTime = bundle.getString("medTime");
        String editRepeat = bundle.getString("isRepeating");

        binding.checkboxRepeating.setChecked(editRepeat.equals("true"));
        isRepeating = editRepeat.equals("true");

        binding.name.setText(editMedName);
        binding.desc.setText(editMedDesc);
        binding.date.setText(editMedDate);
        binding.time.setText(editMedTime);

        TextInputEditText nameField = binding.name;
        TextInputEditText medDesc = binding.desc;
        TextInputEditText txtDate = binding.date;
        TextInputEditText txtTime = binding.time;

        // Add Reminder Button
        binding.addReminder.setOnClickListener(view -> {
            final String name = Objects.requireNonNull(nameField.getText()).toString();
            final String desc = Objects.requireNonNull(medDesc.getText()).toString();
            final String date = Objects.requireNonNull(txtDate.getText()).toString();
            final String time = Objects.requireNonNull(txtTime.getText()).toString();

            if (TextUtils.isEmpty(name)) {
                Snackbar.make(findViewById(R.id.addReminder), "Something went wrong. Check your input values",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {

                // ADD ALARM
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

                Log.d("TEST-DATE", mDay + "-" + mMonth + "-" + mYear);
                Log.d("TEST-TIME", "Hour : " + mHour + " Minute : " + mMin);

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
                    Reminders reminder = new Reminders(editMedID, name, desc, date, time, String.valueOf(isRepeating));
                    mDatabase.editReminder(reminder);

                    // Set Alarm
                    AlarmReceiver.cancelAll(this);
                    AlarmReceiver.offlineAlarms(this);
                    Intent intent = new Intent(this, MainActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    this.finish();
                }
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

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        (view1, year, monthOfYear, dayOfMonth) -> txtDate.setText(checkDigit(dayOfMonth) + "-" + checkDigit(monthOfYear + 1) + "-" + checkDigit(year)), mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Pick Time Button
        btnTimePicker.setOnClickListener(view -> {
            if (view == btnTimePicker) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMin = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        (view12, hourOfDay, minute) -> txtTime.setText(checkDigit(hourOfDay) + ":" + checkDigit(minute)), mHour, mMin, true);
                timePickerDialog.show();
            }
        });
    }

    // Fix Missing Zero
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void onCheckboxClicked(View view) {
        isRepeating = binding.checkboxRepeating.isChecked();
    }
}
