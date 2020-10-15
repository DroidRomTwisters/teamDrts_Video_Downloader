package com.teamdrt.teamdrtdownloader.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamdrt.teamdrtdownloader.R;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    BottomNavigationView bottomNavigationView;
    static Context mctx;

     final Fragment fragment1=new HomeFragment ();
     final Fragment fragment2=new VideoFragment ();
     final Fragment fragment3=new AudioFragment ();
     final Fragment fragment4=new DownloadFragment ();

     Fragment active =fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        mctx=getApplicationContext ();
        String name=getIntent ().getStringExtra ( "name" );
        bottomNavigationView = findViewById ( R.id.bottom_navigation_view );
        SharedPreferences sharedPref = getPreferences ( Context.MODE_PRIVATE );
        String active1 = sharedPref.getString ( "active", null );

        if (name==null) {
            if (active1 != null) {
                if (active1.startsWith ( "HomeFragment" )) {
                    active = fragment1;
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment4, "4" ).hide ( fragment4 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment3, "3" ).hide ( fragment3 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment2, "2" ).hide ( fragment2 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment1, "1" ).show ( fragment1 ).commit ();
                    bottomNavigationView.setSelectedItemId ( R.id.homeFragment );
                } else if (active1.startsWith ( "VideoFragment" )) {
                    active = fragment2;
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment4, "4" ).hide ( fragment4 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment3, "3" ).hide ( fragment3 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment2, "2" ).show ( fragment2 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment1, "1" ).hide ( fragment1 ).commit ();
                    bottomNavigationView.setSelectedItemId ( R.id.videoFragment );
                } else if (active1.startsWith ( "AudioFragment" )) {
                    active = fragment3;
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment4, "4" ).hide ( fragment4 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment3, "3" ).show ( fragment3 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment2, "2" ).hide ( fragment2 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment1, "1" ).hide ( fragment1 ).commit ();
                    bottomNavigationView.setSelectedItemId ( R.id.audioFragment );
                } else if (active1.startsWith ( "DownloadFragment" )) {
                    active = fragment4;
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment4, "4" ).show ( fragment4 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment3, "3" ).hide ( fragment3 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment2, "2" ).hide ( fragment2 ).commit ();
                    getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment1, "1" ).hide ( fragment1 ).commit ();
                    bottomNavigationView.setSelectedItemId ( R.id.downloadFragment );
                }

            } else {
                getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment4, "4" ).hide ( fragment4 ).commit ();
                getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment3, "3" ).hide ( fragment3 ).commit ();
                getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment2, "2" ).hide ( fragment2 ).commit ();
                getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment1, "1" ).show ( fragment1 ).commit ();
                addtosp ( fragment1 );

            }
        }else {
            getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment4, "4" ).show ( fragment4 ).commit ();
            getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment3, "3" ).hide ( fragment3 ).commit ();
            getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment2, "2" ).hide ( fragment2 ).commit ();
            getSupportFragmentManager ().beginTransaction ().add ( R.id.fragment, fragment1, "1" ).hide ( fragment1 ).commit ();
            bottomNavigationView.setSelectedItemId ( R.id.downloadFragment );
            active = fragment4;
            addtosp ( active );
        }

        bottomNavigationView.setOnNavigationItemSelectedListener ( item -> {
            switch (item.getItemId ()) {
                case R.id.homeFragment:
                    getSupportFragmentManager ().beginTransaction ().hide ( active ).show ( fragment1 ).commit ();
                    active = fragment1;
                    addtosp ( active );
                    return true;
                case R.id.videoFragment:
                    getSupportFragmentManager ().beginTransaction ().hide ( active ).show ( fragment2 ).commit ();
                    active = fragment2;
                    addtosp ( active );
                    return true;
                case R.id.audioFragment:
                    getSupportFragmentManager ().beginTransaction ().hide ( active ).show ( fragment3 ).commit ();
                    active = fragment3;
                    addtosp ( active );
                    return true;
                case R.id.downloadFragment:
                    getSupportFragmentManager ().beginTransaction ().hide ( active ).show ( fragment4 ).commit ();
                    active = fragment4;
                    addtosp ( active );
                    return true;
            }
            return false;
        } );
    }

    private void addtosp(Fragment fragment){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString ( "active",fragment.toString () );
        editor.apply ();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged ( newConfig );
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText ( mctx, "device rotated to landscape", Toast.LENGTH_SHORT ).show ();
        }else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText ( mctx, "device rotated to portrait", Toast.LENGTH_SHORT ).show ();

        }
    }
}