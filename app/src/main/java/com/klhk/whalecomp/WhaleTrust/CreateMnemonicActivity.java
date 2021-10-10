package com.klhk.whalecomp.WhaleTrust;

import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;

import java.security.SecureRandom;

import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;
import wallet.core.jni.Hash;
import wallet.core.jni.PrivateKey;
import wallet.core.jni.Purpose;


public class CreateMnemonicActivity extends AppCompatActivity {
    ImageView continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mnemonic);
        continueButton= findViewById(R.id.continueButton);
        System.loadLibrary("TrustWalletCore");
        Preference.getInstance().Initialize(this);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(),PassPhraseActivity.class);
                startActivityForResult(i,1000);

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}