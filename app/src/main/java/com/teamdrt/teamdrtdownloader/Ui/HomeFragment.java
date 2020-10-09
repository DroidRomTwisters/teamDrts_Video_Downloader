package com.teamdrt.teamdrtdownloader.Ui;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.teamdrt.teamdrtdownloader.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
   WebView wv;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Bundle WebViewState;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment ();
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
        View v = inflater.inflate( R.layout.fragment_home, container, false);
        wv= v.findViewById (R.id.home_wv);
        if (savedInstanceState !=null){
            wv.restoreState ( savedInstanceState );
        }else {
            WebSettings webSettings = wv.getSettings ();
            webSettings.setLoadsImagesAutomatically ( true );
            webSettings.setJavaScriptEnabled ( true );
            webSettings.setAppCacheEnabled ( true );
            webSettings.setAllowFileAccess ( true );
            webSettings.setJavaScriptCanOpenWindowsAutomatically ( true );

            wv.setScrollBarStyle ( View.SCROLLBARS_INSIDE_OVERLAY );
            wv.loadUrl ( "http://www.youtube.com/" );

            wv.setWebChromeClient ( new MyWebChromeClient ());
            wv.setWebViewClient ( new WebViewClient () {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith ( "http:" ) || url.startsWith ( "https:" )) {
                        return false;
                    } else {
                        try {
                            Intent intent = new Intent ( Intent.ACTION_VIEW, Uri.parse ( url ) );
                            startActivity ( intent );
                            return true;
                        }
                        catch (ActivityNotFoundException e){
                            Toast.makeText ( MainActivity.mctx, "No Application can perform this Action", Toast.LENGTH_SHORT ).show ();
                        }

                    }
                    return false;
                }
            } );

            wv.setOnKeyListener ( new View.OnKeyListener () {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction ()==KeyEvent.ACTION_DOWN){
                        switch (keyCode){
                            case KeyEvent.KEYCODE_BACK:
                                if (wv.canGoBack ()){
                                    wv.goBack ();
                                    return true;
                                }
                                break;
                        }
                    }
                    return false;
                }
            } );
        }

        wv.setOnLongClickListener ( new View.OnLongClickListener () {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult hitTestResult = wv.getHitTestResult();
                ClipboardManager clipboard = (ClipboardManager) MainActivity.mctx.getSystemService( Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", hitTestResult.getExtra ());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText ( MainActivity.mctx, "Link Copied to Clipboard", Toast.LENGTH_SHORT ).show ();
                return true;
            }
        } );
        return v;
    }


    @Override
    public void onPause() {
        super.onPause ();

    }

    @Override
    public void onResume() {
        super.onResume ();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (wv!=null) {
            wv.saveState ( outState );
        }
        super.onSaveInstanceState ( outState );
    }

    private class MyWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyWebChromeClient(){};

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(MainActivity.mctx.getResources(), 2130837573);
        }

        @Override
        public void onHideCustomView() {
            if (getActivity ()!=null) {
                ((FrameLayout) getActivity ().getWindow ().getDecorView ()).removeView ( this.mCustomView );
                this.mCustomView = null;
                getActivity ().getWindow ().getDecorView ().setSystemUiVisibility ( this.mOriginalSystemUiVisibility );
                getActivity ().setRequestedOrientation ( this.mOriginalOrientation );
                this.mCustomViewCallback.onCustomViewHidden ();
                this.mCustomViewCallback = null;
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = view;
            this.mOriginalSystemUiVisibility = getActivity ().getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getActivity ().getRequestedOrientation();
            this.mCustomViewCallback = callback;
            ((FrameLayout)getActivity ().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
           getActivity ().getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}