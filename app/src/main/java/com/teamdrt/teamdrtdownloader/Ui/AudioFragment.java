package com.teamdrt.teamdrtdownloader.Ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;
import com.teamdrt.teamdrtdownloader.Adapters.AudInfoAdapter;
import com.teamdrt.teamdrtdownloader.Adapters.VidInfoAdapter;
import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.Utils.NumberUtils;
import com.teamdrt.teamdrtdownloader.ViewModels.AudInfoVM;
import com.teamdrt.teamdrtdownloader.ViewModels.LoadState;
import com.teamdrt.teamdrtdownloader.ViewModels.AudInfoVM;
import com.teamdrt.teamdrtdownloader.WorkManager.DownloadWM;
import com.yausername.youtubedl_android.mapper.VideoFormat;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.teamdrt.teamdrtdownloader.Ui.MainActivity.mctx;

public class AudioFragment extends Fragment implements AudInfoAdapter.ClickListener {

    private AudInfoVM audInfoVM;
    private static final String TAG = "arya";
    private TextView title;
    private RecyclerView videolist;
    private AudInfoAdapter vidInfoAdapter;
    private ArrayList<String> videores,ext,formatid,acodec,vcodec;
    private ArrayList<Long> filesize;
    private ArrayList<VideoFormat> audioFormats;
    private MutableLiveData<LoadState> loadState=new MutableLiveData <> (LoadState.INITIAL);

    private ImageView toolbar_image;
    ProgressBar progressBar;
    Toolbar toolbar;
    VideoInfo audioInfo;
    private String url;

    View view=null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NotificationManagerCompat notificationManagerCompat;
    private String mParam1;
    private String mParam2;

    public AudioFragment() {


    }

    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment ();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate ( R.layout.fragment_audio, container, false );
        videolist=v.findViewById ( R.id.audio_list );
        toolbar = v.findViewById(R.id.appbar2);
        toolbar_image = v.findViewById ( R.id.toolbar_image2 );
        videores=new ArrayList <> (  );
        ext=new ArrayList <> (  );
        acodec=new ArrayList<>();
        vcodec=new ArrayList<>();
        filesize=new ArrayList <> ();
        formatid=new ArrayList <> (  );
        videolist.setLayoutManager ( new LinearLayoutManager ( mctx ) );
        title=v.findViewById ( R.id.textView2 );
        notificationManagerCompat=NotificationManagerCompat.from ( mctx );
        progressBar=v.findViewById ( R.id.progressBar2 );
        view=v;
        vidInfoAdapter=new AudInfoAdapter ( mctx,videores,ext ,formatid, AudioFragment.this );
        videolist.setAdapter ( vidInfoAdapter );
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        view.requestFocus ();
        initviews ( view );
    }

    private void initviews(View v){
        audInfoVM= new ViewModelProvider (AudioFragment.this).get ( AudInfoVM.class );
        audInfoVM.audioinfo.observe ( getViewLifecycleOwner(), audioInfo -> {
            this.audioInfo=audioInfo;
            this.audioFormats=audioInfo.getFormats ();
            title.setText ( audioInfo.getTitle () );
            ArrayList<VideoFormat> formats =audioInfo.getFormats ();
            for (int i=0;i<formats.size ();i++){
                VideoFormat videoFormat=formats.get ( i );
                if(videoFormat.getWidth ()==0 && videoFormat.getHeight ()==0) {
                    if (!formatid.contains ( videoFormat.getFormatId () )) {
                        formatid.add ( videoFormat.getFormatId () );
                        String size = NumberUtils.format ( videoFormat.getFilesize () );
                        videores.add ( videoFormat.getFormat () + "  -" + size );
                        ext.add ( videoFormat.getExt () );
                        acodec.add ( videoFormat.getAcodec () );
                        vcodec.add(videoFormat.getVcodec ());
                        filesize.add ( videoFormat.getFilesize () );
                    }
                }
            }
            vidInfoAdapter.notifyDataSetChanged ();
            hideKeyboard ( getActivity () );


        } );

        audInfoVM.thumbnail.observe ( getViewLifecycleOwner(), s -> {
            if (s!=null) {
                Glide.with ( mctx ).load ( audioInfo.getThumbnail () ).into ( toolbar_image );
            }else {
                Glide.with ( mctx ).load ( R.mipmap.ic_launcher_round ).into ( toolbar_image );
            }
        } );

        audInfoVM.url.observe ( getViewLifecycleOwner (),url ->{
            this.url=url;
        } );

        audInfoVM.LoadState.observe ( getViewLifecycleOwner (),loadState1 -> {
            if (loadState1==LoadState.LOADING){
                progressBar.setVisibility ( View.VISIBLE );
            }

            if (loadState1==LoadState.LOADED){
                progressBar.setElevation ( 0 );
                videolist.setElevation ( 2 );
            }
        } );
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
                try {
                    if (URLUtil.isValidUrl(query)) {
                        progressBar.setElevation ( 2 );
                        videolist.setElevation ( 0 );
                        progressBar.setVisibility ( View.VISIBLE );
                        processSearch ( query );
                    }else {
                        Context context=mctx;
                        CharSequence text="Please Enter a Valid Url";
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show ();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    
    @Override
    public void OnDownloadClick(int position) {
        try {
            startDownload ( position,url );
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace ();
        }
    }

    public boolean hideKeyboard(Activity activity) {

        if (activity!=null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService ( Activity.INPUT_METHOD_SERVICE );
            View view = activity.getCurrentFocus ();
            if (view == null) {
                view = new View ( activity );
            }
            if (imm != null) {
                imm.hideSoftInputFromWindow ( view.getWindowToken (), 0 );
                return true;
            }
        }
        return false;
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



    private void startDownload(int position,String url) throws ExecutionException, InterruptedException {

        if (isStoragePermissionGranted ()) {
            String Tag = "DownloadWmTag";
            Data.Builder inputdata = new Data.Builder ();
            inputdata.putString ( "Url",url );
            inputdata.putString ( "formatid", formatid.get ( position ) );
            inputdata.putString ( "Title", audioInfo.getTitle () );
            inputdata.putString ( "acodec",acodec.get ( position ));
            inputdata.putString ( "vcodec",vcodec.get ( position ) );
            inputdata.putLong ( "size",filesize.get ( position ) );
            inputdata.putString ( "ext",ext.get ( position ) );
            if (isWorkScheduled ( Tag )) {
                Toast.makeText ( mctx, "Download is Already Going On Let it Finish First!!", Toast.LENGTH_SHORT ).show ();
            }
            WorkManager workManager = WorkManager.getInstance ( mctx );
            OneTimeWorkRequest.Builder request = new OneTimeWorkRequest.Builder ( DownloadWM.class );
            request.addTag ( Tag );
            request.setInputData ( inputdata.build () );
            OneTimeWorkRequest request1 = request.build ();
            workManager.enqueueUniqueWork ( Tag, ExistingWorkPolicy.KEEP, request1 );

            Toast.makeText ( mctx, "Download Enqueued check Notification for progress", Toast.LENGTH_SHORT ).show ();
        }else {
            Toast.makeText ( mctx, "Grant Storage Permission Then Start Download Again", Toast.LENGTH_SHORT ).show ();
        }

    }


    private void processSearch(String url) throws InterruptedException {
        AudInfoVM audInfoVM=new ViewModelProvider(AudioFragment.this).get ( AudInfoVM.class );
        audInfoVM.loadpb ( url );
    }


    private boolean isWorkScheduled(String tag) {
        WorkManager instance = WorkManager.getInstance(mctx);
        ListenableFuture <List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public void onPause() {
        super.onPause ();
    }

    
}