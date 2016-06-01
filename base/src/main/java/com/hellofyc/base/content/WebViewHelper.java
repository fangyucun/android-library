package com.hellofyc.base.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hellofyc.base.widget.BaseWebView;

public class WebViewHelper {
    BaseWebView mWebView;
    OnPageLoadFinishListener mOnPageLoadFinishListener;

    public WebViewHelper(BaseWebView webView) {
        mWebView = webView;
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
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

    public void loadUrl(String url, OnPageLoadFinishListener listener) {
        mOnPageLoadFinishListener = listener;
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
            if (mOnPageLoadFinishListener != null) {
                mOnPageLoadFinishListener.onPageLoadFinish();
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.stopLoading();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
    }

    class OnWebViewDownloadListener implements DownloadListener {

        private Context mContext;

        public OnWebViewDownloadListener(Context context) {
            mContext = context;
        }

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(intent);
        }

    }

    public interface OnPageLoadFinishListener {
        void onPageLoadFinish();
    }
}