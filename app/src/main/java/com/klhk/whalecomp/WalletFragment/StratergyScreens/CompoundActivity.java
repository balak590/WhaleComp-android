package com.klhk.whalecomp.WalletFragment.StratergyScreens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klhk.whalecomp.DocFragment.DocActivity.Desc2Activity;
import com.klhk.whalecomp.R;

public class CompoundActivity extends AppCompatActivity {
    ImageView backButton, pick_chain,nextStep;
    RelativeLayout connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compound);
        connectButton = findViewById(R.id.connectButton);
        pick_chain = findViewById(R.id.pick_chain);

        backButton = findViewById(R.id.backButton);
        nextStep = findViewById(R.id.nextStep);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompoundActivity.this,R.style.CustomDialog);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(CompoundActivity.this,R.style.CustomDialog);

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

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompoundActivity.this,R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.ad_alert_clicked,null);

                TextView timerText = v.findViewById(R.id.timerText);

                builder.setView(v);

                AlertDialog alert = builder.show();

                new CountDownTimer(4000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        timerText.setText( String.valueOf((millisUntilFinished / 1000)+1));
                    }

                    public void onFinish() {
                        alert.dismiss();
                    }
                }.start();

            }
        });
    }
}