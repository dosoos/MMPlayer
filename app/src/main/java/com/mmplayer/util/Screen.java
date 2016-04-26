package com.mmplayer.util;

import java.io.IOException;

/**
 * Created by Elone on 16/4/19.
 */
public class Screen {

    public static boolean takeScreenShot(String filePath) {
        try {
            Runtime.getRuntime().exec("screencap "+filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
