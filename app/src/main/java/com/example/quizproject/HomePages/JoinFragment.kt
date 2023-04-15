package com.example.quizproject.HomePages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
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
    private var numberquestion=0
    private val options = ArrayList<Button>()
    private var btnSelected: Int? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_join, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)

        val getCodeQuizJoin=args.getCodeQuiz
        dref= FirebaseDatabase.getInstance().getReference("QuizApp")

        options.add(0, txt_answer1)
        options.add(1, txt_answer2)
        options.add(2, txt_answer3)
        options.add(3, txt_answer4)

        getDataFromFirebase(getCodeQuizJoin)


        btn_next_quiz_join.setOnClickListener {
            if (btnSelected!=null) {
                val correctAnswer=getQuizQuestion?.child(numberquestion.toString())?.child("numberAnswer")?.value
                Toast.makeText(context, "correct Answer : $correctAnswer , UserAnswer=$btnSelected", Toast.LENGTH_LONG)
                    .show()
                if (correctAnswer.toString() == btnSelected.toString()){
                    result+=1
                }
                numberquestion += 1
                if (getQuizQuestion?.hasChild(numberquestion.toString()) == true) {
                    setColorDefult()
                    getNextQuestion(numberquestion.toString(), getQuizQuestion!!)
                    Toast.makeText(context, "Qestion number : $numberquestion", Toast.LENGTH_LONG)
                        .show()
                    btnSelected=null
                } else {
                    Toast.makeText(context, "Quiz Finished", Toast.LENGTH_LONG).show()

                    val action=JoinFragmentDirections.actionJoinFragmentToResultQuizFragment(result.toString())
                    navController.navigate(action)
                }
            }else{
                Toast.makeText(context, "please select answer", Toast.LENGTH_LONG).show()
                txt_question_join.setError("please select answer")
            }


        }
        txt_answer1.setOnClickListener {
            selectAnswer(txt_answer1)
        }

        txt_answer2.setOnClickListener {
            selectAnswer(txt_answer2)
        }
        txt_answer3.setOnClickListener {
            selectAnswer(txt_answer3)
        }
        txt_answer4.setOnClickListener {
            selectAnswer(txt_answer4)
        }

    }

    private fun setColorDefult() {
        for (id in options){
            id.background= activity?.let {
                ContextCompat.getDrawable(
                    it,R.drawable.background_txt_answer_join
                )
            }
        }
    }

    private fun selectAnswer(txtAnswer: Button?) {
        setColorDefult()
        if (txtAnswer != null) {
            txtAnswer.background= activity?.let {
                ContextCompat.getDrawable(
                    it,R.drawable.background_answer_selected
                )
            }
        }
        when (txtAnswer) {
            txt_answer1 ->  btnSelected=1
            txt_answer2 ->  btnSelected=2
            txt_answer3 ->  btnSelected=3
            txt_answer4 ->  btnSelected=4
            else -> btnSelected=null
        }
    }

    private fun getNextQuestion(numb: String, getQuizQuestion: DataSnapshot) {
        val qeustion=getQuizQuestion?.child(numb)?.child("question")?.value.toString()
        txt_question_join.text = qeustion
        val answer=getQuizQuestion?.child(numb)?.child("arrayanswr")
        txt_answer1.text = answer?.child("0")?.value.toString()
        txt_answer2.text = answer?.child("1")?.value.toString()
        txt_answer3.text = answer?.child("2")?.value.toString()
        txt_answer4.text = answer?.child("3")?.value.toString()
    }

    private fun getDataFromFirebase(getCodeQuizJoin: String) {
        dref.child("Quizes").child(getCodeQuizJoin).child("QuizQuestions").get().addOnSuccessListener {
            if (it.exists()){
                showfirstquestion(it)

                getQuizQuestion=it

                Log.d("arraytest",getQuizQuestion.toString())

            }else{
                Toast.makeText(context,"QUIZ DOESN'T EXISTS ",Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(context,"QUIZ DOESN'T EXISTS ",Toast.LENGTH_LONG).show()

        }
    }

    private fun showfirstquestion(QuestionQuiz: DataSnapshot) {
        val qeustion=QuestionQuiz?.child(numberquestion.toString())?.child("question")?.value.toString()
        txt_question_join.text = qeustion
        val answer=QuestionQuiz?.child(numberquestion.toString())?.child("arrayanswr")
        txt_answer1.text = answer?.child("0")?.value.toString()
        txt_answer2.text = answer?.child("1")?.value.toString()
        txt_answer3.text = answer?.child("2")?.value.toString()
        txt_answer4.text = answer?.child("3")?.value.toString()
    }








}


