package com.example.remindme;

import android.content.Context;
import android.content.SharedPreferences;

public class WaterSharedPref {
    private final String isActive = "isActive";
    private final SharedPreferences sharedPreferences;

    public WaterSharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPref",
                Context.MODE_PRIVATE);
    }

    public boolean getIsActive() {
        return sharedPreferences.getBoolean(isActive, false);
    }

    public void setIsActive(boolean b) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(isActive, b);
        edit.apply();
    }
}
