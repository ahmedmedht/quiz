package com.example.quizproject.homepages

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.example.quizproject.model.QuestionModel
import com.example.quizproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_code_message.*
import kotlinx.android.synthetic.main.fragment_join_code.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class JoinCodeFragment : Fragment() {
    private lateinit var dref: DatabaseReference
    private lateinit var getQuizQuestion: ArrayList<QuestionModel>
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val checkUserRef:DatabaseReference=FirebaseDatabase.getInstance().getReference("user")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_join_code, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        dref = FirebaseDatabase.getInstance().getReference("QuizApp")
        getQuizQuestion = ArrayList()

        btn_join_quiz_code.setOnClickListener {

            val codeJoin = edt_join_code_quiz.text.toString()
            getDataFromFirebase(codeJoin) { checkQuiz ->
                if (checkQuiz) {

                    val action =
                        JoinCodeFragmentDirections.actionJoinCodeFragmentToJoinFragment(codeJoin)
                    navController.navigate(action)
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun getDataFromFirebase(
        getCodeQuizJoin: String,
        callback: (Boolean) -> Unit
    ) {
        var checkDate = false
        var userCreatedQuiz = ""

        dref.child("Quizzes").child(getCodeQuizJoin).get().addOnSuccessListener { quizSnapshot ->
            if (quizSnapshot.exists()) {
                val dateNow = Date().time
                val getStartTime = quizSnapshot.child("StartTimeQuiz").value.toString().toLong()
                val getEndTime = quizSnapshot.child("EndTimeQuiz").value.toString().toLong()
                userCreatedQuiz = quizSnapshot.child("userIdCreatedQuiz").value.toString()
                if (dateNow >= getStartTime) {
                    if (getEndTime > dateNow) {

                        val userFinishQuizRef = checkUserRef.child(userCreatedQuiz)
                            .child("QuizCreated")
                            .child(getCodeQuizJoin)
                            .child("UserFinishQuiz")
                            .child(currentUser)

                        userFinishQuizRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // The currentUser exists in the database
                                    checkDate = false
                                    Toast.makeText(context, "This user has already entered the quiz", Toast.LENGTH_LONG).show()
                                } else {
                                    // The currentUser does not exist in the database
                                    checkDate = true
                                }
                                callback(checkDate)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("JoinCodeFragment", "Error checking user: ${error.message}")
                                Toast.makeText(context, "Error checking user", Toast.LENGTH_LONG).show()
                                callback(false)
                            }
                        })
                    } else {
                        checkDate = false
                        Toast.makeText(context, "Time Quiz End", Toast.LENGTH_LONG).show()
                        callback(checkDate)
                    }

                } else {
                    checkDate = false
                    Toast.makeText(context, "Quiz has not started yet", Toast.LENGTH_LONG).show()
                    callback(checkDate)
                }

            } else {
                Toast.makeText(context, "QUIZ DOESN'T EXISTS ", Toast.LENGTH_LONG).show()
                checkDate = false
                callback(checkDate)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "QUIZ DOESN'T EXISTS ", Toast.LENGTH_LONG).show()
            checkDate = false
            callback(false)
        }
    }
}