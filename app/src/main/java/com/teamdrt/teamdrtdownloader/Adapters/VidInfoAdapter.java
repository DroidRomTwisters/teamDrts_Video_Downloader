package com.teamdrt.teamdrtdownloader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.VH.VideoInfoVh;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.util.ArrayList;

public class VidInfoAdapter extends RecyclerView.Adapter<VideoInfoVh> {
    Context context;
    ArrayList<String> videores,ext,formatid;
    private ClickListener ClickListener;

    public VidInfoAdapter(Context context, ArrayList<String> videoInfos,ArrayList<String> ext,ArrayList<String> formatid,ClickListener ClickListener){
        this.context=context;
        this.videores=videoInfos;
        this.ext=ext;
        this.formatid=formatid;
        this.ClickListener=ClickListener;
    }

    @NonNull
    @Override
    public VideoInfoVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( context ).inflate ( R.layout.formatssinglelayout,parent,false );
        return new VideoInfoVh(view,ClickListener,formatid);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoInfoVh holder, int position) {
        String videoInfo=videores.get ( position );
        holder.videores.setText ( videoInfo );
        holder.videoformat.setText ( ext.get ( position ) );
    }


    public interface ClickListener{
        void OnDownloadClick(int position) throws YoutubeDLException, InterruptedException;
    }
    @Override
    public int getItemCount() {
        return videores.size ();
    }
}
