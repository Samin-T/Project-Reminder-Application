package com.example.remindme.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.MainActivity;
import com.example.remindme.PHPLinks;
import com.example.remindme.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    private ActivityLoginBinding binding;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new SharedPrefManager(this);

        // Check if user is already logged in and take to MainActivity
        if (sharedPrefManager.getIsLogin() || sharedPrefManager.getEmail().equals("OFFLINE-USER")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

        // Offline
        binding.skipOffline.setOnClickListener(v -> {
            sharedPrefManager.setEmail("OFFLINE-USER");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        });

        // Login
        binding.loginBtn.setOnClickListener(v -> authenticateUser());

        // Register
        binding.registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            this.finish();
        });
    }

    // Authenticate (https://google.github.io/volley/simple.html) ( I used this website to learn about volley requests )
    private void authenticateUser() {
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        String url = PHPLinks.login;

        final String email = Objects.requireNonNull(binding.emailAddress.getText()).toString().trim();
        final String password = Objects.requireNonNull(binding.passwordField.getText()).toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    requestQueue.getCache().clear();

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equals("true")) {
                            // Login
                            sharedPrefManager.isLogin(true);
                            sharedPrefManager.setEmail(jsonObject.getString("email"));

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
