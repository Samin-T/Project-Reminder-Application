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

import org.json.JSONArray;
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
        String url = "http://192.168.0.81/S/reminder/api/register.php";

        final String email = Objects.requireNonNull(binding.emailField.getText()).toString();
        final String phone = Objects.requireNonNull(binding.phoneField.getText()).toString();
        final String password = Objects.requireNonNull(binding.passwordField.getText()).toString();


        if (TextUtils.isEmpty(email)) {
            binding.emailField.setError("Email is required");
            binding.emailField.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    requestQueue.getCache().clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.optString("status").equals("true")) {
                            saveInfo(response);
                            Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            this.finish();
                        } else {

                            if (jsonObject.getString("message").equals("Username already exist!")) {
                                binding.emailField.setError("email provided already exists");
                            }

                            if (jsonObject.getString("message").equals("Parameter missing!")) {
                                Snackbar.make(findViewById(android.R.id.content), "Please fill in all the fields", Snackbar.LENGTH_LONG).show();
                            }

                            //Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("phoneNumber", phone);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void saveInfo(String response) {
        sharedPrefManager.isLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    sharedPrefManager.putEmail(dataobj.getString("email"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
