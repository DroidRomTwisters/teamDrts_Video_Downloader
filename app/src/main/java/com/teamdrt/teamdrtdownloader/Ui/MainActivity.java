package com.teamdrt.teamdrtdownloader.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.teamdrt.teamdrtdownloader.R;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    static Context mctx;
    NavController navController;
    private HomeFragment videoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        mctx=getApplicationContext ();
        if (savedInstanceState!=null) {
            videoFragment = (HomeFragment) getSupportFragmentManager ().getFragment ( savedInstanceState, "VideoFragment" );
        }
        bottomNavigationView = findViewById ( R.id.bottom_navigation_view );
        navController = Navigation.findNavController ( this,R.id.fragment );
        NavigationUI.setupWithNavController ( bottomNavigationView,navController);



    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState ( outState );
        getSupportFragmentManager ().putFragment ( outState,"VideoFragment",videoFragment );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController = Navigation.findNavController ( this,R.id.fragment );
        boolean navigation=NavigationUI.onNavDestinationSelected ( item,navController );
        return navigation || super.onOptionsItemSelected(item);
    }
}