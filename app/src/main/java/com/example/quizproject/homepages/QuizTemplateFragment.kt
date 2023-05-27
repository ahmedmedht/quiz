package com.example.quizproject.homepages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizproject.R
import com.example.quizproject.model.RandomFormat
import com.example.quizproject.model.RecyclerViewFormatRandomAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_quiz_template.*
import kotlinx.android.synthetic.main.fragment_quiz_template.txt_enter_type_select
import kotlinx.android.synthetic.main.fragment_quiz_template.txt_num_q_type_main
import kotlinx.android.synthetic.main.fragment_quiz_template.txt_type_q_main


class QuizTemplateFragment : Fragment() ,RecyclerViewFormatRandomAdapter.ItemClickListener{
    private val args:QuizTemplateFragmentArgs by navArgs()
    private lateinit var mapFormatRandom: ArrayList<RandomFormat>
    private lateinit var adapter: RecyclerViewFormatRandomAdapter
    private var database: DatabaseReference=FirebaseDatabase.getInstance().getReference("QuizApp")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_template, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        mapFormatRandom= arrayListOf()
        val code=args.codeQuiz
        var totalQ=0
        val mainQf:Map<String,Int> = args.mapMain.map
        val randomQuestionF:Map<String,Int> = args.mapRandom.map
        val arrayTypeQuestion:ArrayList<String> = ArrayList()
        if (mainQf.isNotEmpty()) setQuetionMainFormat(mainQf)

        if (randomQuestionF.isNotEmpty()) setQuetionRandomFormat(randomQuestionF)
        ///////////////////////////////////////////////////
        checkBox_11.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["11"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("11")
            }else{
                totalQ-=randomQuestionF["11"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("11")
            }
        }
        checkBox_12.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["12"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("12")
            }else{
                totalQ-=randomQuestionF["12"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("12")
           }
        }
        checkBox_13.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["13"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("13")

            }else{
                totalQ-=randomQuestionF["13"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("13")

            }
        }
        checkBox_21.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["21"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("21")

            }else{
                totalQ-=randomQuestionF["21"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("21")

            }
        }

        checkBox_22.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["22"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("22")

            }else{
                totalQ-=randomQuestionF["22"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("22")

            }
        }

        checkBox_23.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["23"]!!

                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("23")

            }else{
                totalQ-=randomQuestionF["23"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("23")

            }
        }

        checkBox_31.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["31"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("31")

            }else{
                totalQ-=randomQuestionF["31"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("31")

            }
        }

        checkBox_32.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["32"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("32")

            }else{
                totalQ-=randomQuestionF["32"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("32")

            }
        }

        checkBox_33.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                layout_q_random_selected.isVisible=true
                totalQ += randomQuestionF["33"]!!
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.add("33")

            }else{
                totalQ-=randomQuestionF["33"]!!
                if (totalQ<0) totalQ=0
                txt_enter_type_select.text="Total($totalQ)"
                arrayTypeQuestion.remove("33")

            }
        }

        //////////////////////////////////////////////
        img_btn_add_item_to_rv.setOnClickListener {
            val getTotal=edt_enter_num_q_from_type.text.toString()
            if (getTotal==""||getTotal.toInt()>totalQ||getTotal.toInt()<1){
                edt_enter_num_q_from_type.error="error"
            }
            else{
                val n= arrayListOf<String>()
                for (i in arrayTypeQuestion){ n.add(i)}
                mapFormatRandom.add(RandomFormat(n, getTotal.toInt()))

                removeCheckBox(arrayTypeQuestion)
                arrayTypeQuestion.clear()
                adapter = RecyclerViewFormatRandomAdapter(mapFormatRandom)
                adapter.setItemClickListener(this)
                rv_format_q_random.adapter=adapter
                rv_format_q_random.layoutManager= LinearLayoutManager(requireContext())
                totalQ = 0

                txt_enter_type_select.text=""
                edt_enter_num_q_from_type.setText("")
            }
        }
        btn_next_from_template.setOnClickListener {
            var checkRandom=true
            if (randomQuestionF.isNotEmpty()&&mapFormatRandom.isEmpty()){
                    txt_random_format_title.error="please do format for random question"
                    checkRandom=false
                }

            if (randomQuestionF.isNotEmpty()&&mainQf.isNotEmpty()){

                if (checkRandom){
                    uploadTemplateonfirebase(code,mapFormatRandom,mainQf)
                }
            }else if (mainQf.isEmpty()){
                if (checkRandom){
                    uploadTemplateRandom(code,mapFormatRandom)
                }
            }else if (randomQuestionF.isEmpty()){
                uploadTemplateMain(code,mainQf)
            }
            val action=QuizTemplateFragmentDirections.actionQuizTemplateFragmentToTakeCodeFragment(code)
            navController.navigate(action)
        }
    }
    private fun removeCheckBox(arrayTypeQuestion: ArrayList<String>) {
        for (i in arrayTypeQuestion){
            when (i) {
                "11" -> {
                    checkBox_11.isEnabled=false
                }
                "12" -> {
                    checkBox_12.isEnabled=false
                }
                "13" -> {
                    checkBox_13.isEnabled=false
                }
                "21" -> {
                    checkBox_21.isEnabled=false
                }
                "22" -> {
                    checkBox_22.isEnabled=false
                }
                "23" -> {
                    checkBox_23.isEnabled=false
                }
                "31" -> {
                    checkBox_31.isEnabled=false
                }
                "32" -> {
                    checkBox_32.isEnabled=false
                }
                "33" -> {
                    checkBox_33.isEnabled=false
                }
            }

        }

    }
    private fun uploadTemplateMain(code: String,mainQf: Map<String, Int>){

        database.child("Quizzes").child(code).child("TypeQuestions").child("TemplateQuiz").child("MainFormat").setValue(mainQf)
            .addOnFailureListener {

                Toast.makeText(context,"Please try again!!",Toast.LENGTH_SHORT).show()
            }
   }
    private fun uploadTemplateRandom(code: String, mapFormatRandom: java.util.ArrayList<RandomFormat>){

        database.child("Quizzes").child(code).child("TypeQuestions").child("TemplateQuiz").child("RandomFormat").setValue(mapFormatRandom)
            .addOnFailureListener {

                Toast.makeText(context,"Please try again!!",Toast.LENGTH_SHORT).show()
            }

    }

    private fun uploadTemplateonfirebase(
        code: String,
        mapFormatRandom: java.util.ArrayList<RandomFormat>,
        mainQf: Map<String, Int>
    ){

        database.child("Quizzes").child(code).child("TypeQuestions").child("TemplateQuiz").child("MainFormat").setValue(mainQf)
            .addOnFailureListener {

                Toast.makeText(context,"Please try again!!",Toast.LENGTH_SHORT).show()
            }
        database.child("Quizzes").child(code).child("TypeQuestions").child("TemplateQuiz").child("RandomFormat").setValue(mapFormatRandom)
            .addOnFailureListener {

                Toast.makeText(context,"Please try again!!",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onItemRemoved(position: Int) {
        if (position >= 0 && position < mapFormatRandom.size) {
            returnCheckBox(mapFormatRandom[position].arraySelectTypeQuestion)

            mapFormatRandom.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    private fun returnCheckBox(arraySelectTypeQuestion: java.util.ArrayList<String>?) {
        for (i in arraySelectTypeQuestion!!){
            when (i) {
                "11" -> {
                    checkBox_11.isEnabled=true
                    checkBox_11.isChecked=false

                }
                "12" -> {
                    checkBox_12.isEnabled=true
                    checkBox_12.isChecked=false
                }
                "13" -> {
                    checkBox_13.isEnabled=true
                    checkBox_13.isChecked=false
                }
                "21" -> {
                    checkBox_21.isEnabled=true
                    checkBox_21.isChecked=false
                }
                "22" -> {
                    checkBox_22.isEnabled=true
                    checkBox_22.isChecked=false
                }
                "23" -> {
                    checkBox_23.isEnabled=true
                    checkBox_23.isChecked=false
                }
                "31" -> {
                    checkBox_31.isEnabled=true
                    checkBox_31.isChecked=false
                }
                "32" -> {
                    checkBox_32.isEnabled=true
                    checkBox_32.isChecked=false
                }
                "33" -> {
                    checkBox_33.isEnabled=true
                    checkBox_33.isChecked=false

                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuetionRandomFormat(randomQuestionF: Map<String, Int>) {
        for ((key ,value ) in randomQuestionF){
            when (key) {
                "11" -> {
                    checkBox_11.text="Easy-1 =$value"
                    checkBox_11.isEnabled=true
                }
                "12" -> {
                    checkBox_12.text="Easy-2 =$value"
                    checkBox_12.isEnabled=true
                }
                "13" -> {
                    checkBox_13.text="Easy-3 =$value"
                    checkBox_13.isEnabled=true

                }
                "21" -> {
                    checkBox_21.text="Medium-1 =$value"
                    checkBox_21.isEnabled=true

                }
                "22" -> {
                    checkBox_22.text="Medium-2 =$value"
                    checkBox_22.isEnabled=true

                }
                "23" -> {
                    checkBox_23.text="Medium-3 =$value"
                    checkBox_23.isEnabled=true

                }
                "31" -> {
                    checkBox_31.text="Hard-1 =$value"
                    checkBox_31.isEnabled=true

                }
                "32" -> {
                    checkBox_32.text="Hard-2 =$value"
                    checkBox_32.isEnabled=true

                }
                "33" -> {
                    checkBox_33.text="Hard-3 =$value"
                    checkBox_33.isEnabled=true

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuetionMainFormat(a: Map<String, Int>) {
        for ((key ,value ) in a){
            when (key) {
                "00" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"none-0 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "11" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Easy-1 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "12" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Easy-2 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "13" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Easy-3 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "21" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Medium-1 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "22" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Medium-2 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "23" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Medium-3 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "31" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Hard-1 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "32" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Hard-2 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
                "33" -> {
                    txt_type_q_main.text=txt_type_q_main.text.toString()+"\n"+"Hard-3 ="
                    txt_num_q_type_main.text=txt_num_q_type_main.text.toString()+"\n $value"
                }
            }
        }
    }
}