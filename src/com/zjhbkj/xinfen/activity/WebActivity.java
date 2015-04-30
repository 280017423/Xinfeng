package com.zjhbkj.xinfen.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zjhbkj.xinfen.R;
import com.zjhbkj.xinfen.util.EvtLog;
import com.zjhbkj.xinfen.util.StringUtil;
import com.zjhbkj.xinfen.util.UIUtil;
import com.zjhbkj.xinfen.widget.LoadingUpView;

/**
 * 活动详情界面
 * 
 * @author zou.sq
 */
public class WebActivity extends BaseActivity implements OnClickListener {
	private WebView mWebView;
	private String mDetailUri;
	private View mErrorView;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		initVariables();
		initViews();
		initWebViewConfig();
		loadUrl();
	}

	private void initViews() {
		mWebView = (WebView) findViewById(R.id.wv_content);
		mErrorView = findViewById(R.id.ll_error_view);
		mErrorView.setOnClickListener(this);
	}

	private void initVariables() {
		mLoadingUpView = new LoadingUpView(this);
		mDetailUri = getIntent().getStringExtra("url");
		EvtLog.d("aaa", "Url" + mDetailUri);
		if (StringUtil.isNullOrEmpty(mDetailUri)) {
			finish();
		}
	}

	private void loadUrl() {
		mWebView.loadUrl(mDetailUri);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebViewConfig() {
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setAppCacheEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
		settings.setDefaultTextEncodingName("utf-8");
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				showLoadingUpView(mLoadingUpView);
				EvtLog.d("aaa", "onPageStarted");
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView webView, String url) {
				dismissLoadingUpView(mLoadingUpView);
				EvtLog.d("aaa", "onPageFinished");
				super.onPageFinished(webView, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				dismissLoadingUpView(mLoadingUpView);
				UIUtil.setViewVisible(mErrorView);
				UIUtil.setViewGone(mWebView);
				EvtLog.d("aaa", "onReceivedError");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				Uri uri = Uri.parse(url);
				EvtLog.d("aaa", "shouldOverrideUrlLoading");
				if (uri.getScheme().equals("xhr") && uri.getHost().equals("com.xhr88.lp")) {
					startActivity(new Intent("android.intent.action.VIEW", uri));
					return true;
				} else {
					return super.shouldOverrideUrlLoading(webView, url);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			case R.id.ll_error_view:
				UIUtil.setViewVisible(mWebView);
				UIUtil.setViewGone(mErrorView);
				mWebView.reload();
				break;
			default:
				break;
		}
	}

}
