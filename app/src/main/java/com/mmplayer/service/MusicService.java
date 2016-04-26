package com.mmplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mmplayer.events.OverMusicEvent;
import com.mmplayer.events.PauseEvent;
import com.mmplayer.events.RestartEvent;
import com.mmplayer.events.StartEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

/**
 * Created by Elone on 16/4/17.
 */
public class MusicService extends Service {
    public static final int MUSIC_START = 0x000001;
    public static final int MUSIC_STOP = 0x000002;
    public static final int MUSIC_VOER = 0x000003;
    public static final int MUSIC_RESTART = 0x000004;

    private MediaPlayer mediaPlayer;
    private EventBus meventBus = EventBus.getDefault();

    @Override
    public void onCreate() {
        super.onCreate();
        meventBus.register(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                EventBus.getDefault().post(new OverMusicEvent());
            }
        });
    }
    @Override
    public void onDestroy() {
        meventBus.unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void musicStart(StartEvent startEvent){
        try {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(startEvent.mediaBean.url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void musicRestart(RestartEvent restartEvent){
        mediaPlayer.start();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void musicPause(PauseEvent pauseEvent){
        mediaPlayer.pause();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
