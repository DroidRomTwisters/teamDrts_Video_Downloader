package com.teamdrt.teamdrtdownloader.WorkManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.Ui.MainActivity;
import com.yausername.youtubedl_android.DownloadProgressCallback;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;

import java.io.File;
import java.util.Objects;

import static com.teamdrt.teamdrtdownloader.App.CHANNEL_ID;


public class DownloadWM extends Worker {
    NotificationCompat.Builder notification;
    private NotificationManagerCompat notificationManagerCompat;


    public DownloadWM(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super ( context, workerParams );
    }

    @NonNull
    @Override
    public Result doWork() {
        notificationManagerCompat= NotificationManagerCompat.from ( getApplicationContext () );
        notification = new NotificationCompat.Builder ( getApplicationContext (),CHANNEL_ID )
                .setSmallIcon ( android.R.drawable.stat_sys_download )
                .setContentTitle ( getInputData ().getString ( "Title" ))
                .setContentText ( "Download In Progress" )
                .setPriority ( NotificationCompat.PRIORITY_HIGH )
                .setCategory ( NotificationCompat.CATEGORY_PROGRESS)
                .setProgress ( 100,0,false )
                .setOngoing ( true )
                .setAutoCancel ( true )
                .setOnlyAlertOnce ( true );

        ForegroundInfo foregroundInfo= new ForegroundInfo (1,notification.build ());
        setForegroundAsync ( foregroundInfo );

        YoutubeDLRequest request = new YoutubeDLRequest(getInputData ().getString ( "Url" ));
        File youtubeDLDir = getDownloadLocation();

        request.addOption ( "-f", getInputData ().getString ( "formatid" ) + "+bestaudio" );
        request.addOption("-o", youtubeDLDir.getAbsolutePath() + "/%(title)s"+"_"+getInputData ().getString ( "formatid" )+".%(ext)s");

        try {
            YoutubeDL.getInstance ().execute ( request, (progress, etaInSeconds) -> {
                updateNotification ( (int) progress,false,getInputData ().getString ( "Title" ) ,etaInSeconds);
            } );
        } catch (YoutubeDLException | InterruptedException e) {
            updateNotification ( 0,true ,"Download failed",null);
            e.printStackTrace ();
            return Result.failure ();
        }


        return Result.success ();
    }


    public void updateNotification(int Progress, boolean dismiss, @Nullable String Title,@Nullable Long etainSeconds){
        if (!dismiss) {
            notification.setStyle ( new NotificationCompat.BigTextStyle().bigText ( getApplicationContext ().getString ( R.string.eta_in_seconds,etainSeconds ) ));
            notification.setProgress ( 100, Progress, false );
            notificationManagerCompat.notify ( 1, notification.build () );
        }else {
            notification.setSmallIcon ( R.drawable.ic_24px );
            notification.setContentText ( Title );
            notification.setProgress ( 0, Progress, false );
            notification.setOngoing ( false );
            notificationManagerCompat.notify ( 1, notification.build () );
        }
    }

    private File getDownloadLocation() {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File youtubeDLDir = new File(downloadsDir, "FYI Downloader");
        if (!youtubeDLDir.exists()) youtubeDLDir.mkdir();
        return youtubeDLDir;
    }


}
