package com.klhk.whalecomp.WalletFragment.StratergyScreens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klhk.whalecomp.DocFragment.DocActivity.Desc2Activity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;

public class AutoCompoundActivity extends AppCompatActivity {
    ImageView backButton, pick_chain;
    RelativeLayout connectButton,addressActivated;
    TextView text2,text1,addressText;

    final Handler ha=new Handler();
    Runnable myRunnable;
    AlertDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_compound);
        connectButton = findViewById(R.id.connectButton);
        pick_chain = findViewById(R.id.pick_chain);
        addressText = findViewById(R.id.addressText);

        backButton = findViewById(R.id.backButton);
        text2 = findViewById(R.id.text2);
        text1 = findViewById(R.id.text1);

        TextPaint paint = text1.getPaint();
        float width = paint.measureText(text1.getText().toString())-10;

        Shader textShader = new LinearGradient(0, 0, width, text1.getTextSize(),
                new int[]{
                        Color.parseColor("#4BD1E6"),
                        Color.parseColor("#51EACB"),
                }, null, Shader.TileMode.CLAMP);
        text1.getPaint().setShader(textShader);

        textShader = new LinearGradient(0, 0, width, text1.getTextSize(),
                new int[]{
                        Color.parseColor("#FBA12B"),
                        Color.parseColor("#F4CD2E"),
                }, null, Shader.TileMode.CLAMP);
        text2.getPaint().setShader(textShader);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addressActivated = findViewById(R.id.addressActivated);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AutoCompoundActivity.this,R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.pick_wallet,null);
                ImageView closeButton = v.findViewById(R.id.closeButton);
                ImageView walletConnectButton = v.findViewById(R.id.walletConnectButton);
                LinearLayout layoutWallet = v.findViewById(R.id.layoutWallet);
                WebView webView = v.findViewById(R.id.webPageWalletConnect);
                builder.setCancelable(false);

                builder.setView(v);
                dialog1 = builder.show();


                walletConnectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutWallet.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        webView.clearCache(true);

                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.getSettings().setLoadsImagesAutomatically(true);
                        webView.getSettings().setDomStorageEnabled(true);
                        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                        webView.loadUrl("https://whalecomp.com/");


                        webView.setWebViewClient(new WebViewClient(){

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                Intent intent;
                                Log.e("URL",url);

                                if (url.contains("wc:")) {
                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    startActivity(intent);

                                    return true;
                                }
                                return super.shouldOverrideUrlLoading(view, url);
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);

                                myRunnable =new Runnable() {

                                    @Override
                                    public void run() {

                                        webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementById('root1').innerHTML);");

                                        ha.postDelayed(this, 2500);
                                    }
                                };

                                ha.postDelayed(myRunnable, 2500);


                            }

                            @Override
                            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                                super.onFormResubmission(view, dontResend, resend);
                            }
                        });


                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.cancel();
                    }
                });
            }
        });

        addressActivated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AutoCompoundActivity.this,R.style.CustomDialog);

                View v1 = getLayoutInflater().inflate(R.layout.address_alert,null);
                ImageView closeButton = v1.findViewById(R.id.closeButton);
                builder.setCancelable(false);
                TextView addressText1 = v1.findViewById(R.id.addressText);
                addressText1.setText(Preference.getInstance().returnValue("userWalletAddress"));

                ImageView copyButton = v1.findViewById(R.id.copyButton);
                ImageView logoutButton = v1.findViewById(R.id.logoutButton);

                builder.setView(v1);
                AlertDialog dialog = builder.show();

                copyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", Preference.getInstance().returnValue("userWalletAddress"));
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(AutoCompoundActivity.this, "Your Wallet Address copied", Toast.LENGTH_SHORT).show();
                    }
                });

                logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Preference.getInstance().clearPreferences();
                        dialog.cancel();

                        setAddress();
                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });

        pick_chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AutoCompoundActivity.this,R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.pick_chain,null);
                ImageView closeButton = v.findViewById(R.id.closeButton);
                builder.setCancelable(false);

                builder.setView(v);
                AlertDialog dialog = builder.show();

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    public void setAddress(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(Preference.getInstance().returnValue("userWalletAddress").contains("0x")){
                    connectButton.setVisibility(View.GONE);
                    addressActivated.setVisibility(View.VISIBLE);
                    String str = Preference.getInstance().returnValue("userWalletAddress");
                    String str1 = str.substring(0, 6) + ".."+ str.substring(str.length()-4, str.length());
                    addressText.setText(str1);
                }else{
                    connectButton.setVisibility(View.VISIBLE);
                    addressActivated.setVisibility(View.GONE);
                }
            }
        });

    }
    public class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
            Log.e("HTNM",html);

            if(html.contains("0x")){
                ha.removeCallbacks(myRunnable);
                Preference.getInstance().writePreference("userWalletAddress",html);
                dialog1.cancel();
                setAddress();


            }
        }
    }
}