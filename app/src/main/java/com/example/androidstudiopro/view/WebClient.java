package com.example.androidstudiopro.view;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebClient extends WebViewClient {
	private ProgressBar progressBar;
	private boolean isClearHistory = false;
	private WebView webView = null;

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public WebClient(ProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}

	public WebClient(ProgressBar progressBar, boolean isClearHistory,
					WebView webView) {
		super();
		this.progressBar = progressBar;
		this.isClearHistory = isClearHistory;
		this.webView = webView;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		view.loadUrl(url);
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		view.getSettings().setBlockNetworkImage(true);
		view.getSettings().setRenderPriority(RenderPriority.HIGH);
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		view.getSettings().setBlockNetworkImage(false);
		view.getSettings().setRenderPriority(RenderPriority.NORMAL);
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
		super.onPageFinished(view, url);
		if (isClearHistory) {
			webView.clearHistory();
		}
	}
}
