package com.example.quizproject.HomePages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.quizproject.Model.QuestionModel
import com.example.quizproject.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_code_message.*
import kotlinx.android.synthetic.main.fragment_join_code.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

            val codeJoin=edt_join_code_quiz.text.toString()
            val checkQuiz= getDataFromFirebase(codeJoin)
            if (checkQuiz) {
                val action =
                    JoinCodeFragmentDirections.actionJoinCodeFragmentToJoinFragment(codeJoin)
                navController.navigate(action)
            }else Toast.makeText(context,"Error", Toast.LENGTH_LONG).show()

        }

    }

    private fun getDataFromFirebase(getCodeQuizJoin: String):Boolean{
        var checkDate=false

        dref.child("Quizes").child(getCodeQuizJoin).get().addOnSuccessListener {
            if (it.exists()){
                val dateNow=Date().time
                val getStartTime=it.child("StartTimeQuiz").value.toString().toLong()
                val getEndTime=it.child("EndTimeQuiz").value.toString().toLong()
                if (dateNow>=getStartTime){
                    if (getEndTime>dateNow){
                        checkDate=true
                        Toast.makeText(context,"Quiz Started", Toast.LENGTH_LONG).show()
                    }
                    else{
                        checkDate=false
                        Toast.makeText(context,"Time Quiz End", Toast.LENGTH_LONG).show()
                    }
                    Toast.makeText(context,"hello", Toast.LENGTH_LONG).show()

                }else{
                    checkDate=false
                    Toast.makeText(context,"Quiz has not started yet", Toast.LENGTH_LONG).show()
                }

                Log.d("arraytest",checkDate.toString())

            }else{
                Toast.makeText(context,"QUIZ DOESN'T EXISTS ", Toast.LENGTH_LONG).show()
                checkDate=false
            }
        }.addOnFailureListener {
            Toast.makeText(context,"QUIZ DOESN'T EXISTS ", Toast.LENGTH_LONG).show()
            checkDate=false
        }
        Log.d("arraytest12",checkDate.toString())
        return checkDate
    }




}