package com.mmplayer.data;

import android.content.Context;

import com.mmplayer.data.bean.MediaBean;
import com.mmplayer.data.local.MusicData;
import com.mmplayer.data.local.VideoData;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by Elone on 16/4/16.
 */
public class DataContorl {

    public static final int DATA_MUSIC = 1;
    public static final int DATA_VIDEO = 2;

    private static WeakHashMap<Integer, MediaData> mMediaDataCache = new WeakHashMap<Integer, MediaData>();

    public static interface MediaData {
        public List<MediaBean> getData();
    }

    public static MediaData getMediaData(Context context, int whereis) {
        switch (whereis) {
            case DATA_MUSIC:
                synchronized (mMediaDataCache){
                    if (mMediaDataCache.get(DATA_MUSIC) != null){
                        return mMediaDataCache.get(DATA_MUSIC);
                    }
                    mMediaDataCache.put(DATA_MUSIC,new MusicData(context));
                    return mMediaDataCache.get(DATA_MUSIC);
                }
            case DATA_VIDEO:
                synchronized (mMediaDataCache){
                    if (mMediaDataCache.get(DATA_VIDEO) != null){
                        return mMediaDataCache.get(DATA_VIDEO);
                    }
                    mMediaDataCache.put(DATA_VIDEO,new VideoData(context));
                    return mMediaDataCache.get(DATA_VIDEO);
                }

        }
        return null;
    }

}
