package com.klhk.whalecomp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.usage.UsageStats;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.sshadkany.neo;
import com.google.android.material.navigation.NavigationView;
import com.klhk.whalecomp.AlertFragment.AlertFragment;
import com.klhk.whalecomp.AlertFragment.AlertSetupActivity;
import com.klhk.whalecomp.DocFragment.DocActivity.Desc2Activity;
import com.klhk.whalecomp.DocFragment.DocFragment;
import com.klhk.whalecomp.GiftFragment.GiftFragment;
import com.klhk.whalecomp.GlobeFragment.GlobeFragment;
import com.klhk.whalecomp.SettingFragment.AccountQr;
import com.klhk.whalecomp.SettingFragment.PickAddressActivity;
import com.klhk.whalecomp.SettingFragment.SettingFragment;
import com.klhk.whalecomp.SwapFragmnet.SwapFragment;
import com.klhk.whalecomp.WalletFragment.WalletFragment;
import com.klhk.whalecomp.WebBrowser.WebBrowserActivity;
import com.klhk.whalecomp.WhaleTrust.CreateMnemonicActivity;
import com.klhk.whalecomp.WhaleTrust.ImportAddressActivity;
import com.klhk.whalecomp.WhaleTrust.ScanActivity;
import com.klhk.whalecomp.WhaleTrust.SwapWhaleTrustActivity;
import com.klhk.whalecomp.WhaleTrust.WhaleTrustFragment;
import com.klhk.whalecomp.homefragment.HomeFragment;

import com.klhk.whalecomp.utilities.AppController;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.klhk.whalecomp.utilities.LockOut;
import com.klhk.whalecomp.utilities.MyJobService;
import com.klhk.whalecomp.utilities.MyService;
import com.klhk.whalecomp.utilities.Strategy001;
import com.klhk.whalecomp.utilities.service.CompounderService;
import com.klhk.whalecomp.utilities.service.Restarter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.klhk.whalecomp.AlertFragment.AlertFragment.RESULT_FINISHED_ACTIVITY;
import static com.klhk.whalecomp.utilities.Constants.LAST_DATE_MILLI;
import static com.klhk.whalecomp.utilities.Constants.METAMASK;
import static com.klhk.whalecomp.utilities.Constants.TRUSTWALLET;
import static com.klhk.whalecomp.utilities.Constants.URL_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_ID;
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_SLIPPAGE;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_TIMEOUT;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.Strategy001.getLogs;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RelativeLayout bannerLayout,addressActivatedLayout;
    DrawerLayout drawerLayout;
    NavigationView nav_view;

    ImageView giftIcon,docIcon,walletIcon,swapIcon,homeIcon,globeIcon,alarmIcon;
    TextView giftText,docText,walletText, swapText, homeText, globeText,alarmText,addressText,whaleTrustText,settingText;
    ImageView whaleTrustIcon,settingIcon;

    neo openDrawer,switchThemeButton,pick_chain,connectButton,addressActivated,addressActivatedLogo,closeDrawer;

    neo giftLayout,docLayout,walletLayout,swapLayout,homeLayout,globeLayout,alarmLayout,settingLayout,whaleTrustLayout;

    ScrollView navScrollBar;


    final Handler ha=new Handler();
    Runnable myRunnable;
    AlertDialog dialog1,dialog2;

    String FRAGMENT_TAG_WHALECOMP="";
    private static final int PERMISSION_REQUESTS = 1;
    Fragment fragmentSelected=null;
    
    private CompounderService compounderService;
    Intent mServiceIntent;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
//        long millis = System.currentTimeMillis();
//        Preference.getInstance().writePreference(LAST_DATE_MILLI,String.valueOf(millis));
        super.onPause();
    }
    
    public void initJobscheduler(){
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("hi","hi");
        JobInfo.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder = new JobInfo.Builder(101,componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(bundle)
                    .setPeriodic(15000)
                    .setPersisted(true);
        }else{
            builder = new JobInfo.Builder(101,componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(bundle)
                    .setPeriodic(15000)
                    .setPersisted(true);
        }
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(builder.build());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        connectButton = findViewById(R.id.connectButton);
        addressActivated = findViewById(R.id.addressActivated);
        drawerLayout = findViewById(R.id.drawerLayout);
        pick_chain = findViewById(R.id.pick_chain);
        addressText = findViewById(R.id.addressText);
        addressActivatedLayout = findViewById(R.id.addressActivatedLayout);
        Preference.getInstance().Initialize(this);
        System.loadLibrary("TrustWalletCore");

        openDrawer = findViewById(R.id.openDrawer);
        nav_view = findViewById(R.id.nav_view);
        View headerView =nav_view.getHeaderView(0);
        closeDrawer = headerView.findViewById(R.id.closeDrawer);

        headerView.setVerticalScrollBarEnabled(false);

        whaleTrustLayout = headerView.findViewById(R.id.whaleTrustLayout);
        whaleTrustIcon = headerView.findViewById(R.id.whaleTrustIcon);
        whaleTrustText = headerView.findViewById(R.id.whaleTrustText);

        settingIcon = headerView.findViewById(R.id.settingIcon);
        settingLayout = headerView.findViewById(R.id.settingLayout);
        settingText = headerView.findViewById(R.id.settingText);

        giftLayout = headerView.findViewById(R.id.giftLayout);
        giftIcon = headerView.findViewById(R.id.giftIcon);
        giftText = headerView.findViewById(R.id.giftText);

        docLayout = headerView.findViewById(R.id.docLayout);
        docIcon = headerView.findViewById(R.id.docIcon);
        docText = headerView.findViewById(R.id.docText);

        walletLayout= headerView.findViewById(R.id.walletLayout);
        walletIcon = headerView.findViewById(R.id.walletIcon);
        walletText = headerView.findViewById(R.id.walletText);

        swapLayout= headerView.findViewById(R.id.swapLayout);
        swapIcon = headerView.findViewById(R.id.swapIcon);
        swapText = headerView.findViewById(R.id.swapText);

        homeLayout= headerView.findViewById(R.id.homeLayout);
        homeIcon = headerView.findViewById(R.id.homeIcon);
        homeText = headerView.findViewById(R.id.homeText);

        globeLayout= headerView.findViewById(R.id.globeLayout);
        globeIcon= headerView.findViewById(R.id.globeIcon);
        globeText= headerView.findViewById(R.id.globeText);

        alarmLayout=headerView.findViewById(R.id.alarmLayout);
        alarmIcon=headerView.findViewById(R.id.alarmIcon);
        alarmText=headerView.findViewById(R.id.alarmText);
        addressActivatedLogo = findViewById(R.id.addressActivatedLogo);
        switchThemeButton = findViewById(R.id.switchThemeButton);

//        FunctionCall.getBlock("0x6c3db194af864162ede0273df09bde8936a3196a73a45b38de35b45e82b3b32a");
//        Log.e("amount",String.valueOf(new BigInteger("9999")));

        LockOut.checkTimeout(MainActivity.this);
        switchThemeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.switchThemeButton);
                final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                try{
                    if (switchThemeButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                switchThemeButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                switchThemeButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(50);
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });

        connectButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.connectButton);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (connectButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                connectButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                connectButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(50);
                                setConnectButton();
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });

        addressActivated.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.addressActivated);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                ViewGroup viewGroupC2 = findViewById(R.id.addressActivatedLogo);
                final ImageView imageView = (ImageView) viewGroupC2.getChildAt(0);
                try{
                    if (addressActivated.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                addressActivated.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                addressActivatedLogo.setStyle(neo.small_inner_shadow);
                                imageView.setScaleX(textView.getScaleX() * 0.95f);
                                imageView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                addressActivated.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                addressActivatedLogo.setStyle(neo.drop_shadow);
                                imageView.setScaleX(1);
                                imageView.setScaleY(1);
                                Thread.sleep(50);
                                connectedButtonSelected();
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        pick_chain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v1, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.pick_chain);
                final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                try{
                    if (pick_chain.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                pick_chain.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                pick_chain.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(50);
                                setPickChain();
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        closeDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.closeDrawer);
                final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                try{
                    if (closeDrawer.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                closeDrawer.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                closeDrawer.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(50);
                                drawerLayout.closeDrawer(Gravity.LEFT);
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });


        openDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.openDrawer);
                final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                try{
                    if (openDrawer.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                openDrawer.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                openDrawer.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(50);
                                drawerLayout.openDrawer(Gravity.LEFT);
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (fragmentSelected != null) {
                    loadFragment(fragmentSelected,FRAGMENT_TAG_WHALECOMP);

                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (fragmentSelected != null) {
                    loadFragment(fragmentSelected,FRAGMENT_TAG_WHALECOMP);

                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (fragmentSelected != null) {
                    loadFragment(fragmentSelected,FRAGMENT_TAG_WHALECOMP);

                }
            }
        });

        setAddress();
        onMenuItemsClicked();
        //onClickMenuItems();
        FRAGMENT_TAG_WHALECOMP="HOME_SCREEN_FRAGMNET";
        loadFragment(new HomeFragment(),FRAGMENT_TAG_WHALECOMP);

    }

    public void setPickChain(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

        View v = getLayoutInflater().inflate(R.layout.pick_chain,null);
        neo closeButton = v.findViewById(R.id.closeButton);
        builder.setCancelable(false);

        builder.setView(v);
        AlertDialog dialog = builder.show();

        ViewGroup viewGroupC2 = v.findViewById(R.id.closeButton);
        final ImageView textView1 = (ImageView) viewGroupC2.getChildAt(0);
        roundButtonPressed(closeButton,textView1, MainActivity.this,dialog);
    }

    public void connectedButtonSelected(){
        if(Preference.getInstance().returnValue(WALLET_SELECTED).equalsIgnoreCase(WHALETRUST)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

            View v1 = getLayoutInflater().inflate(R.layout.address_alert_whaletrust,null);
            ImageView closeButton = v1.findViewById(R.id.closeButton);
            builder.setCancelable(false);
            TextView addressText1 = v1.findViewById(R.id.addressText);
            addressText1.setText(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
            ImageView switchButton = v1.findViewById(R.id.switchButton);
            ImageView copyButton = v1.findViewById(R.id.copyButton);
            ImageView logoutButton = v1.findViewById(R.id.logoutButton);
            ImageView viewAddressButton = v1.findViewById(R.id.viewAddressButton);
            builder.setView(v1);
            AlertDialog dialog = builder.show();

            switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, PickAddressActivity.class));
                    dialog.cancel();
                }
            });
            viewAddressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://bscscan.com/address/"+Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
                    Intent intent = new Intent(MainActivity.this, WebBrowserActivity.class);
                    intent.putExtra(URL_ADDRESS,url);
                    startActivity(intent);
                }
            });

            copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Your Wallet Address copied", Toast.LENGTH_SHORT).show();
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Preference.getInstance().clearPreferences();
                    dialog.cancel();
                    clearAddressSelected();

                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

            View v1 = getLayoutInflater().inflate(R.layout.address_alert,null);
            ImageView closeButton = v1.findViewById(R.id.closeButton);
            builder.setCancelable(false);
            TextView addressText1 = v1.findViewById(R.id.addressText);
            addressText1.setText(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));

            ImageView copyButton = v1.findViewById(R.id.copyButton);
            ImageView logoutButton = v1.findViewById(R.id.logoutButton);
            ImageView viewAddressButton = v1.findViewById(R.id.viewAddressButton);
            builder.setView(v1);
            AlertDialog dialog = builder.show();

            viewAddressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://bscscan.com/address/"+Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
                    Intent intent = new Intent(MainActivity.this, WebBrowserActivity.class);
                    intent.putExtra(URL_ADDRESS,url);
                    startActivity(intent);
                }
            });

            copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", Preference.getInstance().returnValue("userWalletAddress"));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Your Wallet Address copied", Toast.LENGTH_SHORT).show();
                }
            });

//                    logoutButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            //Preference.getInstance().clearPreferences();
//                            dialog.cancel();
//                            clearAddressSelected();
//
//                        }
//                    });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    disconnectFromExtAPP(true);
                    dialog.cancel();
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        }
    }

    public void setConnectButton(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

        View v = getLayoutInflater().inflate(R.layout.pick_wallet,null);
        ImageView closeButton = v.findViewById(R.id.closeButton);
        neo walletConnectButton = v.findViewById(R.id.walletConnectButton);
        neo whaleTrustButton = v.findViewById(R.id.whaleTrustButton);
        neo metamaskButton = v.findViewById(R.id.metamaskButton);
        LinearLayout layoutWallet = v.findViewById(R.id.layoutWallet);

        bannerLayout = v.findViewById(R.id.bannerLayout);
        builder.setCancelable(false);

        builder.setView(v);
        dialog1 = builder.show();



        whaleTrustButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.whaleTrustButton);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                try{
                    if (whaleTrustButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                whaleTrustButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.98f);
                                textView.setScaleY(textView.getScaleY() * 0.98f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                whaleTrustButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                if(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST).isEmpty()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

                                    View v1 = getLayoutInflater().inflate(R.layout.pick_add_address_whaletrust_new,null);
                                    ImageView closeButton = v1.findViewById(R.id.closeButton);
                                    neo newButton = v1.findViewById(R.id.newButton);
                                    neo importButton = v1.findViewById(R.id.importButton);
                                    builder.setCancelable(false);

                                    builder.setView(v1);
                                    AlertDialog dialog = builder.show();

                                    newButton.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            try{
                                                ViewGroup viewGroupC1 = v.findViewById(R.id.newButton);
                                                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                                                if (newButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                    switch (event.getAction()) {
                                                        case MotionEvent.ACTION_DOWN:

                                                            newButton.setStyle(neo.small_inner_shadow);
                                                            textView.setScaleX(textView.getScaleX() * 0.96f);
                                                            textView.setScaleY(textView.getScaleY() * 0.96f);
                                                            return true; // if you want to handle the touch event
                                                        case MotionEvent.ACTION_UP:
                                                        case MotionEvent.ACTION_CANCEL:
                                                            // RELEASED
                                                            newButton.setStyle(neo.drop_shadow);
                                                            textView.setScaleX(1);
                                                            textView.setScaleY(1);
                                                            Thread.sleep(60);
                                                            dialog1.cancel();
                                                            dialog.cancel();
                                                            startActivity(new Intent(MainActivity.this, CreateMnemonicActivity.class));
                                                            return true; // if you want to handle the touch event
                                                    }
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                return false;
                                            }
                                            return false;
                                        }
                                    });

                                    importButton.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            try{
                                                ViewGroup viewGroupC1 = v.findViewById(R.id.importButton);
                                                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                                                if (importButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                    switch (event.getAction()) {
                                                        case MotionEvent.ACTION_DOWN:

                                                            importButton.setStyle(neo.small_inner_shadow);
                                                            textView.setScaleX(textView.getScaleX() * 0.96f);
                                                            textView.setScaleY(textView.getScaleY() * 0.96f);
                                                            return true; // if you want to handle the touch event
                                                        case MotionEvent.ACTION_UP:
                                                        case MotionEvent.ACTION_CANCEL:
                                                            // RELEASED
                                                            importButton.setStyle(neo.drop_shadow);
                                                            textView.setScaleX(1);
                                                            textView.setScaleY(1);
                                                            Thread.sleep(60);
                                                            dialog1.cancel();
                                                            dialog.cancel();
                                                            startActivity(new Intent(MainActivity.this, ImportAddressActivity.class));
                                                            return true; // if you want to handle the touch event
                                                    }
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                return false;
                                            }
                                            return false;
                                        }
                                    });

                                    closeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                        }
                                    });

                                }else{
                                    Preference.getInstance().writePreference(WALLET_SELECTED,WHALETRUST);
                                    Preference.getInstance().writePreference(WALLET_SELECTED_ADDRESS,Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED));
                                    setAddress();
                                    dialog1.cancel();
                                }
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });

        metamaskButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.metamaskButton);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                try{
                    if (metamaskButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                metamaskButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.98f);
                                textView.setScaleY(textView.getScaleY() * 0.98f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                metamaskButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                dialog1.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//
                                View v1 = getLayoutInflater().inflate(R.layout.webview_layout,null);
                                WebView webView = v1.findViewById(R.id.webPageWalletConnect);
                                //bannerLayout.setVisibility(View.GONE);
                                //webView.setVisibility(View.VISIBLE);
                                webView.clearCache(true);

                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.getSettings().setLoadsImagesAutomatically(true);
                                webView.getSettings().setDomStorageEnabled(true);
                                webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                                webView.loadUrl("https://whalecomp.com/getAddress");


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
                                                Preference.getInstance().writePreference(WALLET_SELECTED,METAMASK);
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
                                builder.setView(v1);
                                dialog2 = builder.show();
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });

        walletConnectButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.walletConnectButton);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                try{
                    if (walletConnectButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                walletConnectButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.98f);
                                textView.setScaleY(textView.getScaleY() * 0.98f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                walletConnectButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                dialog1.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                                View v1 = getLayoutInflater().inflate(R.layout.webview_layout,null);
                                WebView webView = v1.findViewById(R.id.webPageWalletConnect);

                                webView.clearCache(true);

                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.getSettings().setLoadsImagesAutomatically(true);
                                webView.getSettings().setDomStorageEnabled(true);
                                webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                                webView.loadUrl("https://whalecomp.com/getAddress");


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
                                                Preference.getInstance().writePreference(WALLET_SELECTED,TRUSTWALLET);
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

                                builder.setView(v1);
                                dialog2 = builder.show();
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();
            }
        });
    }

    public void disconnectFromExtAPP(boolean clearAddr) {
        String url = "https://whalecomp.com/disconnectAddress";

        Log.e("URL",url);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

        View v1 = getLayoutInflater().inflate(R.layout.progress_dialog,null);
        RotateAnimation rotate = new RotateAnimation(0, 3600, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());

        ImageView image= v1.findViewById(R.id.progressCircle);

        image.startAnimation(rotate);
        WebView webView = v1.findViewById(R.id.webViewLayout);
        builder.setCancelable(false);

        builder.setView(v1);
        AlertDialog dialog1 = builder.show();

        webView.clearCache(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        //webView.addJavascriptInterface(new MainActivity.MyJavaScriptInterface(), "HTMLOUT");
        webView.loadUrl(url);


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
                if(clearAddr){
                    clearAddressSelected();
                }
                dialog1.cancel();

            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }
        });
        //Preference.getInstance().clearPreferences();


        setAddress();





    }



    public void clearAddressSelected(){
        Preference.getInstance().writePreference(WALLET_SELECTED_ADDRESS,"");
        Preference.getInstance().writePreference(WALLET_SELECTED,"");
        setAddress();
        Fragment frg = null;
        frg = getSupportFragmentManager().findFragmentByTag("WHALETRUST_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){

            homeLayout.performClick();

        }
    }
    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    public void clickHomeScreen(){
        homeLayout.performClick();
    }

    public void clickStrgsScreen(){
        docLayout.performClick();
    }

    public void clickMyStrgsScreen(){
        walletLayout.performClick();
    }

    public void clickWhaleTrustScreen(){
        whaleTrustLayout.performClick();
    }



    @Override
    protected void onResume() {
        //LockOut.checkTimeout(MainActivity.this);

        Log.e("Fragment refreshed","true");
        Fragment frg = null;
        frg = getSupportFragmentManager().findFragmentByTag("ALARM_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true1");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        frg = getSupportFragmentManager().findFragmentByTag("WHALETRUST_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true2");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        frg = getSupportFragmentManager().findFragmentByTag("SETTING_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true3");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        frg = getSupportFragmentManager().findFragmentByTag("HOME_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true3");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        frg = getSupportFragmentManager().findFragmentByTag("SWAP_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true3");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        frg = getSupportFragmentManager().findFragmentByTag("WALLET_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true3");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        frg = getSupportFragmentManager().findFragmentByTag("DOC_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true3");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        frg = getSupportFragmentManager().findFragmentByTag("ALARM_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            Log.e("Fragment refreshed","true3");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
        setAddress();

        super.onResume();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001&&resultCode==RESULT_OK){
            Log.e("Reached","activityResult1");
            setAddress();
            Fragment frg = null;
            frg = getSupportFragmentManager().findFragmentByTag("WHALETRUST_SCREEN_FRAGMNET");
            if(frg !=null && frg.isVisible()){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        }
        if(requestCode==1001&&resultCode!=RESULT_OK){
            Log.e("Reached","activityResult2");
            clickHomeScreen();
        }

    }

    public void buttonPressedEffect(ImageView button){
        button.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                button.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
            }
        }, 130);
    }

    int oldSelected=1;
    ViewGroup viewGroup2;

    public boolean setLayout(MotionEvent event,LinearLayout textView, neo neoLayout, int number){
        TextView selectedText =(TextView) textView.getChildAt(1);
        ImageView selectedImage =(ImageView) textView.getChildAt(0);
        if(oldSelected==1){
            viewGroup2= findViewById(R.id.homeLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            homeLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            homeLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="HOME_SCREEN_FRAGMNET";
            fragmentSelected = new HomeFragment();
        }else if(oldSelected ==2){
            viewGroup2= findViewById(R.id.globeLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            globeLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            globeLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="GLOBE_SCREEN_FRAGMNET";
            fragmentSelected = new GlobeFragment();
        }else if(oldSelected ==3){
            viewGroup2= findViewById(R.id.alarmLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            alarmLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            alarmLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="ALARM_SCREEN_FRAGMNET";
            fragmentSelected = new AlertFragment();
        }else if(oldSelected ==4){
            viewGroup2= findViewById(R.id.settingLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            settingLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            settingLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="SETTING_SCREEN_FRAGMNET";
            fragmentSelected = new SettingFragment();
        }else if(oldSelected ==5){
            viewGroup2= findViewById(R.id.whaleTrustLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            whaleTrustLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            whaleTrustLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="WHALETRUST_SCREEN_FRAGMNET";
            fragmentSelected = new WhaleTrustFragment();
        }else if(oldSelected ==6){
            viewGroup2= findViewById(R.id.giftLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            giftLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            giftLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="GIFT_SCREEN_FRAGMNET";
            fragmentSelected = new GiftFragment();
        }else if(oldSelected ==7){
            viewGroup2= findViewById(R.id.docLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            docLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            docLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="DOC_SCREEN_FRAGMNET";
            fragmentSelected = new DocFragment();
        }else if(oldSelected ==8){
            viewGroup2= findViewById(R.id.walletLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            walletLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            walletLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="WALLET_SCREEN_FRAGMNET";
            fragmentSelected = new WalletFragment();
        }else if(oldSelected ==9){
            viewGroup2= findViewById(R.id.swapLayout);
            LinearLayout layout = (LinearLayout) viewGroup2.getChildAt(0);
            swapLayout.setLight_color(getResources().getColor(R.color.light_shadow));
            swapLayout.setDark_color(getResources().getColor(R.color.dark_shadow));
            TextView text = (TextView) layout.getChildAt(1);
            text.setTextColor(getResources().getColor(R.color.text_color));
            ImageView image =(ImageView) layout.getChildAt(0);
            image.setColorFilter(getResources().getColor(R.color.text_color));
            layout.setBackground(getResources().getDrawable(R.drawable.background_color));
            FRAGMENT_TAG_WHALECOMP="SWAP_SCREEN_FRAGMNET";
            fragmentSelected = new SwapFragment();
        }
        oldSelected=number;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                oldSelected=number;
//                loadFragment(fragmentSelected,FRAGMENT_TAG_WHALECOMP);
//            }
//        }).start();

        try{
            if (neoLayout.isShapeContainsPoint(event.getX(), event.getY())) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        neoLayout.setStyle(neo.small_inner_shadow);
                        textView.setScaleX(textView.getScaleX() * 0.96f);
                        textView.setScaleY(textView.getScaleY() * 0.96f);

                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED

                        neoLayout.setStyle(neo.drop_shadow);
                        selectedText.setTextColor(getResources().getColor(R.color.white));
                        selectedImage.setColorFilter(getResources().getColor(R.color.white));
                        neoLayout.setLight_color(getResources().getColor(R.color.light_shadow));
                        neoLayout.setDark_color(getResources().getColor(R.color.blue_shadow));
                        textView.setScaleX(1);
                        textView.setScaleY(1);
                        textView.setBackground(getResources().getDrawable(R.drawable.blue_gradient));



                        Thread.sleep(50);
                        drawerLayout.closeDrawer(GravityCompat.START);


                        return true; // if you want to handle the touch event
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private void onMenuItemsClicked(){
//        homeLayout,globeLayout,alarmLayout,settingLayout,whaleTrustLayout,giftLayout,docLayout,walletLayout,swapLayout;

        giftLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.giftLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,giftLayout, 6);
            }
        });
        docLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.docLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,docLayout, 7);
            }
        });
        walletLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.walletLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,walletLayout, 8);
            }
        });
        swapLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.swapLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,swapLayout, 9);
            }
        });
        homeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.homeLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,homeLayout, 1);
            }
        });
        globeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.globeLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,globeLayout, 2);
            }
        });
        alarmLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.alarmLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,alarmLayout, 3);
            }
        });
        settingLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.settingLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,settingLayout, 4);
            }
        });
        whaleTrustLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.whaleTrustLayout);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                return setLayout(event,textView,whaleTrustLayout, 5);
            }
        });
    }

//    private void onClickMenuItems() {
//
//        whaleTrustLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="WHALETRUST_SCREEN_FRAGMNET";
//                loadFragment(new WhaleTrustFragment(),FRAGMENT_TAG_WHALECOMP);
//                whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol_white));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.bg_color));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//
//                    }
//                });
//
//            }
//        }, 200);
//
//
//
//            }
//        });
//        settingLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="SETTING_SCREEN_FRAGMNET";
//                loadFragment(new SettingFragment(),FRAGMENT_TAG_WHALECOMP);
//                settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                settingText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol_white));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setting_menu_white));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        settingText.setTextColor(getResources().getColor(R.color.bg_color));
//
//                    }
//                }, 200);
//
//
//            }
//        });
//
//        giftLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="GIFT_SCREEN_FRAGMNET";
//                loadFragment(new GiftFragment(),FRAGMENT_TAG_WHALECOMP);
//                giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                giftText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.bg_color));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol_white));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//
//                    }
//                }, 200);
//
//
//            }
//        });
//        homeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="HOME_SCREEN_FRAGMNET";
//                loadFragment(new HomeFragment(),FRAGMENT_TAG_WHALECOMP);
//                homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                homeText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        homeText.setTextColor(getResources().getColor(R.color.bg_color));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol_white));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//
//                    }
//                }, 200);
//
//
//            }
//        });
//        swapLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="SWAP_SCREEN_FRAGMNET";
//                loadFragment(new SwapFragment(),FRAGMENT_TAG_WHALECOMP);
//                swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                swapText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.bg_color));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol_white));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//
//                    }
//                }, 200);
//
//
//            }
//        });
//
//        walletLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="WALLET_SCREEN_FRAGMNET";
//                loadFragment(new WalletFragment(),FRAGMENT_TAG_WHALECOMP);
//                walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                walletText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.bg_color));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_white));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//
//                    }
//                }, 200);
//
//
//            }
//        });
//
//        docLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="DOC_SCREEN_FRAGMNET";
//                loadFragment(new DocFragment(),FRAGMENT_TAG_WHALECOMP);
//                docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                docText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.bg_color));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon_white));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//
//                    }
//                }, 200);
//
//
//            }
//        });
//
//        globeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="GLOBE_SCREEN_FRAGMNET";
//                loadFragment(new GlobeFragment(),FRAGMENT_TAG_WHALECOMP);
//                globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                globeText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeText.setTextColor(getResources().getColor(R.color.bg_color));
//                        alarmText.setTextColor(getResources().getColor(R.color.black));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_white));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//
//                    }
//                }, 200);
//
//
//            }
//        });
//
//        alarmLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FRAGMENT_TAG_WHALECOMP="ALARM_SCREEN_FRAGMNET";
//                loadFragment(new AlertFragment(),FRAGMENT_TAG_WHALECOMP);
//                alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white_pressed));
//                alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_black));
//                alarmText.setTextColor(getResources().getColor(R.color.black));
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        homeText.setTextColor(getResources().getColor(R.color.black));
//                        swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        globeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        alarmLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
//                        globeText.setTextColor(getResources().getColor(R.color.black));
//                        alarmText.setTextColor(getResources().getColor(R.color.bg_color));
//                        globeIcon.setImageDrawable(getResources().getDrawable(R.drawable.globe_black));
//                        alarmIcon.setImageDrawable(getResources().getDrawable(R.drawable.alarm_white));
//                        docText.setTextColor(getResources().getColor(R.color.black));
//                        walletText.setTextColor(getResources().getColor(R.color.black));
//                        swapText.setTextColor(getResources().getColor(R.color.black));
//                        giftText.setTextColor(getResources().getColor(R.color.black));
//                        giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
//                        walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.bulb_menu_black));
//                        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
//                        swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
//                        docIcon.setImageDrawable(getResources().getDrawable(R.drawable.progress_icon));
//                        whaleTrustLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        whaleTrustIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
//                        whaleTrustText.setTextColor(getResources().getColor(R.color.black));
//                        settingIcon.setImageDrawable(getResources().getDrawable(R.drawable.setings_menu_black));
//                        settingLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
//                        settingText.setTextColor(getResources().getColor(R.color.black));
//                        drawerLayout.closeDrawer(Gravity.LEFT);
//
//                    }
//                }, 200);
//
//
//            }
//        });
//    }

    private boolean loadFragment(Fragment fragment,String FRAGMENT_TAG){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout,fragment,FRAGMENT_TAG)
                    .commit();

            fragmentSelected=null;

            return true;
        }
        return false;

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
                Preference.getInstance().writePreference(WALLET_SELECTED_ADDRESS,html);

//                dialog1.cancel();
                dialog2.cancel();
                setAddress();


            }
        }
    }

    public void refreshWithFragmnet( ){
        setAddress();
        loadFragment(new WhaleTrustFragment(),"WHALETRUST_SCREEN_FRAGMNET");
    }



    public void setAddress(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS).contains("0x")){
                    Log.e( "run: ","available" );
                    connectButton.setVisibility(View.GONE);
                    addressActivatedLayout.setVisibility(View.VISIBLE);
                    String str = Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS);
                    String str1 = str.substring(0, 6) + ".."+ str.substring(str.length()-4, str.length());
                    addressText.setText(str1);
                }else{
                    connectButton.setVisibility(View.VISIBLE);
                    addressActivatedLayout.setVisibility(View.GONE);
                    Log.e( "run: ","no available" );
                }
            }
        });

    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(getApplicationContext(), permission)) {
                return false;
            }
        }
        return true;
    }
    private void getRuntimePermissions() {
        List allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(getApplicationContext(), permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, (String[]) allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }
    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    getApplication().getPackageManager()
                            .getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (allPermissionsGranted()) {
            startService(new Intent(getApplicationContext(), MyService.class));
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

}