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
import kotlinx.android.synthetic.main.fragment_create_question.*


class CreateQuestionFragment : Fragment() {
    private lateinit var addQuestion:ArrayList<QuestionModel>
    private val args:CreateQuestionFragmentArgs by navArgs()
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
                Toast.makeText(context,btn_next_or_finish_question.text.toString() ,Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_createQuestionFragment_to_homeFragment)
            }
            if (numberquestion==1){
                btn_next_or_finish_question.text="finish"
                Toast.makeText(context,"change next to finish",Toast.LENGTH_SHORT).show()
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


}

