package com.teamdrt.teamdrtdownloader.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamdrt.teamdrtdownloader.R;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    static Context mctx;

    private Fragment fragment1=new HomeFragment ();
    private Fragment fragment2=new VideoFragment ();
    private Fragment fragment3=new AudioFragment ();
    private Fragment fragment4=new DownloadFragment ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        mctx=getApplicationContext ();
        bottomNavigationView = findViewById ( R.id.bottom_navigation_view );

        //getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment,fragment4 ).hide ( fragment4 ).
        NavController navController=Navigation.findNavController ( this,R.id.fragment );
        NavigationUI.setupWithNavController ( bottomNavigationView,navController );



    }


}