package com.klhk.whalecomp.DocFragment.DocActivity;

import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.R;

public class DescActivity extends AppCompatActivity {
    ImageView continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_desc);
        continueButton = findViewById(R.id.continueButton);


        EditText calculateText =  findViewById(R.id.calculateText);
        ImageView calculateButton = findViewById(R.id.calculateButton);
        TextView profit140Text =  findViewById(R.id.profit140Text);
        TextView profit20Text = findViewById(R.id.profit20Text);


        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!calculateText.getText().toString().trim().isEmpty()){
                    int amount = Integer.parseInt(calculateText.getText().toString());
                    double profit20 = (amount*.2);
                    double profit140 = (amount*1.4);

                    profit140Text.setText("$"+String.format("%.2f",profit140));
                    profit20Text.setText("$"+String.format("%.2f",profit20));
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        });


        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(DescActivity.this,Desc2Activity.class),4001);
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