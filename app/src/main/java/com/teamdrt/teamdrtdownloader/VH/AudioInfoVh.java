package com.teamdrt.teamdrtdownloader.VH;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamdrt.teamdrtdownloader.Adapters.AudInfoAdapter;
import com.teamdrt.teamdrtdownloader.Adapters.VidInfoAdapter;
import com.teamdrt.teamdrtdownloader.R;
import com.yausername.youtubedl_android.YoutubeDLException;

import java.util.ArrayList;

public class AudioInfoVh extends RecyclerView.ViewHolder {
    private TextView audioformat,audiores;
    private ImageView auddown;
    AudInfoAdapter.ClickListener clickListener;
    public AudioInfoVh(@NonNull View itemView, final AudInfoAdapter.ClickListener clickListener, ArrayList <String> formatid) {
        super ( itemView );
        auddown=itemView.findViewById ( R.id.viddown  );
        audioformat=itemView.findViewById ( R.id.formatinfo );
        audiores=itemView.findViewById ( R.id.VideoResolution );
        this.clickListener=clickListener;
        auddown.setOnClickListener ( v -> {
            try {
                clickListener.OnDownloadClick (  getAdapterPosition ()  );
            } catch (YoutubeDLException e) {
                e.printStackTrace ();
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        } );
    }

    public void setdetail(String audioreso,String ext){
        audioformat.setText ( ext );
        audiores.setText ( audioreso );
    }
}
