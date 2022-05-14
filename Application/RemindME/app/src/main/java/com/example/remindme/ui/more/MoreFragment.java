package com.example.remindme.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.remindme.databinding.FragmentMoreBinding;
import com.example.remindme.history.HistoryActivity;
import com.example.remindme.user.LoginActivity;
import com.example.remindme.user.RegisterActivity;
import com.example.remindme.user.SharedPrefManager;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;
    private SharedPrefManager sharedPrefManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MoreViewModel moreViewModel = new ViewModelProvider(this).get(MoreViewModel.class);

        binding = FragmentMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPrefManager = new SharedPrefManager(root.getContext());

        // Display users Email
        binding.textView.setText(sharedPrefManager.getEmail());

        if (sharedPrefManager.getEmail().equals("OFFLINE-USER")) {
            binding.logoutButton.setEnabled(false);
            binding.loginLayout.setEnabled(true);
            binding.accLayout.setEnabled(true);

            // Login button
            binding.loginLayout.setOnClickListener(v -> {
                sharedPrefManager.isLogin(false);
                sharedPrefManager.setEmail("");
                Intent intent = new Intent(root.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

        } else {
            binding.loginLayout.setEnabled(false);
            binding.accLayout.setEnabled(false);
        }

        // Logout button
        binding.logoutButton.setOnClickListener(v -> {
            sharedPrefManager.isLogin(false);
            Intent intent = new Intent(root.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // History button
        binding.historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(root.getContext(), HistoryActivity.class);
            startActivity(intent);
        });

        // Register button
        binding.accLayout.setOnClickListener(v -> {
            Intent intent = new Intent(root.getContext(), RegisterActivity.class);
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