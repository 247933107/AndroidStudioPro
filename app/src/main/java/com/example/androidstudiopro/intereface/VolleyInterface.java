package com.example.androidstudiopro.intereface;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import android.content.Context;

public abstract class VolleyInterface {
	public Context context;
	public static Listener<String> mListener;
	public static ErrorListener mErrorListener;
	public abstract void onMySuccess(String result);
	public abstract void onMyError(VolleyError error);
	
	public VolleyInterface(Context context, Listener<String> listener,
			ErrorListener erroelistener) {
		this.context = context;
		this.mListener = listener;
		this.mErrorListener = erroelistener;
	}
	public Listener<String> loadingListener(){
		mListener = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				onMySuccess(response);
			}
		};
		return mListener;
	}
	public ErrorListener errorListener(){
		mErrorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				onMyError(error);
			}
		};
		return mErrorListener;
	}
}
