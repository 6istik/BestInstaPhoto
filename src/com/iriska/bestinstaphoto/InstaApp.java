package com.iriska.bestinstaphoto;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap.Config;

public class InstaApp extends Application {
	private static SharedPreferences pref;
	private static Editor editPref;
	public static final String ACCESS_TOKEN = "insta_accesstoken";
	public static final String PREFERENCE_USERNAME = "preference_username";
	static ImageLoader imageLoader;
	
	@Override
	public void onCreate() {
		super.onCreate();

		// get preferences and settings
		pref = getSharedPreferences("com.iriska.bestinstaphoto", MODE_PRIVATE);
		
		// Create global configuration and initialize ImageLoader with this
		// configuration

		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .build();

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .defaultDisplayImageOptions(options)
        .build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
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
