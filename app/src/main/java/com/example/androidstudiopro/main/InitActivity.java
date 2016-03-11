package com.example.androidstudiopro.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.androidstudiopro.R;
import com.example.androidstudiopro.base.BaseActivity;

/**
 * Created by zhangyue on 2016/3/9.
 */
public class InitActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_view);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
      mHandler.postAtTime(new Runnable() {
          @Override
          public void run() {
              Intent intent = new Intent();
              intent.setClass(InitActivity.this,MainActivity.class);
              startActivity(intent);
              finish();
          }
      },1000);
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
        }
    };
}
