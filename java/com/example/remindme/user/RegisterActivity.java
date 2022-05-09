package com.example.remindme.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.MainActivity;
import com.example.remindme.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    private ActivityRegisterBinding binding;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager = new SharedPrefManager(this);

        // Register
        binding.registerBtn.setOnClickListener(v -> registerMe());

        // Login
        binding.loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        });
    }

    private void registerMe() {

        requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        String url = "http://192.168.0.81/remindme/api/register.php";

        final String email = Objects.requireNonNull(binding.emailAddress.getText()).toString();
        final String password = Objects.requireNonNull(binding.passwordField.getText()).toString();
        final String cPassword = Objects.requireNonNull(binding.confirmPasswordField.getText()).toString();


        if (TextUtils.isEmpty(email)) {
            binding.emailField.setError("email is required");
            binding.emailField.requestFocus();
            return;
        }

        if (!cPassword.equals(password)) {
            binding.confirmPasswordField.setError("passwords do not match");
            binding.confirmPasswordField.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    requestQueue.getCache().clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.optString("status").equals("true")) {
                            sharedPrefManager.isLogin(true);
                            sharedPrefManager.setEmail(jsonObject.optString("email"));

                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            this.finish();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
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
