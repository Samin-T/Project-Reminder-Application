package com.example.remindme.ui.medication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.PHPLinks;
import com.example.remindme.databinding.FragmentMedicationBinding;
import com.example.remindme.offline.AddReminder;
import com.example.remindme.offline.RecyclerAdapter;
import com.example.remindme.offline.Reminders;
import com.example.remindme.offline.reminderDB;
import com.example.remindme.online.AddOnlineReminder;
import com.example.remindme.online.Reminder;
import com.example.remindme.online.ReminderAdapter;
import com.example.remindme.user.SharedPrefManager;
import com.example.remindme.ui.medication.MedicationViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicationFragment extends Fragment {

    List<Reminder> reminderList;
    RecyclerView recyclerView;
    private FragmentMedicationBinding binding;
    private SharedPrefManager sharedPrefManager;
    private LottieAnimationView lottieAnimationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MedicationViewModel medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);

        binding = FragmentMedicationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lottieAnimationView = binding.animationView;

        sharedPrefManager = new SharedPrefManager(root.getContext());

        if (sharedPrefManager.getEmail().equals("OFFLINE-USER")) {
            RecyclerView reminderView = binding.recyclerView;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            reminderView.setLayoutManager(linearLayoutManager);
            reminderView.setHasFixedSize(true);
            reminderDB mDatabase = new reminderDB(getContext());
            ArrayList<Reminders> reminders = mDatabase.listReminders();
            if (reminders.size() > 0) {
                lottieAnimationView.setVisibility(View.GONE);
                reminderView.setVisibility(View.VISIBLE);
                RecyclerAdapter mAdapter = new RecyclerAdapter(getContext(), reminders);
                reminderView.setAdapter(mAdapter);
            } else {
                reminderView.setVisibility(View.GONE);
            }

            // Add new reminder
            binding.createReminder.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), AddReminder.class);
                startActivity(intent);
            });

        } else {
            recyclerView = binding.recyclerView;
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            reminderList = new ArrayList<>();
            listRemindersOnline();

            // Add new reminder
            binding.createReminder.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), AddOnlineReminder.class);
                startActivity(intent);
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void listRemindersOnline() {
        String URL_REMINDERS = PHPLinks.listReminder;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REMINDERS,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject reminders = jsonArray.getJSONObject(i);
                            reminderList.add(new Reminder(
                                    reminders.getInt("reminder_id"),
                                    reminders.getString("med_name"),
                                    reminders.getString("med_desc"),
                                    reminders.getString("date"),
                                    reminders.getString("time"),
                                    reminders.getString("isRepeating"),
                                    reminders.getString("user_email"),
                                    reminders.getString("hcw_email")
                            ));
                        }

                        if (reminderList.size() > 0) {
                            lottieAnimationView.setVisibility(View.GONE);
                        }

                        //creating adapter object and setting it to recyclerview
                        ReminderAdapter adapter = new ReminderAdapter(getActivity(), reminderList);
                        recyclerView.setAdapter(adapter);
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

        Volley.newRequestQueue(requireActivity()).add(stringRequest);
    }
}
