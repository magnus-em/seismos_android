package net.seismos.android.seismos.global;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String PREFERENCES = "PREFERENCES";
    public static final String PREF_MIN_MAG = "PREF_MIN_MAG";
    public static final String PREF_MAX_MAG = "PREF_MAX_MAG";
    public static final String PREF_EQ_NOTIF = "PREF_EQ_NOTIF";
    public static final String PREF_EQ_NOTIF_MIN = "PREF_EQ_NOTIF_MIN";
    public static final String PREF_NODE_COVERAGE = "PREF_NODE_COVERAGE";
    public static final String PREF_TIME_FRAME = "PREF_TIME_FRAME";

    public static SharedPreferences preferences;

    public static void init(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES, 0);

        if (!preferences.contains(PREF_TIME_FRAME)) {
            preferences.edit().putString(PREF_TIME_FRAME, "month").apply();
        }
        if (!preferences.contains(PREF_MIN_MAG)) {
            preferences.edit().putString(PREF_MIN_MAG, "5").apply();
        }

        if (!preferences.contains(PREF_MAX_MAG)) {
            preferences.edit().putString(PREF_MAX_MAG, "10").apply();
        }

        if (!preferences.contains(PREF_EQ_NOTIF)) {
            preferences.edit().putBoolean(PREF_EQ_NOTIF, true).apply();
        }

        if (!preferences.contains(PREF_EQ_NOTIF_MIN)) {
            preferences.edit().putString(PREF_EQ_NOTIF_MIN, "7").apply();
        }

        if (!preferences.contains(PREF_NODE_COVERAGE)) {
            preferences.edit().putBoolean(PREF_NODE_COVERAGE, false).apply();
        }

    }
}
