package com.teamdrt.teamdrtdownloader.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamdrt.teamdrtdownloader.Databases.AppDatabse;
import com.teamdrt.teamdrtdownloader.Databases.Download;
import com.teamdrt.teamdrtdownloader.Databases.DownloadsDao;
import com.teamdrt.teamdrtdownloader.Databases.DownloadsRepository;
import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.VH.DownloadsVh;

import java.io.File;
import java.util.List;

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsVh> {

    private List <Download> alldownloads;
    ViewGroup parent;
    Context context;

    public DownloadsAdapter(List <Download> alldownloads) {
        this.alldownloads = alldownloads;
    }

    public void addItems(List <Download> alldownloads){
        this.alldownloads = alldownloads;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public DownloadsVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent=parent;
        context = parent.getContext ();
        View view =LayoutInflater.from(parent.getContext()).inflate( R.layout.download_single_layout, parent, false);
        return new DownloadsVh (view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DownloadsVh holder, int position) {
        holder.title_tv.setText ( alldownloads.get ( position ).getTitle () );
        holder.progressBar.setProgress ( alldownloads.get ( position ).getDownloadedPercent ().intValue () );
        holder.dtv.setText(alldownloads.get ( position ).getDownloadedPercent ().toString ()+"%");
        if (alldownloads.get ( position ).getMediaType ().equals ( "audio" )) {
            holder.format_ic.setImageResource ( R.drawable.ic_baseline_audiotrack_24 );
        }else {
            holder.format_ic.setImageResource ( R.drawable.ic_baseline_play_circle_filled_24);
        }
        File file=new File(alldownloads.get ( position ).getDownoadedPath ());
        if (file.exists ()) {

            holder.cv.setOnClickListener ( v -> holder.viewContent ( alldownloads.get ( position ).getDownoadedPath (), v.getContext () ) );
        }else {
            //notifyDataSetChanged ();
            //Toast.makeText ( context, file.getAbsolutePath () , Toast.LENGTH_SHORT ).show ();
            DownloadsDao downloadsDao= AppDatabse.getInstance (context).downloadsDao ();
            DownloadsRepository repository=new DownloadsRepository(downloadsDao);
            repository.delete ( alldownloads.get ( position ) );
        }

    }

    @Override
    public int getItemCount() {
        return alldownloads.size ();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
