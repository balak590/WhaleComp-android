package com.klhk.whalecomp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.klhk.whalecomp.DocFragment.DocFragment;
import com.klhk.whalecomp.GiftFragment.GiftFragment;
import com.klhk.whalecomp.SwapFragmnet.SwapFragment;
import com.klhk.whalecomp.WalletFragment.WalletFragment;
import com.klhk.whalecomp.homefragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RelativeLayout connectButton;
    DrawerLayout drawerLayout;
    ImageView closeDrawer,openDrawer,pick_chain;
    NavigationView nav_view;

    ImageView giftLayout,giftIcon,docLayout,docIcon,walletLayout,walletIcon,swapLayout,swapIcon,homeLayout,homeIcon;
    TextView giftText,docText,walletText, swapText, homeText;
    ScrollView navScrollBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = findViewById(R.id.connectButton);
        drawerLayout = findViewById(R.id.drawerLayout);
        pick_chain = findViewById(R.id.pick_chain);

        openDrawer = findViewById(R.id.openDrawer);
        nav_view = findViewById(R.id.nav_view);
        View headerView =nav_view.getHeaderView(0);
        closeDrawer = headerView.findViewById(R.id.closeDrawer);

        headerView.setVerticalScrollBarEnabled(false);

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



        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.pick_wallet,null);
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

        pick_chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialog);

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

        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        onClickMenuItems();
        loadFragment(new HomeFragment());

    }

    private void onClickMenuItems() {

        giftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
                homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeText.setTextColor(getResources().getColor(R.color.black));
                swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docText.setTextColor(getResources().getColor(R.color.black));
                walletText.setTextColor(getResources().getColor(R.color.black));
                swapText.setTextColor(getResources().getColor(R.color.black));
                giftText.setTextColor(getResources().getColor(R.color.bg_color));
                drawerLayout.closeDrawer(Gravity.LEFT);
                giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol_white));
                walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
                swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
                docIcon.setImageDrawable(getResources().getDrawable(R.drawable.document_symbol));
                loadFragment(new GiftFragment());
            }
        });
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
                homeText.setTextColor(getResources().getColor(R.color.bg_color));
                swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docText.setTextColor(getResources().getColor(R.color.black));
                walletText.setTextColor(getResources().getColor(R.color.black));
                swapText.setTextColor(getResources().getColor(R.color.black));
                giftText.setTextColor(getResources().getColor(R.color.black));
                giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
                walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol_white));
                swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
                docIcon.setImageDrawable(getResources().getDrawable(R.drawable.document_symbol));
                drawerLayout.closeDrawer(Gravity.LEFT);
                loadFragment(new HomeFragment());
            }
        });
        swapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeText.setTextColor(getResources().getColor(R.color.black));
                swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
                walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docText.setTextColor(getResources().getColor(R.color.black));
                walletText.setTextColor(getResources().getColor(R.color.black));
                swapText.setTextColor(getResources().getColor(R.color.bg_color));
                giftText.setTextColor(getResources().getColor(R.color.black));
                giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
                walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
                swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol_white));
                docIcon.setImageDrawable(getResources().getDrawable(R.drawable.document_symbol));
                drawerLayout.closeDrawer(Gravity.LEFT);
                loadFragment(new SwapFragment());
            }
        });

        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeText.setTextColor(getResources().getColor(R.color.black));
                swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
                docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docText.setTextColor(getResources().getColor(R.color.black));
                walletText.setTextColor(getResources().getColor(R.color.bg_color));
                swapText.setTextColor(getResources().getColor(R.color.black));
                giftText.setTextColor(getResources().getColor(R.color.black));
                giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
                walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol_white));
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
                swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
                docIcon.setImageDrawable(getResources().getDrawable(R.drawable.document_symbol));
                drawerLayout.closeDrawer(Gravity.LEFT);
                loadFragment(new WalletFragment());
            }
        });

        docLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                homeText.setTextColor(getResources().getColor(R.color.black));
                swapLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                walletLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_white));
                docLayout.setImageDrawable(getResources().getDrawable(R.drawable.nav_bar_button_blue));
                docText.setTextColor(getResources().getColor(R.color.bg_color));
                walletText.setTextColor(getResources().getColor(R.color.black));
                swapText.setTextColor(getResources().getColor(R.color.black));
                giftText.setTextColor(getResources().getColor(R.color.black));
                giftIcon.setImageDrawable(getResources().getDrawable(R.drawable.gift_symbol));
                walletIcon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_symbol));
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home_symbol));
                swapIcon.setImageDrawable(getResources().getDrawable(R.drawable.swap_symbol));
                docIcon.setImageDrawable(getResources().getDrawable(R.drawable.documnet_symbol_white));
                drawerLayout.closeDrawer(Gravity.LEFT);
                loadFragment(new DocFragment());
            }
        });
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout,fragment)
                    .commit();

            return true;
        }
        return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}