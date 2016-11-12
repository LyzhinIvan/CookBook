package com.cookbook.helpers;

import android.content.Context;

public class UpdatingHelper extends PreferencesWorker {

    private static final String KEY_LAST_UPDATE = "last_update";

    public UpdatingHelper(Context context) {
        super(context);
    }

    public long getLastUpdatingTime() {
        return prefs.getLong(KEY_LAST_UPDATE, 0);
    }

    public void setLastUpdatingTime(long lastUpdate) {
        prefs.edit().putLong(KEY_LAST_UPDATE, lastUpdate).apply();
    }
}
