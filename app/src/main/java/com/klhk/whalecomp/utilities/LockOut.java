package com.klhk.whalecomp.utilities;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.WhaleTrust.TxConfirmActivity;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import static com.klhk.whalecomp.utilities.Constants.LAST_DATE_MILLI;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_TIMEOUT;

public class LockOut {



    public static void checkTimeout(Context context){
        long millis = System.currentTimeMillis();
        long TimeInforground = millis;
        if(!Preference.getInstance().returnValue(LAST_DATE_MILLI).isEmpty()){
            TimeInforground=millis - Long.parseLong(Preference.getInstance().returnValue(LAST_DATE_MILLI));
        }
        //promptPassword(context);
        if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("1")){
            //always
            promptPassword(context);
        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("2")){
            //1 min
            if(TimeInforground>1000){
                promptPassword(context);
            }
        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("3")){
            //5 min
            if(TimeInforground>5000){
                promptPassword(context);
            }

        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("4")){
            //1 hour
            if(TimeInforground>1000*60){
                promptPassword(context);
            }

        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("5")){
            //5 hour
            if(TimeInforground>1000*60*5){
                promptPassword(context);
            }

        }else if(Preference.getInstance().returnValue(WHALETRUST_LOCK_TIMEOUT).equalsIgnoreCase("6")){
            //never

        }else{
            promptPassword(context);
        }

    }

    public static void promptPassword(Context context){
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
                    ((FragmentActivity) context).finish();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(context, "Finger Print authenticated", Toast.LENGTH_SHORT).show();
//                        SendFunds sendFunds = new SendFunds();
//                        sendFunds.execute(i.getStringExtra("address"),i.getStringExtra("amount"));
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
            if(!Preference.getInstance().returnValue("pass_key_word_whaletrust").isEmpty()){
                Intent intent = new Intent(context, CustomKeypadActivity.class);
                intent.putExtra("inputType","6");
                context.startActivity(intent);
            }
        }
    }
}
