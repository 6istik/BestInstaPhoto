package com.iriska.bestinstaphoto;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class InstaApp extends Application {
	private static SharedPreferences pref;
	private static Editor editPref;
	public static final String ACCESS_TOKEN = "insta_accesstoken";

	
	@Override
	public void onCreate() {
		super.onCreate();

		// get preferences and settings
		pref = getSharedPreferences("com.iriska.bestinstaphoto", MODE_PRIVATE);
	}
	
	public static void SavePreference(String name, Object preference) {
		editPref = pref.edit();
		if (preference.getClass().equals(Integer.TYPE)) {
			editPref.putInt(name, (Integer) preference);
		}
		if (preference.getClass().equals(String.class)) {
			editPref.putString(name, (String) preference);
		}
		if (preference.getClass().equals(Boolean.TYPE)) {
			editPref.putBoolean(name, (Boolean) preference);
		}
		editPref.commit();
	}

	public static SharedPreferences GetPreference() {
		return pref;
	}
}
