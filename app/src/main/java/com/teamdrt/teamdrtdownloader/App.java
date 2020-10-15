package com.teamdrt.teamdrtdownloader;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

public class App extends Application {
    public static final String CHANNEL_ID="Downloads";
    @Override
    public void onCreate() {
        super.onCreate ();
        try {
            YoutubeDL.getInstance().init( getApplicationContext ());
            FFmpeg.getInstance().init(getApplicationContext ());
        } catch (YoutubeDLException e) {
            Log.e("TAG" , "failed to initialize youtubedl-android", e);
        }

        createNotificaionchannel();
    }

    private void createNotificaionchannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel (
                    CHANNEL_ID,
                    "Notification For Download Progress",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription ( "Notification For Download Progress" );
            NotificationManager manager=getSystemService ( NotificationManager.class );
            if (manager != null) {
                manager.createNotificationChannel ( channel );
            }
        }
    }


}
