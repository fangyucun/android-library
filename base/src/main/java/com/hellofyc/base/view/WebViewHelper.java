package com.hellofyc.base.view;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hellofyc.base.utils.UrlUtils;
import com.hellofyc.base.widget.BaseWebView;

public class WebViewHelper {
    BaseWebView mWebView;
    OnPageLoadFinishedListener mOnPageLoadFinishedListener;

    public WebViewHelper(BaseWebView webView) {
        mWebView = webView;
        init();
    }

    public static WebViewHelper newInstance(BaseWebView webView) {
        return new WebViewHelper(webView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        if (mWebView == null) {
            throw new IllegalArgumentException("webview cannot be null!");
        }

        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setWebChromeClient(new DefaultWebChromeClient());
        mWebView.setWebViewClient(new DefaultWebViewClient());
        mWebView.setDownloadListener(new OnWebViewDownloadListener(mWebView.getContext()));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mWebView.setOnJsImageClickEnabled();
    }

    public BaseWebView getWebView() {
        return mWebView;
    }

    public void onPause() {
        mWebView.onPause();
    }

    public void onResume() {
        mWebView.onResume();
    }

    public void loadBlankUrl() {
        mWebView.loadUrl("about:blank");
    }

    public void loadUrl(String url) {
        loadUrl(url, null);
    }

    public void loadUrl(String url, OnPageLoadFinishedListener listener) {
        mOnPageLoadFinishedListener = listener;
        mWebView.loadUrl(url);
    }

    class DefaultWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

    }

    class DefaultWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.getSettings().setBlockNetworkImage(false);
            if (mOnPageLoadFinishedListener != null) {
                mOnPageLoadFinishedListener.onPageLoadFinish(OnPageLoadFinishedListener.CODE_SUCCESS);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.stopLoading();
            if (mOnPageLoadFinishedListener != null) {
                mOnPageLoadFinishedListener.onPageLoadFinish(OnPageLoadFinishedListener.CODE_FAILURE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (view != null && (UrlUtils.isUrl(url))) {
                view.loadUrl(url);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

}