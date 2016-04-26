package com.mmplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmplayer.data.bean.MediaBean;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.mmplayer.R;
import com.mmplayer.events.NextMusicEvent;
import com.mmplayer.events.PauseEvent;
import com.mmplayer.events.PrivateMusicEvent;
import com.mmplayer.events.RestartEvent;
import com.mmplayer.events.StartEvent;
import com.mmplayer.events.UpdateInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Elone on 16/4/17.
 */
public class MusicPlayerActivity extends Activity {

    private static final String TAG = MusicPlayerActivity.class.getName();
    private static final int MUSIC_PLAYING = 0;
    private static final int MUSIC_STOPING = 1;

    @InjectView(R.id.title)
    public TextView mTitle;
    @InjectView(R.id.summary)
    public TextView mSummary;
    @InjectView(R.id.cd)
    public ImageView mcd;
    @InjectView(R.id.previte)
    public ImageView mPrevite;
    @InjectView(R.id.next)
    public ImageView mNext;
    @InjectView(R.id.play)
    public ImageView mPlay;

    private Animation manimation;
    private MediaBean mCurrMusic;
    private int mState = MUSIC_PLAYING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        EventBus.getDefault().register(this);
        ButterKnife.inject(this);
        if (savedInstanceState != null){
            mCurrMusic = (MediaBean) savedInstanceState.getSerializable("mediabean");
        }else if(getIntent().getExtras() != null){
            mCurrMusic = (MediaBean) getIntent().getExtras().getSerializable("mediabean");
        }
        setPlay(android.R.drawable.ic_media_pause);
        startAnim();
        setPlayTitle(mCurrMusic.title);
        setPlaySummary(mCurrMusic.title);
        EventBus.getDefault().post(new StartEvent(mCurrMusic));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrMusic == null)
            mCurrMusic = (MediaBean) getIntent().getExtras().getSerializable("mediabean");



    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.play)
    public void onClickPlay(View v) {
        switch (mState){
            case MUSIC_PLAYING://从播放转暂停
                setPlay(android.R.drawable.ic_media_play);
                stopAnim();
                EventBus.getDefault().post(new PauseEvent());
                mState = MUSIC_STOPING;

                break;
            case MUSIC_STOPING://从暂停转播放
                setPlay(android.R.drawable.ic_media_pause);
                startAnim();
                EventBus.getDefault().post(new RestartEvent());
                mState = MUSIC_STOPING;

                break;
        }
    }

    @OnClick(R.id.previte)
    public void onClickPrevite(View v) {
        EventBus.getDefault().post(new PrivateMusicEvent());
    }

    @OnClick(R.id.next)
    public void onClickNext(View v) {
        EventBus.getDefault().post(new NextMusicEvent());
    }

    public void startAnim() {
        if (manimation ==null)
            manimation = AnimationUtils.loadAnimation(this, R.anim.recycle);
        mcd.startAnimation(manimation);
    }

    @Subscribe
    public void onReceivedUpdate(UpdateInfoEvent event){
        if (event == null)
            return;
        if (event.mediaBean == null)
            return;
        if (event.mediaBean.title == null)
            return;
        if (event.mediaBean.singer == null)
            return;
        setPlay(android.R.drawable.ic_media_pause);
        setPlayTitle(event.mediaBean.title);
        setPlaySummary(event.mediaBean.title);

    }

    public void stopAnim() {
        mcd.clearAnimation();
    }

    public void setPlay(int res){
        if (res <= 0){
            return;
        }
        mPlay.setImageResource(res);
    }

    public void setPlayTitle(String title) {
        if (title == null){
            return;
        }
        mTitle.setText(title);
    }

    public void setPlaySummary(String summary) {
        if (summary == null){
            return;
        }
        mSummary.setText(summary);
    }

}
