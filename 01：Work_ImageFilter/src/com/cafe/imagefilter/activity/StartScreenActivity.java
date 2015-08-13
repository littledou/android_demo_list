package com.cafe.imagefilter.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cafe.imagefilter.BaseFragmentActivity;
import com.cafe.imagefilter.R;

/**
 * 应用启动屏
 */
public class StartScreenActivity extends BaseFragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup_screen);

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(mContext, PubGetImageFromPhoneACtivity.class));
			}
		}, 1000);
	}
}
