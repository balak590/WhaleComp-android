package com.klhk.whalecomp.utilities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class BioMetricDetector {
    Context context;

    public BioMetricDetector(Context context) {
        this.context = context;
    }

//    BiometricManager biometricManager = androidx.biometric.BiometricManager.from(context);
//        switch (BiometricManager.Authenticators) {
//
//        // this means we can use biometric sensor
//        case BiometricManager.BIOMETRIC_SUCCESS:
//
//            break;
//
//        // this means that the device doesn't have fingerprint sensor
//        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//
//            break;
//
//        // this means that biometric sensor is not available
//        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//
//            break;
//
//        // this means that the device doesn't contain your fingerprint
//        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//
//            break;
//    }
//    // creating a variable for our Executor
//    Executor executor = ContextCompat.getMainExecutor(context);
//    // this will give us result of AUTHENTICATION
//    final BiometricPrompt biometricPrompt = new BiometricPrompt(context, executor, new BiometricPrompt.AuthenticationCallback() {
//        @Override
//        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//            super.onAuthenticationError(errorCode, errString);
//        }
//
//        // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
//        @Override
//        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//            super.onAuthenticationSucceeded(result);
//        }
//        @Override
//        public void onAuthenticationFailed() {
//            super.onAuthenticationFailed();
//        }
//    });
    // creating a variable for our promptInfo
    // BIOMETRIC DIALOG
    final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("GFG")
            .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
}
