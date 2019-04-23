package me.yaran.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class BP {


    private static final String PREFS_NAME = "preferenceName";
    private static final String TAG = "SP - Atiar: ";

    public static final String Key_Counter = "counter";

    public static final String value_urlNeedToOpen = "https://yaran.me/";
    public static final String key_urlNeedToOpen = "yaran";

    /*****************************//* Strat shared preferences *//******************************/


    public static boolean setPreference(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }



    public static String getPreference(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "None");
    }

    public static boolean setPreferenceInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getPreferenceInt(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.e(TAG,Key_Counter +" value is : "+ settings.getInt(Key_Counter,0));
        return settings.getInt(key, 0);
    }
    public static int getPreferenceIntDrawable(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key,0);
    }


    public static boolean setPreferenceUrlNeedToOpe(Context context, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key_urlNeedToOpen, value);
        return editor.commit();
    }

    public static String getPreferenceUrlNeedToOpen(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key_urlNeedToOpen, value_urlNeedToOpen);
    }

    /*****************************//* End shared preferences *//******************************/



}
