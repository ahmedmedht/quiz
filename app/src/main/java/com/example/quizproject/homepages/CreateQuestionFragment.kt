package com.example.quizproject.homepages

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible

import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.model.QuestionModel
import com.example.quizproject.R
import com.example.quizproject.model.QuestionTextAnswer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_question.*


class CreateQuestionFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private val args:CreateQuestionFragmentArgs by navArgs()
    private lateinit var addQuestion:ArrayList<QuestionModel>
    private lateinit var databaseuser: DatabaseReference
    private var strArray: List<Char>  = emptyList()
    private lateinit var questionTextArray:ArrayList<QuestionTextAnswer>
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var btnWriteNow:RadioButton
    private var strImg=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_create_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addQuestion= ArrayList()
        questionTextArray=ArrayList()
        val navController = Navigation.findNavController(view)
        var numberquestion=args.numberQuestion
        val startQuizTime=args.timeStartQuiz
        val endQuizTime=args.timeEndQuiz
        val totalNumber=args.numTxtQ
//        for (n in 0..totalNumber){
//            btn_next_or_finish_question.setOnClickListener {
//
//            }
//        }
        if (totalNumber==0){
            layout_add_question_question.isVisible=true
        }
            var txtEdit=totalNumber


        database = FirebaseDatabase.getInstance().getReference("QuizApp")
        databaseuser = FirebaseDatabase.getInstance().getReference("user")

        radioButton.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton.text.toString())
            btnWriteNow=radioButton
        }
        radioButton2.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton2.text.toString())
            btnWriteNow=radioButton2
        }
        radioButton3.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton3.text.toString())
            btnWriteNow=radioButton3
        }
        radioButton4.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton4.text.toString())
            btnWriteNow=radioButton4
        }

        img_btn_change_txt_answer.setOnClickListener {
            btnWriteNow.text = edt_enter_answer.text.toString()
            layout_answer_enter.isVisible=false
        }




        btn_next_or_finish_question.setOnClickListener {
            if (txtEdit>0){
                if (edt_question_quiz.text.toString()==""){
                    edt_question_quiz.error="Please Enter Your Question!"
                }else{
                    var checkImg=false
                    if (strImg!=""){
                        checkImg=true
                    }
                    questionTextArray.add(QuestionTextAnswer(edt_question_quiz.text.toString(),checkImg,strImg))

                    strImg=""
                    layout_image_put.isVisible=false
                    txtEdit-=1
                }
                if (txtEdit==0){
                    layout_add_question_question.isVisible=true
                }
                edt_question_quiz.setText("")
            }
            else {
                if (btn_next_or_finish_question.text.toString().equals("next")) {
                    numberquestion -= 1
                    addDatatoArray()

                    strImg=""
                    layout_image_put.isVisible=false

                    edt_question_quiz.setText("")
                    radioButton.text = ""
                    radioButton2.text = ""
                    radioButton3.text = ""
                    radioButton4.text = ""
                    Toast.makeText(
                        context,
                        "change to next question $numberquestion",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    numberquestion -= 1
                    addDatatoArray()
                    getrandomarray()
                    val randomcode = strArray.random().toString() + strArray.random()
                        .toString() + strArray.random().toString() + strArray.random()
                        .toString() + strArray.random().toString()

                    uploadQuizToFirebase(
                        randomcode,
                        addQuestion,
                        startQuizTime,
                        endQuizTime,
                        currentUser,
                        args.numberQuestion
                    )


                    val action =
                        CreateQuestionFragmentDirections.actionCreateQuestionFragmentToTakeCodeFragment(
                            randomcode
                        )
                    navController.navigate(action)
                }
                if (numberquestion == 1) {
                    btn_next_or_finish_question.text = "finish"

                }

            }
        }

        get_img.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

            startActivityForResult(intent,100);

            layout_image_put.isVisible=true

        }
        img_remove_image_edit.setOnClickListener {
            strImg=""
            layout_image_put.isVisible=false
        }

    }


    private fun writeAnswer() {
        edt_enter_answer.isVisible=true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            val uri: Uri? = data?.data
            strImg=uri.toString()
            Log.d("strImg",strImg)
            img_question_edit.setImageURI(strImg.toUri())


        }
    }
    private fun addDatatoArray() {
        val arryAnswer: ArrayList<String> = ArrayList()
        arryAnswer.add(radioButton.text.toString())
        arryAnswer.add(radioButton2.text.toString())
        arryAnswer.add(radioButton3.text.toString())
        arryAnswer.add(radioButton4.text.toString())

        val answerNumber=checkedradio()
        var cheackImg=false
        if (strImg!="") cheackImg=true

        addQuestion.add(QuestionModel(edt_question_quiz.text.toString(),arryAnswer,answerNumber,cheackImg,strImg))
    }

    private fun checkedradio(): Int {

        return when (radio_group_btn_enter_answer.checkedRadioButtonId) {
            R.id.radioButton -> 1
            R.id.radioButton2 -> 2
            R.id.radioButton3 -> 3
            R.id.radioButton4 -> 4
            else -> 0
        }

    }
    private fun uploadQuizToFirebase(
        quizid: String,
        questionQuiz: ArrayList<QuestionModel>,
        startQuizTime: Long,
        endQuizTime: Long,
        currentUser: FirebaseUser?,
        numberQuestion: Int
    ) {
        if(questionTextArray.isEmpty()){1+1}
            else{
                database.child("Quizzes").child(quizid).child("TextQuestions").setValue(questionTextArray)

            }
        database.child("Quizzes").child(quizid).child("StartTimeQuiz").setValue(startQuizTime)
        database.child("Quizzes").child(quizid).child("QuizName").setValue(args.nameQuiz)
        database.child("Quizzes").child(quizid).child("EndTimeQuiz").setValue(endQuizTime)
        database.child("Quizzes").child(quizid).child("QuizQuestions").setValue(questionQuiz)
        database.child("Quizzes").child(quizid).child("NumberQuestion").setValue(numberQuestion)
        if (currentUser != null) {
            database.child("Quizzes").child(quizid).child("userIdCreatedQuiz").setValue(currentUser.uid)
        }

        if (currentUser != null) {
            databaseuser.child(currentUser.uid).child("QuizCreated").child(quizid).setValue(numberQuestion)

                .addOnCompleteListener{
                    Toast.makeText(context,"QUIZ UPLOAD SUCCESSFULL",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(context,"Error fail upload quiz ${err.message}",Toast.LENGTH_SHORT).show()
                }
        }


    }

    private fun getrandomarray() {
        for (i in 'a'..'z'){
            strArray=(strArray+i)
        }
        for (i in 'A'..'Z'){
            strArray=(strArray+i)
        }
        for (i in '0'..'9'){
            strArray=(strArray+i)
        }

    }

}
