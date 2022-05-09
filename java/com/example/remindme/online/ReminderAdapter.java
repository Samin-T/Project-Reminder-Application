package com.example.remindme.online;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.R;
import com.example.remindme.alarm.AlarmReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private final Context context;
    private final List<Reminder> reminderList;
    private RequestQueue requestQueue;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ReminderViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        final Reminder reminder = reminderList.get(position);

        holder.tvName.setText(reminder.getMedName());
        holder.tvDesc.setText(reminder.getMedDesc());
        holder.tvDate.setText(reminder.getMedDate());
        holder.tvTime.setText(reminder.getMedTime());

        holder.deleteButton.setOnClickListener(view -> {
            deleteReminder(reminder.getId());
            context.startActivity(((Activity) context).getIntent());
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(0, 0);
            context.startActivity(((Activity) context).getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        });

        holder.editButton.setOnClickListener(view -> editReminder(reminder, reminder.getId()));

    }

    private void editReminder(final Reminder reminder, int ID) {
        if (reminder != null) {
            Intent intent = new Intent(context, EditOnlineReminder.class);
            intent.putExtra("ID", reminder.getId());
            intent.putExtra("medName", reminder.getMedName());
            intent.putExtra("medDesc", reminder.getMedDesc());
            intent.putExtra("medDate", reminder.getMedDate());
            intent.putExtra("medTime", reminder.getMedTime());
            intent.putExtra("isRepeating", reminder.getIsRepeating());
            context.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void deleteReminder(int id) {
        requestQueue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.81/remindme/api/deletereminder.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    requestQueue.getCache().clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("true")) {
                            Log.i("REMINDER DELETED", id + " was deleted");
                            AlarmReceiver.setReminderID(id);
                            AlarmReceiver.cancelAlarm(context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvTime, tvDesc;
        Button deleteButton, editButton;

        ReminderViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.medName);
            tvDesc = itemView.findViewById(R.id.medDesc);
            tvDate = itemView.findViewById(R.id.medDate);
            tvTime = itemView.findViewById(R.id.medTime);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}

