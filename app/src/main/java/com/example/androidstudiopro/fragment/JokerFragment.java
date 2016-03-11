package com.example.androidstudiopro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.androidstudiopro.R;
import com.example.androidstudiopro.base.BaseFragment;
import com.example.androidstudiopro.intereface.VolleyInterface;
import com.example.androidstudiopro.intereface.VolleyRequest;
import com.example.androidstudiopro.list.ListViewDataAdapter;
import com.example.androidstudiopro.list.ViewHolderBase;
import com.example.androidstudiopro.model.Jokers;
import com.example.androidstudiopro.utils.DensityUtil;
import com.example.androidstudiopro.view.LoadMoreContainer;
import com.example.androidstudiopro.view.LoadMoreHandler;
import com.example.androidstudiopro.view.LoadMoreListViewContainer;

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
public class JokerFragment extends BaseFragment {
    private View mView;
    private Context mContext;
    private ListView mListView;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    ListViewDataAdapter<Jokers> mAdapter;
    private LoadMoreListViewContainer moreListViewContainer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main,container,false);
        mContext = getActivity();
        initView();
        return mView;
    }

    private void initView() {
        mListView = (ListView) mView.findViewById(R.id.rotate_header_list_view);
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) mView.findViewById(R.id.rotate_header_list_view_frame);
        moreListViewContainer = (LoadMoreListViewContainer) mView.findViewById(R.id.load_more_list_view_container);
        mPtrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        mAdapter = new ListViewDataAdapter<Jokers>();
        mAdapter.setViewHolderClass(this, ViewHolder.class);
        mListView.setAdapter(mAdapter);
        mPtrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                volley();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        View headerMarginView = new View(mContext);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext,20)));
        mListView.addHeaderView(headerMarginView);
        mPtrClassicFrameLayout.setLoadingMinTime(1000);
//        // the following are default settings
//        mPtrClassicFrameLayout.setResistance(1.7f);
//        mPtrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
//        mPtrClassicFrameLayout.setDurationToClose(200);
//        mPtrClassicFrameLayout.setDurationToCloseHeader(1000);
//        // default is false
//        mPtrClassicFrameLayout.setPullToRefresh(false);
//        // default is true
//        mPtrClassicFrameLayout.setKeepHeaderWhenRefresh(true);
        mPtrClassicFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrClassicFrameLayout.autoRefresh();
            }
        }, 100);
        //////////////////////////////////////////////////////////////////////
        moreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
//                mDataModel.queryNextPage();
            }
        });

        // process data
//        EventCenter.bindContainerAndHandler(this, new DemoSimpleEventHandler() {
//
//            public void onEvent(ImageListDataEvent event) {
//
//                // ptr
//                mPtrClassicFrameLayout.refreshComplete();
//
//                // load more
//                moreListViewContainer.loadMoreFinish(mDataModel.getListPageInfo().isEmpty(), mDataModel.getListPageInfo().hasMore());
//
//                mAdapter.notifyDataSetChanged();
//            }
//
//            public void onEvent(ErrorMessageDataEvent event) {
//                moreListViewContainer.loadMoreError(0, event.message);
//            }
//
//        }).tryToRegisterIfNot();

        // auto load data
        mPtrClassicFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrClassicFrameLayout.autoRefresh(false);
            }
        }, 150);

        volley();
    }
    private void volley() {
        long p = System.currentTimeMillis();
        long m = (p/1000)-37659294;
        String url = "http://japi.juhe.cn/joke/content/list.from?key=0387ce49ebf86da430611dc3e7c1a668&page=2&pagesize=10&sort=asc&time="+m;
        VolleyRequest.RequestGet(mContext, url, "abcGet", new VolleyInterface(mContext, VolleyInterface.mListener,VolleyInterface.mErrorListener) {

            @Override
            public void onMySuccess(String result) {
                // TODO Auto-generated method stub
                Log.e("------->", result);
                mAdapter.getDataList().clear();
                try {
                    JSONObject j = new JSONObject(result);
                    JSONArray ja = j.optJSONObject("result").optJSONArray("data");
                    List<Jokers> jss = new ArrayList<Jokers>();
                    for (int i=0;i<ja.length();i++){
                        Jokers jo = new Jokers();
                        jo.setContent(ja.optJSONObject(i).optString("content"));
                        jo.setUpdatetime(ja.optJSONObject(i).optString("updatetime"));
                        jss.add(jo);
                    }
                    mAdapter.getDataList().addAll(jss);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPtrClassicFrameLayout.refreshComplete();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMyError(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });
    }
    private class ViewHolder extends ViewHolderBase<Jokers>{
        TextView mContent;
        TextView mTitle;
        @Override
        public View createView(LayoutInflater layoutInflater) {
            View v = layoutInflater.inflate(R.layout.layout_list, null);
            mContent = (TextView)v.findViewById(R.id.content);
            mTitle = (TextView) v.findViewById(R.id.time);
            return v;
        }

        @Override
        public void showData(int position, Jokers itemData) {
            mContent.setText(itemData.getContent());
            mTitle.setText(itemData.getUpdatetime());
        }
    }
}
