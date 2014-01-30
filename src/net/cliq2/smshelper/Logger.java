package net.cliq2.smshelper;

import java.util.Locale;

public class Logger {
	private final static String LOG_TAG = Config.TAG;

	private static boolean log = false;

	public static void setDebugLogging(boolean enabled) {
		log = enabled;
	}

	public static void d(String message) {
		if (log)
			android.util.Log.d(LOG_TAG, message);
	}

	public static void d(String message, Object... args) {
		if (log)
			d(String.format(Locale.ENGLISH, message, args));
	}

	public static void ex(Exception e) {
		if (log) {
			e.printStackTrace();
			android.util.Log.e(
					LOG_TAG,
					String.format(Locale.ENGLISH, "Exception %s",
							e.getMessage()));
		}
	}

	public static void i(String message, Object... args) {
		android.util.Log.i(LOG_TAG,
				String.format(Locale.ENGLISH, message, args));
	}
}
