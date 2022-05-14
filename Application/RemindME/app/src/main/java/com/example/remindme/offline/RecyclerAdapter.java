package com.example.remindme.offline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.R;
import com.example.remindme.alarm.AlarmReceiver;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ReminderViewHolder> {
    private final Context context;
    private final reminderDB mDatabase;
    private final ArrayList<Reminders> listReminders;

    public RecyclerAdapter(Context context, ArrayList<Reminders> listReminders) {
        this.context = context;
        this.listReminders = listReminders;
        mDatabase = new reminderDB(context);
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
        final Reminders reminders = listReminders.get(position);
        holder.tvName.setText(reminders.getName());
        holder.tvDesc.setText(reminders.getMedDesc());
        holder.tvDate.setText(reminders.getDate());
        holder.tvTime.setText(reminders.getTime());
        holder.editButton.setOnClickListener(view -> editReminder(reminders, reminders.getId()));
        holder.deleteButton.setOnClickListener(view -> {
            mDatabase.deleteReminder(reminders.getId());
            AlarmReceiver.setReminderID(reminders.getId());
            AlarmReceiver.cancelAlarm(context);
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(0, 0);
            context.startActivity(((Activity) context).getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        });
    }

    @Override
    public int getItemCount() {
        return listReminders.size();
    }

    private void editReminder(final Reminders reminders, int ID) {
        if (reminders != null) {
            Intent intent = new Intent(context, EditReminder.class);
            intent.putExtra("ID", ID);
            intent.putExtra("medName", reminders.getName());
            intent.putExtra("medDesc", reminders.getMedDesc());
            intent.putExtra("medDate", reminders.getDate());
            intent.putExtra("medTime", reminders.getTime());
            intent.putExtra("isRepeating", reminders.getIsRepeating());
            context.startActivity(intent);
        }
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
