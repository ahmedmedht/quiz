package com.example.quizproject.homepages

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

import com.example.quizproject.R
import com.example.quizproject.model.ModelAnswer
import com.example.quizproject.model.QuestionModel
import com.example.quizproject.model.QuestionTextAnswer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_join.*
import java.util.concurrent.CountDownLatch
import com.google.firebase.database.FirebaseDatabase as FirebaseDatabase

class JoinFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var dataJoinUser: DatabaseReference=FirebaseDatabase.getInstance().getReference("user").child(currentUser)

    private lateinit var dref: DatabaseReference
    private val args:JoinFragmentArgs by navArgs()

    private var result=0
    private var numberquestion=0

    private val options = ArrayList<Button>()
    private var btnSelected: Int? =null

    private var quizQuestionMcq=ArrayList<QuestionModel>()
    private var quizQuestionText=ArrayList<QuestionTextAnswer>()

    private var getAnswerUser=ArrayList<ModelAnswer>()


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
                getAnswerUser.add(ModelAnswer(txt_question_join.text.toString(),edt_answer_join.text.toString()))
                numberquestion+=1
                if(quizQuestionText.size>numberquestion){
                    img_question_join.isVisible=false

                    showFirstQuestion()
                    edt_answer_join.setText("")
                }
                else{
                    edt_answer_join.isVisible=false
                    layout_answer_join.isVisible=true
                    img_question_join.isVisible=false

                    numberquestion=0
                    showFirstQuestionMcq()
                }
            }
            else {
                if (btnSelected != null) {
                    val correctAnswer = quizQuestionMcq[numberquestion].numberAnswer
                    numberquestion += 1
                    getAnswerUser.add(ModelAnswer(txt_question_join.text.toString(),btnSelected.toString()))

                        if (correctAnswer.toString() == btnSelected.toString()) {
                        result += 1
                    }

                    if (quizQuestionMcq.size>numberquestion) {
                        img_question_join.isVisible=false

                        setColorDefult()
                        showFirstQuestionMcq()

                        btnSelected = null
                    } else {
                        Toast.makeText(context, "Quiz Finished", Toast.LENGTH_LONG).show()
                        uploadAnswersUser(result)

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

    private fun uploadAnswersUser(result: Int) {
        dataJoinUser.child("QuizJoin").child(args.getCodeQuiz).child("answers user").setValue(getAnswerUser)
        dataJoinUser.child("QuizJoin").child(args.getCodeQuiz).child("score").setValue(result)

    }

    private fun showFirstQuestionMcq() {
        layout_answer_join.isVisible=true
        Log.d("dataQuiz",quizQuestionMcq.toString())

        val qeustion= quizQuestionMcq[numberquestion].question
        txt_question_join.text = qeustion

        txt_answer1.text = quizQuestionMcq[numberquestion].arrayAnswer?.get(0)
        txt_answer2.text = quizQuestionMcq[numberquestion].arrayAnswer?.get(1)
        txt_answer3.text = quizQuestionMcq[numberquestion].arrayAnswer?.get(2)
        txt_answer4.text = quizQuestionMcq[numberquestion].arrayAnswer?.get(3)

        val checkQ=quizQuestionMcq[numberquestion].checkImg
        if (checkQ){
            img_question_join.setImageBitmap(convertStringToBitmap(quizQuestionMcq[numberquestion].imgQuestion))
            img_question_join.isVisible=true
        }
    }
    fun convertStringToBitmap(imageString: String): Bitmap? {
        val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun showFirstQuestion() {
        Log.d("check",quizQuestionText.size.toString())
        Log.d("check2",numberquestion.toString())
        edt_answer_join.isVisible=true
        txt_question_join.text= quizQuestionText[numberquestion].questionEdit
        val checkQ=quizQuestionText[numberquestion].checkImg
        if (checkQ){
            img_question_join.setImageBitmap(convertStringToBitmap(quizQuestionText[numberquestion].imgQuestion))
            Log.d("imageQ",quizQuestionText[numberquestion].imgQuestion)

            img_question_join.isVisible=true
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

                            arrayAnswr.add(n.child("arrayAnswer").child("0").value.toString())
                            arrayAnswr.add(n.child("arrayAnswer").child("1").value.toString())
                            arrayAnswr.add(n.child("arrayAnswer").child("2").value.toString())
                            arrayAnswr.add(n.child("arrayAnswer").child("3").value.toString())

                            val numcorrectAnswer = n.child("numberAnswer").value.toString().toInt()
                            val imgQ=n.child("imgQuestion").value.toString()
                            val checkImage=n.child("checkImg").value.toString().toBoolean()


                            quizQuestionMcq.add(QuestionModel(q, arrayAnswr, numcorrectAnswer,checkImage,imgQ))

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
                            val editQ=n.child("questionEdit").value.toString()
                            val checkImg=n.child("checkImg").value.toString().toBoolean()
                            val imgQ=n.child("imgQuestion").value.toString()

                            quizQuestionText.add(QuestionTextAnswer(editQ,checkImg,imgQ))
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




