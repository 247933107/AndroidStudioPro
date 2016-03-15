package com.example.androidstudiopro.main.ui;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.androidstudiopro.R;
import com.example.androidstudiopro.base.BaseActivity;
import com.example.androidstudiopro.view.WebClient;

/**
 * Created by zhangyue on 2016/3/12.
 */
public class WebViewActivity extends BaseActivity {
    private WebView mWebView;
    private String url = "";
    private ProgressBar progressbar;
    @Override
    public void initView() {
        setContentView(R.layout.webview_layout);
        mWebView = (WebView) findViewById(R.id.web_view);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void initData() {
        url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebClient(progressbar));
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }
}
