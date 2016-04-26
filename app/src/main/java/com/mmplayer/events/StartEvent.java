package com.mmplayer.events;

import com.mmplayer.data.bean.MediaBean;

/**
 * Created by Elone on 16/4/17.
 */
public class StartEvent {
    public MediaBean mediaBean;

    public StartEvent(MediaBean mediaBean) {
        this.mediaBean = mediaBean;
    }
}
