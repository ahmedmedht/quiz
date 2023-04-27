package com.example.quizproject.homepages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_result_quiz.*

class ResultQuizFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private lateinit var database: DatabaseReference
    private lateinit var databaseUserCreated: DatabaseReference

    private val args:ResultQuizFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        val resultQ=args.score
        txt_result_quiz.text="result = $resultQ"
        uploadDataScore(resultQ)
        btn_go_home_from_result.setOnClickListener {
            navController.navigate(R.id.action_resultQuizFragment_to_homeFragment)
        }
    }


    private fun uploadDataScore(resultQ: String) {
        val codeQuiz=args.codeQuiz
        val getUserIdCreated:String=getUserCreated(codeQuiz)


        database.child("user").child(getUserIdCreated).child("QuizCreated").child(codeQuiz).child("UserFinishQuiz")
            .child(currentUser).setValue(resultQ)
    }


    private fun getUserCreated(codeQuiz: String): String {
        var userId=""
        databaseUserCreated.child("QuizApp").child("Quizzes").child(codeQuiz)
            .child("userIdCreatedQuiz").get().addOnSuccessListener {
                if (it.exists()){
                    userId=it.value.toString()
                    Toast.makeText(context,"Upload score user successful ", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"Erorr score user not upload", Toast.LENGTH_LONG).show()

                }
            }.addOnFailureListener {
                Toast.makeText(context,"Erorr", Toast.LENGTH_LONG).show()

            }
        return userId
    }


}