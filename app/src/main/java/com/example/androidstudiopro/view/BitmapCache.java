package com.example.androidstudiopro.view;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache{
	public LruCache<String, Bitmap> cache;
	public int max = 10*1024*1024;//10M
	public BitmapCache() {
		cache = new LruCache<String, Bitmap>(max){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		return cache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		cache.put(url, bitmap);
	}

}
