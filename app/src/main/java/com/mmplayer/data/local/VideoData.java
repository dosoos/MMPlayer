package com.mmplayer.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.mmplayer.data.DataContorl;
import com.mmplayer.data.bean.MediaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elone on 16/4/16.
 */
public class VideoData implements DataContorl.MediaData {

    private Context mContext;
    ContentResolver mContextContentResolver;

    public VideoData(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<MediaBean> getData() {
        if (mContextContentResolver == null) {
            mContextContentResolver = mContext.getContentResolver();
        }
        Cursor query = mContextContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        ArrayList<MediaBean> mediaBeen = new ArrayList<>();
        while (query.moveToNext()) {
            if (query.getLong(query.getColumnIndex(MediaStore.Video.Media.SIZE)) < 1024 * 1000) {
                //文件小于1000KB不加入列表
                continue;
            }
            MediaBean mediaBean = new MediaBean();
            mediaBean.flag = 2;
            mediaBean.title = query.getString(query.getColumnIndex(MediaStore.Audio.Media.TITLE));
            mediaBean.album = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            mediaBean.singer = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            mediaBean.size = query.getLong(query.getColumnIndex(MediaStore.Audio.Media.SIZE));
            mediaBean.time = query.getLong(query.getColumnIndex(MediaStore.Audio.Media.DURATION));
            mediaBean.url = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            mediaBeen.add(mediaBean);
        }
        query.close();
        return mediaBeen;
    }
}
