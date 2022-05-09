package com.example.remindme.ui.water;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.remindme.WaterReceiver;
import com.example.remindme.databinding.FragmentWaterBinding;

public class WaterFragment extends Fragment {

    private FragmentWaterBinding binding;
    private Long waterInterval;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WaterViewModel waterViewModel =
                new ViewModelProvider(this).get(WaterViewModel.class);

        binding = FragmentWaterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (binding.switchBtn.isChecked())
                WaterReceiver.setWaterReminder(requireContext(), waterInterval);
            else
                WaterReceiver.cancelReminder(requireContext());
        });

        binding.layout.setOnClickListener(v -> waterInterval = 60000L); //1800000L
        binding.layout1.setOnClickListener(v -> waterInterval = 3600000L);
        binding.layout2.setOnClickListener(v -> waterInterval = 7200000L);
        binding.layout3.setOnClickListener(v -> waterInterval = 10800000L);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}