package com.example.quizproject.HomePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.R
import kotlinx.android.synthetic.main.fragment_result_quiz.*

class ResultQuizFragment : Fragment() {
    private val args:ResultQuizFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_result_quiz, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        val resultQ=args.score
        txt_result_quiz.text="result = $resultQ"
        btn_go_home_from_result.setOnClickListener {
            navController.navigate(R.id.action_resultQuizFragment_to_homeFragment)
        }
    }


}