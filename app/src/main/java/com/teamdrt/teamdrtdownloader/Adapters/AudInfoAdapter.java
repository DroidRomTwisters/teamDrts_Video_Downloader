package com.teamdrt.teamdrtdownloader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.VH.AudioInfoVh;
import com.teamdrt.teamdrtdownloader.VH.AudioInfoVh;
import com.yausername.youtubedl_android.YoutubeDLException;

import java.util.ArrayList;


public class AudInfoAdapter extends RecyclerView.Adapter<AudioInfoVh>{
    Context context;
    ArrayList <String> videores,ext,formatid;
    private ClickListener ClickListener;

    public AudInfoAdapter(Context context, ArrayList<String> videoInfos, ArrayList<String> ext, ArrayList<String> formatid,  ClickListener ClickListener){
        this.context=context;
        this.videores=videoInfos;
        this.ext=ext;
        this.formatid=formatid;
        this.ClickListener=ClickListener;
    }

    @NonNull
    @Override
    public AudioInfoVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( context ).inflate ( R.layout.formatssinglelayout,parent,false );
        return new AudioInfoVh(view,ClickListener,formatid);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioInfoVh holder, int position) {
        String videoInfo=videores.get ( position );
        holder.setdetail ( videoInfo,ext.get ( position ) );

    }

    public interface ClickListener{
        void OnDownloadClick(String formatid);
    }
    @Override
    public int getItemCount() {
        return videores.size ();
    }
}
