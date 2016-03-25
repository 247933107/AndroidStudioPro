package com.example.androidstudiopro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.example.androidstudiopro.R;
import com.example.androidstudiopro.adapter.MainListAdapter;
import com.example.androidstudiopro.base.BaseFragment;
import com.example.androidstudiopro.intereface.VolleyInterface;
import com.example.androidstudiopro.intereface.VolleyRequest;
import com.example.androidstudiopro.model.Jokers;
import com.example.androidstudiopro.view.OnRefreshAndLoadMoreListener;
import com.example.androidstudiopro.view.RefreshLoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * jokers page
 * Created by zhangyue on 2016/3/11.
 */
public class JokerFragment extends BaseFragment implements OnRefreshAndLoadMoreListener {
    private View mView;
    private Context mContext;
    private RefreshLoadMoreListView mListView;
    private MainListAdapter mainListAdapter;
    List<Jokers> jss = new ArrayList<Jokers>();
    private boolean isLoad = true;
    private int page = 1;
    private String pagesize = "10";

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
        mainListAdapter = new MainListAdapter(getActivity(), 1);
        mListView.setAdapter(mainListAdapter);
        onRefresh();
    }

    private void volley() {
        long p = System.currentTimeMillis();
        long m = (p / 1000) - 37659294;
        String url = "http://japi.juhe.cn/joke/content/text.from?key=0387ce49ebf86da430611dc3e7c1a668&page=" + page + "&pagesize=" + pagesize;
        VolleyRequest.RequestGet(mContext, url, "abcGet", new VolleyInterface(mContext, VolleyInterface.mListener, VolleyInterface.mErrorListener) {

            @Override
            public void onMySuccess(String result) {
                // TODO Auto-generated method stub
                Log.e("------->", result);
                if (jss.size() > 0) {
                    jss.clear();
                }
                JSONObject j = JSON.parseObject(result).getJSONObject("result");
                JSONArray array = j.getJSONArray("data");
                for (int i = 0; i < array.size(); i++) {
                    Jokers jokers = new Jokers();
                    jokers.setContent(array.getJSONObject(i).getString("content"));
                    jokers.setUpdatetime(array.getJSONObject(i).getString("updatetime"));
                    jss.add(jokers);
                }
                mListView.stopRefresh();
                mHandler.obtainMessage(1).sendToTarget();

            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        isLoad = true;
        volley();
    }

    @Override
    public void onLoadMore() {
        page++;
        isLoad = false;
        volley();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mainListAdapter.refreshJokerInfo(jss, isLoad);
                    break;
            }
        }
    };
}
