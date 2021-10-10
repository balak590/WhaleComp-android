package com.klhk.whalecomp.SettingFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WhaleTrust.TxConfirmActivity;
import com.klhk.whalecomp.WhaleTrust.TxSuccessActivity;
import com.klhk.whalecomp.utilities.FunctionCall;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class MnemonicDescActivity extends AppCompatActivity {

    ImageView continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemonic_desc);
        continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptPassword(MnemonicDescActivity.this);

            }
        });

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);
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
                    Toast.makeText(context, "Finger Print authentication failed", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(context, "Finger Print authenticated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplication(),MnemonicPhraseActivity.class));
                    finish();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(context, "Finger Print authentication failed", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            startActivity(new Intent(getApplication(),MnemonicPhraseActivity.class));
            finish();
        }
    }
}