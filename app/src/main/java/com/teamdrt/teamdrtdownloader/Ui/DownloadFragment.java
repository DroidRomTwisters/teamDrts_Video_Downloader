package com.teamdrt.teamdrtdownloader.Ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.teamdrt.teamdrtdownloader.Adapters.DownloadsAdapter;
import com.teamdrt.teamdrtdownloader.Databases.Download;
import com.teamdrt.teamdrtdownloader.R;
import com.teamdrt.teamdrtdownloader.ViewModels.DownloadsVM;
import com.teamdrt.teamdrtdownloader.ViewModels.VidInfoVM;

import java.util.ArrayList;
import java.util.List;

import static com.teamdrt.teamdrtdownloader.Ui.MainActivity.mctx;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFragment extends Fragment {

    private TextView nod;
    private List<Download> downloads;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView download_list;
    private DownloadsAdapter adapter;

    private String mParam1;
    private String mParam2;

    public DownloadFragment() {
        // Required empty public constructor
    }

    public static DownloadFragment newInstance(String param1, String param2) {
        DownloadFragment fragment = new DownloadFragment ();
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
        setRetainInstance ( true );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        download_list=view.findViewById ( R.id.download_list );
        download_list.setLayoutManager ( new LinearLayoutManager ( mctx ) );
        downloads=new ArrayList<>();
        adapter=new DownloadsAdapter(downloads);
        adapter.setHasStableIds ( true );
        download_list.setAdapter(adapter);
        nod=view.findViewById(R.id.textView5);
        init ();
        return view;

    }

    public void init(){
        DownloadsVM vm= new ViewModelProvider (DownloadFragment.this).get ( DownloadsVM.class );
        vm.getAllDownloads ( mctx );
        vm.allDownloads.observe ( getViewLifecycleOwner (), downloads -> {
            if (downloads.size ()!=0){
                nod.setVisibility ( View.GONE );
                download_list.setVisibility ( View.VISIBLE );
            }else {
                nod.setVisibility ( View.VISIBLE );
                download_list.setVisibility ( View.GONE);
            }
            adapter.addItems ( downloads );
        } );
    }
}