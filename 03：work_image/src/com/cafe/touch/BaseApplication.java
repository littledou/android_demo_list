package com.cafe.touch;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import android.app.Application;

public class BaseApplication extends Application{

	private RequestQueue mQueue;
	private static BaseApplication mApplication;
	@Override
	public void onCreate() {
		super.onCreate();
		MobclickAgent.updateOnlineConfig(this);
		setmQueue(Volley.newRequestQueue(this));
		mApplication = this;
	}

	public static BaseApplication getInstence(){
		return mApplication;
	}
	public RequestQueue getmQueue() {
		return mQueue;
	}
	public void setmQueue(RequestQueue mQueue) {
		this.mQueue = mQueue;
	}

}
