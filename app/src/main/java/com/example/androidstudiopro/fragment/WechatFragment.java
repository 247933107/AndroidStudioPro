package com.example.androidstudiopro.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidstudiopro.R;
import com.example.androidstudiopro.base.BaseFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zhangyue on 2016/3/11.
 */
public class WechatFragment extends BaseFragment {
    private View mView;
    private Context mContext;
    private SimpleDraweeView mAnimatedGifView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.wechat_layout, container, false);
        mContext = getActivity();
        initView();
        return mView;
    }

    private void initView() {
        mAnimatedGifView = (SimpleDraweeView) mView.findViewById(R.id.animated_gifs);
        DraweeController animatedGifController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(Uri.parse("http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201603/11/045353932C0FF36B1831DB2E318E8B53.gif"))
                .build();
        mAnimatedGifView.setController(animatedGifController);
    }
}
