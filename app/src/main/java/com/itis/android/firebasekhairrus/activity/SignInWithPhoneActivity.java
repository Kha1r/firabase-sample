package com.itis.android.firebasekhair.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.itis.android.firebasekhair.R;

import java.util.concurrent.TimeUnit;

public class SignInWithPhoneActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private Button btnSignIn, btnGoBack;

    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken token;

    private String verificationId;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SignInWithPhoneActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_phone);

        firebaseAuth = FirebaseAuth.getInstance();

        initFields();
        initClickListeners();
    }

    private void initFields() {
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnSignIn = findViewById(R.id.btn_sign_up);
        btnGoBack = findViewById(R.id.btn_go_back);
    }

    private void initClickListeners() {
        btnGoBack.setOnClickListener(v -> {
            finish();
        });
        btnSignIn.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                        }

                        @Override
                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            verificationId = s;
                            token = forceResendingToken;
                            new MaterialDialog.Builder(SignInWithPhoneActivity.this)
                                    .title(R.string.write_code)
                                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                                    .input(R.string.code, R.string.empty_code, (dialog, input) -> {
                                        PhoneAuthCredential credential = PhoneAuthProvider
                                                .getCredential(verificationId, input.toString());
                                        signInWithPhoneAuthCredential(credential);
                                    }).show();
                        }
                    });
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignInWithPhoneActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser user = task.getResult().getUser();
                        MainActivity.start(SignInWithPhoneActivity.this);
                        finish();
                    }
                });
    }
}
