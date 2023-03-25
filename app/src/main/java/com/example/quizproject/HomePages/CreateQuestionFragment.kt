package com.example.quizproject.HomePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.Model.QuestionModel
import com.example.quizproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_create_question.*


class CreateQuestionFragment : Fragment() {
    private lateinit var addQuestion:ArrayList<QuestionModel>
    private val args:CreateQuestionFragmentArgs by navArgs()
    private lateinit var database: DatabaseReference
    private var strArra: List<Char>  = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_create_question, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addQuestion= ArrayList()
        val navController = Navigation.findNavController(view)
        var numberquestion=args.numberQuestion

        database = FirebaseDatabase.getInstance().getReference("QuizApp")



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
                var randomcode=strArra.random().toString()+strArra.random().toString()+strArra.random().toString()+strArra.random().toString()+strArra.random().toString()

                writeNewUser(randomcode,addQuestion)


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
        val rbId = radio_group_btn_enter_answer.getCheckedRadioButtonId()

        when (rbId) {
            R.id.radioButton -> return 1
            R.id.radioButton2 -> return 2
            R.id.radioButton3 -> return 3
            R.id.radioButton4 -> return 4
            else -> return 0
        }

    }
    fun writeNewUser(quizid: String,qeustionQuiz:ArrayList<QuestionModel>) {

            database.child("Quizes").child(quizid).setValue(qeustionQuiz)

                .addOnCompleteListener{
                    Toast.makeText(context,"QUIZ UPLOAD SUCCESSFULL",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(context,"Error fail upload quiz ${err.message}",Toast.LENGTH_SHORT).show()
                }


    }

    private fun getrandomarray() {
        for (i in 'a'..'z'){
            strArra=(strArra+i)
        }
        for (i in 'A'..'Z'){
            strArra=(strArra+i)
        }
        for (i in '0'..'9'){
            strArra=(strArra+i)
        }

    }

}

