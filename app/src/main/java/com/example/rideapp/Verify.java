package com.example.rideapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {
    private static final String TAG = "PhoneAuth";
    private Button veryPhoneBtn;
    private Button resendCodeBtn;
    private Button codeVerifyBtn;

    private EditText phoneText;
    private EditText codeText;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth fbAuth;
    FirebaseUser firebaseUser;
    private CountryCodePicker countryCodePicker;
    String phoneNumber;
    private ProgressDialog loadingBar;
    private TextView phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);


        phoneText = (EditText) findViewById(R.id.phoneText);
        codeText = (EditText) findViewById(R.id.codeText);
        veryPhoneBtn = (Button) findViewById(R.id.veryPhoneBtn);
        codeVerifyBtn = (Button) findViewById(R.id.verifyCode);
        resendCodeBtn = (Button) findViewById(R.id.resendCode);
        countryCodePicker = findViewById(R.id.ccp);
        loadingBar = new ProgressDialog(this);


        codeVerifyBtn.setVisibility(View.INVISIBLE);
        resendCodeBtn.setVisibility(View.INVISIBLE);
        codeText.setVisibility(View.INVISIBLE);
//        veryPhoneBtn.setVisibility(View.VISIBLE);
//        phoneText.setVisibility(View.VISIBLE);
//        phoneText.setEnabled(true);

        countryCodePicker.registerCarrierNumberEditText(phoneText);

        fbAuth = FirebaseAuth.getInstance();

    }

    public void verifyPhoneNumber(View view){
        phoneNumber =  countryCodePicker.getFullNumberWithPlus();

        if (phoneNumber.isEmpty() || TextUtils.isEmpty(phoneNumber) || phoneNumber.length()<9) {
            phoneText.setError("Invalid Phone");
        }
        else {
            setUpVerificationCallbacks();
            countryCodePicker.setVisibility(View.INVISIBLE);
            phoneText.setVisibility(View.INVISIBLE);
            veryPhoneBtn.setVisibility(View.INVISIBLE);

            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    10,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    verificationCallbacks);        // OnVerificationStateChangedCallbacks
            // [END start_phone_auth]
        }
    }
    private void setUpVerificationCallbacks(){
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                loadingBar.dismiss();
                codeVerifyBtn.setVisibility(View.INVISIBLE);
                resendCodeBtn.setVisibility(View.INVISIBLE);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText(Verify.this, e.getMessage()+"Invalid Phone Number",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                resendingToken = token;

                resendCodeBtn.setVisibility(View.INVISIBLE);
                veryPhoneBtn.setVisibility(View.INVISIBLE);
                codeVerifyBtn.setVisibility(View.VISIBLE);
                phoneText.setVisibility(View.INVISIBLE);
                countryCodePicker.setVisibility(View.INVISIBLE);
                codeText.setVisibility(View.VISIBLE);

                loadingBar.dismiss();
            }
        };
    }

    public void verifyCode(View view){
        String code = codeText.getText().toString();
        if (code.isEmpty()){
            codeText.setError("Invalid Code");
        }
        else {
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
            Toast.makeText(this, "Code Verified", Toast.LENGTH_LONG).show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            resendCodeBtn.setVisibility(View.INVISIBLE);
                            countryCodePicker.setVisibility(View.INVISIBLE);
                            veryPhoneBtn.setVisibility(View.INVISIBLE);
                            codeVerifyBtn.setVisibility(View.INVISIBLE);
                            phoneText.setVisibility(View.INVISIBLE);
                            finish();
                            FirebaseUser user = task.getResult().getUser();

                            loadingBar.dismiss();
                            Toast.makeText(Verify.this,"Success",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Verify.this,RegisterUser.class);
                            intent.putExtra("PhoneNumber",phoneNumber);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                //Invalid verification code entered.
                            }
                        }
                    }
                });
    }
    public void resendCode(View view){
        String phoneNumber = phoneText.getText().toString();
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                5,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendingToken);
    }
}

