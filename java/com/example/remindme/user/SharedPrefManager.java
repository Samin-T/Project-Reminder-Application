package com.example.remindme.user;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private final String isLoggedIn = "isLogin";
    private final String EMAIL = "USER_EMAIL";
    private final SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPref",
                Context.MODE_PRIVATE);
    }

    public void isLogin(boolean b) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(isLoggedIn, b);
        edit.apply();
    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean(isLoggedIn, false);
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    // Email Get & Set
    public void setEmail(String email) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(EMAIL, email);
        edit.apply();
    }
}