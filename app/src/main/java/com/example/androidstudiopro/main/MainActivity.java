package com.example.androidstudiopro.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androidstudiopro.R;
import com.example.androidstudiopro.base.BaseActivity;
import com.example.androidstudiopro.fragment.JokerFragment;
import com.example.androidstudiopro.fragment.PicFragment;
import com.example.androidstudiopro.fragment.WechatFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    //    private SimpleDraweeView mAnimatedGifView;
    private FragmentManager mFragmentManager;
    private JokerFragment mJokerFragment;
    private PicFragment mPicFragment;
    private WechatFragment wechatFragment;
    private TextView tv_1, tv_2, tv_3, tv_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        mFragmentManager = getFragmentManager();
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);


//        mAnimatedGifView = (SimpleDraweeView) findViewById(R.id.animated_gif);
//        DraweeController animatedGifController = Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true).setUri(Uri.parse("http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201602/26/9CEC209CE521A3C710F151D1A7209823.gif")).build();
//        mAnimatedGifView.setController(animatedGifController);
    }

    @Override
    public void initData() {
        tabSelection(1);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mPicFragment != null) {
            transaction.hide(mPicFragment);
        }
        if (mJokerFragment != null) {
            transaction.hide(mJokerFragment);
        }
        if (wechatFragment != null) {
            transaction.hide(wechatFragment);
        }
    }

    private void tabSelection(int index) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 1:
                if (mJokerFragment == null) {
                    mJokerFragment = new JokerFragment();
                    fragmentTransaction.add(R.id.frame_layout, mJokerFragment);
                } else {
                    fragmentTransaction.show(mJokerFragment);
                }
                break;
            case 2:
                if (mPicFragment == null) {
                    mPicFragment = new PicFragment();
                    fragmentTransaction.add(R.id.frame_layout, mPicFragment);
                } else {
                    fragmentTransaction.show(mPicFragment);
                }

                break;
            case 3: {
//                mTvStation.setTextColor(Color.BLUE);
                if (wechatFragment == null) {
                    wechatFragment = new WechatFragment();
                    fragmentTransaction.add(R.id.frame_layout, wechatFragment);
                } else {
                    fragmentTransaction.show(wechatFragment);
                }
                break;
            }
            case 4: {
//                mTvTransfer.setTextColor(Color.BLUE);
//                if (mTransferFragment == null) {
//                    mTransferFragment = new TransferFragment();
//                    fragmentTransaction.add(R.id.activity_content, mTransferFragment);
//                } else {
//                    fragmentTransaction.show(mTransferFragment);
//                }
                break;
            }
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                tabSelection(1);
                break;
            case R.id.tv_2:
                tabSelection(2);
                break;
            case R.id.tv_3:
                tabSelection(3);
                break;
            case R.id.tv_4:
                tabSelection(4);
                break;
        }
    }
}
