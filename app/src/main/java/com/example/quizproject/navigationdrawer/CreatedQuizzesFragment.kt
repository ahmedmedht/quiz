package com.example.quizproject.navigationdrawer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizproject.R
import com.example.quizproject.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_created_quizzes.img_back_created
import kotlinx.android.synthetic.main.fragment_created_quizzes.rv_created_quiz
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileOutputStream


class CreatedQuizzesFragment : Fragment() ,AdapterQreatedQuiz.ItemClickListener{
    private var createdQuizInfo: ArrayList<ModelCreatedQuiz> =ArrayList()
    private lateinit var adapter: AdapterQreatedQuiz
    private val currentUser=FirebaseAuth.getInstance().currentUser
    private val dataRef=FirebaseDatabase.getInstance().reference
    private val dataQuiz=FirebaseDatabase.getInstance().reference
    private val dataUserJoinQuiz:ArrayList<ModelExel> =ArrayList()
    private val questionQuiz=ArrayList<String>()
    private var codeQuizExel:String=""
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
                adapter.setItemClickListener(this)
                rv_created_quiz.adapter=adapter
                rv_created_quiz.layoutManager= LinearLayoutManager(requireContext())


            }
        }
        img_back_created.setOnClickListener {
            val navController= Navigation.findNavController(view)
            navController.navigateUp()
        }
    }

    private fun getDataQuizFromFIrebase(codeQuizExel: String, callback: (Boolean) -> Unit) {
        dataQuiz.child("QuizApp").child("Quizzes").child(codeQuizExel).child("QuizQuestions")
            .get().addOnSuccessListener {
                if (it.exists()){
                    for (i in it.children){
                        questionQuiz.add(i.child("question").value.toString())
                    }
                    getAnswerUser(codeQuizExel){itt->
                        if (itt) callback(true)
                        else callback(false)
                    }
                    
                }else{
                    callback(false)
                }
            }
    }

    private fun getAnswerUser(codeQuizExel: String,callback: (Boolean) -> Unit) {
        dataQuiz.child("user").child(currentUser?.uid.toString()).child("QuizCreated").child(codeQuizExel).get().addOnSuccessListener {
            if (it.exists()){
                val mapIt=it.value
                if (mapIt is Int){
                    Toast.makeText(context,"no one joined quiz",Toast.LENGTH_SHORT).show()
                    callback(false)
                }else{
                    val userFinishQuiz=it.child("UserFinishQuiz").value as? Map<String, Any>
                    val userUid=ArrayList<String>()
                    if (userFinishQuiz != null) {
                        for (user in userFinishQuiz){
                            userUid.add(user.key)
                        }
                        dataUserJoinQuiz.clear()
                        val totalUsers = userUid.size
                        var completedUsers = 0

                        for (u in userUid){
                            val userScore=it.child("UserFinishQuiz").child(u).child("score").value.toString().toInt()
                            getEmail(u){email->
                                val modelAnswerUser=ArrayList<ModelAnswer>()
                                val arrayAnswerUser=it.child("UserFinishQuiz").child(u).child("Answers user").children
                                for (a in arrayAnswerUser){
                                    val question=a.child("question").value.toString()
                                    val answer=a.child("answer").value.toString()
                                    modelAnswerUser.add(ModelAnswer(question,answer))
                                }
                                dataUserJoinQuiz.add(ModelExel(email,modelAnswerUser,userScore))
                                completedUsers++
                                if (completedUsers == totalUsers) {
                                    callback(true)
                                }
                            }

                        }
                    }else callback(false)

                }
            }
        }

    }

    private fun getEmail(key: String,callback: (String) -> Unit) {
        var em=""
        dataRef.child("user").child(key).get().addOnSuccessListener {

            if (it.exists()){
                em=it.child("email").value.toString()
                callback(em)

            }else {
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
                em=""
                callback(em)
            }
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

    override fun getDataQuiz(position: Int) {
        if (position >=0 && position < createdQuizInfo.size){
            codeQuizExel= createdQuizInfo[position].codeQuiz
            getDataQuizFromFIrebase(codeQuizExel){ittt->
                if (ittt){
                    Log.d("DataUser",dataUserJoinQuiz.toString())


                    //putDataInExel(dataUserJoinQuiz,questionQuiz)

                }else Log.d("Dataaaaaaaaaaaaa","problem")
            }
        }
    }

    private fun putDataInExel(
        dataUserJoinQuiz: ArrayList<ModelExel>,
        questionQuiz: ArrayList<String>
    ) {

        val workbook: Workbook = WorkbookFactory.create(true)
        val sheet = workbook.createSheet("Sheet 1")
        val titleRow=sheet.createRow(0)
        val emailColum=titleRow.createCell(0)
        emailColum.setCellValue("Email")
        val scoreColum=titleRow.createCell(1)
        scoreColum.setCellValue("Score")
        var rowIndex = 1
        for (rowData in dataUserJoinQuiz){
            val row = sheet.createRow(rowIndex++)
            var columnIndex = 0
            val cell = row.createCell(columnIndex++)
            cell.setCellValue(rowData.emailUser)
            var columnIndexScore = 0
            val cellScore = row.createCell(columnIndexScore++)
            cellScore.setCellValue(rowData.score.toString())
        }

        // Write workbook to output Excel file
        val file = File(requireContext().getExternalFilesDir(null), "outFile.xlsx")
        val fileOutputStream = FileOutputStream(file)
        workbook.write(fileOutputStream)
        fileOutputStream.close()
        downloadExel()
    }

    private fun downloadExel() {
        val file = File(requireContext().getExternalFilesDir(null), "outFile.xlsx")
        val fileUri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "application/vnd.ms-excel")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(intent)

    }


}