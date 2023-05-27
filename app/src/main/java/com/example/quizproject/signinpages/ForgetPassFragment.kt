package com.example.quizproject.signinpages

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.quizproject.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_forget_pass.*


class ForgetPassFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_forget_pass, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController= Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        btn_next_forget.setOnClickListener {
            sendMessageToEmail(context)
            navController.navigateUp()
        }

    }

    private fun sendMessageToEmail(context: Context?) {
        val email=edt_txt_email_forget.text.toString()
        if (email=="") Toast.makeText(context,"Please Enter Your Email",Toast.LENGTH_SHORT).show()
        else{
            resetPassword(email,context)
        }
    }

    private fun resetPassword(email: String, context: Context?) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    // Password reset email failed to send
                    Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}