package com.klhk.whalecomp.WalletFragment.StratergyScreens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.DocFragment.DocActivity.Desc2Activity;
import com.klhk.whalecomp.DocFragment.DocActivity.DescActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.klhk.whalecomp.utilities.Hex;
import com.klhk.whalecomp.utilities.Strategy001;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.klhk.whalecomp.utilities.Constants.APY_CALCULATED_EPS;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.CAN_EXECUTE_COMPOUNDING;
import static com.klhk.whalecomp.utilities.Constants.EPSSTAKED;
import static com.klhk.whalecomp.utilities.Constants.EPS_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.NEXT_EXECUTION_TIME;
import static com.klhk.whalecomp.utilities.Constants.STAKED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.Hex.hexToBigInteger;
import static com.klhk.whalecomp.utilities.Hex.hexToDecimal;
import static com.klhk.whalecomp.utilities.Strategy001.claimableRewards;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StrategyInfoActivity extends AppCompatActivity {
//    neo compoundNotAvailable,compoundAvailable;
    ImageView downButton;
    neo stopCompounding,exitStrategy,claimSwapAll,addEpsButton,compoundTimerButton;
    TextView tokenAmountText,amountText,stakedTokenAmountText,stakedAmountText,stakedTokenAmountText1,stakedAmountText1,claimSwapText,pendingTime,apyText,rewardsAmountText;

    LinearLayout addStakeList,headingTab,pendingTimeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_info);

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);
        stopCompounding = findViewById(R.id.stopCompounding);
        exitStrategy = findViewById(R.id.exitStrategy);
        claimSwapAll = findViewById(R.id.claimSwapAll);
        addStakeList = findViewById(R.id.addStakeList);

        tokenAmountText = findViewById(R.id.tokenAmountText);
        amountText = findViewById(R.id.amountText);
        stakedTokenAmountText = findViewById(R.id.stakedTokenAmountText);
        stakedAmountText = findViewById(R.id.stakedAmountText);
        stakedTokenAmountText1 = findViewById(R.id.stakedTokenAmountText1);
        stakedAmountText1 = findViewById(R.id.stakedAmountText1);
        claimSwapText = findViewById(R.id.claimSwapText);
        apyText = findViewById(R.id.apyText);
        downButton = findViewById(R.id.downButton);
        headingTab = findViewById(R.id.headingTab);
        rewardsAmountText = findViewById(R.id.rewardsAmountText);

//        compoundNotAvailable = findViewById(R.id.compoundNotAvailable);
//        compoundAvailable = findViewById(R.id.compoundAvailable);
        pendingTime = findViewById(R.id.pendingTime);
        pendingTimeLayout = findViewById(R.id.pendingTimeLayout);
        compoundTimerButton = findViewById(R.id.compoundTimerButton);


        addEpsButton = findViewById(R.id.addEpsButton);

        TextPaint paint = amountText.getPaint();
        float width = paint.measureText(amountText.getText().toString());

        Shader textShader = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#0159FF"),
                        Color.parseColor("#01D1FF"),
                }, null, Shader.TileMode.CLAMP);
        tokenAmountText.getPaint().setShader(textShader);
        textShader = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#0159FF"),
                        Color.parseColor("#01D1FF"),
                }, null, Shader.TileMode.CLAMP);
        amountText.getPaint().setShader(textShader);
        rewardsAmountText.getPaint().setShader(textShader);

        Shader textShader1 = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#49CFE6"),
                        Color.parseColor("#51EAC9"),
                }, null, Shader.TileMode.CLAMP);
        claimSwapText.getPaint().setShader(textShader1);
        textShader1 = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#FAA228"),
                        Color.parseColor("#EFD341"),
                }, null, Shader.TileMode.CLAMP);
        apyText.getPaint().setShader(textShader1);

//        if(!Preference.getInstance().returnBoolean(CAN_EXECUTE_COMPOUNDING)){
//            compoundNotAvailable.setVisibility(View.VISIBLE);
//            compoundAvailable.setVisibility(View.GONE);
//        }else{
//            compoundNotAvailable.setVisibility(View.GONE);
//            compoundAvailable.setVisibility(View.VISIBLE);
//            compoundAvailableText.setTextColor(getResources().getColor(R.color.bg_color));
//        }


        addEpsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.addEpsButton);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                try{
                    if (addEpsButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                addEpsButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.99f);
                                textView.setScaleY(textView.getScaleY() * 0.99f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                addEpsButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                startActivity(new Intent(StrategyInfoActivity.this, Desc2Activity.class));
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });


        try{
            JSONArray array = new JSONArray();
            if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()){
                array =  new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
            }
            for(int i=0;i<array.length();i++) {
                Log.e("updating", "amount1");
                JSONObject object = new JSONObject(array.get(i).toString());
                if (object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))) {

                    if (object.get(EPSSTAKED).toString().equalsIgnoreCase("yes")) {

                        try {
                            Log.e("APY calc", String.format("%.2f", Double.parseDouble(object.get(APY_CALCULATED_EPS).toString())));
                            String apyCalc = String.format("%.2f", Double.parseDouble(object.get(APY_CALCULATED_EPS).toString()));
                            apyText.setText(apyCalc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try{
                            if(object.getBoolean(CAN_EXECUTE_COMPOUNDING)){
//                                複利滾動
                                pendingTime.setText("複利滾動");
                                compoundTimerButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.compoundTimerButton);
                                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                        try{
                                            if (compoundTimerButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:
                                                        compoundTimerButton.setStyle(neo.small_inner_shadow);
//                                textView.setScaleX(textView.getScaleX() * 0.95f);
//                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event

                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        compoundTimerButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(80);
                                                        Intent i = new Intent(StrategyInfoActivity.this, StrategyStepsActivity.class);
                                                        i.putExtra("stepName","1");
                                                        startActivity(i);
                                                        return true; // if you want to handle the touch event
                                                }
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            return false;
                                        }
                                        return false;
                                    }
                                });
                            }else{
                                Date date = new Date(Long.parseLong(object.get(NEXT_EXECUTION_TIME).toString()));
                                Date date2 = new Date(System.currentTimeMillis());
                                long millis = date.getTime() - date2.getTime();
                                int diffInhr = (int) (millis / (1000 * 60 * 60));
                                int diffInmin = (int) ((millis / (1000 * 60)) % 60);
                                pendingTime.setText("下次滾動操作預計在"+String.valueOf(diffInhr)+"小時"+String.valueOf(diffInmin)+"分鐘后");
                                pendingTime.setTextColor(getResources().getColor(R.color.bg_color));
                                pendingTimeLayout.setBackground(getResources().getDrawable(R.drawable.grey_gradient));
                                compoundTimerButton.setDark_color(getResources().getColor(R.color.dark_shadow));
                                compoundTimerButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.compoundTimerButton);
                                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                        try{
                                            if (compoundTimerButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        compoundTimerButton.setStyle(neo.small_inner_shadow);
//                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
//                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        compoundTimerButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(40);
                                                        return true; // if you want to handle the touch event
                                                }
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            return false;
                                        }
                                        return false;
                                    }
                                });

                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        compoundAvailable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(StrategyInfoActivity.this,StrategyStepsActivity.class);
//                i.putExtra("stepName","1");
//                startActivity(i);
//                finish();
//            }
//        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                //String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), EPS_ADDRESS);
                double amountOut = FunctionCall.getSwapAmount(EPS_ADDRESS,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
                String totalBalance = Strategy001.totalBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                String totalLockedOutput = Strategy001.lockedBalances(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                String totalLocked = "0x"+totalLockedOutput.substring(2,66);
                int totalLockedCount = Hex.hexToInteger("0x"+totalLockedOutput.substring(258,322));
                String claimBalance = claimableRewards(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                String[] claimBal = claimBalance.split("000000000000000000000000a7f552078dcc247c2684336020c03648500c6d9f");
                String epsClaim = FunctionCall.divideBy18(hexToBigInteger("0x"+claimBal[1].substring(0,64)));
//                int totalLockedCount = Integer.parseInt(totalLockedOutput.substring(258,322));

                String totalLocked1 = Strategy001.unLockedBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tokenAmountText.setText(String.format("%.4f", Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger((totalBalance))))));
                            double amountUSD1 = Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger((totalBalance))))* amountOut;
                            amountText.setText("≈ $"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
                            stakedTokenAmountText.setText(String.format("%.4f", Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger(totalLocked)))));
                            amountUSD1 = Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger(totalLocked))) * amountOut;
                            stakedAmountText.setText("≈ $"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
                            stakedTokenAmountText1.setText(String.format("%.4f", Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger(totalLocked1)))));
                            amountUSD1 = Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger(totalLocked1))) * amountOut;
                            stakedAmountText1.setText("≈ $"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
                            rewardsAmountText.setText(String.format("%.4f", Double.parseDouble(epsClaim)));

                            if(Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger(totalLocked1)))>0){
                                claimSwapAll.setVisibility(View.VISIBLE);
                            }

                            if(addStakeList.getChildCount()>0){
                                addStakeList.removeAllViews();
                            }
                            int count =322;
                            for(int i=0;i<totalLockedCount;i++){
                                headingTab.setVisibility(View.VISIBLE);
                                View view = getLayoutInflater().inflate(R.layout.template_stake_item,null,false);
                                TextView stakeCount = view.findViewById(R.id.stakeCount);
                                TextView stakeAmount = view.findViewById(R.id.stakeAmount);
                                TextView stakePercent = view.findViewById(R.id.stakePercent);
                                TextView stakeDate = view.findViewById(R.id.stakeDate);
                                Log.e("count1",String.valueOf(count)+","+String.valueOf(count+64));
                                Log.e("next hex1",totalLockedOutput.substring(count,count+64));
                                Double stakeAmountValue = Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger("0x"+totalLockedOutput.substring(count,count+64))));
                                count=count+64;
                                Log.e("count2",String.valueOf(count)+","+String.valueOf(count+64));
                                Log.e("next hex2",totalLockedOutput.substring(count,count+64));
                                Log.e("Date", String.valueOf(Hex.hexToLong("0x"+totalLockedOutput.substring(count,count+64))));

                                //Date date = new Date(Hex.hexToLong("0x"+totalLockedOutput.substring(count,count+64))*1000);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(Hex.hexToLong("0x"+totalLockedOutput.substring(count,count+64))*1000);
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                stakeDate.setText(fmt.format(calendar.getTime()));
                                stakeAmount.setText(String.format("%.4f", stakeAmountValue));
                                count=count+64;

                                double stakePercenValue = (stakeAmountValue/Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger(totalLocked))))*100;
                                stakePercent.setText(String.format("%.2f", stakePercenValue)+"%");
                                stakeCount.setText(String.valueOf(i+1));

                                addStakeList.addView(view);
                            }

                            if(totalLockedCount>3){
                                downButton.setVisibility(View.VISIBLE);

                            }else{
                                downButton.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).start();

        claimSwapAll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.claimSwapAll);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (claimSwapAll.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                claimSwapAll.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                claimSwapAll.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(100);
                                triggerNotification(getResources().getString(R.string.eps_claim_stake_text),1);
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });

        exitStrategy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.exitStrategy);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (exitStrategy.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                exitStrategy.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                exitStrategy.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(100);
                                triggerNotification(getResources().getString(R.string.eps_exit_strategy),2);
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });


        stopCompounding.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.stopCompounding);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (stopCompounding.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                stopCompounding.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                stopCompounding.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(100);
                                triggerNotification(getResources().getString(R.string.eps_stop_compunding),3);
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });

    }

    public void triggerNotification(String notificationText,int strId){
        AlertDialog.Builder builder = new AlertDialog.Builder(StrategyInfoActivity.this,R.style.CustomDialog);

        View v = getLayoutInflater().inflate(R.layout.strategy_notification,null);
        ImageView confirmButton = v.findViewById(R.id.confirmButton);
        ImageView cancelButton = v.findViewById(R.id.cancelButton);
        TextView alertText = v.findViewById(R.id.notificationText);
        alertText.setText(notificationText);
        builder.setCancelable(false);

        builder.setView(v);
        AlertDialog dialog = builder.show();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strId==1){
                    Intent i = new Intent(StrategyInfoActivity.this,StrategyStepsActivity.class);
                    i.putExtra("stepName","7");
                    startActivity(i);
                }
                dialog.cancel();
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });
    }
}