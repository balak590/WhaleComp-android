package com.klhk.whalecomp.SettingFragment;

import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.R;

public class UpdateActivity extends AppCompatActivity {

    ImageView playStoreButton,worldWideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        worldWideButton = findViewById(R.id.worldWideButton);
        playStoreButton = findViewById(R.id.playStoreButton);

        playStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        worldWideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}