package com.teamdrt.teamdrtdownloader.ViewModels;

import android.app.Application;
import android.app.DownloadManager;
import android.app.ListActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.teamdrt.teamdrtdownloader.Databases.Download;
import com.teamdrt.teamdrtdownloader.Databases.DownloadsRepository;
import com.teamdrt.teamdrtdownloader.Utils.NumberUtils;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.mapper.VideoFormat;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.util.ArrayList;

public class VidInfoVM extends ViewModel {


    private static final Integer INITIAL = 1;
    private static final Integer LOADED = 3;
    private static final Integer LOADING = 2;
    public MutableLiveData<Integer> LoadState=new MutableLiveData <> ( INITIAL );
    public MutableLiveData<String> thumbnail=new MutableLiveData <> (  );
    public MutableLiveData<VideoInfo> videoinfo=new MutableLiveData <> (  );
    public MutableLiveData<String> url=new MutableLiveData <> (  );


    private void updateLoading(Integer loadState){
        this.LoadState.postValue(loadState);
    }

    private void updateThumbanil(String thumbnail){
        this.thumbnail.postValue ( thumbnail );
    }

    private void updatevideoinfo(VideoInfo videoInfo){
        this.videoinfo.postValue(videoInfo);
    }

    private void updateurl(String url){
        this.url.postValue ( url );
    }
    public void getDetails(String url){
        updateLoading ( LOADING );
        VideoInfo videoInfo = null;
        try {
            videoInfo = YoutubeDL.getInstance ().getInfo ( url );
        } catch (YoutubeDLException | InterruptedException e) {
            updateLoading ( LOADED );
            e.printStackTrace ();
        }
        updateurl ( url );
        updatevideoinfo ( videoInfo );
        updateLoading ( LOADED );
        updateThumbanil ( videoInfo.getThumbnail () );
    }



}

