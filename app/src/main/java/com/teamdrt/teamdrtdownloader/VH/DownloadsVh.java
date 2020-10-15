package com.teamdrt.teamdrtdownloader.VH;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.teamdrt.teamdrtdownloader.BuildConfig;
import com.teamdrt.teamdrtdownloader.R;

import java.io.File;

public class DownloadsVh extends RecyclerView.ViewHolder {

    public CardView cv;
    public TextView title_tv,dtv,dsize_tv;
    public ImageView format_ic;
    public ProgressBar progressBar;
    public DownloadsVh(@NonNull View itemView) {
        super ( itemView );
        cv=itemView.findViewById ( R.id.cv );
        title_tv=itemView.findViewById ( R.id.title_tv );
        dtv=itemView.findViewById ( R.id.download_percent_tv );
        dsize_tv=itemView.findViewById ( R.id.download_size_tv );
        format_ic=itemView.findViewById ( R.id.format_ic );
        progressBar=itemView.findViewById ( R.id.download_pb );
    }

    public void viewContent(String path, Context ctx){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.addFlags ( Intent.FLAG_GRANT_READ_URI_PERMISSION );
        File file=new File(path);
            Uri uri = FileProvider.getUriForFile ( ctx, BuildConfig.APPLICATION_ID + ".FileProvider", file );
            String mimetype = ctx.getContentResolver ().getType ( uri );
            if (mimetype == null) {
                mimetype = "*/*";
            }
            intent.setDataAndType ( uri, mimetype );
            if (intent.resolveActivity ( ctx.getPackageManager () ) != null) {
                ctx.startActivity ( intent );
            } else {
                Toast.makeText ( ctx, "No app Found to handle this event", Toast.LENGTH_SHORT ).show ();
            }
    }

}
