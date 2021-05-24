package com.klhk.whalecomp.DocFragment.DocActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.R;

public class Desc2Activity extends AppCompatActivity {
    ImageView backButton, pick_chain;
    RelativeLayout connectButton;
    TextView token1PickLayout,token1Text;
    String toke1;
    ImageView token1Img;
    LinearLayout token1Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc2);
        connectButton = findViewById(R.id.connectButton);
        pick_chain = findViewById(R.id.pick_chain);

        backButton = findViewById(R.id.backButton);
        token1PickLayout = findViewById(R.id.token1PickLayout);
        token1Text = findViewById(R.id.token1Text);
        token1Img = findViewById(R.id.token1Img);
        token1Layout = findViewById(R.id.token1Layout);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Desc2Activity.this,R.style.CustomDialog);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(Desc2Activity.this,R.style.CustomDialog);

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

        token1PickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Desc2Activity.this,R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.pick_token,null);
                ImageView bnbToken = v.findViewById(R.id.bnbToken);
                ImageView btcToken = v.findViewById(R.id.btcToken);
                ImageView ethToken = v.findViewById(R.id.ethToken);
                ImageView usdcToken = v.findViewById(R.id.usdcToken);
                ImageView usdtToken = v.findViewById(R.id.usdtToken);
                ImageView busdToken = v.findViewById(R.id.busdToken);
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



                bnbToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="bnb";
                        setToken1();
                        dialog.cancel();
                    }
                });
                btcToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="btc";
                        setToken1();
                        dialog.cancel();
                    }
                });
                ethToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="eth";
                        setToken1();
                        dialog.cancel();
                    }
                });
                usdcToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="usdc";
                        setToken1();
                        dialog.cancel();
                    }
                });
                usdtToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="usdt";
                        setToken1();
                        dialog.cancel();
                    }
                });
                busdToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="busd";
                        setToken1();
                        dialog.cancel();
                    }
                });
            }
        });
    }

    public void setToken1(){
        token1PickLayout.setText("");
        token1Layout.setVisibility(View.VISIBLE);
//        token1Text.setVisibility(View.VISIBLE);
        if(toke1.isEmpty()){
            token1PickLayout.setText("選擇幣種");
            token1Layout.setVisibility(View.GONE);

        }else if(toke1.equalsIgnoreCase("bnb")){
            token1Text.setText("BNB");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.bnb_token));
        }else if(toke1.equalsIgnoreCase("btc")){
            token1Text.setText("BTC");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.btc_token));
        }else if(toke1.equalsIgnoreCase("usdc")){
            token1Text.setText("USDC");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.usdc_token));
        }else if(toke1.equalsIgnoreCase("usdt")){
            token1Text.setText("USDT");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.usdt_token));
        }else if(toke1.equalsIgnoreCase("eth")){
            token1Text.setText("ETH");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.eth_token));
        }else if(toke1.equalsIgnoreCase("busd")){
            token1Text.setText("BUSD");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.busd_token));
        }
    }
}