package com.example.quizproject.HomePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.quizproject.Model.QuestionModel
import com.example.quizproject.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_code_message.*
import kotlinx.android.synthetic.main.fragment_join_code.*


class JoinCodeFragment : Fragment() {
    private lateinit var dref:DatabaseReference
    private lateinit var getQuizQuestion:ArrayList<QuestionModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_join_code, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        dref= FirebaseDatabase.getInstance().getReference("QuizApp")
        getQuizQuestion=ArrayList()

        btn_join_quiz_code.setOnClickListener {
            val action=JoinCodeFragmentDirections.actionJoinCodeFragmentToJoinFragment(edt_join_code_quiz.text.toString())
            navController.navigate(action)
        }

    }



}