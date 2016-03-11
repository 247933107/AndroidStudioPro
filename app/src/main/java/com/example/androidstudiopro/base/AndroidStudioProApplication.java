package com.example.androidstudiopro.base;

import android.app.Application;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangyue on 2016/3/9.
 */
public class AndroidStudioProApplication extends Application {
    static AndroidStudioProApplication app;
    public static RequestQueue queues;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        queues = Volley.newRequestQueue(getApplicationContext());
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setRequestListeners(listeners)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build();
        Fresco.initialize(app, config);
    }
    public static RequestQueue getHttpQueues() {
        return queues;
    }
    public static AndroidStudioProApplication getInstance() {
        return app;
    }
}
