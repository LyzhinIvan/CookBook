package com.cookbook.helpers;


import android.content.Context;
import android.content.SharedPreferences;

public abstract class PreferencesWorker {
    protected static final String APP_PREFS = "CookBook_Prefs";
    protected static SharedPreferences prefs;

    public PreferencesWorker(Context context) {
        if (prefs==null)
            prefs = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }
}
