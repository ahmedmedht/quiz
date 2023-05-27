package com.example.quizproject.navigationdrawer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizproject.R
import com.example.quizproject.model.AdapterQreatedQuiz
import com.example.quizproject.model.ModelCreatedQuiz
import com.example.quizproject.model.RecyclerViewFormatRandomAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_created_quizzes.img_back_created
import kotlinx.android.synthetic.main.fragment_created_quizzes.rv_created_quiz


class CreatedQuizzesFragment : Fragment() {
    private var createdQuizInfo: ArrayList<ModelCreatedQuiz> =ArrayList()
    private lateinit var adapter: AdapterQreatedQuiz
    private val currentUser=FirebaseAuth.getInstance().currentUser
    private val dataRef=FirebaseDatabase.getInstance().reference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_created_quizzes, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromFirebase(){
            if (it){
                Log.d("arrayCodeQuizzes",createdQuizInfo.toString())
                adapter = AdapterQreatedQuiz(createdQuizInfo)
                rv_created_quiz.adapter=adapter
                rv_created_quiz.layoutManager= LinearLayoutManager(requireContext())
            }
        }
        img_back_created.setOnClickListener {
            val navController= Navigation.findNavController(view)
            navController.navigateUp()
        }


    }

    private fun getDataFromFirebase(callback: (Boolean) -> Unit) {
        val arrayCodeQuizzes:ArrayList<String> = ArrayList()
        dataRef.child("user").child(currentUser?.uid.toString()).get().addOnSuccessListener {
            if (it.exists()){
                val userIt = it.value as? Map<String, Any>
                val checkUserCreated=userIt?.contains("QuizCreated")
                if (checkUserCreated == true){
                    val quizCreated=it.child("QuizCreated").value as? Map<String, Any>
                    if (quizCreated != null) {
                        for (co in quizCreated){
                            Log.d("codeQuiz",co.key)
                            arrayCodeQuizzes.add(co.key)


                        }
                        addDataToArrayList(arrayCodeQuizzes){itt->
                            if (itt) callback(true)
                        }
                    }
                }

            }else{
                callback(false)
                Toast.makeText(context,"Please try again!",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            callback(false)

            Toast.makeText(context,"Failure, Please try again!",Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun addDataToArrayList(
        arrayCodeQuizzes: ArrayList<String>,
        callback: (Boolean) -> Unit
    ) {
        for (code in arrayCodeQuizzes) {
            dataRef.child("QuizApp").child("Quizzes").child(code).get().addOnSuccessListener {
                if (it.exists()) {
                    val nameQuiz = it.child("QuizName").value.toString()
                    val quizStTime = it.child("StartTimeQuiz").value.toString()
                    createdQuizInfo.add(ModelCreatedQuiz(code, nameQuiz, quizStTime))
                    callback(true)
                } else {
                    callback(false)
                    Toast.makeText(context, "Please try again!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                callback(false)
                Toast.makeText(context, "Failure, Please try again!", Toast.LENGTH_SHORT).show()
            }
        }

    }




}