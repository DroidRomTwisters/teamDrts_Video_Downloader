package com.teamdrt.teamdrtdownloader;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate ();

        try {
            YoutubeDL.getInstance().init( getApplicationContext ());
            FFmpeg.getInstance().init(getApplicationContext ());
        } catch (YoutubeDLException e) {
            Log.e("TAG" , "failed to initialize youtubedl-android", e);
        }
    }
}
