package com.example.remindme.history;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.databinding.ActivityHistoryBinding;
import com.example.remindme.user.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    List<History> historyList;
    private SharedPrefManager sharedPrefManager;
    private ActivityHistoryBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        historyList = new ArrayList<>();
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getEmail().equals("OFFLINE-USER")) {
            RecyclerView historyView = binding.recyclerView;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            historyView.setLayoutManager(linearLayoutManager);
            historyView.setHasFixedSize(true);
            HistoryDB historyDB = new HistoryDB(this);
            ArrayList<History> history = historyDB.listHistory();
            if (history.size() > 0) {
                historyView.setVisibility(View.VISIBLE);
                HistoryRecyclerView mAdapter = new HistoryRecyclerView(this, history);
                historyView.setAdapter(mAdapter);
            } else {
                historyView.setVisibility(View.GONE);
            }
        } else {
            listOnlineHistory();
        }
    }

    public void listOnlineHistory() {

        RecyclerView historyView = binding.recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        historyView.setLayoutManager(linearLayoutManager);
        historyView.setHasFixedSize(true);

        String URL_REMINDERS = "http://192.168.0.81/remindme/api/listhistory.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REMINDERS,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject history = jsonArray.getJSONObject(i);
                            historyList.add(new History(
                                    history.getString("medName"),
                                    history.getString("medDate"),
                                    history.getString("medTime"),
                                    history.getString("status")
                            ));
                        }

                        HistoryRecyclerView adapter = new HistoryRecyclerView(this, historyList);
                        historyView.setAdapter(adapter);
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
        Volley.newRequestQueue(this).add(stringRequest);
    }
}

