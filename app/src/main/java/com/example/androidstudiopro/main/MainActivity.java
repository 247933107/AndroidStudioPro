package com.example.androidstudiopro.main;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.androidstudiopro.R;
import com.example.androidstudiopro.base.BaseActivity;
import com.example.androidstudiopro.intereface.VolleyInterface;
import com.example.androidstudiopro.intereface.VolleyRequest;
import com.example.androidstudiopro.model.GitHubService;
import com.example.androidstudiopro.model.Jokers;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {
    private SimpleDraweeView mAnimatedGifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        mAnimatedGifView = (SimpleDraweeView) findViewById(R.id.animated_gif);
        DraweeController animatedGifController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(Uri.parse("http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201602/26/9CEC209CE521A3C710F151D1A7209823.gif"))
                .build();
        mAnimatedGifView.setController(animatedGifController);
    }

    @Override
    public void initData() {
        new Thread(getData).start();
    }

    private Runnable getData = new Runnable() {
        @Override
        public void run() {
            volley();
        }
    };

    private void volley() {
        long p = System.currentTimeMillis();
        long m = p/1000;
        String url = "http://japi.juhe.cn/joke/content/list.from?key=0387ce49ebf86da430611dc3e7c1a668&page=2&pagesize=10&sort=asc&time="+m;
        VolleyRequest.RequestGet(this, url, "abcGet", new VolleyInterface(this, VolleyInterface.mListener,VolleyInterface.mErrorListener) {

            @Override
            public void onMySuccess(String result) {
                // TODO Auto-generated method stub
                Log.e("------->", result);
            }

            @Override
            public void onMyError(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });
    }

}
