package me.yaran.app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "Splash Screen";
    private static int SPLASH_TIME_OUT = 22000;              // Splash screen timer

    int counter = 0;
    VideoView videoView;

    //FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        counter = BP.getPreferenceInt(this,BP.Key_Counter);
        videoView = (VideoView) findViewById(R.id.videoView);


        if (counter == 0){
            videoView.setVisibility(View.VISIBLE);

            //String path = "android.resource://" + getPackageName() + "/" + R.raw.intro;
            String path = "https://yaran.me/wp-content/uploads/2019/01/video-1548653232.mp4";
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoPath(path);
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();

                    Intent i = new Intent(SplashScreen.this, Web.class);
                    startActivity(i);
                    finish();

                }
            });
        }

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent i = new Intent(SplashScreen.this, Web.class);
                    startActivity(i);
                    finish();
            }
        }, SPLASH_TIME_OUT);*/
    }

}