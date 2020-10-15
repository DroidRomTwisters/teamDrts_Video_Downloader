package com.teamdrt.teamdrtdownloader.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.teamdrt.teamdrtdownloader.R;

import java.util.Timer;
import java.util.TimerTask;

public class Splash_Screen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.splash_screen );

    }

    @Override
    protected void onResume() {
        super.onResume ();
        Intent intent = new Intent(this,MainActivity.class);
        SharedPreferences sharedPref = getPreferences ( Context.MODE_PRIVATE );
        String fboot=sharedPref.getString ( "fboot","true" );
        if (fboot.equals ( "true" )){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString ( "fboot","false" );
            editor.apply ();
            Timer timer = new Timer();
            timer.schedule ( new TimerTask () {
                @Override
                public void run() {
                    startActivity ( intent );
                }
            } ,3000);
        }else {
            Timer timer = new Timer();
            timer.schedule ( new TimerTask () {
                @Override
                public void run() {
                    startActivity ( intent );
                }
            } ,300);
        }

    }
}
