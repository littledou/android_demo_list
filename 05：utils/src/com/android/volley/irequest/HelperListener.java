package com.android.volley.irequest;

import com.android.volley.VolleyError;

public interface HelperListener {

	public void onResponse(String response);
	public void onErrorResponse(VolleyError error);
}
