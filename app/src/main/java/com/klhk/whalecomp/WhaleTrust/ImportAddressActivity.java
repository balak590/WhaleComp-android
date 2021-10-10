package com.klhk.whalecomp.WhaleTrust;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.utilities.Wallet;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;

import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_METHOD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class ImportAddressActivity extends AppCompatActivity {

    LinearLayout mnemonicPatternLayout;
    RelativeLayout cardLayout;
    EditText keystoreLayout,secretKeyLayout;
    EditText verifyPattern1,verifyPattern2,verifyPattern3,verifyPattern4,verifyPattern5,verifyPattern6,verifyPattern7,verifyPattern8,verifyPattern9,verifyPattern10,verifyPattern11,verifyPattern12;

    ImageView privateKeyPressed,keystorePressed,mnemonicPressed,continueButton;
    int importType;
    String pattern="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.loadLibrary("TrustWalletCore");
        setContentView(R.layout.activity_import_address);
        cardLayout = findViewById(R.id.cardLayout);
        mnemonicPatternLayout = findViewById(R.id.mnemonicPatternLayout);
        keystoreLayout  = findViewById(R.id.keystoreLayout);
        secretKeyLayout  = findViewById(R.id.secretKeyLayout);
        mnemonicPressed = findViewById(R.id.mnemonicPressed);
        keystorePressed  = findViewById(R.id.keystorePressed);
        privateKeyPressed  = findViewById(R.id.privateKeyPressed);
        verifyPattern1 = findViewById(R.id.verifyPattern1);
        verifyPattern2 = findViewById(R.id.verifyPattern2);
        verifyPattern3 = findViewById(R.id.verifyPattern3);
        verifyPattern4 = findViewById(R.id.verifyPattern4);
        verifyPattern5 = findViewById(R.id.verifyPattern5);
        verifyPattern6 = findViewById(R.id.verifyPattern6);
        verifyPattern7 = findViewById(R.id.verifyPattern7);
        verifyPattern8 = findViewById(R.id.verifyPattern8);
        verifyPattern9 = findViewById(R.id.verifyPattern9);
        verifyPattern10 = findViewById(R.id.verifyPattern10);
        verifyPattern11 = findViewById(R.id.verifyPattern11);
        verifyPattern12 = findViewById(R.id.verifyPattern12);
        continueButton = findViewById(R.id.continueButton);

        importType=1;

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(importType==1){
                    pattern = verifyPattern1.getText().toString()+" "+verifyPattern2.getText().toString()+" "+
                            verifyPattern3.getText().toString()+" "+verifyPattern4.getText().toString()+" "+
                            verifyPattern5.getText().toString()+" "+verifyPattern6.getText().toString()+" "+
                            verifyPattern7.getText().toString()+" "+verifyPattern8.getText().toString()+" "+
                            verifyPattern9.getText().toString()+" "+verifyPattern10.getText().toString()+" "+
                            verifyPattern11.getText().toString()+" "+verifyPattern12.getText().toString();
                    HDWallet newWallet = new HDWallet(pattern, "");
                    PrivateKey pk = newWallet.getKeyForCoin(CoinType.ETHEREUM);
                    Wallet currentWallet = new Wallet(CoinType.ETHEREUM.deriveAddress(pk));

                    Intent i = new Intent(getApplicationContext(), CustomKeypadActivity.class);
                    i.putExtra("inputType","1");
                    i.putExtra(WHALETRUST_ADDRESS,currentWallet.address);
                    i.putExtra(WHALETRUST_ADDRESS_PATTERN,pattern);
                    i.putExtra(WHALETRUST_ADDRESS_METHOD,"phrase");
                    startActivityForResult(i,1000);
//                    finish();
//                    Preference.getInstance().writePreference("userWalletAddress.whaleTrust",currentWallet.address);
//                    Preference.getInstance().writePreference("userWalletAddress.whaleTrust.pattern",pattern);
//                    Preference.getInstance().writePreference("userWalletAddress.whaleTrust.walletType","pattern");
//                    finish();
                }else if(importType==2){

                }else if(importType==3){
                    String privateKey= secretKeyLayout.getText().toString();
                    BigInteger key = new BigInteger(privateKey, 16);
                    if (!WalletUtils.isValidPrivateKey(privateKey)) {
                        Toast.makeText(ImportAddressActivity.this, "Invalid private key", Toast.LENGTH_SHORT).show();
                    }
                    ECKeyPair keypair = ECKeyPair.create(key);
                    String address = Numeric.prependHexPrefix(Keys.getAddress(keypair));

                    Intent i = new Intent(getApplicationContext(), CustomKeypadActivity.class);
                    i.putExtra("inputType","1");
                    i.putExtra(WHALETRUST_ADDRESS,address);
                    i.putExtra(WHALETRUST_ADDRESS_PATTERN,privateKey);
                    i.putExtra(WHALETRUST_ADDRESS_METHOD,"privateKey");
                    startActivityForResult(i,1000);
//                    Preference.getInstance().writePreference("userWalletAddress.whaleTrust",address);
//                    Preference.getInstance().writePreference("userWalletAddress.whaleTrust.privateKey",privateKey);
//                    Preference.getInstance().writePreference("userWalletAddress.whaleTrust.walletType","privateKey");
                    //finish();
                }
            }
        });

        mnemonicPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importType=1;
                cardLayout.setVisibility(View.GONE);
                mnemonicPatternLayout.setVisibility(View.VISIBLE);
                keystoreLayout.setVisibility(View.GONE);
                secretKeyLayout.setVisibility(View.GONE);
                mnemonicPressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_pressed));
                keystorePressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_unpressed));
                privateKeyPressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_unpressed));
            }
        });
        keystorePressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importType=2;
                cardLayout.setVisibility(View.VISIBLE);
                mnemonicPatternLayout.setVisibility(View.GONE);
                keystoreLayout.setVisibility(View.VISIBLE);
                secretKeyLayout.setVisibility(View.GONE);
                mnemonicPressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_unpressed));
                keystorePressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_pressed));
                privateKeyPressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_unpressed));
            }
        });
        privateKeyPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importType=3;
                cardLayout.setVisibility(View.VISIBLE);
                mnemonicPatternLayout.setVisibility(View.GONE);
                keystoreLayout.setVisibility(View.GONE);
                secretKeyLayout.setVisibility(View.VISIBLE);
                mnemonicPressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_unpressed));
                keystorePressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_unpressed));
                privateKeyPressed.setImageDrawable(getResources().getDrawable(R.drawable.import_button_pressed));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);

    }
}