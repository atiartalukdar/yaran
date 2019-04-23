package me.yaran.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.onesignal.OneSignal;

import atiar.DetectConnection;

public class Main2Activity extends AppCompatActivity {
    Context mContext;
    private final String tag = "AtiarsTag= ";

    WebView mWebview;
    String MyUA = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
    ImageView imageView;
    ProgressBar pd;
    SwipeRefreshLayout swipeRefreshLayout;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    String linkToOpen = "https://yaran.me/";


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linkToOpen = BP.getPreferenceUrlNeedToOpen(this);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .autoPromptLocation(true)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                //.setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                .init();

        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();
        mContext = this;


        initView();

        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            initView();
        } else {
            openWebView();
            //renderWebPage(linkToOpen);
        }

        CookieSyncManager.createInstance(mContext);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!DetectConnection.checkInternetConnection(mContext)) {
                    Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                    initView();
                }else {
                    mWebview.reload();
                }
            }

        });

    }


    private void openWebView() {

        mWebview.setWebViewClient(new MyBrowser());
        mWebview.clearCache(true);
        mWebview.clearHistory();

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebview.loadUrl(linkToOpen);
        //mWebview.reload();
    }


    // Custom method to render a web page
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void renderWebPage(String urlToRender){
        //WEBVIEW

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebview.getSettings().setUserAgentString(MyUA);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setAppCachePath(getApplication().getCacheDir().toString());
        mWebview.setWebViewClient(new WebViewClient(){


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //playVideo();
                pd.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                // Do something when page loading finished
                swipeRefreshLayout.setRefreshing(false);
                pd.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                mWebview.setVisibility(View.VISIBLE);
                mWebview.reload();
            }

        });

        mWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebview.loadUrl(urlToRender);
    }



    private void initView() {
        mWebview = findViewById(R.id.webViewMain);
        imageView = findViewById(R.id.splashImage);
        pd = findViewById(R.id.pbMain);
        swipeRefreshLayout = findViewById(R.id.swipRefreshLayoutMain);

        pd.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        mWebview.setVisibility(View.GONE);

    }


    @Override
    public void onBackPressed() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    public void onStart() {
        super.onStart();
        swipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (mWebview.getScrollY() == 0)
                            swipeRefreshLayout.setEnabled(true);
                        else
                            swipeRefreshLayout.setEnabled(false);

                    }
                });
    }

    @Override
    public void onStop() {
        swipeRefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        super.onStop();
    }


    class MyBrowser extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd.setVisibility(View.VISIBLE);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            if (findViewById(R.id.splashImage).getVisibility() == View.VISIBLE) {
                findViewById(R.id.webViewMain).setVisibility(View.VISIBLE);
                findViewById(R.id.splashImage).setVisibility(View.GONE);
                pd.setVisibility(View.GONE);
                mWebview.reload();

                BP.setPreferenceUrlNeedToOpe(getApplicationContext(),BP.value_urlNeedToOpen);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            if(url != null && !(url.startsWith("http://") || url.startsWith("https://")))
            {
                Toast.makeText(getApplicationContext(),"Please wait a moment..",Toast.LENGTH_LONG).show();
                Log.e(tag,"Others share= " + url);
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;

            }else
            {
                Log.e(tag,"webview= " + url);
                return false;
            }

        }

    }



}
