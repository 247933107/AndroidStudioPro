/*
 * Copyright (C) 2015 shenghaiyang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androidstudiopro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidstudiopro.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 下拉刷新，上滑加载更多
 */
public class RefreshLoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    //下拉刷新的header
    private View mRefreshHeader;
    //下拉刷新的标题
    private TextView mRefreshTitle;
    //上次刷新的时间
    private TextView mRefreshTime;
    //下拉的箭头
    private ImageView mRefreshArrowImg;
    //刷新时显示的进度
    private ProgressBar mRefreshProgress;
    //header的高度
    private int mHeaderHeight;
    //当前第一个可见的Item的位置
    private int mFirstVisibleItem;
    //滑动的状态
    private int mScrollState;
    //手指按下的起始坐标
//    private int startX = 0;
    private int startY = 0;
    //是否是从头部开始
    private boolean isRemark;
    //当前状态
    private int state;
    //无状态
    private final int NORMAL = 0;
    //下拉
    private final int PULL_DOWN = 1;
    //正在刷新
    private final int REFRESHING = 2;
    //释放
    private final int PULL_TO_RELEASE = 4;
    //刷新的回调接口
    private OnRefreshAndLoadMoreListener mListener;
    //记录当前时间
    private String mNowTime;
    //加载更多的View
    private View mLoadMoreFooter;
    //加载更多显示的文字
    private TextView mLoadMoreTxv;
    //加载更多显示的ProgressBar
    private ProgressBar mLoadMoreProgress;

    public RefreshLoadMoreListView(Context context) {
        this(context, null);
    }

    public RefreshLoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化header布局
        mRefreshHeader = LayoutInflater.from(context).inflate(R.layout.header, null);
        mRefreshTitle = (TextView) mRefreshHeader.findViewById(R.id.refreshTitle);
        mRefreshTime = (TextView) mRefreshHeader.findViewById(R.id.refreshTime);
        mRefreshArrowImg = (ImageView) mRefreshHeader.findViewById(R.id.refreshArrowImg);
        mRefreshProgress = (ProgressBar) mRefreshHeader.findViewById(R.id.refreshProgress);
        //开始不显示ProgressBar
        mRefreshProgress.setVisibility(GONE);
        //初始化刷新时间
        mNowTime = "上次更新时间：" + getNowDate();
        mRefreshTime.setText(mNowTime);
        //隐藏header部分,先测量header的高度，然后获取header的高度，然后隐藏
        measureViewHeight(mRefreshHeader);
        mHeaderHeight = mRefreshHeader.getMeasuredHeight();
        setHeaderViewTopPadding(-mHeaderHeight);
        //添加header
        addHeaderView(mRefreshHeader);
        //初始化footer布局
        mLoadMoreFooter = LayoutInflater.from(context).inflate(R.layout.footer, null);
        mLoadMoreTxv = (TextView) mLoadMoreFooter.findViewById(R.id.loadMoreTxv);
        mLoadMoreProgress = (ProgressBar) mLoadMoreFooter.findViewById(R.id.loadMoreProgress);
        //开始时不显示ProgressBar
        mLoadMoreProgress.setVisibility(GONE);
        //添加footer
        addFooterView(mLoadMoreFooter);
        //添加滑动监听
        setOnScrollListener(this);
        //设置item的分割线
//        setDividerHeight(2);
    }


    /**
     * 测量View的高
     *
     * @param child view
     */
    private void measureViewHeight(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = getChildMeasureSpec(0, 0, params.width);
        int tempHeight = params.height;
        int childHeightSpec;
        if (tempHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 设置header的topPadding
     *
     * @param topPadding topPadding值
     */
    private void setHeaderViewTopPadding(int topPadding) {
        mRefreshHeader.setPadding(mRefreshHeader.getPaddingLeft(),
                topPadding,
                mRefreshHeader.getPaddingRight(),
                mRefreshHeader.getPaddingBottom());
        mRefreshHeader.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滑动状态
        mScrollState = scrollState;
        //滑动到底部
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE &&
                view.getLastVisiblePosition() == getAdapter().getCount() - 1) {
            mLoadMoreProgress.setVisibility(VISIBLE);
            mLoadMoreTxv.setText("正在加载……");
            //回调的loadmore
            mListener.onLoadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //当前第一个可见的item
        mFirstVisibleItem = firstVisibleItem;
    }

    /**
     * 处理header的显示以及刷新
     *
     * @param ev MotionEvent对象
     * @return 调用父类该方法的返回值
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mFirstVisibleItem == 0) {
                    //确定按下的起点
//                    startX = (int) ev.getX();
                    startY = (int) ev.getY();
                    isRemark = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isRemark) {
                    break;
                }
                if (state == REFRESHING) {
                    //正在刷新时，禁止滑动
                    return true;
                } else {
                    //滑动的偏移量
//                    int offsetX = (int) (ev.getX() - startX);
                    int offsetY = (int) (ev.getY() - startY);
                    int top = offsetY - mHeaderHeight - 10;
                    switch (state) {
                        case NORMAL:
                            if (offsetY > 0) {
                                state = PULL_DOWN;
                            }
                            updateHeaderView();
                            break;
                        case PULL_DOWN:
                            setHeaderViewTopPadding(top);
                            if (offsetY >= mHeaderHeight + 60 &&
                                    mScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                                state = PULL_TO_RELEASE;
                            }
                            updateHeaderView();
                            break;
                        case PULL_TO_RELEASE:
                            setHeaderViewTopPadding(top);
                            if (offsetY < mHeaderHeight + 60 && offsetY > 0) {
                                state = PULL_DOWN;
                            } else if (offsetY <= 0) {
                                state = NORMAL;
                            }
                            updateHeaderView();
                            break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == PULL_TO_RELEASE) {
                    state = REFRESHING;
                    updateHeaderView();
                    mListener.onRefresh();
                    mNowTime = "上次更新时间：" + getNowDate();
                } else {
                    state = NORMAL;
                    isRemark = false;
                    updateHeaderView();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据状态改变header中的内容
     */
    private void updateHeaderView() {
        //箭头的动画
        RotateAnimation anim = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        RotateAnimation anim1 = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);

        switch (state) {
            case NORMAL:
                if (-mRefreshHeader.getMeasuredHeight() <= mHeaderHeight) {
                    setHeaderViewTopPadding(-mHeaderHeight);
                } else {
                    setHeaderViewTopPadding(-mRefreshHeader.getMeasuredHeight());
                }
                break;
            case PULL_DOWN:
                mRefreshTitle.setText("下拉可以刷新");
                mRefreshTime.setText(mNowTime);
                mRefreshTime.setVisibility(VISIBLE);
                mRefreshProgress.setVisibility(GONE);
                mRefreshArrowImg.setVisibility(VISIBLE);
                mRefreshArrowImg.setImageResource(R.mipmap.arrow_down);
                break;
            case REFRESHING:
                setHeaderViewTopPadding(0);
                mRefreshProgress.setVisibility(VISIBLE);
                mRefreshArrowImg.setVisibility(GONE);
                mRefreshTitle.setText("正在刷新……");
                mRefreshTime.setVisibility(GONE);
                break;
            case PULL_TO_RELEASE:
                mRefreshTitle.setText("松开可以刷新");
                mRefreshTime.setText(mNowTime);
                mRefreshArrowImg.setVisibility(VISIBLE);
                mRefreshTime.setVisibility(VISIBLE);
                mRefreshProgress.setVisibility(GONE);
                mRefreshArrowImg.setImageResource(R.mipmap.arrow_up);
                break;
            default:
                break;
        }
    }

    /**
     * 获取当前时间并格式化为字符串
     *
     * @return 返回当前时间的字符串
     */
    private String getNowDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * 停止refresh
     */
    public void stopRefresh() {
        state = NORMAL;
        updateHeaderView();
    }

    /**
     * 停止loadMore
     */
    public void stopLoadMore() {
        mLoadMoreTxv.setText("加载更多");
        mLoadMoreProgress.setVisibility(GONE);
    }

    /**
     * 设置OnRefreshAndLoadMoreListener
     *
     * @param listener OnRefreshAndLoadMoreListener的对象
     */
    public void setOnRefreshAndLoadMoreListener(OnRefreshAndLoadMoreListener listener) {
        mListener = listener;
    }

}