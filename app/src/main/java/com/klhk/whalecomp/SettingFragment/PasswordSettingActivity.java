package com.klhk.whalecomp.SettingFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WhaleTrust.SwapWhaleTrustActivity;

import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_TIMEOUT;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class PasswordSettingActivity extends AppCompatActivity {

    ImageView changePassword,toggleFingerPrint,toggleWhaleTrustseed,toggleCurrencyexchange,toggleOptStg1,toggleOptStg2,toggleOptStg3,signoffDurationButton;
    TextView signoffDurationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);
        changePassword = findViewById(R.id.changePassword);
        toggleFingerPrint = findViewById(R.id.toggleFingerPrint);
        toggleWhaleTrustseed = findViewById(R.id.toggleWhaleTrustseed);
        toggleCurrencyexchange = findViewById(R.id.toggleCurrencyexchange);
        toggleOptStg1 = findViewById(R.id.toggleOptStg1);
        toggleOptStg2 = findViewById(R.id.toggleOptStg2);
        toggleOptStg3 = findViewById(R.id.toggleOptStg3);
        signoffDurationButton = findViewById(R.id.signoffDurationButton);
        signoffDurationText = findViewById(R.id.signoffDurationText);

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("1")){
            signoffDurationText.setText("立即");
        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("2")){
            signoffDurationText.setText("如果離開1分鐘");
        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("3")){
            signoffDurationText.setText("如果離開5分鐘");
        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("4")){
            signoffDurationText.setText("如果離開1小時");
        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("5")){
            signoffDurationText.setText("如果離開5小時");
        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("6")){
            signoffDurationText.setText("永不");
        }

        if(Preference.getInstance().returnBoolean(WHALETRUST_LOCK_FINGERPRINT)){
            toggleFingerPrint.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
        }else{
            toggleFingerPrint.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
        }

        signoffDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordSettingActivity.this,R.style.CustomDialog);

                View v1 = getLayoutInflater().inflate(R.layout.set_lock_timeout,null);
                neo closeButton = v1.findViewById(R.id.closeButton);
                neo immediatelyButton = v1.findViewById(R.id.immediatelyButton);
                neo left1MinButton = v1.findViewById(R.id.left1MinButton);
                neo left5MinsButton = v1.findViewById(R.id.left5MinsButton);
                neo left1HourButton = v1.findViewById(R.id.left1HourButton);
                neo left5HrButton = v1.findViewById(R.id.left5HrButton);
                neo leftNeverButton = v1.findViewById(R.id.leftNeverButton);
                builder.setCancelable(false);

                builder.setView(v1);
                AlertDialog dialog = builder.show();
                ViewGroup viewGroupC1 = v1.findViewById(R.id.closeButton);
                final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                roundButtonPressed(closeButton,textView, PasswordSettingActivity.this,dialog);

                immediatelyButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ViewGroup viewGroupC1 = v.findViewById(R.id.immediatelyButton);
                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                        return setPasswordLock(immediatelyButton,textView,event,dialog,"1","立即");
                    }
                });

                left1MinButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ViewGroup viewGroupC1 = v.findViewById(R.id.left1MinButton);
                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                        return setPasswordLock(left1MinButton,textView,event,dialog,"2","如果離開1分鐘");
                    }
                });

                left5MinsButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ViewGroup viewGroupC1 = v.findViewById(R.id.left5MinsButton);
                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                        return setPasswordLock(left5MinsButton,textView,event,dialog,"3","如果離開5分鐘");
                    }
                });

                left1HourButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ViewGroup viewGroupC1 = v.findViewById(R.id.left1HourButton);
                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                        return setPasswordLock(left1HourButton,textView,event,dialog,"4","如果離開1小時");
                    }
                });

                left5HrButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ViewGroup viewGroupC1 = v.findViewById(R.id.left5HrButton);
                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                        return setPasswordLock(left5HrButton,textView,event,dialog,"5","如果離開5小時");
                    }
                });

                leftNeverButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ViewGroup viewGroupC1 = v.findViewById(R.id.leftNeverButton);
                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                        return setPasswordLock(leftNeverButton,textView,event,dialog,"6","永不");
                    }
                });

            }
        });


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomKeypadActivity.class);
                intent.putExtra("inputType","3");
                startActivity(intent);

            }
        });

        toggleFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleFingerPrint.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleFingerPrint.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                    Preference.getInstance().writeBooleanPreference(WHALETRUST_LOCK_FINGERPRINT,false);
                }else{
                    toggleFingerPrint.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                    Preference.getInstance().writeBooleanPreference(WHALETRUST_LOCK_FINGERPRINT,true);
                }
            }
        });
        toggleWhaleTrustseed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleWhaleTrustseed.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleWhaleTrustseed.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleWhaleTrustseed.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
        toggleCurrencyexchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleCurrencyexchange.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleCurrencyexchange.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleCurrencyexchange.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
        toggleOptStg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleOptStg1.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleOptStg1.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleOptStg1.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
        toggleOptStg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleOptStg2.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleOptStg2.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleOptStg2.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
        toggleOptStg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleOptStg3.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleOptStg3.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleOptStg3.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
    }

    public boolean setPasswordLock(neo button,LinearLayout textView, MotionEvent event, AlertDialog dialog,String number,String passwordText){
        try{


            if (button.isShapeContainsPoint(event.getX(), event.getY())) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        button.setStyle(neo.small_inner_shadow);
                        textView.setScaleX(textView.getScaleX() * 0.96f);
                        textView.setScaleY(textView.getScaleY() * 0.96f);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        button.setStyle(neo.drop_shadow);
                        textView.setScaleX(1);
                        textView.setScaleY(1);
                        Thread.sleep(60);
                        Preference.getInstance().writePreference(WHALETRUST_LOCK_TIMEOUT,number);
                        signoffDurationText.setText(passwordText);
                        dialog.cancel();
                        return true; // if you want to handle the touch event
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}