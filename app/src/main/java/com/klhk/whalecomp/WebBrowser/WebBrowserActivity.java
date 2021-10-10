package com.klhk.whalecomp.WebBrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.R;

import static com.klhk.whalecomp.utilities.Constants.URL_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class WebBrowserActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);
        webView = findViewById(R.id.webView);

        String url = getIntent().getStringExtra(URL_ADDRESS);
        webView.clearCache(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent;
                Log.e("URL",url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }
        });
    }
}