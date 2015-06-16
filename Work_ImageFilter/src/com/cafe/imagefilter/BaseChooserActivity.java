package com.cafe.imagefilter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;

public class BaseChooserActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initStrictMode();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	private void initStrictMode() {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads().detectDiskWrites().detectNetwork()
			.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
			.build());
		}
	}

	public void cancel(View paramView)
	{
		finish();
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent)
	{
		finish();
		return true;
	}
}
