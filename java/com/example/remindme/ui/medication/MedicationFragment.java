package com.example.remindme.ui.medication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.remindme.databinding.FragmentMedicationBinding;
import com.example.remindme.offline.AddReminder;
import com.example.remindme.offline.RecyclerAdapter;
import com.example.remindme.offline.Reminders;
import com.example.remindme.offline.reminderDB;
import com.example.remindme.user.SharedPrefManager;

import java.util.ArrayList;

public class MedicationFragment extends Fragment {

    private MedicationViewModel homeViewModel;
    private FragmentMedicationBinding binding;
    private SharedPrefManager sharedPrefManager;
    private LottieAnimationView lottieAnimationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MedicationViewModel medicationViewModel =
                new ViewModelProvider(this).get(MedicationViewModel.class);

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
            Log.i("ONLINE REMINDER", "SET");
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}