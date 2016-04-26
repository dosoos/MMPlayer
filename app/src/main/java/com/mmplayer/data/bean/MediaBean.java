package com.mmplayer.data.bean;

import android.net.Uri;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by Elone on 16/4/16.
 */
public class MediaBean implements Serializable {
    public int flag;//1.music,2.video,3.netvideo
    public boolean initialization = false;
    public String title;
    public String singer;
    public String album;
    public String url;
    public Uri uri;
    public long size;
    public long time;
    public String name;
    public String episodeNumber;
    public String intime;
    public String language;
    public float price;
    public String source;
    public String value;
    public long frames;
}
