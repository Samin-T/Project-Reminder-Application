package com.example.remindme.user;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private final String INTRO = "intro";
    private final String EMAIL = "USER_EMAIL";
    private final SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
    }

    public void isLogin(boolean loginorout) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(INTRO, loginorout);
        edit.apply();
    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean(INTRO, false);
    }

    public void putEmail(String loginorout) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(EMAIL, loginorout);
        edit.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }
}