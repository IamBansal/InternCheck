package com.example.interncheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var username : EditText
    private lateinit var city : EditText
    private lateinit var signupBtn : Button
    private lateinit var logIn : TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        email = findViewById(R.id.etEmailSignup)
        password = findViewById(R.id.etPasswordSignup)
        username = findViewById(R.id.etUsername)
        city = findViewById(R.id.etCity)
        signupBtn = findViewById(R.id.btnSignUp)
        logIn = findViewById(R.id.tvLogIn)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        signupBtn.setOnClickListener {
            signupUser()
        }

        logIn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

    }

    private fun signupUser() {
        val emailText = email.text.toString().trim()
        val passText = password.text.toString().trim()
        val usernameText = username.text.toString().trim()
        val cityText = city.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passText) || TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(cityText)){
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Empty Fields!!")
                .setMessage("Fields can't be empty.\nFill all then hit SignUp.")
                .setPositiveButton("Okay"){_,_->}
                .create()
                .show()
        } else {

            firebaseAuth.createUserWithEmailAndPassword(emailText, passText).addOnCompleteListener{task ->
                if (task.isSuccessful){

                    val map = HashMap<String, String>()
                    map["Username"] = usernameText
                    map["City"] = cityText
                    firebaseDatabase.reference.child("UserIntern").child(firebaseAuth.currentUser!!.uid).setValue(map).addOnCompleteListener{task1 ->
                        if (task1.isSuccessful){
                            Toast.makeText(this, "Signed up successfully.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, task1.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}