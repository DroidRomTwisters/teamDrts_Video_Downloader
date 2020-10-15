package com.teamdrt.teamdrtdownloader.ViewModels;

import android.content.Context;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teamdrt.teamdrtdownloader.Databases.AppDatabse;
import com.teamdrt.teamdrtdownloader.Databases.Download;
import com.teamdrt.teamdrtdownloader.Databases.DownloadsDao;
import com.teamdrt.teamdrtdownloader.Databases.DownloadsRepository;

import java.util.List;

public class DownloadsVM extends ViewModel {
    private DownloadsRepository repository;
    public LiveData<List <Download>> allDownloads;

    private void updateDownloads(LiveData<List<Download>> allDownloads) {
        this.allDownloads = allDownloads;
    }

    public void getAllDownloads(Context ctx){
        DownloadsDao downloadsDao=AppDatabse.getInstance ( ctx ).downloadsDao ();
        repository= new DownloadsRepository(downloadsDao);
        updateDownloads ( repository.getAllDownloads () );
    }

}
