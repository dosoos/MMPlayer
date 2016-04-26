package com.mmplayer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mmplayer.R;
import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.FinestWebViewActivity;
import com.thefinestartist.finestwebview.helpers.UrlParser;
import com.thefinestartist.finestwebview.listeners.BroadCastManager;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Elone on 16/4/20.
 */
public class MyWebViewActivity extends Activity {

    @InjectView(R.id.webview)
    public WebView mWebView;
//    @InjectView(R.id.fab)
//    public FloatingActionButton mFloatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netvideo_player);
        ButterKnife.inject(this);
        mWebView = new WebView(MyWebViewActivity.this);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {

                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                Toast.makeText(MyWebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
//        mWebView.setWebViewClient(new MyWebViewClient());
//        WebSettings settings = mWebView.getSettings();
//        setSettings(settings);

    }

    @SuppressLint("NewApi")
    private void setSettings(WebSettings setting) {
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);
        setting.setSupportZoom(true);

        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);
        // 全屏显示
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setBlockNetworkImage(false);
        setting.setMediaPlaybackRequiresUserGesture(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> hearders = new HashMap<>();
        hearders.put("", "");
        mWebView.loadUrl(getIntent().getStringExtra("url"), hearders);
//        mWebView.loadUrl("http://news.baidu.com/");
//        new FinestWebView.Builder(this).show(getIntent().getStringExtra("url"));
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            BroadCastManager.onPageStarted(FinestWebViewActivity.this, key, url);
//            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
//                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
//            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            BroadCastManager.onPageFinished(FinestWebViewActivity.this, key, url);

//            if (updateTitleFromHtml)
//                title.setText(view.getTitle());
//            urlTv.setText(UrlParser.getHost(url));
//            requestCenterLayout();
//
//            if (view.canGoBack() || view.canGoForward()) {
//                back.setVisibility(showIconBack ? View.VISIBLE : View.GONE);
//                forward.setVisibility(showIconForward ? View.VISIBLE : View.GONE);
//                back.setEnabled(!disableIconBack && (rtl ? view.canGoForward() : view.canGoBack()));
//                forward.setEnabled(!disableIconForward && (rtl ? view.canGoBack() : view.canGoForward()));
//            } else {
//                back.setVisibility(View.GONE);
//                forward.setVisibility(View.GONE);
//            }
//
//            if (injectJavaScript != null)
//                webView.loadUrl(injectJavaScript);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.endsWith(".mp4")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
                // If we return true, onPageStarted, onPageFinished won't be called.
                return true;
            } else if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") || url.startsWith("mms:") || url.startsWith("mmsto:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
                return true; // If we return true, onPageStarted, onPageFinished won't be called.
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
//            BroadCastManager.onLoadResource(FinestWebViewActivity.this, key, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
//            BroadCastManager.onPageCommitVisible(FinestWebViewActivity.this, key, url);
        }
    }

}
