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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_result_quiz.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ResultQuizFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private lateinit var database: DatabaseReference 
    private lateinit var databaseUserCreated: DatabaseReference
    private lateinit var dataJoinUser: DatabaseReference

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
        databaseUserCreated=FirebaseDatabase.getInstance().reference
        database=FirebaseDatabase.getInstance().reference
        dataJoinUser=FirebaseDatabase.getInstance().reference

        GlobalScope.launch(Dispatchers.Main) {
            val userIdCreatedQuiz = getUserIdCreated(args.codeQuiz)
            uploadDataScore(resultQ, args.codeQuiz, userIdCreatedQuiz)
            Toast.makeText(context,"Upload score user successful ", Toast.LENGTH_LONG).show()
        }

        btn_go_home_from_result.setOnClickListener {
            navController.navigate(R.id.action_resultQuizFragment_to_homeFragment)
        }
    }

    private suspend fun getUserIdCreated(codeQuiz: String): String {
        val dataSnapshot = databaseUserCreated.child("QuizApp").child("Quizzes").child(codeQuiz)
            .child("userIdCreatedQuiz").get().await()

        return dataSnapshot.value.toString()
    }

    private fun uploadDataScore(resultQ: String, codeQuiz: String, userIdCreatedQuiz: String) {
        database.child("user").child(userIdCreatedQuiz).child("QuizCreated").child(codeQuiz).child("UserFinishQuiz")
            .child(currentUser).setValue(resultQ)


    }
}