package com.itis.android.firebasekhairrus.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.itis.android.firebasekhairrus.R

import java.util.concurrent.TimeUnit

class SignInWithPhoneActivity : AppCompatActivity() {

    private var etPhoneNumber: EditText? = null
    private var btnSignIn: Button? = null
    private var btnGoBack: Button? = null

    private var firebaseAuth: FirebaseAuth? = null
    private var token: PhoneAuthProvider.ForceResendingToken? = null

    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_with_phone)

        firebaseAuth = FirebaseAuth.getInstance()

        initFields()
        initClickListeners()
    }

    private fun initFields() {
        etPhoneNumber = findViewById(R.id.et_phone_number)
        btnSignIn = findViewById(R.id.btn_sign_up)
        btnGoBack = findViewById(R.id.btn_go_back)
    }

    private fun initClickListeners() {
        btnGoBack!!.setOnClickListener { finish() }
        btnSignIn!!.setOnClickListener {
            val phoneNumber = etPhoneNumber!!.text.toString()
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {}

                        override fun onVerificationFailed(e: FirebaseException) {}

                        override fun onCodeSent(s: String?,
                                forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
                            super.onCodeSent(s, forceResendingToken)
                            verificationId = s
                            token = forceResendingToken
                            MaterialDialog.Builder(this@SignInWithPhoneActivity)
                                    .title(R.string.write_code)
                                    .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                                    .input(R.string.code, R.string.empty_code) { _, input ->
                                        val credential = PhoneAuthProvider
                                                .getCredential(verificationId!!, input.toString())
                                        signInWithPhoneAuthCredential(credential)
                                    }.show()
                        }
                    })
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this@SignInWithPhoneActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        val user = task.result.user
                        MainActivity.start(this@SignInWithPhoneActivity)
                        finish()
                    }
                }
    }

    companion object {

        fun start(activity: Activity) {
            val intent = Intent(activity, SignInWithPhoneActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
