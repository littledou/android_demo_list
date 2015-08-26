package com.cafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

public class AppInfoUtils {

	public static int getVersionCode(Context context) {
		int versionCode = 1;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getApplicationContext().getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
