package com.mmplayer.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.mmplayer.R;
import com.mmplayer.data.bean.MediaBean;
import com.mmplayer.fragment.VideoFragment;
import com.mmplayer.util.Screen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Elone on 16/4/17.
 */
public class VideoPlayerActivity extends Activity {

    private static final String TAG = VideoPlayerActivity.class.getName();

    @InjectView(R.id.videoView)
    public VideoView mVideoView;

    @InjectView(R.id.fab)
    public FloatingActionButton mFlaotButton;
    private MediaBean mCurrVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.inject(this);
        if (savedInstanceState != null){
            mCurrVideo = (MediaBean) savedInstanceState.getSerializable("mediabean");
        }else if(getIntent().getExtras() != null){
            mCurrVideo = (MediaBean) getIntent().getExtras().getSerializable("mediabean");
        }
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoPath(mCurrVideo.url);
        mVideoView.start();

    }

    @OnClick(R.id.fab)
    public void onFlaotButtonClick(View v){
        v.setVisibility(View.GONE);
        takeScreenshot(mVideoView.getCurrentPosition());
        v.setVisibility(View.VISIBLE);

    }

    public void takeScreenshot(long time ){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mCurrVideo.url);
        Bitmap drawingCache = mediaMetadataRetriever.getFrameAtTime(time/30);
        if (drawingCache == null){
            Toast.makeText(getApplicationContext(), "截屏失败", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        String currTime = dataFormat.format(new Date(System.currentTimeMillis()));
        String filePath = rootPath+"/Pictures/Screenshots/";
        File folder = new File(filePath);
        if (!folder.exists())
            folder.mkdir();
        File file = new File(filePath,"Screenshot_"+currTime+".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            drawingCache.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            drawingCache.recycle();
            Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
        Toast.makeText(getApplicationContext(), "截屏失败", Toast.LENGTH_SHORT).show();
    }

}
