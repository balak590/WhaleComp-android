package com.klhk.whalecomp.SettingFragment;

import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.R;

public class NotificationManagemntActivity extends AppCompatActivity {
    ImageView toggleSystemNotification,toggleTransaction,toggleOptimizerRemainder,toggleProductUpDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_managemnt);
        toggleSystemNotification= findViewById(R.id.toggleSystemNotification);
        toggleTransaction= findViewById(R.id.toggleTransaction);
        toggleOptimizerRemainder= findViewById(R.id.toggleOptimizerRemainder);
        toggleProductUpDown= findViewById(R.id.toggleProductUpDown);

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        toggleSystemNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleSystemNotification.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleSystemNotification.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleSystemNotification.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
        toggleTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleTransaction.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleTransaction.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleTransaction.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
        toggleOptimizerRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleOptimizerRemainder.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleOptimizerRemainder.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleOptimizerRemainder.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
        toggleProductUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleProductUpDown.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                    toggleProductUpDown.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                }else{
                    toggleProductUpDown.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                }
            }
        });
    }
}