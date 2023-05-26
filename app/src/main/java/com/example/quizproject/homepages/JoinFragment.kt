package com.example.quizproject.homepages

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

import com.example.quizproject.R
import com.example.quizproject.model.ModelAnswer
import com.example.quizproject.model.ModelMainQuestion
import com.example.quizproject.model.QuestionModel
import com.example.quizproject.model.RandomFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_join.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CountDownLatch
import com.google.firebase.database.FirebaseDatabase as FirebaseDatabase

class JoinFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var dataJoinUser: DatabaseReference=FirebaseDatabase.getInstance().getReference("user").child(currentUser)
    private var dataCreatedQuizUser: DatabaseReference=FirebaseDatabase.getInstance().reference

    private lateinit var dref: DatabaseReference
    private val args:JoinFragmentArgs by navArgs()

    private var result=0
    private var numberquestion=0

    private val options = ArrayList<Button>()
    private var btnSelected: Int? =null

    private var quizQuestionMcq=ArrayList<QuestionModel>()

    private var getAnswerUser=ArrayList<ModelAnswer>()

    //////////////////////////////////////JOINRandom
    private var allQuestionMain:ArrayList<ModelMainQuestion> =ArrayList()
    private var allQuestionRandom:ArrayList<ModelMainQuestion> =ArrayList()

    private var templateRandomQuestion:ArrayList<RandomFormat> = ArrayList()
    private var questionRandomShow:ArrayList<String> =ArrayList()
    private var questionAllShow:ArrayList<String> =ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_join, container, false)
    }



    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        val getCodeQuizJoin=args.getCodeQuiz

        dref= FirebaseDatabase.getInstance().getReference("QuizApp")

        options.add(0, txt_answer1)
        options.add(1, txt_answer2)
        options.add(2, txt_answer3)
        options.add(3, txt_answer4)



        getTemplate(getCodeQuizJoin){
            if (it){
                if (templateRandomQuestion.isNotEmpty()){
                    questionRandomSet(templateRandomQuestion,allQuestionRandom)
                    questionRandomShow.shuffle()
                }
                Log.d("allQuestionMain1",allQuestionMain.toString())

                if (allQuestionMain.isNotEmpty()){
                    Log.d("allQuestionMain",allQuestionMain.toString())
                    for (i in allQuestionMain){
                        questionAllShow.add(i.numberQuestion)
                    }
                }
                if (questionRandomShow.isNotEmpty()){
                    Log.d("allQuestionMain2",questionRandomShow.toString())
                    Log.d("allQuestionRandom",questionRandomShow.toString())

                    for (i in questionRandomShow){
                        questionAllShow.add(i)
                    }
                }
            }
        }
        getDataFromFirebase(getCodeQuizJoin) {
            if (it) {
                showFirstQuestionMcq()
            }
        }
        getQuizName(getCodeQuizJoin)



        btn_next_quiz_join.setOnClickListener {
            if (btn_next_quiz_join.text.toString()=="finish"){
                Toast.makeText(context, "Quiz Finished", Toast.LENGTH_LONG).show()
                if(edt_answer_join.isVisible){
                    getAnswerUser.add(ModelAnswer(txt_question_join.text.toString(),edt_answer_join.text.toString()))
                }else{
                    if (btnSelected != null) {
                        val correctAnswer = quizQuestionMcq[questionAllShow[numberquestion].toInt()].numberAnswer
                        getAnswerUser.add(ModelAnswer(txt_question_join.text.toString(),btnSelected.toString()))
                        if (correctAnswer.toString() == btnSelected.toString()) {
                            result += 1
                        }
                        btnSelected = null
                    } else {
                        Toast.makeText(context, "please select answer", Toast.LENGTH_LONG).show()
                        txt_question_join.error = "please select answer"
                    }
                }
                GlobalScope.launch(Dispatchers.Main) {
                    val userIdCreatedQuiz = getUserIdCreated(getCodeQuizJoin)
                    uploadAnswersUser(result,userIdCreatedQuiz)
                }

                val action = JoinFragmentDirections.actionJoinFragmentToResultQuizFragment(
                    result.toString()
                )
                navController.navigate(action)
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }else{
                if(edt_answer_join.isVisible){
                    getAnswerUser.add(ModelAnswer(txt_question_join.text.toString(),edt_answer_join.text.toString()))
                    numberquestion+=1
                    edt_answer_join.setText("")
                    showFirstQuestionMcq()
                }else if (layout_answer_join.isVisible){
                    if (btnSelected != null) {
                        val correctAnswer = quizQuestionMcq[questionAllShow[numberquestion].toInt()].numberAnswer
                        numberquestion += 1
                        getAnswerUser.add(ModelAnswer(txt_question_join.text.toString(),btnSelected.toString()))

                        if (correctAnswer.toString() == btnSelected.toString()) {
                            result += 1
                        }

                            setColorDefult()
                            showFirstQuestionMcq()
                            btnSelected = null

                    } else {
                        Toast.makeText(context, "please select answer", Toast.LENGTH_LONG).show()
                        txt_question_join.error = "please select answer"
                    }
                }

            }





        }
        txt_answer1.setOnClickListener {
            selectAnswer(txt_answer1)
        }

        txt_answer2.setOnClickListener {
            selectAnswer(txt_answer2)
        }
        txt_answer3.setOnClickListener {
            selectAnswer(txt_answer3)
        }
        txt_answer4.setOnClickListener {
            selectAnswer(txt_answer4)
        }

    }

    private suspend fun getUserIdCreated(codeQuizJoin: String): String {
        val dataSnapshot = dataCreatedQuizUser.child("QuizApp").child("Quizzes").child(codeQuizJoin)
            .child("userIdCreatedQuiz").get().await()

        return dataSnapshot.value.toString()
    }

    private fun getQuizName(codeQuizJoin: String) {
        dref.child("Quizzes").child(codeQuizJoin).get().addOnSuccessListener {
                if (it.exists()){
                    val quizName=it.child("QuizName").value.toString()
                    txt_quiz_title_join.text=quizName
                }
            }
        }

    private fun questionRandomSet(templateRandomQuestions: ArrayList<RandomFormat>, allQuestionRandom:ArrayList<ModelMainQuestion>?) {
        templateRandomQuestions .forEach { temp->
            val arrayQuestionSelect:ArrayList<String> =ArrayList()
            for (tempType in temp.arraySelectTypeQuestion!!){
                for (allQ in allQuestionRandom!!){
                    if (allQ.questionClassification==tempType){
                        arrayQuestionSelect.add(allQ.numberQuestion)
                    }
                }
            }
            arrayQuestionSelect.shuffle()
            for (i in 0 until temp.totalQ!!){
                questionRandomShow.add(arrayQuestionSelect[i])
            }
        }
    }

    private fun uploadAnswersUser(result: Int, userIdCreatedQuiz: String) {
        dataJoinUser.child("QuizJoin").child(args.getCodeQuiz).child("answers user").setValue(getAnswerUser)
        dataJoinUser.child("QuizJoin").child(args.getCodeQuiz).child("score").setValue(result)
        dataCreatedQuizUser.child("user").child(userIdCreatedQuiz).child("QuizCreated").child(args.getCodeQuiz).child("UserFinishQuiz")
            .child(currentUser).child("score").setValue(result)
        dataCreatedQuizUser.child("user").child(userIdCreatedQuiz).child("QuizCreated").child(args.getCodeQuiz).child("UserFinishQuiz")
            .child(currentUser).child("Answers user").setValue(getAnswerUser)


    }

    fun convertStringToBitmap(imageString: String): Bitmap? {
        val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
    private fun showFirstQuestionMcq() {

        Log.d("dataQuiz",questionAllShow.toString())
        val num=questionAllShow[numberquestion].toInt()
        val qeustion= quizQuestionMcq[num].question
        txt_question_join.text = qeustion
        img_question_join.isVisible=false
        txt_answer1.text = quizQuestionMcq[num].arrayAnswer?.get(0)
        txt_answer2.text = quizQuestionMcq[num].arrayAnswer?.get(1)
        txt_answer3.text = quizQuestionMcq[num].arrayAnswer?.get(2)
        txt_answer4.text = quizQuestionMcq[num].arrayAnswer?.get(3)

        val checkQ=quizQuestionMcq[num].checkImg
        if (checkQ){
            img_question_join.setImageBitmap(convertStringToBitmap(quizQuestionMcq[num].imgQuestion))
            img_question_join.isVisible=true
        }
        val checkMcq=quizQuestionMcq[num].numberAnswer
        if (checkMcq != null) {
            if (checkMcq>0){
                edt_answer_join.isVisible=false
                layout_answer_join.isVisible=true
            }else{
                edt_answer_join.isVisible=true
                layout_answer_join.isVisible=false
            }

        }
        if(questionAllShow.size==numberquestion+1){
            btn_next_quiz_join.text = "finish"
        }
    }






    private fun setColorDefult() {
        for (id in options){
            id.background= activity?.let {
                ContextCompat.getDrawable(
                    it,R.drawable.background_txt_answer_join
                )
            }
        }
    }

    private fun selectAnswer(txtAnswer: Button?) {
        setColorDefult()
        if (txtAnswer != null) {
            txtAnswer.background= activity?.let {
                ContextCompat.getDrawable(
                    it,R.drawable.background_answer_selected
                )
            }
        }
        btnSelected = when (txtAnswer) {
            txt_answer1 -> 1
            txt_answer2 -> 2
            txt_answer3 -> 3
            txt_answer4 -> 4
            else -> null
        }
    }

    private fun getTemplate(
        codeQuizJoin: String,
        callback: (Boolean) -> Unit
    ) {
        dref.child("Quizzes").child(codeQuizJoin).child("TypeQuestions").get().addOnSuccessListener {
            val typeQuestionsIt = it.value as? Map<String, Any>

            if (typeQuestionsIt!=null){
                val hasMain=typeQuestionsIt.containsKey("Main")
                val hasRandom=typeQuestionsIt.containsKey("Random")
                val hasTemplate=typeQuestionsIt.containsKey("TemplateQuiz")
                if (hasMain){
                    val getMainQuestionIt=it.child("Main").children
                    for (i in getMainQuestionIt){
                        val numberQuestionMain=i.child("numberQuestion").value.toString()
                        val typeQuestionMain=i.child("questionClassification").value.toString()
                        Log.d("itemMain","n=$numberQuestionMain / t=$typeQuestionMain")
                        allQuestionMain.add(ModelMainQuestion(numberQuestionMain,typeQuestionMain))
                    }
                }
                if (hasRandom){
                    val getRandomQuestionIt=it.child("Random").children
                    for (i in getRandomQuestionIt){
                        val numberQuestionRandom=i.child("numberQuestion").value.toString()
                        val typeQuestionRandom=i.child("questionClassification").value.toString()
                        Log.d("itemMain","n=$numberQuestionRandom / t=$typeQuestionRandom")
                        allQuestionRandom.add(ModelMainQuestion(numberQuestionRandom,typeQuestionRandom))
                    }
                }
                if (hasTemplate){
                    val templateRandomIt=it.child("TemplateQuiz").value as? Map<String, Any>
                    val hasRandomFormat= templateRandomIt?.containsKey("RandomFormat")
                    if (hasRandomFormat == true){
                        val formatRandomIt=it.child("TemplateQuiz").child("RandomFormat").children
                        for (i in formatRandomIt){
                            val arrayTypesQuestions = ArrayList<String>()
                            for (i2 in i.child("arraySelectTypeQuestion").children){
                                arrayTypesQuestions.add(i2.value.toString())
                            }
                            val totalQuestions=i.child("totalQ").value.toString()
                            templateRandomQuestion.add(RandomFormat(arrayTypesQuestions,totalQuestions.toInt()))
                        }
                        //templateRandomQuestion=it.child("TemplateQuiz").child("RandomFormat").value as ArrayList<RandomFormat>
                    }
                }


                callback(true)
            }else{
                Toast.makeText(context,"Error,Please try join again!",Toast.LENGTH_SHORT).show()
                callback(false)
            }
        }.addOnFailureListener {
            Toast.makeText(context,"Error,Please try join again!",Toast.LENGTH_SHORT).show()
            callback(false)
        }
    }
    private fun getDataFromFirebase(
        getCodeQuizJoin: String,
        callback: (Boolean) ->Unit
    ) {

            dref.child("Quizzes").child(getCodeQuizJoin).child("QuizQuestions").get().addOnSuccessListener {
            if (it.exists()){

                        val latch = CountDownLatch(it.childrenCount.toInt())

                        for (n in it.children) {
                            val q = n.child("question").value.toString()
                            val arrayAnswr = ArrayList<String>()

                            arrayAnswr.add(n.child("arrayAnswer").child("0").value.toString())
                            arrayAnswr.add(n.child("arrayAnswer").child("1").value.toString())
                            arrayAnswr.add(n.child("arrayAnswer").child("2").value.toString())
                            arrayAnswr.add(n.child("arrayAnswer").child("3").value.toString())

                            val numcorrectAnswer = n.child("numberAnswer").value.toString().toInt()
                            val imgQ=n.child("imgQuestion").value.toString()
                            val checkImage=n.child("checkImg").value.toString().toBoolean()


                            quizQuestionMcq.add(QuestionModel(q, arrayAnswr, numcorrectAnswer,checkImage,imgQ))

                            latch.countDown()
                        }

                        latch.await()
                        callback(true)
                        Log.d("questionsMcq", quizQuestionMcq.toString())

            }else{
                Toast.makeText(context,"QUIZ DOESN'T EXISTS ",Toast.LENGTH_LONG).show()
                Log.d("questionsMcqNo", quizQuestionMcq.toString())
                callback(false)
                callback(false)


            }
        }.addOnFailureListener {
            Toast.makeText(context,"QUIZ DOESN'T EXISTS ",Toast.LENGTH_LONG).show()
            Log.d("questionsMcqNoNo", quizQuestionMcq.toString())


        }
    }



/////////////////////////////////////////////////////////////////////



}




