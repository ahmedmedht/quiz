package com.example.quizproject.HomePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.quizproject.Model.QuestionModel

import com.example.quizproject.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_join_code.*
import com.google.firebase.database.FirebaseDatabase as FirebaseDatabase

class JoinFragment : Fragment() {

    private lateinit var dref: DatabaseReference
    private lateinit var getQuizQuestion:ArrayList<QuestionModel>
    private val args:JoinFragmentArgs by navArgs()
    private lateinit var data: FirebaseDatabase
    private val getCodeQuizJoin=args.getCodeQuiz

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_join, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dref= FirebaseDatabase.getInstance().getReference("QuizApp")
        data=FirebaseDatabase.getInstance()
        getQuizQuestion=ArrayList()

        getDataFromFirebase()

    }

    private fun getDataFromFirebase() {
        dref.child(getCodeQuizJoin).get().addOnSuccessListener {
            if (it.exists()){

            }else{

            }
        }.addOnFailureListener {
            
        }
    }






}