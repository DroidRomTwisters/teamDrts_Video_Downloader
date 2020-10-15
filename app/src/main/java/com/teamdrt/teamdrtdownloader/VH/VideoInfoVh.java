package com.teamdrt.teamdrtdownloader.VH;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamdrt.teamdrtdownloader.Adapters.VidInfoAdapter;
import com.teamdrt.teamdrtdownloader.R;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.util.ArrayList;

public class VideoInfoVh extends RecyclerView.ViewHolder {
    public TextView videoformat,videores;
    public ImageView viddown;
    VidInfoAdapter.ClickListener clickListener;
    public VideoInfoVh(@NonNull View itemView, final VidInfoAdapter.ClickListener clickListener, ArrayList<String> formatid) {
        super ( itemView );
        viddown=itemView.findViewById ( R.id.viddown  );
        videoformat=itemView.findViewById ( R.id.formatinfo );
        videores=itemView.findViewById ( R.id.VideoResolution );
        this.clickListener=clickListener;
        viddown.setOnClickListener ( v -> {
            try {
                clickListener.OnDownloadClick ( getAdapterPosition () );
            } catch (YoutubeDLException e) {
                e.printStackTrace ();
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        } );
    }



}
