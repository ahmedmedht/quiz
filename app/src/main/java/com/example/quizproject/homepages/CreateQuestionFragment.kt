package com.example.quizproject.homepages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.model.QuestionModel
import com.example.quizproject.R
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
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_create_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addQuestion= ArrayList()
        val navController = Navigation.findNavController(view)
        var numberquestion=args.numberQuestion
        val startQuizTime=args.timeStartQuiz
        val endQuizTime=args.timeEndQuiz


        database = FirebaseDatabase.getInstance().getReference("QuizApp")
        databaseuser = FirebaseDatabase.getInstance().getReference("user")



        btn_next_or_finish_question.setOnClickListener {

            if (btn_next_or_finish_question.text.toString().equals("next"))
            {
                numberquestion-=1
                addDatatoArray()
                edt_question_quiz.setText("")
                radioButton.text=""
                radioButton2.text=""
                radioButton3.text=""
                radioButton4.text=""
                Toast.makeText(context,"change to next question $numberquestion",Toast.LENGTH_SHORT).show()

            }else{
                numberquestion-=1
                addDatatoArray()
                getrandomarray()
                var randomcode=strArray.random().toString()+strArray.random().toString()+strArray.random().toString()+strArray.random().toString()+strArray.random().toString()

                uploadQuizToFirebase(randomcode,addQuestion,startQuizTime,endQuizTime,currentUser,args.numberQuestion)


                val action=CreateQuestionFragmentDirections.actionCreateQuestionFragmentToTakeCodeFragment(randomcode)
                navController.navigate(action)
            }
            if (numberquestion==1){
                btn_next_or_finish_question.text="finish"

            }


        }

    }




    private fun addDatatoArray() {
        val arryAnswer: ArrayList<String> = ArrayList()
        arryAnswer.add(radioButton.text.toString())
        arryAnswer.add(radioButton2.text.toString())
        arryAnswer.add(radioButton3.text.toString())
        arryAnswer.add(radioButton4.text.toString())

        val answerNumber=checkedradio()

        addQuestion.add(QuestionModel(edt_question_quiz.text.toString(),arryAnswer,answerNumber))
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
        database.child("Quizzes").child(quizid).child("StartTimeQuiz").setValue(startQuizTime)
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
