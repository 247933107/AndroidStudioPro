package com.example.androidstudiopro.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidstudiopro.R;
import com.example.androidstudiopro.main.ui.WebViewActivity;
import com.example.androidstudiopro.model.Jokers;
import com.example.androidstudiopro.model.WechatInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyue on 2016/3/14.
 */
public class MainListAdapter extends BaseAdapter {
    private List<Jokers> list = new ArrayList<>();
    private List<WechatInfo> listInfo = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int type = 0;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .displayer(new RoundedBitmapDisplayer(15)).cacheOnDisk(true).showImageOnLoading(R.mipmap.ic_launcher)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).build();
    public MainListAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
        mInflater = LayoutInflater.from(mContext);
    }
    public void refreshJokerInfo(List<Jokers> list,boolean is){
        if (is){
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void refreshWechatInfo(List<WechatInfo> list,boolean is){
        if (is){
            this.listInfo.clear();
        }
        this.listInfo.addAll(list);
        notifyDataSetChanged();
    }
    public MainListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        if (!mImageLoader.isInited()) {
            mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
    }

    @Override
    public int getCount() {
        if (type ==1) {
            return list.size();
        } else {
            return listInfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (type == 1) {
            return list.get(position);
        } else {
            return listInfo.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null==convertView){
            viewHolder = new ViewHolder();
            if (type == 1) {
                convertView = mInflater.inflate(R.layout.layout_list,null);
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.content);
                viewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
            } else {
                convertView = mInflater.inflate(R.layout.wechat_layout,null);
                viewHolder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.layout);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.wechat_view);
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.wechat_con);
                viewHolder.mTime = (TextView) convertView.findViewById(R.id.wehcat_title);
                viewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url",listInfo.get(position).getUrl()));
                    }
                });
            }
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (type==1){
            Jokers jokers = list.get(position);
            viewHolder.mTitle.setText(jokers.getContent());
            viewHolder.mTime.setText("更新："+jokers.getUpdatetime());
        }else {
             WechatInfo wechatInfo = listInfo.get(position);
            viewHolder.mTitle.setText(wechatInfo.getTitle());
            viewHolder.mTime.setText("来自："+wechatInfo.getSource());
            mImageLoader.displayImage(wechatInfo.getFirstImg(),viewHolder.imageView,options);
        }
        return convertView;
    }
    private class ViewHolder{
        private LinearLayout mLinearLayout;
        private TextView mTitle;
        private TextView mTime;
        private ImageView imageView;
    }
}
