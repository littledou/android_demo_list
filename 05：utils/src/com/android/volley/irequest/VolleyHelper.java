package com.android.volley.irequest;

import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyHelper {


	// 实例化请求队列

	private static RequestQueue mQueue;

	public RequestQueue initQueue(Context mContext){
		if(mQueue==null){
			mQueue = Volley.newRequestQueue(mContext);
		}	
		return mQueue;
	}


	private static void IM(Request<?> mRequest){
		mQueue.add(mRequest);
	}

	public static void IGETString(String url,final HelperListener listener){
		StringRequest stringRequest = new StringRequest(
				url, new  Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						listener.onResponse(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						listener.onErrorResponse(error);
					}
				}){
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {//设置头信息 
				return null;
			}  
			@Override  
			protected Map<String, String> getParams() throws AuthFailureError {//设置参数 
				return null;
			}  
		};
		IM(stringRequest);
	}

	public static void IPOSTString(String url,final HelperListener listener){
		StringRequest request = new StringRequest(
				Request.Method.POST,  
				url,  
				new Response.Listener<String>() {
					@Override  
					public void onResponse(String response) {
						listener.onResponse(response);
					}  
				},  
				new Response.ErrorListener() {  
					@Override  
					public void onErrorResponse(VolleyError error) {  
						listener.onErrorResponse(error);
					}  
				}){  
			@Override  
			public Map<String, String> getHeaders() throws AuthFailureError {//设置头信息  
				return null;  
			}  
			@Override  
			protected Map<String, String> getParams() throws AuthFailureError {//设置参数  
				return null;  
			}  
		};  
		IM(request);
	}



}
