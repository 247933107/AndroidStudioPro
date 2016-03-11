package com.example.androidstudiopro.base;

import android.app.Activity;
import android.os.Bundle;

import com.example.androidstudiopro.utils.ActivityManager;

/**
 * Created by zhangyue on 2016/3/9.
 */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();
}
