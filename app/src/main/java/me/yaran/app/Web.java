package me.yaran.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;

import atiar.DetectConnection;
import io.fabric.sdk.android.Fabric;

public class Web extends AppCompatActivity{
    WebView mWebview;
    String linkToOpen = "https://yaran.me/";
    String MyUA = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    LinearLayout linearLayoutForSplash, linearLayoutForVideo;
    Context mContext;
    ProgressBar mProgressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    VideoView videoView;

    int counter = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getSupportActionBar().hide();
        setContentView(R.layout.activity_web);
        mContext = this;

        linearLayoutForSplash = findViewById(R.id.splash);
        mWebview  =  findViewById(R.id.webView);
        mProgressBar = findViewById(R.id.pb);
        swipeRefreshLayout = findViewById(R.id.swipRefreshLayout);

        linearLayoutForVideo = findViewById(R.id.videoLinearLayout);
        videoView = findViewById(R.id.videoView);


        //read the valu of sharedpreferenes for first time run the app
        counter = BP.getPreferenceInt(this,BP.Key_Counter);

        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled(false);
        }else {
            renderWebPage(linkToOpen);
        }


        CookieSyncManager.createInstance(mContext);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!DetectConnection.checkInternetConnection(mContext)) {
                    Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setEnabled(false);
                }else {
                    mWebview.reload();
                }
            }

        });


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
                playVideo();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                // Do something when page loading finished
                swipeRefreshLayout.setRefreshing(false);
                linearLayoutForSplash.setVisibility(View.GONE);
                mWebview.setVisibility(View.VISIBLE);
            }

        });

        mWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebview.loadUrl(urlToRender);
        //mWebview.reload();
    }

    @Override
    public void onBackPressed() {
        if(mWebview.canGoBack()) {
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

    private void playVideo(){

        if (counter == 0){
            BP.setPreferenceInt(mContext,BP.Key_Counter,1);
            linearLayoutForVideo.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            //String path = "android.resource://" + getPackageName() + "/" + R.raw.intro;
            String path = "https://yaran.me/wp-content/uploads/2019/01/video-1548653232.mp4";
            MediaController mediaController = new MediaController(mContext);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoPath(path);
            videoView.requestFocus();
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    linearLayoutForVideo.setVisibility(View.GONE);
                    videoView.setVisibility(View.GONE);
                }
            });
        }
    }

}
