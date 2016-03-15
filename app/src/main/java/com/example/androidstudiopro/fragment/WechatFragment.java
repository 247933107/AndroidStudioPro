package com.example.androidstudiopro.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.androidstudiopro.R;
import com.example.androidstudiopro.adapter.MainListAdapter;
import com.example.androidstudiopro.base.AndroidStudioProApplication;
import com.example.androidstudiopro.base.BaseFragment;
import com.example.androidstudiopro.intereface.VolleyInterface;
import com.example.androidstudiopro.intereface.VolleyRequest;
import com.example.androidstudiopro.list.ListViewDataAdapter;
import com.example.androidstudiopro.list.ViewHolderBase;
import com.example.androidstudiopro.main.ui.WebViewActivity;
import com.example.androidstudiopro.model.WechatInfo;
import com.example.androidstudiopro.utils.DensityUtil;
import com.example.androidstudiopro.view.BitmapCache;
import com.example.androidstudiopro.view.OnRefreshAndLoadMoreListener;
import com.example.androidstudiopro.view.RefreshLoadMoreListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * jokers page
 * Created by zhangyue on 2016/3/11.
 */
public class WechatFragment extends BaseFragment implements OnRefreshAndLoadMoreListener {
    private View mView;
    private Context mContext;
    private RefreshLoadMoreListView mListView;
    private MainListAdapter mainListAdapter;
    List<WechatInfo> jss = new ArrayList<WechatInfo>();
    private boolean isLoad = true;
    private int pno = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main, container, false);
        mContext = getActivity();
        initView();
        return mView;
    }

    private void initView() {
        mListView = (RefreshLoadMoreListView) mView.findViewById(R.id.rotate_header_list_view);
        mListView.setOnRefreshAndLoadMoreListener(this);
        mainListAdapter = new MainListAdapter(mContext);
        mListView.setAdapter(mainListAdapter);
        volley();
    }

    private void volley() {
        String url = "http://v.juhe.cn/weixin/query?key=b20cf7f425f307cefa6bac31f9b88c54&pno=" + pno + "&dtype=json";
        VolleyRequest.RequestGet(mContext, url, "kkkGet", new VolleyInterface(mContext, VolleyInterface.mListener, VolleyInterface.mErrorListener) {

            @Override
            public void onMySuccess(String result) {
                Log.e("------->", result);
                if (jss.size()>0){
                    jss.clear();
                }
                try {
                    JSONObject j = new JSONObject(result);
                    JSONArray ja = j.optJSONObject("result").optJSONArray("list");
                    for (int i = 0; i < ja.length(); i++) {
                        WechatInfo jo = new WechatInfo();
                        jo.setId(ja.optJSONObject(i).optString("id"));
                        jo.setTitle(ja.optJSONObject(i).optString("title"));
                        jo.setSource(ja.optJSONObject(i).optString("source"));
                        jo.setFirstImg(ja.optJSONObject(i).optString("firstImg"));
                        jo.setUrl(ja.optJSONObject(i).optString("url"));
                        jss.add(jo);
                    }
                    mListView.stopRefresh();
                    mHandler.obtainMessage(1).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mainListAdapter.refreshWechatInfo(jss, isLoad);
                    break;
            }
        }
    };

    @Override
    public void onRefresh() {
        pno = 1;
        isLoad = true;
        volley();
    }

    @Override
    public void onLoadMore() {
        pno++;
        isLoad = false;
        volley();
    }
}

