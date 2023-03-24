package com.example.quizproject.HomePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.R
import kotlinx.android.synthetic.main.fragment_take_code.*
import kotlinx.android.synthetic.main.fragment_take_code.view.*


class TakeCodeFragment : Fragment() {

    private val args:TakeCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_take_code, container, false)
        val codeQuiz=args.codeQuiz

        view.txt_code_quiz.setText(codeQuiz)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        btn_go_home_from_take_code.setOnClickListener {
            navController.navigate(R.id.action_takeCodeFragment_to_homeFragment)
        }
    }


}