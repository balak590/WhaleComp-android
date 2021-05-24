package com.klhk.whalecomp.WalletFragment.StratergyScreens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klhk.whalecomp.DocFragment.DocActivity.Desc2Activity;
import com.klhk.whalecomp.R;

public class AutoCompoundActivity extends AppCompatActivity {
    ImageView backButton, pick_chain;
    RelativeLayout connectButton;
    TextView text2,text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_compound);
        connectButton = findViewById(R.id.connectButton);
        pick_chain = findViewById(R.id.pick_chain);

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

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AutoCompoundActivity.this,R.style.CustomDialog);

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
}