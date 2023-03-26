package com.example.quizproject.HomePages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs

import com.example.quizproject.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_join.*
import com.google.firebase.database.FirebaseDatabase as FirebaseDatabase

class JoinFragment : Fragment() {

    private lateinit var dref: DatabaseReference
    private var getQuizQuestion:DataSnapshot?=null
    private val args:JoinFragmentArgs by navArgs()
    private var result=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_join, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getCodeQuizJoin=args.getCodeQuiz
        dref= FirebaseDatabase.getInstance().getReference("QuizApp")
        var numberquestion=1

        getDataFromFirebase(getCodeQuizJoin)

        //showfirstquestion()



        btn_next_quiz_join.setOnClickListener {
            if(getQuizQuestion?.hasChild(numberquestion.toString()) == true){
                val qeustion=getQuizQuestion?.child(numberquestion.toString())?.child("question")?.value.toString()
                txt_question_join.text = qeustion
                val answer=getQuizQuestion?.child("0")?.child("arrayanswr")
                txt_answer1.text = answer?.child("0")?.value.toString()
                txt_answer2.text = answer?.child("1")?.value.toString()
                txt_answer3.text = answer?.child("2")?.value.toString()
                txt_answer4.text = answer?.child("3")?.value.toString()
                numberquestion += 1
            }else{

            }
        }

    }
    private fun getDataFromFirebase(getCodeQuizJoin: String) {
        dref.child("Quizes").child(getCodeQuizJoin).get().addOnSuccessListener {
            if (it.exists()){

                getQuizQuestion=it

                Log.d("arraytest",getQuizQuestion.toString())

            }else{
                Toast.makeText(context,"QUIZ DOESN'T EXISTS ",Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(context,"getQuizQuestion.toString()",Toast.LENGTH_LONG).show()

        }
    }

    private fun showfirstquestion() {
        val qeustion=getQuizQuestion?.child("0")?.child("question")?.value.toString()
        txt_question_join.text = qeustion
        val answer=getQuizQuestion?.child("0")?.child("arrayanswr")
        txt_answer1.text = answer?.child("0")?.value.toString()
        txt_answer2.text = answer?.child("1")?.value.toString()
        txt_answer3.text = answer?.child("2")?.value.toString()
        txt_answer4.text = answer?.child("3")?.value.toString()
    }








}


