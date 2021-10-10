package com.klhk.whalecomp.WalletFragment.StratergyScreens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WhaleTrust.SwapWhaleTrustActivity;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.klhk.whalecomp.utilities.Strategy001;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.concurrent.Executor;

import static com.klhk.whalecomp.utilities.Constants.APY_CALCULATED_EPS;
import static com.klhk.whalecomp.utilities.Constants.CAN_EXECUTE_COMPOUNDING;
import static com.klhk.whalecomp.utilities.Constants.EPSSTAKED;
import static com.klhk.whalecomp.utilities.Constants.EPS_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.LAST_EXECUTION_BLOCK;
import static com.klhk.whalecomp.utilities.Constants.LAST_EXECUTION_TIME;
import static com.klhk.whalecomp.utilities.Constants.NEXT_EXECUTION_TIME;
import static com.klhk.whalecomp.utilities.Constants.STAKED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.FunctionCall.getBlock;
import static com.klhk.whalecomp.utilities.FunctionCall.pancakeRouterv2;
import static com.klhk.whalecomp.utilities.Strategy001.epsStakerAddress;
import static com.klhk.whalecomp.utilities.Strategy001.getLogs;

public class StrategyStepsActivity extends AppCompatActivity {

    TextView stepNumberText,stepNumberDescription,continueButtonText;
    RelativeLayout nextStepButton;
    ImageView continueButton;

    int stepNumber =1;
    String stepDescription ="正在從EPS鎖倉合約中提取收益 ";
    String stepNameText ="滾動步驟（1/2)";
    String txHash="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_steps);

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);



        stepNumberText = findViewById(R.id.stepNumberText);
        stepNumberDescription = findViewById(R.id.stepNumberDescription);
        nextStepButton = findViewById(R.id.nextStepButton);
        continueButton = findViewById(R.id.continueButton);
        continueButtonText = findViewById(R.id.continueButtonText);

        if(getIntent().getStringExtra("stepName")!=null){

            if(getIntent().getStringExtra("stepName").equalsIgnoreCase("3")){
                stepNumber = 3;
                stepNumberText.setText("滾動步驟（2/2)");
                stepDescription ="正在將EPS收益轉入EPS鎖倉合約 ";
            }

            if(getIntent().getStringExtra("stepName").equalsIgnoreCase("1")){
                stepNumber = 1;
                stepNumberText.setText("滾動步驟（1/2)");
                stepDescription ="正在從EPS鎖倉合約中提取收益 ";
            }

            if(getIntent().getStringExtra("stepName").equalsIgnoreCase("5")){
                stepNumber = 5;
                stepNumberText.setText("首次應用策略");
                stepDescription ="正在將EPS代幣轉入EPS鎖倉合約 ";
            }

            if(getIntent().getStringExtra("stepName").equalsIgnoreCase("7")){
                stepNumber = 7;
                stepNumberText.setText("退出策略");
                stepDescription ="正在將EPS代幣從鎖倉合約轉出 ";
            }

        }




        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stepNumber==2){
                    Intent i = new Intent(StrategyStepsActivity.this,StrategyStepsActivity.class);
                    i.putExtra("stepName","3");
                    startActivity(i);
                    finish();
                }else{
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

            }
        });

        promptPassword(StrategyStepsActivity.this);
    }
    int count =0;
    String stepDescription1=stepDescription;
    public void triggerRuningText(){

        if(stepNumber==1){
            try{
                String txHash = Strategy001.getReward();
                Handler ha=new Handler();
                Runnable myRunnable;
                myRunnable =new Runnable() {

                    @Override
                    public void run() {
                        try{
                            if(!FunctionCall.checkTransactionByHash(txHash)){
                                Log.e("running","checkTransactionByHash(approve)");
                                if (count>3){
                                    stepDescription1 = stepDescription;
                                    count=0;
                                }else{
                                    stepDescription1 = stepDescription1+ ".";
                                }

                                stepNumberDescription.setText( stepDescription1);
                                count++;
                                ha.postDelayed(this, 500);
                            }else{
                                //Preference.getInstance().writeBooleanPreference(CAN_EXECUTE_COMPOUNDING,false);
//                                Log.e("running","swapTokens1");
//                                EthBlock block = getBlock(txHash);
                                nextStepButton.setVisibility(View.VISIBLE);
                                stepNumber++;
                                stepNumberDescription.setText("EPS鎖倉合約中收益提取成功。 ");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                };

                ha.postDelayed(myRunnable, 500);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if(stepNumber==3){
            try{
                boolean allowanceHex = FunctionCall.getAllowance(EPS_ADDRESS,epsStakerAddress);

                if(!allowanceHex){
                    String approve =FunctionCall.approveTokens(EPS_ADDRESS,epsStakerAddress);
                    Handler ha=new Handler();
                    Runnable myRunnable;
                    myRunnable =new Runnable() {

                        @Override
                        public void run() {
                            try{
                                if(!FunctionCall.checkTransactionByHash(approve)){
                                    ha.postDelayed(this, 500);
                                }else{
                                    String txHash = Strategy001.stake();
                                    Handler ha=new Handler();
                                    Runnable myRunnable;
                                    myRunnable =new Runnable() {

                                        @Override
                                        public void run() {

                                            if(!FunctionCall.checkTransactionByHash(txHash)){
                                                Log.e("running","checkTransactionByHash(approve)");
                                                if (count>3){
                                                    stepDescription1 = stepDescription;
                                                    count=0;
                                                }else{
                                                    stepDescription1 = stepDescription1+ ".";
                                                }

                                                stepNumberDescription.setText( stepDescription1);
                                                count++;
                                                ha.postDelayed(this, 500);
                                            }else{
                                                Log.e("running","swapTokens3");
                                                try {
                                                    EthBlock block = getBlock(txHash);
                                                    JSONArray array = new JSONArray();
                                                    if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()) {
                                                        array = new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
                                                    }

                                                    JSONArray tempArray = new JSONArray();
                                                    boolean addressAvail =false;
                                                    for (int i = 0; i < array.length(); i++) {
                                                        JSONObject object = new JSONObject(array.get(i).toString());
                                                        if (object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))) {
                                                            object.put(LAST_EXECUTION_TIME,String.valueOf(block.getBlock().getTimestamp()));
                                                            object.put(LAST_EXECUTION_BLOCK,String.valueOf(block.getBlock().getNumber()));
                                                            object.put(CAN_EXECUTE_COMPOUNDING,false);
                                                            object.put(EPSSTAKED,"yes");
                                                            addressAvail=true;
                                                        }

                                                        tempArray.put(object);
                                                    }
                                                    if(!addressAvail){
                                                        JSONObject object = new JSONObject();
                                                        object.put(WHALETRUST_ADDRESS_SELECTED,Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED));
                                                        object.put(LAST_EXECUTION_TIME,String.valueOf(block.getBlock().getTimestamp()));
                                                        object.put(LAST_EXECUTION_BLOCK,String.valueOf(block.getBlock().getNumber()));
                                                        object.put(CAN_EXECUTE_COMPOUNDING,false);
                                                        object.put(EPSSTAKED,"yes");

                                                        array.put(object);
                                                        Preference.getInstance().writePreference(STAKED_ADDRESS,array.toString());
                                                    }else{
                                                        Preference.getInstance().writePreference(STAKED_ADDRESS,tempArray.toString());
                                                    }

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }




                                                nextStepButton.setVisibility(View.VISIBLE);
                                                stepNumber++;
                                                stepNumberDescription.setText("EPS收益成功轉入EPS鎖倉合約。 ");
                                                continueButtonText.setText("完 成");
                                            }

                                        }
                                    };

                                    ha.postDelayed(myRunnable, 500);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };

                    ha.postDelayed(myRunnable, 500);
                }else {
                    String txHash = Strategy001.stake();
                    Handler ha=new Handler();
                    Runnable myRunnable;
                    Log.e("tx step3",txHash);
                    myRunnable =new Runnable() {

                        @Override
                        public void run() {
                            try{
                                if(!FunctionCall.checkTransactionByHash(txHash)){
                                    Log.e("running","checkTransactionByHash step 3");
                                    if (count>3){
                                        stepDescription1 = stepDescription;
                                        count=0;
                                    }else{
                                        stepDescription1 = stepDescription1+ ".";
                                    }

                                    stepNumberDescription.setText( stepDescription1);
                                    count++;
                                    ha.postDelayed(this, 500);
                                }else{
                                    Log.e("running","swapTokens1");
                                    EthBlock block1 = getBlock(txHash);

                                    Log.e("Block", String.valueOf(block1.getBlock().getNumber()));

                                    try {
                                        JSONArray array = new JSONArray();
                                        if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()) {
                                            array = new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
                                        }

                                        JSONArray tempArray = new JSONArray();
                                        boolean addressAvail =false;
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = new JSONObject(array.get(i).toString());
                                            if (object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))) {
                                                object.put(LAST_EXECUTION_TIME,String.valueOf(block1.getBlock().getTimestamp()));
                                                object.put(LAST_EXECUTION_BLOCK,String.valueOf(block1.getBlock().getNumber()));
                                                object.put(CAN_EXECUTE_COMPOUNDING,false);
                                                object.put(EPSSTAKED,"yes");
                                                addressAvail=true;
                                            }

                                            tempArray.put(object);
                                        }
                                        if(!addressAvail){
                                            JSONObject object = new JSONObject();
                                            object.put(WHALETRUST_ADDRESS_SELECTED,Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED));
                                            object.put(LAST_EXECUTION_TIME,String.valueOf(block1.getBlock().getTimestamp()));
                                            object.put(LAST_EXECUTION_BLOCK,String.valueOf(block1.getBlock().getNumber()));
                                            object.put(CAN_EXECUTE_COMPOUNDING,false);
                                            object.put(EPSSTAKED,"yes");

                                            array.put(object);
                                            Preference.getInstance().writePreference(STAKED_ADDRESS,array.toString());
                                        }else{
                                            Preference.getInstance().writePreference(STAKED_ADDRESS,tempArray.toString());
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    nextStepButton.setVisibility(View.VISIBLE);
                                    stepNumber++;
                                    stepNumberDescription.setText("EPS收益成功轉入EPS鎖倉合約");
                                    continueButtonText.setText("下一步");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };

                    ha.postDelayed(myRunnable, 500);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(stepNumber==5){
            try{

                String amount = getIntent().getStringExtra("epsAmount");
                Log.e("amount",String.valueOf(new BigInteger(amount)));
                //String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), EPS_ADDRESS);

                String txHash1 = Strategy001.stake(amount);
                Log.e("txHash",txHash1);

                Handler ha=new Handler();
                Runnable myRunnable;

                myRunnable =new Runnable() {

                    @Override
                    public void run() {
                        try{
                            if(!FunctionCall.checkTransactionByHash(txHash1)){
                                Log.e("running","checkTransactionByHash(approve)");
                                if (count>3){
                                    stepDescription1 = stepDescription;
                                    count=0;
                                }else{
                                    stepDescription1 = stepDescription1+ ".";
                                }

                                stepNumberDescription.setText( stepDescription1);
                                count++;
                                ha.postDelayed(this, 1000);
                            }else{
                                Log.e("running","swapTokens1");
                                EthBlock block = getBlock(txHash1);

                                Log.e("Block", String.valueOf(block.getBlock().getNumber()));

                                try {
                                    JSONArray array = new JSONArray();
                                    if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()) {
                                        array = new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
                                    }

                                    JSONArray tempArray = new JSONArray();
                                    boolean addressAvail =false;
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = new JSONObject(array.get(i).toString());
                                        if (object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))) {
                                            object.put(LAST_EXECUTION_TIME,String.valueOf(block.getBlock().getTimestamp()));
                                            object.put(LAST_EXECUTION_BLOCK,String.valueOf(block.getBlock().getNumber()));
                                            object.put(CAN_EXECUTE_COMPOUNDING,false);
                                            object.put(EPSSTAKED,"yes");
                                            addressAvail=true;
                                        }

                                        tempArray.put(object);
                                    }
                                    if(!addressAvail){
                                        JSONObject object = new JSONObject();
                                        object.put(WHALETRUST_ADDRESS_SELECTED,Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED));
                                        object.put(LAST_EXECUTION_TIME,String.valueOf(block.getBlock().getTimestamp()));
                                        object.put(LAST_EXECUTION_BLOCK,String.valueOf(block.getBlock().getNumber()));
                                        object.put(CAN_EXECUTE_COMPOUNDING,false);
                                        object.put(EPSSTAKED,"yes");

                                        array.put(object);
                                        Preference.getInstance().writePreference(STAKED_ADDRESS,array.toString());
                                    }else{
                                        Preference.getInstance().writePreference(STAKED_ADDRESS,tempArray.toString());
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                nextStepButton.setVisibility(View.VISIBLE);
                                stepNumber++;
                                stepNumberDescription.setText("已完成EPS鎖倉，已開啓複利滾動策略。");
                                continueButtonText.setText("確 認");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                ha.postDelayed(myRunnable, 1000);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if(stepNumber==7){
            try{
                String txHash = Strategy001.getReward();
                Handler ha=new Handler();
                Runnable myRunnable;

                myRunnable =new Runnable() {

                    @Override
                    public void run() {
                        try{
                            if(!FunctionCall.checkTransactionByHash(txHash)){
                                Log.e("running","checkTransactionByHash(approve)");
                                if (count>3){
                                    stepDescription1 = stepDescription;
                                    count=0;
                                }else{
                                    stepDescription1 = stepDescription1+ ".";
                                }

                                stepNumberDescription.setText( stepDescription1);
                                count++;
                                ha.postDelayed(this, 500);
                            }else{
                                Log.e("running","swapTokens1");
                                Preference.getInstance().writeBooleanPreference(CAN_EXECUTE_COMPOUNDING,false);
                                nextStepButton.setVisibility(View.VISIBLE);
                                stepNumber++;
                                stepNumberDescription.setText("EPS代幣已轉入您的錢包。");
                                continueButtonText.setText("下一步");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                ha.postDelayed(myRunnable, 500);
            }catch (Exception e){
                e.printStackTrace();
            }
            //String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), EPS_ADDRESS);

        }
//        new CountDownTimer(3000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                stepDescription = stepDescription+ ".";
//                stepNumberDescription.setText( stepDescription);
//            }
//            public void onFinish() {
//                stepNumber++;
//                nextStepButton.setVisibility(View.VISIBLE);
//                if(stepNumber ==2){
//                    stepNumberDescription.setText("EPS鎖倉合約中收益提取成功");
//                }else if(stepNumber ==4){
//                    stepNumberDescription.setText("EPS收益成功轉入EPS鎖倉合約");
//                    continueButtonText.setText("完 成");
//                }
//            }
//        }.start();

    }

    public void callAd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StrategyStepsActivity.this,R.style.CustomDialog);
        View v = getLayoutInflater().inflate(R.layout.ad_alert_clicked,null);
        TextView timerText = v.findViewById(R.id.timerText);
        builder.setView(v);
        AlertDialog alert = builder.show();
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText( String.valueOf((millisUntilFinished / 1000)+1));
            }
            public void onFinish() {
                alert.dismiss();
                triggerRuningText();
            }
        }.start();
    }

    public void promptPassword(Context context){
        if(Preference.getInstance().returnBoolean(WHALETRUST_LOCK_FINGERPRINT)){
            BiometricManager biometricManager = BiometricManager.from(context);

            switch (biometricManager.canAuthenticate()){
                case BiometricManager.BIOMETRIC_SUCCESS:
                    //scannerMessage.setText("Authentication success");
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    //scannerMessage.setText("No Finger print hardware detected");
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    //scannerMessage.setText("No finger print enrolled");
                    break;

                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    //scannerMessage.setText("No Finger print hardware detected");
                    break;

            }

            Executor executor = ContextCompat.getMainExecutor(context);
            BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    //Toast.makeText(context, "Finger Print authentication failed", Toast.LENGTH_SHORT).show();
                    //((FragmentActivity) context).finish();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    //Toast.makeText(context, "Finger Print authenticated", Toast.LENGTH_SHORT).show();
                    callAd();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    //Toast.makeText(context, "Finger Print authentication failed", Toast.LENGTH_SHORT).show();
                    //((FragmentActivity) context).finish();
                }
            });
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Authenticate")
                    .setDescription("Please place your finger on scanner")
                    .setNegativeButtonText("Cancel")
                    .build();

            biometricPrompt.authenticate(promptInfo);
        }else{
            Intent intent = new Intent(context, CustomKeypadActivity.class);
            intent.putExtra("inputType","6");
            startActivityForResult(intent,100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            callAd();
        }
    }
}