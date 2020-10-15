package com.teamdrt.teamdrtdownloader.BR;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;



public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String tag="DownloadWmTag";
        boolean running;
        WorkManager instance = WorkManager.getInstance(context);
        ListenableFuture <List <WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
                if (running){
                    instance.cancelAllWorkByTag(tag);
                    CharSequence text="Download Cancelled";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.show ();
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
