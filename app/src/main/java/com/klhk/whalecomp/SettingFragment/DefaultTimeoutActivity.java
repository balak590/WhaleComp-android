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
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_TIMEOUT;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class DefaultTimeoutActivity extends AppCompatActivity {

    EditText timeoutDuration;
    ImageView continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_timeout);
        timeoutDuration= findViewById(R.id.timeoutDuration);
        continueButton =findViewById(R.id.continueButton);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        if(!Preference.getInstance().returnValue(WHALECOMP_TIMEOUT).isEmpty()){
            timeoutDuration.setText(Preference.getInstance().returnValue(WHALECOMP_TIMEOUT));
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeoutDuration.getText().toString().isEmpty()){
                    Preference.getInstance().writePreference(WHALECOMP_TIMEOUT,timeoutDuration.getText().toString());
                    finish();
                }
                else{
                    Toast.makeText(DefaultTimeoutActivity.this, "Please provide default transaction timeout.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}