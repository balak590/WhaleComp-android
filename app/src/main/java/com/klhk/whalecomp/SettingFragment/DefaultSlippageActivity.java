package com.klhk.whalecomp.SettingFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;

import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_SLIPPAGE;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class DefaultSlippageActivity extends AppCompatActivity {

    EditText customSlippage;
    ImageView continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_slippage);
        customSlippage= findViewById(R.id.customSlippage);
        continueButton =findViewById(R.id.continueButton);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        if(!Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE).isEmpty()){
            customSlippage.setText(Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE));
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!customSlippage.getText().toString().isEmpty()){
                    Preference.getInstance().writePreference(WHALECOMP_SLIPPAGE,customSlippage.getText().toString());
                    finish();
                }
                else{
                    Toast.makeText(DefaultSlippageActivity.this, "Please provide slippage.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}