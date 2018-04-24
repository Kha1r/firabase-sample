package com.itis.android.firebasekhairrus.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.itis.android.firebasekhairrus.R

class ForgetPasswordActivity : AppCompatActivity() {

    private var etEmail: EditText? = null
    private var btnResetPassword: Button? = null
    private var btnGoBack: Button? = null

    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        firebaseAuth = FirebaseAuth.getInstance()

        initFields()
        initClickListeners()
    }

    private fun initFields() {
        etEmail = findViewById(R.id.email)
        btnResetPassword = findViewById(R.id.btn_reset_pass)
        btnGoBack = findViewById(R.id.btn_go_back)
    }

    private fun initClickListeners() {
        btnGoBack!!.setOnClickListener { finish() }
        btnResetPassword!!.setOnClickListener {
            val email = etEmail!!.text.toString().trim{ it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@ForgetPasswordActivity, getString(R.string.error_email), Toast.LENGTH_SHORT)
                        .show()
            } else {
                firebaseAuth!!.sendPasswordResetEmail(email)
                        .addOnCompleteListener {
                            Toast.makeText(this@ForgetPasswordActivity, getString(R.string.check_email),
                                    Toast.LENGTH_SHORT).show()
                        }
            }
        }
    }

    companion object {

        fun start(activity: Activity) {
            val intent = Intent(activity,ForgetPasswordActivity::class.java) //Intent(activity, ForgetPasswordActivity::class)
            activity.startActivity(intent)
        }
    }
}
