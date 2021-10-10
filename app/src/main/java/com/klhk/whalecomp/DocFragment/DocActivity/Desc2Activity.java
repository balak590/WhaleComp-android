package com.klhk.whalecomp.DocFragment.DocActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.StrategyStepsActivity;
import com.klhk.whalecomp.WhaleTrust.SwapWhaleTrustActivity;
import com.klhk.whalecomp.utilities.FunctionCall;

import static com.klhk.whalecomp.utilities.Constants.EPS_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import java.math.BigInteger;

public class Desc2Activity extends AppCompatActivity {
    ImageView sliderButton,continueButton;
    TextView token1PickLayout,token1Text,tokenBalance;
    EditText tokenEntered;
    String toke1;
    ImageView token1Img;
    LinearLayout token1Layout;
    boolean clickedToggle=false;
    String amount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_desc2);

        token1PickLayout = findViewById(R.id.token1PickLayout);
        token1Text = findViewById(R.id.token1Text);
        token1Img = findViewById(R.id.token1Img);
        token1Layout = findViewById(R.id.token1Layout);
        sliderButton = findViewById(R.id.sliderButton);
        tokenBalance = findViewById(R.id.tokenBalance);

        tokenEntered = findViewById(R.id.tokenEntered);

        continueButton = findViewById(R.id.continueButton);

        new Thread(new Runnable() {
            @Override
            public void run() {
                amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), EPS_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tokenBalance.setText("餘額:"+String.format("%.4f", Double.parseDouble(amount)));
                    }
                });
            }
        }).start();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenEntered.getText().toString().isEmpty()){
                    triggerNotification("Please enter number of token.");
                }else{
                    if(Double.valueOf(tokenEntered.getText().toString()) > Double.valueOf(amount)){
                        triggerNotification("您當前錢包内代幣餘額不足");
                    }else{
                        Intent i = new Intent(Desc2Activity.this, StrategyStepsActivity.class);
                        i.putExtra("stepName","5");
                        i.putExtra("epsAmount",tokenEntered.getText().toString());
                        startActivityForResult(i,4001);
                    }

                }
            }
        });




        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        sliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clickedToggle){
                    clickedToggle= true;
                    sliderButton.setImageDrawable(getResources().getDrawable(R.drawable.toggle_off));
                }else{
                    clickedToggle = false;
                    sliderButton.setImageDrawable(getResources().getDrawable(R.drawable.toggle_on));
                }
            }
        });


        token1PickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Desc2Activity.this, SwapWhaleTrustActivity.class);
                i.putExtra("isSecondToken","yes");
                i.putExtra("tokenName","EPS");
                i.putExtra("tokenAddr",EPS_ADDRESS);
                i.putExtra("tokenImage","https://assets.coingecko.com/coins/images/14498/large/ellipsis.png?1616556354");
                startActivity(i);
            }
        });
    }
    public void triggerNotification(String notificationText){
        AlertDialog.Builder builder = new AlertDialog.Builder(Desc2Activity.this,R.style.CustomDialog);

        View v = getLayoutInflater().inflate(R.layout.error_notification,null);
        ImageView confirmButton = v.findViewById(R.id.confirmButton);
        ImageView closeButton = v.findViewById(R.id.closeButton);
        TextView alertText = v.findViewById(R.id.errorMessage);
        alertText.setText(notificationText);

        builder.setCancelable(false);

        builder.setView(v);
        AlertDialog dialog = builder.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4001&&resultCode==RESULT_OK){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}