package com.klhk.whalecomp.WalletFragment.StratergyScreens;

import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.R;

public class MyStratergyActivity extends AppCompatActivity {
    ImageView  pick_chain;
    RelativeLayout connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stratergy);
        connectButton = findViewById(R.id.connectButton);
        pick_chain = findViewById(R.id.pick_chain);


        TextView amountText = findViewById(R.id.amountText);

        TextPaint paint = amountText.getPaint();
        float width = paint.measureText(amountText.getText().toString());

        Shader textShader = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#0159FF"),
                        Color.parseColor("#01D1FF"),
                }, null, Shader.TileMode.CLAMP);
        amountText.getPaint().setShader(textShader);

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);
    }
}