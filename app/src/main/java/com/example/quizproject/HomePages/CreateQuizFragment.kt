package com.example.quizproject.HomePages

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.quizproject.R
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import java.text.SimpleDateFormat
import java.util.*


class CreateQuizFragment : Fragment(){

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
            val calendar = Calendar.getInstance()
            val dialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(calendar)
            }
            activity?.let {
                DatePickerDialog(
                    it, dialog, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
        //time select
        time_picker.setOnClickListener {
            opentimepicker()
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
            navController.navigate(R.id.action_createQuizFragment_to_createQuestionFragment)
        }
    }

    private fun opentimepicker() {
        val currentTime=Calendar.getInstance()
        val startHour=currentTime.get(Calendar.HOUR)
        val startMin=currentTime.get(Calendar.MINUTE)
        activity?.let {
            TimePickerDialog(it,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                time_picker.setText("$hourOfDay : $minute")
            },startHour ,startMin ,false).show()
        }
    }


    private fun updateLabel(calendar: Calendar) {
        val format="dd-MM-yyyy"
        val sdf=SimpleDateFormat(format, Locale.UK)
        data_picker.setText(sdf.format(calendar.time))
    }


}


