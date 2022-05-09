package com.example.remindme.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.remindme.databinding.FragmentMoreBinding;
import com.example.remindme.user.LoginActivity;
import com.example.remindme.user.SharedPrefManager;


public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;
    private SharedPrefManager sharedPrefManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MoreViewModel moreViewModel =
                new ViewModelProvider(this).get(MoreViewModel.class);

        binding = FragmentMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPrefManager = new SharedPrefManager(root.getContext());
        // Logout button
        binding.logoutButton.setOnClickListener(v -> {
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