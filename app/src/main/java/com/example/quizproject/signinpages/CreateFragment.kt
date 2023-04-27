package com.example.quizproject.signinpages

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizproject.model.AccountInfo
import com.example.quizproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create.*


class CreateFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_create, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        mAuth = Firebase.auth

        txt_login_create.setOnClickListener {
            navController.navigate(R.id.action_createFragment_to_loginFragment)
        }
        btn_create_account_create.setOnClickListener {
            val email=edt_txt_email_create.text.toString()
            val pass=edt_txt_password_create.text.toString()
            val username=edt_txt_username_create.text.toString()

            if(username.equals("")){
                edt_txt_username_create.setError("UserName is required!")
            }else if(email.equals("")){
                edt_txt_email_create.setError("Email is required!")
            }else if(pass.equals("")){
                edt_txt_password_create.setError("Password is required!")
            }else if(edt_txt_password_check_create.text.toString().equals("")){
                edt_txt_password_check_create.setError("Confirm Password is required!")
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edt_txt_email_create.setError("Please provide valid email!")
            }else if(pass.length < 6){
                edt_txt_password_create.setError("Min password lengh should be 6 characters!")
            }else if(!edt_txt_password_check_create.text.toString().equals(pass)){
                edt_txt_password_check_create.setError("Confirm Password not equal Password!")
            }
            else {
                signup(username, email, pass, navController)
            }
        }





    }

        private fun signup(name: String,email: String, pass: String, navController: NavController) {
            activity?.let {
                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(
                        it
                    ){ task ->
                        if (task.isSuccessful) {
                            addUserToDatebase(name,email,mAuth.currentUser?.uid!!)

                            Toast.makeText(context, "Successfully Registered", Toast.LENGTH_LONG)
                                .show()
                            navController.navigate(R.id.action_createFragment_to_loginFragment)

                        } else {
                            Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show()

                        }
                    }
            }

        }
    private fun addUserToDatebase(name: String, email: String, uid: String) {
        mDbRef= FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).setValue(AccountInfo(name,email,uid))
    }
}






