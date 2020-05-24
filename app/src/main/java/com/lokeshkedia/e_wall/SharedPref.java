package com.lokeshkedia.e_wall;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences mySharedPref;

    public SharedPref(Context context) {
        mySharedPref = context.getSharedPreferences("filename1", Context.MODE_PRIVATE);

    }

    public void setFirstTimeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("FirstTime", state);
        editor.apply();
    }

    public boolean loadFirstTimeState() {
        Boolean state = mySharedPref.getBoolean("FirstTime", true);
        return state;
    }
}
