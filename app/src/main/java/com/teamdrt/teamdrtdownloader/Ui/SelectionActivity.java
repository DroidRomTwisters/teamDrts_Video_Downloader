package com.teamdrt.teamdrtdownloader.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamdrt.teamdrtdownloader.R;

public class SelectionActivity extends AppCompatActivity {


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
                Intent intent1 = new Intent(SelectionActivity.this,MainActivity.class);
                intent1.putExtra ( "url",url );
                intent1.putExtra ( "action","video" );
                intent1.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent1 );
    }
    }

}