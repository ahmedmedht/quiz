package com.example.quizproject.signinpages

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.fragment_login.*

// TODO: Rename parameter arguments, choose names that match

class LoginFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = Firebase.auth

        val navController=Navigation.findNavController(view)
        txt_register.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_createFragment)
        }
        txt_forget_password.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_forgetPassFragment)
        }
        btn_login_login.setOnClickListener {
            val email=edt_txt_email_login.text.toString()
            val pass=edt_txt_pass_login.text.toString()


            if(email.equals("")){
                edt_txt_email_create.setError("Email is required!")
            }else if(pass.equals("")){
                edt_txt_password_create.setError("Password is required!")
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edt_txt_email_create.setError("Please provide valid email!")
            }else if(pass.length < 6){
                edt_txt_password_create.setError("Min password lengh should be 6 characters!")
            }
            else {
                signin(email, pass, navController)
            }



        }

    }

    private fun signin(email: String, pass: String, navController: NavController) {
        activity?.let {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(it){
                    task ->

                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser

                    if (user != null) {
                        if(user.isEmailVerified) {

                            Toast.makeText(context, "Successfully Login", Toast.LENGTH_LONG).show()
                            navController.navigate(R.id.action_loginFragment_to_homeActivity)
                        }else{
                            user.sendEmailVerification()
                            Toast.makeText(context, "Check your email to verify your account!", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context,"Faild to login! please check your credentials ", Toast.LENGTH_LONG).show()

                }
            }
        }
    }


}