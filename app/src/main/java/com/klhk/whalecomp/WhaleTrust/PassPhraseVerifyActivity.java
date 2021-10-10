package com.klhk.whalecomp.WhaleTrust;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_METHOD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class PassPhraseVerifyActivity extends AppCompatActivity {

    TextView passPhrase1,passPhrase2,passPhrase3,passPhrase4,passPhrase5,passPhrase6,passPhrase7,passPhrase8,passPhrase9,passPhrase10,passPhrase11,passPhrase12;
    TextView verifyPattern1,verifyPattern2,verifyPattern3,verifyPattern4,verifyPattern5,verifyPattern6,verifyPattern7,verifyPattern8,verifyPattern9,verifyPattern10,verifyPattern11,verifyPattern12;
    TextView verifyError;
    int clickCount =1;
    String phraseArr[];
    String walletAddress;
    String phrase;

    List<String> shuffleArray = new ArrayList<String>();
    boolean passError;
    ImageView passPhraseImg1,passPhraseImg2,passPhraseImg3,passPhraseImg4,passPhraseImg5,passPhraseImg6,passPhraseImg7,passPhraseImg8,passPhraseImg9,passPhraseImg10,passPhraseImg11,passPhraseImg12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_phrase_verify);
        System.loadLibrary("TrustWalletCore");
        phrase = getIntent().getStringExtra("phrase");
        walletAddress = getIntent().getStringExtra("walletAddress");
        passError = false;
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

        verifyError = findViewById(R.id.verifyError);

        passPhraseImg1 = findViewById(R.id.passPhraseImg1);
        passPhraseImg2 = findViewById(R.id.passPhraseImg2);
        passPhraseImg3 = findViewById(R.id.passPhraseImg3);
        passPhraseImg4 = findViewById(R.id.passPhraseImg4);
        passPhraseImg5 = findViewById(R.id.passPhraseImg5);
        passPhraseImg6 = findViewById(R.id.passPhraseImg6);
        passPhraseImg7 = findViewById(R.id.passPhraseImg7);
        passPhraseImg8 = findViewById(R.id.passPhraseImg8);
        passPhraseImg9 = findViewById(R.id.passPhraseImg9);
        passPhraseImg10 = findViewById(R.id.passPhraseImg10);
        passPhraseImg11 = findViewById(R.id.passPhraseImg11);
        passPhraseImg12 = findViewById(R.id.passPhraseImg12);
        phraseArr =phrase.split(" ");
        Log.e("phrase",phrase);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        for(int i=0;i<phraseArr.length;i++){
            shuffleArray.add(phraseArr[i]);
        }
        Collections.shuffle(shuffleArray);

        for(int i=0;i<shuffleArray.size();i++){
            if(i==0){ passPhrase1.setText(shuffleArray.get(i)); }
            if(i==1){ passPhrase2.setText(shuffleArray.get(i)); }
            if(i==2){ passPhrase3.setText(shuffleArray.get(i)); }
            if(i==3){ passPhrase4.setText(shuffleArray.get(i)); }
            if(i==4){ passPhrase5.setText(shuffleArray.get(i)); }
            if(i==5){ passPhrase6.setText(shuffleArray.get(i)); }
            if(i==6){ passPhrase7.setText(shuffleArray.get(i)); }
            if(i==7){ passPhrase8.setText(shuffleArray.get(i)); }
            if(i==8){ passPhrase9.setText(shuffleArray.get(i)); }
            if(i==9){ passPhrase10.setText(shuffleArray.get(i)); }
            if(i==10){ passPhrase11.setText(shuffleArray.get(i)); }
            if(i==11){ passPhrase12.setText(shuffleArray.get(i)); }

        }


        passPhraseImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg1,1,passPhrase1.getText().toString());

            }
        });
        passPhraseImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg2,2,passPhrase2.getText().toString());
            }
        });
        passPhraseImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg3,3,passPhrase3.getText().toString());
            }
        });
        passPhraseImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg4,4,passPhrase4.getText().toString());
            }
        });
        passPhraseImg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg5,5,passPhrase5.getText().toString());
            }
        });
        passPhraseImg6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg6,6,passPhrase6.getText().toString());
            }
        });
        passPhraseImg7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg7,7,passPhrase7.getText().toString());
            }
        });
        passPhraseImg8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg8,8,passPhrase8.getText().toString());
            }
        });
        passPhraseImg9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg9,9,passPhrase9.getText().toString());
            }
        });
        passPhraseImg10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg10,10,passPhrase10.getText().toString());
            }
        });
        passPhraseImg11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg11,11,passPhrase11.getText().toString());
            }
        });
        passPhraseImg12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSetPhrase(passPhraseImg12,12,passPhrase12.getText().toString());
            }
        });
    }
    String lastPhrase="";
    public void checkAndSetPhrase(ImageView passPhraseImg, int clickedNum,String pushedPhrase){
        Log.e("Count",String.valueOf(clickCount));
        Log.e("lastPhrase",lastPhrase);
        Log.e("pushedPhrase",pushedPhrase);
        try{
            if(!passError){
                Log.e("reched","1");
                //if(!lastPhrase.equalsIgnoreCase(pushedPhrase)){
                    //Log.e("reched","2");
                    buttonPressedEffect(passPhraseImg);
                    if(clickCount ==1 ){ verifyPattern1.setText(pushedPhrase);}
                    if(clickCount ==2 ){ verifyPattern2.setText(pushedPhrase);}
                    if(clickCount ==3 ){ verifyPattern3.setText(pushedPhrase);}
                    if(clickCount ==4 ){ verifyPattern4.setText(pushedPhrase);}
                    if(clickCount ==5 ){ verifyPattern5.setText(pushedPhrase);}
                    if(clickCount ==6 ){ verifyPattern6.setText(pushedPhrase);}
                    if(clickCount ==7 ){ verifyPattern7.setText(pushedPhrase);}
                    if(clickCount ==8 ){ verifyPattern8.setText(pushedPhrase);}
                    if(clickCount ==9 ){ verifyPattern9.setText(pushedPhrase);}
                    if(clickCount ==10 ){ verifyPattern10.setText(pushedPhrase);}
                    if(clickCount ==11 ){ verifyPattern11.setText(pushedPhrase);}
                    if(clickCount ==12 ){ verifyPattern12.setText(pushedPhrase);
                        Intent i = new Intent(getApplicationContext(), CustomKeypadActivity.class);
                        i.putExtra("inputType","1");
                        i.putExtra(WHALETRUST_ADDRESS,walletAddress);
                        i.putExtra(WHALETRUST_ADDRESS_PATTERN,phrase);
                        i.putExtra(WHALETRUST_ADDRESS_METHOD,"phrase");
//                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS,walletAddress);
//                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS_PATTERN,phrase);
                        startActivityForResult(i,1000);
                        finish();
                    }


                    if(clickCount-1!= Arrays.asList(phraseArr).indexOf(pushedPhrase)){
                        verifyError.setVisibility(View.VISIBLE);
                        passError=true;
                        Log.e("reched","3");
                    }else{
                        passPhraseImg.setOnClickListener(null);
                    }
                    lastPhrase=pushedPhrase;
                    clickCount++;


//                }else{
//
//                    if(clickCount>1){
//                        clickCount--;
//                        Log.e("reched","4");
//                    }
//                    if(clickCount==1){
//                        lastPhrase="";
//                        Log.e("reched","5");
//                    }else{
//                        Log.e("reched","6");
//                        lastPhrase = phraseArr[clickCount-1];
//                    }
//                    buttonUnPressedEffect(passPhraseImg);
//                    //passPhraseImg.setImageDrawable(getResources().getDrawable(R.drawable.pass_code_box));
//                    if(clickCount ==1 ){ verifyPattern1.setText("");}
//                    if(clickCount ==2 ){ verifyPattern2.setText("");}
//                    if(clickCount ==3 ){ verifyPattern3.setText("");}
//                    if(clickCount ==4 ){ verifyPattern4.setText("");}
//                    if(clickCount ==5 ){ verifyPattern5.setText("");}
//                    if(clickCount ==6 ){ verifyPattern6.setText("");}
//                    if(clickCount ==7 ){ verifyPattern7.setText("");}
//                    if(clickCount ==8 ){ verifyPattern8.setText("");}
//                    if(clickCount ==9 ){ verifyPattern9.setText("");}
//                    if(clickCount ==10 ){ verifyPattern10.setText("");}
//                    if(clickCount ==11 ){ verifyPattern11.setText("");}
//                    if(clickCount ==12 ){ verifyPattern12.setText("");}
//                }


            }else{
                Log.e("reched","7");
                if(lastPhrase.equalsIgnoreCase(pushedPhrase)){
                    Log.e("clickCount", String.valueOf(clickCount));
                    buttonUnPressedEffect(passPhraseImg);
                    if(clickCount>1){
                        Log.e("reched","9");
                        clickCount--;
                    }
                    if(clickCount==1){
                        Log.e("reched","10");
                        lastPhrase="";
                    }else{
                        Log.e("reched","11");
                        lastPhrase = phraseArr[clickCount-1];
                    }
                    if(clickCount ==1 ){ verifyPattern1.setText("");}
                    if(clickCount ==2 ){ verifyPattern2.setText("");}
                    if(clickCount ==3 ){ verifyPattern3.setText("");}
                    if(clickCount ==4 ){ verifyPattern4.setText("");}
                    if(clickCount ==5 ){ verifyPattern5.setText("");}
                    if(clickCount ==6 ){ verifyPattern6.setText("");}
                    if(clickCount ==7 ){ verifyPattern7.setText("");}
                    if(clickCount ==8 ){ verifyPattern8.setText("");}
                    if(clickCount ==9 ){ verifyPattern9.setText("");}
                    if(clickCount ==10 ){ verifyPattern10.setText("");}
                    if(clickCount ==11 ){ verifyPattern11.setText("");}
                    if(clickCount ==12 ){ verifyPattern12.setText("");}

                    verifyError.setVisibility(View.GONE);
                    passError=false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("Count",String.valueOf(clickCount));
        Log.e("lastPhrase",lastPhrase);
        Log.e("pushedPhrase",pushedPhrase);
    }
    public void buttonPressedEffect(ImageView button){
        button.setImageDrawable(getResources().getDrawable(R.drawable.pass_pressed1));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                button.setImageDrawable(getResources().getDrawable(R.drawable.pass_pressed2));
            }
        }, 130);
    }
    public void buttonUnPressedEffect(ImageView button){
        button.setImageDrawable(getResources().getDrawable(R.drawable.pass_pressed1));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                button.setImageDrawable(getResources().getDrawable(R.drawable.pass_code_box));
            }
        }, 130);
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