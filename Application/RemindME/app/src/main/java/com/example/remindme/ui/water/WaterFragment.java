package com.example.remindme.ui.water;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.remindme.WaterReceiver;
import com.example.remindme.WaterSharedPref;
import com.example.remindme.databinding.FragmentWaterBinding;

public class WaterFragment extends Fragment {

    private FragmentWaterBinding binding;
    private Long waterInterval;
    private WaterSharedPref waterSharedPref;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WaterViewModel dashboardViewModel = new ViewModelProvider(this).get(WaterViewModel.class);

        binding = FragmentWaterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        waterSharedPref = new WaterSharedPref(requireContext());

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });

        // setChecked
        binding.switchBtn.setChecked(waterSharedPref.getIsActive());

        // Switch On/Off
        binding.switchBtn.setOnCheckedChangeListener((switchBtn, isChecked) -> {
            if (binding.switchBtn.isChecked()) {
                if (waterInterval == null) {
                    binding.switchBtn.setChecked(false);
                    Toast.makeText(requireContext(), "Please select an interval", Toast.LENGTH_SHORT).show();
                } else {
                    waterSharedPref.setIsActive(true);
                    WaterReceiver.setWaterReminder(requireContext(), waterInterval);
                    Toast.makeText(requireContext(), "Water Reminder: On", Toast.LENGTH_SHORT).show();
                }
            } else {
                WaterReceiver.cancelReminder(requireContext());
                waterSharedPref.setIsActive(false);
                Toast.makeText(requireContext(), "Water Reminder: Off", Toast.LENGTH_SHORT).show();
            }
        });

        binding.layout.setOnClickListener(v -> {
            waterInterval = 1800000L; // 60000L;
            binding.layout.setBackgroundColor(Color.argb(90, 204, 204, 204));
            binding.layout1.setBackgroundColor(0);
            binding.layout2.setBackgroundColor(0);
            binding.layout3.setBackgroundColor(0);
        });
        binding.layout1.setOnClickListener(v -> {
            waterInterval = 3600000L;
            binding.layout1.setBackgroundColor(Color.argb(90, 204, 204, 204));
            binding.layout.setBackgroundColor(0);
            binding.layout2.setBackgroundColor(0);
            binding.layout3.setBackgroundColor(0);
        });
        binding.layout2.setOnClickListener(v -> {
            waterInterval = 7200000L;
            binding.layout2.setBackgroundColor(Color.argb(90, 204, 204, 204));
            binding.layout1.setBackgroundColor(0);
            binding.layout.setBackgroundColor(0);
            binding.layout3.setBackgroundColor(0);
        });
        binding.layout3.setOnClickListener(v -> {
            waterInterval = 10800000L;
            binding.layout3.setBackgroundColor(Color.argb(90, 204, 204, 204));
            binding.layout1.setBackgroundColor(0);
            binding.layout2.setBackgroundColor(0);
            binding.layout.setBackgroundColor(0);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}