package com.example.remindme.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.remindme.databinding.FragmentNotificationsBinding;
import com.example.remindme.user.LoginActivity;
import com.example.remindme.user.SharedPrefManager;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private SharedPrefManager sharedPrefManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPrefManager = new SharedPrefManager(root.getContext());

        // Display users Email
        binding.textView.setText(sharedPrefManager.getEmail());

        if (sharedPrefManager.getEmail().equals("OFFLINE-USER"))
            binding.button.setEnabled(false);

        // Logout button
        binding.button.setOnClickListener(v -> {
            sharedPrefManager.isLogin(false);
            Intent intent = new Intent(root.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}