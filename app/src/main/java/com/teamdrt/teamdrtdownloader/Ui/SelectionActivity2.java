package com.teamdrt.teamdrtdownloader.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.teamdrt.teamdrtdownloader.R;

public class SelectionActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_selection );
        initviews ( getIntent () );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent ( intent );
        initviews ( intent );
    }

    public void initviews(Intent intent){
        if (Intent.ACTION_SEND.equals ( intent.getAction () )){
            String url=intent.getStringExtra ( Intent.EXTRA_TEXT );
            Intent intent1 = new Intent(SelectionActivity2.this,MainActivity.class);
            intent1.putExtra ( "url",url );
            intent1.putExtra ( "action","audio" );
            intent1.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent1 );
        }
    }
}