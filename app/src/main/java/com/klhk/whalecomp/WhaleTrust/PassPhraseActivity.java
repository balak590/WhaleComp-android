package com.klhk.whalecomp.WhaleTrust;

import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.utilities.Wallet;

import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;

public class PassPhraseActivity extends AppCompatActivity {

    TextView passPhrase1,passPhrase2,passPhrase3,passPhrase4,passPhrase5,passPhrase6,passPhrase7,passPhrase8,passPhrase9,passPhrase10,passPhrase11,passPhrase12;
    private static final int DEFAULT_KEY_STRENGTH = 128;
    ImageView continueButton;
    String phraseText,walletAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_phrase);
        System.loadLibrary("TrustWalletCore");
        passPhrase1 = findViewById(R.id.passPhrase1);
        passPhrase2 = findViewById(R.id.passPhrase2);
        passPhrase3 = findViewById(R.id.passPhrase3);
        passPhrase4 = findViewById(R.id.passPhrase4);
        passPhrase5 = findViewById(R.id.passPhrase5);
        passPhrase6 = findViewById(R.id.passPhrase6);
        passPhrase7 = findViewById(R.id.passPhrase7);
        passPhrase8 = findViewById(R.id.passPhrase8);
        passPhrase9 = findViewById(R.id.passPhrase9);
        passPhrase10 = findViewById(R.id.passPhrase10);
        passPhrase11 = findViewById(R.id.passPhrase11);
        passPhrase12 = findViewById(R.id.passPhrase12);
        continueButton = findViewById(R.id.continueButton);
        phraseText="";

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        HDWallet newWallet = new HDWallet(DEFAULT_KEY_STRENGTH, "");
        PrivateKey pk = newWallet.getKeyForCoin(CoinType.ETHEREUM);
        Wallet currentWallet = new Wallet(CoinType.ETHEREUM.deriveAddress(pk));
        Log.e("Address",currentWallet.address);
        walletAddress = currentWallet.address;
        String phrase[] = newWallet.mnemonic().split(" ");


        for(int i=0;i<phrase.length;i++){
            if(i==0){ passPhrase1.setText(phrase[i]); }
            if(i==1){ passPhrase2.setText(phrase[i]); }
            if(i==2){ passPhrase3.setText(phrase[i]); }
            if(i==3){ passPhrase4.setText(phrase[i]); }
            if(i==4){ passPhrase5.setText(phrase[i]); }
            if(i==5){ passPhrase6.setText(phrase[i]); }
            if(i==6){ passPhrase7.setText(phrase[i]); }
            if(i==7){ passPhrase8.setText(phrase[i]); }
            if(i==8){ passPhrase9.setText(phrase[i]); }
            if(i==9){ passPhrase10.setText(phrase[i]); }
            if(i==10){ passPhrase11.setText(phrase[i]); }
            if(i==11){ passPhrase12.setText(phrase[i]); }

        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(),PassPhraseVerifyActivity.class);
                i.putExtra("phrase",newWallet.mnemonic());
                i.putExtra("walletAddress",walletAddress);
                startActivityForResult(i,1000);
            }
        });
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