package com.example.interncheck

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var loginBtn : Button
    private lateinit var showPass : TextView
    private lateinit var signUp : TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.etEmailLogin)
        password = findViewById(R.id.etPasswordLogin)
        loginBtn = findViewById(R.id.btnLogin)
        showPass = findViewById(R.id.tvShowHide)
        signUp = findViewById(R.id.tvSignUp)
        firebaseAuth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            loginUser()
        }

        showPass.setOnClickListener {
            showHidePassword()
        }

        signUp.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showHidePassword() {
        if (!TextUtils.isEmpty(password.text)) {
            if (showPass.text == "Hide Password") {
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                showPass.text = "Show Password"
            } else {
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPass.text = "Hide Password"
            }
        } else {
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            showPass.text = "Show Password"
        }
    }

    private fun loginUser() {

        val emailText = email.text.toString().trim()
        val passText = password.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passText)){
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Empty Fields!!")
                .setMessage("Email and password fields can't be empty.")
                .setPositiveButton("Okay"){_,_->}
                .create()
                .show()
        } else {
            firebaseAuth.signInWithEmailAndPassword(emailText, passText).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
