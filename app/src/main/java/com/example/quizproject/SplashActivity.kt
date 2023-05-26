package com.example.quizproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val checkStaySignIn=sharedPreferences.getBoolean("Stay sign",false)
        mAuth = Firebase.auth

        // Set the duration of the splash screen
        Handler().postDelayed({
            // Start your app main activity
            startActivity(Intent(this, MainActivity::class.java))
            if (checkStaySignIn){
                val getEmail=sharedPreferences.getString("Email","")
                val getPass=sharedPreferences.getString("Pass","")
                if (getEmail!="" && getPass!="") getEmail?.let {
                    if (getPass != null) {
                        signin(it, getPass)
                    }
                }


            }
            // Finish this activity
            finish()
        }, 4000)
    }

    private fun signin(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this){
                task ->

            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    if(user.isEmailVerified) {

                        Toast.makeText(this, "Successfully Login", Toast.LENGTH_LONG).show()
                        goHomeActivity()
                    }else{
                        user.sendEmailVerification()
                        Toast.makeText(this, "Check your email to verify your account!", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this,"Faild to login! please check your credentials ", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun goHomeActivity() {
        val i=Intent(this,HomeActivity::class.java)
        startActivity(i)
    }
}