package com.example.securetext.Utility;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Context;
//import android.hardware.biometrics.BiometricPrompt;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class FingerPrintAuthenticator {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ViewGroup layout;

    Context context;
    AuthenticatorTask authenticatorTask;


    public FingerPrintAuthenticator(Context context, AuthenticatorTask authenticatorTask, ViewGroup layout) {
        this.context = context;
        this.authenticatorTask = authenticatorTask;
        this.layout = layout;
    }


    public void addAuthentication() {
        BiometricManager biometricManager = BiometricManager.from(context);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
//                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
//                startActivityForResult(enrollIntent, REQUEST_CODE);
                Toast.makeText(context, "No FingerPrint Assigned", Toast.LENGTH_SHORT).show();
                authenticatorTask.afterValidationSuccess();
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(context);

        biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                authenticatorTask.onValidationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authenticatorTask.afterValidationSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });


        // Lets user authenticate using either a Class 3 biometric or
        // their lock screen credential (PIN, pattern, or password).
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Validation")
                .setSubtitle("Use FingerPrint to Login")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();
        biometricPrompt.authenticate(promptInfo);


    }
}
