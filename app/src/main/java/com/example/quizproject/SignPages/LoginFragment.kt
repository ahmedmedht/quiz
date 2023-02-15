package com.example.quizproject.SignPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.quizproject.R
import kotlinx.android.synthetic.main.fragment_login.*

// TODO: Rename parameter arguments, choose names that match

class LoginFragment : Fragment() {


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
        val navController=Navigation.findNavController(view)
        txt_register.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_createFragment)
        }
        txt_forget_password.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_forgetPassFragment)
        }

    }
}