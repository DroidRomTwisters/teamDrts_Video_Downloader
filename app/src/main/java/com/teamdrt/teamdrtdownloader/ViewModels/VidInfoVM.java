package com.teamdrt.teamdrtdownloader.ViewModels;


import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import static com.teamdrt.teamdrtdownloader.ViewModels.LoadState.INITIAL;
import static com.teamdrt.teamdrtdownloader.ViewModels.LoadState.LOADED;
import static com.teamdrt.teamdrtdownloader.ViewModels.LoadState.LOADING;

public class VidInfoVM extends ViewModel {


    public MutableLiveData<LoadState> LoadState=new MutableLiveData <> ( INITIAL );
    public MutableLiveData<String> thumbnail=new MutableLiveData <> (  );
    public MutableLiveData<VideoInfo> videoinfo=new MutableLiveData <> (  );
    public MutableLiveData<String> url=new MutableLiveData <> (  );


    private void updateLoading(LoadState loadState){
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

    public void loadpb(String url){
        updateLoading ( LOADING );
        getDetails ( url );
    }
    public void getDetails(String url) {
        new details ().execute ( url );
    }

    public class details extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String url=strings[0];
            VideoInfo videoInfo = null;
            try {
                videoInfo = YoutubeDL.getInstance ().getInfo ( url );
            } catch (YoutubeDLException | InterruptedException e) {
                updateLoading ( LOADED );
                e.printStackTrace ();
            }
            if (videoInfo!=null) {
                updateurl ( url );
                updatevideoinfo ( videoInfo );
                updateLoading ( LOADED );
                updateThumbanil ( videoInfo.getThumbnail () );
            }
            return null;
        }
    }
}

