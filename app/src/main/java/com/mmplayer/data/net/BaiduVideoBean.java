package com.mmplayer.data.net;

import com.mmplayer.data.bean.MediaBean;

import java.util.List;

/**
 * Created by Elone on 16/4/20.
 */
public class BaiduVideoBean {

    public ContentBean[] data;
    public int errNum;
    public String errMsg;

    public static class ContentBean{
        public List<MediaBean> mPlayUrl;
    }

}
