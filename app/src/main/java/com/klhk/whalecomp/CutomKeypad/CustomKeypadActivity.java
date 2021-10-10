package com.klhk.whalecomp.CutomKeypad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.WalletFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_METHOD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_PASSWORD;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class CustomKeypadActivity extends AppCompatActivity {
    ImageView numberButton0,numberButton1,numberButton2,numberButton3,numberButton4,numberButton5,numberButton6,numberButton7,numberButton8,numberButton9,backSpace;
    TextView number0,number1,number2,number3,number4,number5,number6,number7,number8,number9,pageDesc;
    ImageView input1,input2,input3,input4,input5,input6;
    List<Integer> shuffleArray = new ArrayList<Integer>();
    int counter =0;

    String passCode="";
    String passType,verifyPassCode;

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_keypad);
        numberButton0 = findViewById(R.id.numberButton0);
        numberButton1 = findViewById(R.id.numberButton1);
        numberButton2 = findViewById(R.id.numberButton2);
        numberButton3 = findViewById(R.id.numberButton3);
        numberButton4 = findViewById(R.id.numberButton4);
        numberButton5 = findViewById(R.id.numberButton5);
        numberButton6 = findViewById(R.id.numberButton6);
        numberButton7 = findViewById(R.id.numberButton7);
        numberButton8 = findViewById(R.id.numberButton8);
        numberButton9 = findViewById(R.id.numberButton9);
        backSpace = findViewById(R.id.backSpace);
        pageDesc = findViewById(R.id.pageDesc);

        number0 = findViewById(R.id.number0);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);
        input5 = findViewById(R.id.input5);
        input6 = findViewById(R.id.input6);
        Intent intent = getIntent();
        passType = intent.getStringExtra("inputType");
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);
        backButton.setVisibility(View.INVISIBLE);
        if(!passType.equalsIgnoreCase("6")){
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        //creating pass
        if(passType.equalsIgnoreCase("1")){
            pageDesc.setText("設置密碼");
        }
        //confirm create pass
        else if(passType.equalsIgnoreCase("2")){
            pageDesc.setText("再次確認密碼");
            verifyPassCode = intent.getStringExtra("verifyPassCode");
        }
        //change password check
        else if(passType.equalsIgnoreCase("3")){
            pageDesc.setText("輸入當前密碼");
            verifyPassCode = Preference.getInstance().returnValue(WHALETRUST_PASSWORD);
        }
        //new password
        else if(passType.equalsIgnoreCase("4")){
            pageDesc.setText("設置新密碼");
        }
        //verify new password
        else if(passType.equalsIgnoreCase("5")){
            pageDesc.setText("再次確認新密碼");
            verifyPassCode = intent.getStringExtra("verifyPassCode");
        }
        else if(passType.equalsIgnoreCase("6")){
            pageDesc.setText("輸入當前密碼");
            verifyPassCode = Preference.getInstance().returnValue(WHALETRUST_PASSWORD);
        }

        createNumberPad();
        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(backSpace);
                if(counter>0){
                    counter--;
                    passCode = passCode.substring(0, passCode.length() - 1);
                }

                setInputField();
            }
        });
    }

    public void createNumberPad(){
        shuffleArray = new ArrayList<Integer>();
        passCode="";
        input1.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        input2.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        input3.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        input4.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        input5.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        input6.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        counter=0;
        for(int i=0;i<10;i++){
            shuffleArray.add(i);
        }
        Collections.shuffle(shuffleArray);

        for(int i=0;i<shuffleArray.size();i++){
            if(i==0){ number0.setText(shuffleArray.get(i).toString()); }
            if(i==1){ number1.setText(shuffleArray.get(i).toString()); }
            if(i==2){ number2.setText(shuffleArray.get(i).toString()); }
            if(i==3){ number3.setText(shuffleArray.get(i).toString()); }
            if(i==4){ number4.setText(shuffleArray.get(i).toString()); }
            if(i==5){ number5.setText(shuffleArray.get(i).toString()); }
            if(i==6){ number6.setText(shuffleArray.get(i).toString()); }
            if(i==7){ number7.setText(shuffleArray.get(i).toString()); }
            if(i==8){ number8.setText(shuffleArray.get(i).toString()); }
            if(i==9){ number9.setText(shuffleArray.get(i).toString()); }


        }

        numberButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton0);
                if(counter<6){
                    counter++;
                    passCode=passCode+number0.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton1);
                if(counter<6){
                    counter++;
                    passCode=passCode+number1.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton2);
                if(counter<6){
                    counter++;
                    passCode=passCode+number2.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton3);
                if(counter<6){
                    counter++;
                    passCode=passCode+number3.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton4);
                if(counter<6){
                    counter++;
                    passCode=passCode+number4.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton5);
                if(counter<6){
                    counter++;
                    passCode=passCode+number5.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton6);
                if(counter<6){
                    counter++;
                    passCode=passCode+number6.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton7);
                if(counter<6){
                    counter++;
                    passCode=passCode+number7.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton8);
                if(counter<6){
                    counter++;
                    passCode=passCode+number8.getText().toString();
                    setInputField();
                }
            }
        });
        numberButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressedEffect(numberButton9);

                if(counter<6){
                    counter++;
                    passCode=passCode+number9.getText().toString();
                    setInputField();
                }
            }
        });
    }



    public void setInputField(){
        if(counter==0){
            input1.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        }
        if(counter ==1){
            input1.setImageDrawable(getResources().getDrawable(R.drawable.number_entered_input));
            input2.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        }
        if(counter ==2){
            input2.setImageDrawable(getResources().getDrawable(R.drawable.number_entered_input));
            input3.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        }
        if(counter ==3){
            input3.setImageDrawable(getResources().getDrawable(R.drawable.number_entered_input));
            input4.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        }
        if(counter ==4){
            input4.setImageDrawable(getResources().getDrawable(R.drawable.number_entered_input));
            input5.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        }
        if(counter ==5){
            input5.setImageDrawable(getResources().getDrawable(R.drawable.number_entered_input));
            input6.setImageDrawable(getResources().getDrawable(R.drawable.number_input));
        }
        if(counter ==6){
            input6.setImageDrawable(getResources().getDrawable(R.drawable.number_entered_input));
            Log.e("Passcode",passCode);
            if(passType.equalsIgnoreCase("1")){
                Intent intent = new Intent(getApplicationContext(),CustomKeypadActivity.class);
                intent.putExtra("inputType","2");
                intent.putExtra("verifyPassCode",passCode);
                intent.putExtra(WHALETRUST_ADDRESS,getIntent().getStringExtra(WHALETRUST_ADDRESS));
                intent.putExtra(WHALETRUST_ADDRESS_PATTERN,getIntent().getStringExtra(WHALETRUST_ADDRESS_PATTERN));
                intent.putExtra(WHALETRUST_ADDRESS_METHOD,getIntent().getStringExtra(WHALETRUST_ADDRESS_METHOD));
                startActivityForResult(intent,2000);
                //finish();
            }
            else if(passType.equalsIgnoreCase("2")){
                if(verifyPassCode.equalsIgnoreCase(passCode)){
                    //triggerNotification("您的密碼已設置完成",false,this);
                    //Preference.getInstance().writePreference("pass_key_word_whaletrust",passCode);
                    try{
                        JSONObject object = new JSONObject();
                        object.put(WHALETRUST_ADDRESS,getIntent().getStringExtra(WHALETRUST_ADDRESS));
                        object.put(WHALETRUST_ADDRESS_PATTERN,getIntent().getStringExtra(WHALETRUST_ADDRESS_PATTERN));
                        object.put(WHALETRUST_PASSWORD,passCode);
                        object.put(WHALETRUST_ADDRESS_METHOD,getIntent().getStringExtra(WHALETRUST_ADDRESS_METHOD));
                        Log.e("reahced",object.toString());
                        JSONArray array = new JSONArray();
                        if(!Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST).isEmpty()){
                            array =  new JSONArray(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST));
                        }

                        if(array.length()==0){
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS_SELECTED,getIntent().getStringExtra(WHALETRUST_ADDRESS));
                        }
                        array.put(object);
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS_LIST,array.toString());
                        Preference.getInstance().writePreference(WALLET_SELECTED_ADDRESS,object.getString(WHALETRUST_ADDRESS));
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS,object.getString(WHALETRUST_ADDRESS));
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS_PATTERN,object.getString(WHALETRUST_ADDRESS_PATTERN));
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS_METHOD,object.getString(WHALETRUST_ADDRESS_METHOD));
                        Preference.getInstance().writePreference(WHALETRUST_PASSWORD,object.getString(WHALETRUST_PASSWORD));
                        Preference.getInstance().writePreference(WALLET_SELECTED,WHALETRUST);

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else{
                    triggerNotification("密碼前後輸入不一致",true,this);
                }
            }
            else if(passType.equalsIgnoreCase("3")){
                if(verifyPassCode.equalsIgnoreCase(passCode)){
                    Intent intent = new Intent(getApplicationContext(),CustomKeypadActivity.class);
                    intent.putExtra("inputType","4");
                    startActivityForResult(intent,2000);
                    //finish();
                }else{
                    triggerNotification("您輸入的密碼不正確",true,this);
                }
            }
            else if(passType.equalsIgnoreCase("4")){
                Intent intent = new Intent(getApplicationContext(),CustomKeypadActivity.class);
                intent.putExtra("inputType","5");
                intent.putExtra("verifyPassCode",passCode);
                startActivityForResult(intent,2000);
                //finish();
            }
            else if(passType.equalsIgnoreCase("5")){
                if(verifyPassCode.equalsIgnoreCase(passCode)){
                    try{
                        JSONArray array = new JSONArray(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST));
                        JSONArray newArray = new JSONArray();
                        Log.e("JSONARRAY",array.toString());
                        for(int i=0;i<array.length();i++){
                            JSONObject obj = new JSONObject(array.get(i).toString());
                            JSONObject object = new JSONObject();
                            object.put(WHALETRUST_ADDRESS,obj.getString(WHALETRUST_ADDRESS));
                            object.put(WHALETRUST_ADDRESS_PATTERN,obj.getString(WHALETRUST_ADDRESS_PATTERN));
                            object.put(WHALETRUST_ADDRESS_METHOD,obj.getString(WHALETRUST_ADDRESS_METHOD));


                            if(obj.get(WHALETRUST_ADDRESS).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){
                                object.put(WHALETRUST_PASSWORD,passCode);
                            }else{
                                object.put(WHALETRUST_PASSWORD,obj.getString(WHALETRUST_PASSWORD));
                            }
                            newArray.put(object);
                        }
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS_LIST,newArray.toString());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Preference.getInstance().writePreference(WHALETRUST_PASSWORD,passCode);
                    //triggerNotification("您的密碼已修改完成",false,this);
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    //finish();
                }else{
                    triggerNotification("密碼前後輸入不一致",true,this);
                }
            }

            else if(passType.equalsIgnoreCase("6")){
                if(verifyPassCode.equalsIgnoreCase(passCode)){
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else{
                    triggerNotification("您輸入的密碼不正確",true,this);
                }
            }

        }
    }

    public void buttonPressedEffect(ImageView button){
        button.setImageDrawable(getResources().getDrawable(R.drawable.number_pressed));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                button.setImageDrawable(getResources().getDrawable(R.drawable.number_unpressed));
            }
        }, 130);
    }

    public void triggerNotification(String notificationText, boolean isError, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomKeypadActivity.this,R.style.CustomDialog);

        View v = getLayoutInflater().inflate(R.layout.password_notification,null);
        neo confirmButton = v.findViewById(R.id.confirmButton);

        TextView alertText = v.findViewById(R.id.alertText);
        alertText.setText(notificationText);
        if(isError){
            alertText.setTextColor(getResources().getColor(R.color.red));
        }
        builder.setCancelable(false);

        builder.setView(v);
        AlertDialog dialog = builder.show();

        confirmButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try{
                    ViewGroup viewGroupC1 = v.findViewById(R.id.confirmButton);
                    final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                    if (confirmButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                confirmButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.96f);
                                textView.setScaleY(textView.getScaleY() * 0.96f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                confirmButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                dialog.cancel();
                                if(!isError){
                                    ((Activity) context).finish();
                                }else{
                                    createNumberPad();
                                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2000){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}