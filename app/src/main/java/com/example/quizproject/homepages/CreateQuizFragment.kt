package com.example.quizproject.homepages

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.quizproject.R
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import java.text.SimpleDateFormat
import java.util.*


class CreateQuizFragment : Fragment(){
    private var stTimeQuiz:Long?=null
    private var enTimeQuiz:Long?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_quiz, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        //date select
        data_picker.setOnClickListener {
            getDataPicker(data_picker)
        }
        data_picker_end.setOnClickListener {
            getDataPicker(data_picker_end)
        }
        //time select
        time_picker.setOnClickListener {
            opentimepicker(time_picker)
        }
        time_picker_end.setOnClickListener {
            opentimepicker(time_picker_end)
        }

        check_random_question.setOnClickListener {
            if (check_random_question.isChecked) {
                layout_invisible_number_question.isVisible = true
            } else {
                layout_invisible_number_question.isVisible = false
                Toast.makeText(context, "nice", Toast.LENGTH_LONG).show()
            }
        }

        btn_cancle_create_quiz.setOnClickListener {
            navController.navigateUp()


        }

        btn_next_create_quiz.setOnClickListener {
            val numberq=edt_total_number_q_create_quiz.text.toString()
            val date=getdateandtime()
            if (date!=null){
            if (date<1){
                txt_time_end_quiz_create.error = "End date and must be greater then start date and time"


            }else {
                if (edt_total_number_q_create_quiz.text.toString()!="") {
                    val action =
                        stTimeQuiz?.let { it1 ->
                            enTimeQuiz?.let { it2 ->
                                CreateQuizFragmentDirections.actionCreateQuizFragmentToCreateQuestionFragment(
                                    numberq.toInt(),
                                    it1,
                                    it2
                                )
                            }
                        }
                    action?.let { it1 -> navController.navigate(it1) }
                }else{
                    Toast.makeText(context,"Please enter all required",Toast.LENGTH_LONG).show()
                }
            }
            }else Toast.makeText(context,"Please enter all required",Toast.LENGTH_LONG).show()

        }
    }

    private fun getDataPicker(datap: TextView) {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(calendar,datap)
        }
        activity?.let {
            DatePickerDialog(
                it, dialog, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun opentimepicker(ttime:TextView){
        val currentTime=Calendar.getInstance()
        val startHour=currentTime.get(Calendar.HOUR)
        val startMin=currentTime.get(Calendar.MINUTE)


        activity?.let {
            TimePickerDialog(it,TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                ttime.text = "$hourOfDay:$minute"

                Toast.makeText(context, "$hourOfDay : $minute", Toast.LENGTH_LONG).show()
            },startHour ,startMin ,true).show()
        }

    }


    private fun updateLabel(calendar: Calendar,datap:TextView) {
        val format="dd-MM-yyyy"
        val sdf=SimpleDateFormat(format, Locale.ITALY)
        datap.text = sdf.format(calendar.time)
    }

    private fun getdateandtime(): Long? {
        val stTime=time_picker.text.toString()+":00"
        val enTime=time_picker_end.text.toString()+":00"
        val stDate=data_picker.text.toString()
        val enDate=data_picker_end.text.toString()
        val format=SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ITALY)

        Log.d("timeInfotest","test=$enDate $enTime")
        val convertDatest=format.parse("$stDate $stTime")
        val convertDateen=format.parse("$enDate $enTime")

        stTimeQuiz= convertDatest?.time
        enTimeQuiz= convertDateen?.time
        return if ((convertDateen != null) && (convertDatest != null)) {
            convertDateen.time-convertDatest.time
        }else null

    }


}


