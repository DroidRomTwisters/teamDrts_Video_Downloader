package com.teamdrt.teamdrtdownloader.Ui;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.teamdrt.teamdrtdownloader.Adapters.VidInfoAdapter;
import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.Utils.NumberUtils;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.BuildConfig;
import com.yausername.youtubedl_android.DownloadProgressCallback;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;
import com.yausername.youtubedl_android.mapper.VideoFormat;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.teamdrt.teamdrtdownloader.App.CHANNEL_ID;
import static com.teamdrt.teamdrtdownloader.Ui.MainActivity.mctx;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment implements VidInfoAdapter.ClickListener {

    private static final String TAG = "arya";
    private TextView title;
    private RecyclerView videolist;
    private VidInfoAdapter vidInfoAdapter;
    private ArrayList<String> videores,ext,formatid;
    private String link;
    private String thumbnaillink;
    private ImageView toolbar_image;
    ProgressBar progressBar;
    Toolbar toolbar;
    ActionBar actionBar;
    VideoInfo videoInfo;
    NotificationCompat.Builder notification;
    private boolean downloading = false;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NotificationManagerCompat notificationManagerCompat;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters

    private DownloadProgressCallback callback = new DownloadProgressCallback() {
        @Override
        public void onProgressUpdate(final float progress, final long etaInSeconds) {
            getActivity ().runOnUiThread( () -> {
                        updateNotification ( (int) progress,false,null);
                    }
            );
        }
    };

    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1, param1 );
        args.putString ( ARG_PARAM2, param2 );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (getArguments () != null) {
            mParam1 = getArguments ().getString ( ARG_PARAM1 );
            mParam2 = getArguments ().getString ( ARG_PARAM2 );
        }
        //setRetainInstance ( true );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate ( R.layout.fragment_video, container, false );
        videolist=v.findViewById ( R.id.video_list );
        toolbar = v.findViewById(R.id.appbar);
        toolbar_image = v.findViewById ( R.id.toolbar_image );
        videores=new ArrayList <> (  );
        ext=new ArrayList <> (  );
        formatid=new ArrayList <> (  );
        videolist.setLayoutManager ( new LinearLayoutManager ( mctx ) );
        progressBar=v.findViewById ( R.id.progressBar );
        title=v.findViewById ( R.id.textView );
        notificationManagerCompat=NotificationManagerCompat.from ( mctx );
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        if (savedInstanceState!=null){
            videores=savedInstanceState.getStringArrayList ( "videores" );
            ext=savedInstanceState.getStringArrayList ( "ext" );
            formatid=savedInstanceState.getStringArrayList ( "formatid" );
            thumbnaillink=savedInstanceState.getString ( "thumbnaillink" );
            setAdapters ();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored ( savedInstanceState );

    }

    @Override
    public void onResume() {
        super.onResume ();

        Menu menu=toolbar.getMenu ();
        MenuItem ourSearchItem = menu.findItem(R.id.search_bar);

        SearchView sv = (SearchView) ourSearchItem.getActionView();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                link=query;
                new getdetails ().execute (  );
                progressBar.setVisibility ( View.VISIBLE );
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void OnDownloadClick(String formatid) {
        startDownload (formatid);
    }

    private class getdetails extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                videoInfo = YoutubeDL.getInstance ().getInfo ( link );
                ArrayList<VideoFormat> formats=videoInfo.getFormats ();
                for (int i=0;i<formats.size ();i++){
                    VideoFormat videoFormat=formats.get ( i );
                    if(videoFormat.getWidth ()!=0 && videoFormat.getHeight ()!=0) {
                        if (!formatid.contains ( videoFormat.getFormatId () )) {
                            formatid.add ( videoFormat.getFormatId () );
                            String size = NumberUtils.format ( videoFormat.getFilesize () );
                            videores.add ( videoFormat.getFormat () + "  -" + size );
                            ext.add ( videoFormat.getExt () );
                        }
                    }
                }
            } catch (YoutubeDLException e) {
                e.printStackTrace ();
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute ( s );
            thumbnaillink = videoInfo.getThumbnail ();
            setAdapters ();
            hideKeyboard ( getActivity () );

        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity!=null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService ( Activity.INPUT_METHOD_SERVICE );
            View view = activity.getCurrentFocus ();
            if (view == null) {
                view = new View ( activity );
            }
            if (imm != null) {
                imm.hideSoftInputFromWindow ( view.getWindowToken (), 0 );
                view.clearFocus ();
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mctx.checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity (), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    private void startDownload(String formatid) {
        if (downloading) {
            Toast.makeText(mctx, "cannot start download. a download is already in progress", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isStoragePermissionGranted()) {
            Toast.makeText(mctx, "grant storage permission and retry", Toast.LENGTH_LONG).show();
            return;
        }


        YoutubeDLRequest request = new YoutubeDLRequest(link);
        File youtubeDLDir = getDownloadLocation();
        request.addOption ( "-f",formatid+"+bestaudio" );
        request.addOption("-o", youtubeDLDir.getAbsolutePath() + "/%(title)s"+"_"+formatid+".%(ext)s");

        showNotification (videoInfo.getTitle ());

        downloading = true;

        Disposable disposable = Observable.fromCallable(() -> YoutubeDL.getInstance().execute(request, callback))
                .subscribeOn( Schedulers.newThread())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe(youtubeDLResponse -> {
                    updateNotification ( 0,true ,"Download finished");
                    //TODO tvCommandOutput.setText(youtubeDLResponse.getOut());
                    Toast.makeText(mctx, "download successful", Toast.LENGTH_LONG).show();
                    downloading = false;
                }, e -> {
                    if(BuildConfig.DEBUG) Log.e(TAG,  "failed to download", e);
                    updateNotification ( 0,true ,"Download failed");
                    //TODO tvCommandOutput.setText(e.getMessage());
                    Toast.makeText(mctx, "download failed", Toast.LENGTH_LONG).show();
                    downloading = false;
                });
        compositeDisposable.add(disposable);

    }

    private File getDownloadLocation() {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File youtubeDLDir = new File(downloadsDir, "youtubedl-android");
        if (!youtubeDLDir.exists()) youtubeDLDir.mkdir();
        return youtubeDLDir;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy ();
    }

    @Override
    public void onPause() {
        super.onPause ();
    }

    public void showNotification(String Title){

        notification = new NotificationCompat.Builder ( mctx,CHANNEL_ID )
                .setSmallIcon ( android.R.drawable.stat_sys_download )
                .setContentTitle ( "Downloading "+Title)
                .setContentText ( "Download In Progress" )
                .setPriority ( NotificationCompat.PRIORITY_LOW )
                .setCategory ( NotificationCompat.CATEGORY_PROGRESS)
                .setProgress ( 100,0,false )
                .setOngoing ( true )
                .setAutoCancel ( true )
                .setOnlyAlertOnce ( true );

        notificationManagerCompat.notify ( 1,notification.build () );
    }

    public void updateNotification(int Progress, boolean dismiss, @Nullable String Title){
        if (!dismiss) {
            notification.setProgress ( 100, Progress, false );
            notificationManagerCompat.notify ( 1, notification.build () );
        }else {
            notification.setSmallIcon ( R.drawable.ic_24px );
            notification.setContentText ( Title );
            notification.setProgress ( 0, Progress, false );
            notification.setOngoing ( false );
            notificationManagerCompat.notify ( 1, notification.build () );
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState ( outState );
        outState.putStringArrayList ( "videores",videores );
        outState.putStringArrayList ( "ext",ext );
        outState.putStringArrayList ( "formatid",formatid );
        outState.putString ( "link",link );
        outState.putString ( "thumbnaillink",thumbnaillink );
    }

    public void setAdapters(){
        Glide.with ( mctx ).load ( thumbnaillink ).into ( toolbar_image );
        vidInfoAdapter=new VidInfoAdapter ( mctx,videores,ext ,formatid, VideoFragment.this );
        videolist.setAdapter ( vidInfoAdapter );
        title.setText ( videoInfo.getTitle () );
    }
}