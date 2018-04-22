package com.itis.android.firebasekhairrus.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.itis.android.firebasekhairrus.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword, btnGoBack;

    private FirebaseAuth firebaseAuth;

    public static void start(Activity activity){
        Intent intent = new Intent(activity,ForgetPasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        firebaseAuth = FirebaseAuth.getInstance();

        initFields();
        initClickListeners();
    }

    private void initFields() {
        etEmail = findViewById(R.id.email);
        btnResetPassword = findViewById(R.id.btn_reset_pass);
        btnGoBack = findViewById(R.id.btn_go_back);
    }

    private void initClickListeners() {
        btnGoBack.setOnClickListener(v -> {
            finish();
        });
        btnResetPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ForgetPasswordActivity.this, getString(R.string.error_email), Toast.LENGTH_SHORT)
                        .show();
            } else {
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(command -> Toast.makeText(ForgetPasswordActivity.this, getString(R.string.check_email),
                        Toast.LENGTH_SHORT).show());
            }
        });
    }
}
