package com.teamdrt.teamdrtdownloader.WorkManager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.teamdrt.teamdrtdownloader.BR.NotificationReceiver;
import com.teamdrt.teamdrtdownloader.Databases.AppDatabse;
import com.teamdrt.teamdrtdownloader.Databases.Download;
import com.teamdrt.teamdrtdownloader.Databases.DownloadsDao;
import com.teamdrt.teamdrtdownloader.Databases.DownloadsRepository;
import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.Ui.MainActivity;
import com.yausername.youtubedl_android.DownloadProgressCallback;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;

import org.w3c.dom.Document;

import java.io.File;
import java.util.Date;
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
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType ( Uri.parse ( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"FYI_Downloader" ),"*/*" );
        Context context = getApplicationContext ();
        PendingIntent content=PendingIntent.getActivity(context, 0, intent, 0);

        Intent bcIntent=new Intent(context, NotificationReceiver.class );
        PendingIntent content1=PendingIntent.getActivity ( context,0,bcIntent,PendingIntent.FLAG_UPDATE_CURRENT );

        notificationManagerCompat= NotificationManagerCompat.from ( getApplicationContext () );
        notification = new NotificationCompat.Builder ( getApplicationContext (),CHANNEL_ID )
                .setSmallIcon ( android.R.drawable.stat_sys_download )
                .setContentTitle ( getInputData ().getString ( "Title" ))
                .setContentText ( "Download In Progress" )
                .setPriority ( NotificationCompat.PRIORITY_HIGH )
                .setCategory ( NotificationCompat.CATEGORY_PROGRESS)
                .setProgress ( 100,0,false )
                .setContentIntent(content)
                .setColor ( context.getResources ().getColor(R.color.colorPrimary) )
                .setOngoing ( true )
                .setAutoCancel ( true )
                .setOnlyAlertOnce ( true );

        ForegroundInfo foregroundInfo= new ForegroundInfo (1,notification.build ());
        setForegroundAsync ( foregroundInfo );

        YoutubeDLRequest request = new YoutubeDLRequest(getInputData ().getString ( "Url" ));
        File youtubeDLDir = getDownloadLocation();

        if (getInputData ().getString ( "acodec" ).equals ( "none" ) && !getInputData ().getString ( "vcodec" ).equals ( "none" )) {
            request.addOption ( "--merge-output-format","mkv" );
            request.addOption ( "-f", getInputData ().getString ( "formatid" ) + "+bestaudio" );
            request.addOption("-o", youtubeDLDir.getPath ()+File.separator+getInputData ().getString ( "Title" ).replaceAll ( "[\\W]","" )+"_"+getInputData ().getString ( "formatid" )+".%(ext)s");

        }else if (!getInputData ().getString ( "acodec" ).equals ( "none" ) && !getInputData ().getString ( "vcodec" ).equals ( "none" )){
            request.addOption ( "-f", getInputData ().getString ( "formatid" ));
            request.addOption("-o", youtubeDLDir.getPath ()+File.separator+getInputData ().getString ( "Title" ).replaceAll ( "[\\W]","" )+"_"+getInputData ().getString ( "formatid" )+".%(ext)s");

        }else if (!getInputData ().getString ( "acodec" ).equals ( "none" ) && getInputData ().getString ( "vcodec" ).equals ( "none" )){
            request.addOption ( "--audio-format","mp3" );
            request.addOption ( "-x" );
            request.addOption ( "-f", getInputData ().getString ( "formatid" ));
            request.addOption("-o", youtubeDLDir.getPath ()+File.separator+getInputData ().getString ( "Title" ).replaceAll ( "[\\W]","" )+"_"+getInputData ().getString ( "formatid" )+".mp3");

        }

        try {
            YoutubeDL.getInstance ().execute ( request, (progress, etaInSeconds) -> {
                updateNotification ( (int) progress,false,getInputData ().getString ( "Title" ) ,etaInSeconds);
            } );

        } catch (YoutubeDLException | InterruptedException e) {
            updateNotification ( 0,true ,"Download failed",null);
            e.printStackTrace ();
            return Result.failure ();
        }

        DownloadsDao downloadsDao= AppDatabse.getInstance (getApplicationContext ()).downloadsDao ();
        DownloadsRepository repository=new DownloadsRepository(downloadsDao);
        Download download=new Download ( getInputData ().getString ( "Title" ), new Date().getTime (),100.0);
        download.setDownloadSize ( getInputData ().getLong ( "size",0L) );
        if (!getInputData ().getString ( "acodec" ).equals ( "none" ) && getInputData ().getString ( "vcodec" ).equals ( "none" )) {
            download.setMediaType ("audio");
        }else {
            download.setMediaType ( "video" );
        }
        download.setTotalsize ( getInputData ().getLong ( "size",0L ));
        File file;
        if (getInputData ().getString ( "acodec" ).equals ( "none" ) && !getInputData ().getString ( "vcodec" ).equals ( "none" )) {
            String ext=getInputData ().getString ( "ext" );
            if (ext.equals ( "webm" )){
                ext="mkv";
            }
            file = new File ( youtubeDLDir.getPath ()+File.separator+getInputData ().getString ( "Title" ).replaceAll ( "[\\W]","" )+"_"+getInputData ().getString ( "formatid" )+".mkv" );
        }else if (!getInputData ().getString ( "acodec" ).equals ( "none" ) && !getInputData ().getString ( "vcodec" ).equals ( "none" )){
            String ext=getInputData ().getString ( "ext" );
            if (ext.equals ( "webm" )){
                ext="mkv";
            }
            file = new File ( youtubeDLDir.getPath ()+File.separator+getInputData ().getString ( "Title" ).replaceAll ( "[\\W]","" )+"_"+getInputData ().getString ( "formatid" )+"."+ext );
        }else {
            file = new File ( youtubeDLDir.getPath ()+File.separator+getInputData ().getString ( "Title" ).replaceAll ( "[\\W]","" )+"_"+getInputData ().getString ( "formatid" )+".mp3" );
        }
        Uri treeUri =Uri.parse ( file.getAbsolutePath () );
        download.setDownoadedPath ( treeUri.toString () );
        repository.insert ( download );
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
        File youtubeDLDir = new File(downloadsDir, "FYI_Downloader");
        if (!youtubeDLDir.exists()) youtubeDLDir.mkdir();
        return youtubeDLDir;
    }


}
