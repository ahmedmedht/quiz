package com.example.quizproject.signinpages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.quizproject.R
import kotlinx.android.synthetic.main.fragment_forget_pass.*


class ForgetPassFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_forget_pass, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController= Navigation.findNavController(view)
        btn_next_forget.setOnClickListener {
            navController.navigate(R.id.action_forgetPassFragment_to_codeMessageFragment)
        }

    }
}