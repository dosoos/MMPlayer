package com.mmplayer.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import com.mmplayer.data.DataContorl;
import com.mmplayer.data.bean.MediaBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elone on 16/4/16.
 */
public class MusicData implements DataContorl.MediaData {
    private static final Logger logger = LoggerFactory.getLogger(MusicData.class);

    private Context mContext;
    ContentResolver mContextContentResolver;

    public MusicData(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<MediaBean> getData() {
        if (mContextContentResolver == null) {
            mContextContentResolver = mContext.getContentResolver();
        }
        mContext.grantUriPermission("org.hidream", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Cursor query = mContextContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        ArrayList<MediaBean> mediaBeen = new ArrayList<>();
        while (query.moveToNext()) {
            if (query.getLong(query.getColumnIndex(MediaStore.Audio.Media.SIZE)) < 1024 * 200) {
                //文件小于200KB不加入列表
                continue;
            }
            MediaBean mediaBean = new MediaBean();
            mediaBean.flag = 1;//1.音乐,2.视频
            mediaBean.size = query.getLong(query.getColumnIndex(MediaStore.Audio.Media.SIZE));
            mediaBean.title = query.getString(query.getColumnIndex(MediaStore.Audio.Media.TITLE));
            mediaBean.album = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            mediaBean.singer = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            mediaBean.url = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            mediaBeen.add(mediaBean);
        }
        query.close();
        return mediaBeen;
    }
}
