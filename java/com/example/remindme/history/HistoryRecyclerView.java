package com.example.remindme.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.R;

import java.util.List;

public class HistoryRecyclerView extends RecyclerView.Adapter<HistoryRecyclerView.HistoryViewHolder> {
    private final Context context;
    private final List<History> historyList;

    public HistoryRecyclerView(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        History history = historyList.get(position);

        holder.tvName.setText(history.getMedName());
        holder.tvDate.setText(history.getMedDate());
        holder.tvTime.setText(history.getMedTime());
        holder.tvStatus.setText(history.getStatus());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvTime, tvStatus;

        HistoryViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.medName);
            tvDate = itemView.findViewById(R.id.medDate);
            tvTime = itemView.findViewById(R.id.medTime);
            tvStatus = itemView.findViewById(R.id.medStatus);
        }
    }
}

