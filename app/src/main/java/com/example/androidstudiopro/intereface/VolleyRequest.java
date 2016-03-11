package com.example.androidstudiopro.intereface;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.example.androidstudiopro.base.AndroidStudioProApplication;

import java.util.Map;

public class VolleyRequest {
	public static StringRequest stringRequest;
	public static Context mContext;

	public static void RequestGet(Context context, String url, String tag,
			VolleyInterface vif) {
		AndroidStudioProApplication.getHttpQueues().cancelAll(tag);
		stringRequest = new StringRequest(Method.GET, url,
				vif.loadingListener(), vif.errorListener());
		stringRequest.setTag(tag);
		AndroidStudioProApplication.getHttpQueues().add(stringRequest);
		AndroidStudioProApplication.getHttpQueues().start();
	}

	public static void RequestPost(Context context, String url, String tag,
			final Map<String, String> params, VolleyInterface vif) {
		AndroidStudioProApplication.getHttpQueues().cancelAll(tag);
		stringRequest = new StringRequest(Method.POST, url, vif.loadingListener(), vif.errorListener()){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError {
			return params;
		}};
		stringRequest.setTag(tag);
		AndroidStudioProApplication.getHttpQueues().add(stringRequest);
		AndroidStudioProApplication.getHttpQueues().start();
	}
}
