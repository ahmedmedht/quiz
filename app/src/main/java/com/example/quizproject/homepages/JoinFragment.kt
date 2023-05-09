package com.example.quizproject.homepages

import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

import com.example.quizproject.R
import com.example.quizproject.model.QuestionModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_join.*
import java.util.concurrent.CountDownLatch
import com.google.firebase.database.FirebaseDatabase as FirebaseDatabase

class JoinFragment : Fragment() {

    private lateinit var dref: DatabaseReference
    private val args:JoinFragmentArgs by navArgs()

    private var result=0
    private var numberquestion=0

    private val options = ArrayList<Button>()
    private var btnSelected: Int? =null

    private var quizQuestionMcq=ArrayList<QuestionModel>()
    private var quizQuestionText=ArrayList<String>()

    private var getAnswerUser=ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_join, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        var checkTextQ=false
        val getCodeQuizJoin=args.getCodeQuiz

        dref= FirebaseDatabase.getInstance().getReference("QuizApp")

        options.add(0, txt_answer1)
        options.add(1, txt_answer2)
        options.add(2, txt_answer3)
        options.add(3, txt_answer4)
        checkTextQuestion(getCodeQuizJoin)
        getDataFromFirebase(getCodeQuizJoin) {
            if (it){


                if (quizQuestionText.isEmpty()) Log.d("TextQ1", "no Text Question")
                else checkTextQ=true

                if (checkTextQ){
                    showFirstQuestion()
                }else{
                    showFirstQuestionMcq()
                }
            }else{
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
        }




        btn_next_quiz_join.setOnClickListener {
            if(edt_answer_join.isVisible){
                getAnswerUser.add(edt_answer_join.text.toString())
                numberquestion+=1
                if(quizQuestionText.size>numberquestion){
                    Log.d("check",quizQuestionText.size.toString())
                    Log.d("check2",numberquestion.toString())


                    showFirstQuestion()
                    edt_answer_join.setText("")
                }
                else{
                    edt_answer_join.isVisible=false
                    layout_answer_join.isVisible=true
                    numberquestion=0
                    showFirstQuestionMcq()
                }
            }
            else {
                if (btnSelected != null) {
                    val correctAnswer = quizQuestionMcq[numberquestion].numberAnswer
                    numberquestion += 1
                    if (correctAnswer.toString() == btnSelected.toString()) {
                        result += 1
                    }

                    if (quizQuestionMcq.size>numberquestion) {

                        setColorDefult()
                        showFirstQuestionMcq()

                        btnSelected = null
                    } else {
                        Toast.makeText(context, "Quiz Finished", Toast.LENGTH_LONG).show()

                        val action = JoinFragmentDirections.actionJoinFragmentToResultQuizFragment(
                            result.toString(),
                            args.getCodeQuiz
                        )
                        navController.navigate(action)
                    }
                } else {
                    Toast.makeText(context, "please select answer", Toast.LENGTH_LONG).show()
                    txt_question_join.error = "please select answer"
                }
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

    private fun showFirstQuestionMcq() {
        layout_answer_join.isVisible=true
        Log.d("dataQuiz",quizQuestionMcq.toString())

        val qeustion= quizQuestionMcq[numberquestion].question
        txt_question_join.text = qeustion

        txt_answer1.text = quizQuestionMcq[numberquestion].arrayanswr?.get(0)
        txt_answer2.text = quizQuestionMcq[numberquestion].arrayanswr?.get(1)
        txt_answer3.text = quizQuestionMcq[numberquestion].arrayanswr?.get(2)
        txt_answer4.text = quizQuestionMcq[numberquestion].arrayanswr?.get(3)
    }

    private fun showFirstQuestion() {
        Log.d("check",quizQuestionText.size.toString())
        Log.d("check2",numberquestion.toString())
        edt_answer_join.isVisible=true
        txt_question_join.text= quizQuestionText[numberquestion]
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
        btnSelected = when (txtAnswer) {
            txt_answer1 -> 1
            txt_answer2 -> 2
            txt_answer3 -> 3
            txt_answer4 -> 4
            else -> null
        }
    }


    private fun getDataFromFirebase(
        getCodeQuizJoin: String,
        callback: (Boolean) ->Unit
    ) {

            dref.child("Quizzes").child(getCodeQuizJoin).child("QuizQuestions").get().addOnSuccessListener {
            Log.d("questionsMcqIt", quizQuestionMcq.toString())
            if (it.exists()){
                        Log.d("questionsMcq", quizQuestionMcq.toString())
                        val latch = CountDownLatch(it.childrenCount.toInt())

                        for (n in it.children) {
                            val q = n.child("question").value.toString()
                            val arrayAnswr = ArrayList<String>()
                            arrayAnswr.add(n.child("arrayanswr").child("0").value.toString())
                            arrayAnswr.add(n.child("arrayanswr").child("1").value.toString())
                            arrayAnswr.add(n.child("arrayanswr").child("2").value.toString())
                            arrayAnswr.add(n.child("arrayanswr").child("3").value.toString())

                            n.child("arrayanswr").value
                            val numcorrectAnswer = n.child("numberAnswer").value.toString().toInt()
                            quizQuestionMcq.add(QuestionModel(q, arrayAnswr, numcorrectAnswer))

                            latch.countDown()
                        }

                        latch.await()
                        callback(true)
                        Log.d("questionsMcq", quizQuestionMcq.toString())

            }else{
                Toast.makeText(context,"QUIZ DOESN'T EXISTS ",Toast.LENGTH_LONG).show()
                Log.d("questionsMcqNo", quizQuestionMcq.toString())
                callback(false)
                callback(false)


            }
        }.addOnFailureListener {
            Toast.makeText(context,"QUIZ DOESN'T EXISTS ",Toast.LENGTH_LONG).show()
            Log.d("questionsMcqNoNo", quizQuestionMcq.toString())


        }
    }


    private fun checkTextQuestion(codeQuizJoin: String) {
        dref.child("Quizzes").child(codeQuizJoin).child("TextQuestions")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (n in snapshot.children) {
                            Log.d("TextQ", "run Text Question")

                            quizQuestionText.add(n.value.toString())
                        }

                    } else {
                        Log.d("TextQ", "no Text Question")

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TextQ", "error Text Question")

                    Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
                }
            })
    }


}


