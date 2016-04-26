package com.mmplayer.events;


import com.mmplayer.data.bean.MediaBean;

/**
 * Created by Elone on 16/4/17.
 */
public class UpdateInfoEvent {
    public MediaBean mediaBean;

    public UpdateInfoEvent(MediaBean mediaBean) {
        this.mediaBean = mediaBean;
    }
}
