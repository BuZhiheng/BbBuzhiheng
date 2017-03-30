package cn.lankao.com.lovelankao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.lankao.com.lovelankao.LApplication;
public class PrefUtil {
	private static final SharedPreferences settings;
	static {
		Context context = LApplication.getCtx();
		settings = context.getSharedPreferences(context.getPackageName(),
				Context.MODE_PRIVATE);
	}
	public static String getString(String key, final String defaultValue) {
		return settings.getString(key, defaultValue);

	}

	public static void putString(final String key, final String value) {
		settings.edit().putString(key, value).commit();

	}

	public static boolean getBoolean(final String key,
			final boolean defaultValue) {
		return settings.getBoolean(key, defaultValue);

	}

	public static boolean hasKey(final String key) {

		return settings.contains(key);

	}

	public static void putBoolean(final String key, final boolean value) {

		settings.edit().putBoolean(key, value).commit();

	}

	public static void putInt(final String key, final int value) {
		settings.edit().putInt(key, value).commit();

	}

	public static int getInt(final String key, final int defaultValue) {
		return settings.getInt(key, defaultValue);

	}

	public static void putFloat(final String key, final float value) {

		settings.edit().putFloat(key, value).commit();

	}

	public static float getFloat(final String key, final float defaultValue) {

		return settings.getFloat(key, defaultValue);

	}

	public static void putLong(final String key, final long value) {

		settings.edit().putLong(key, value).commit();

	}

	public static long getLong(final String key, final long defaultValue) {
		return settings.getLong(key, defaultValue);

	}

	public static void clearPreference(final SharedPreferences p) {
		final Editor editor = p.edit();
		editor.clear();
		editor.commit();

	}
	public static void clear() {
		final Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
}