package com.example.quizproject.homepages
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible

import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.model.QuestionModel
import com.example.quizproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_question.*

import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import com.example.quizproject.model.MapType
import com.example.quizproject.model.ModelMainQuestion
import java.io.ByteArrayOutputStream
import java.util.*
class CreateQuestionFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private val args:CreateQuestionFragmentArgs by navArgs()
    private var addQuestion=ArrayList<QuestionModel>()
    private lateinit var databaseuser: DatabaseReference
    private var strArray: List<Char>  = emptyList()
    private var questionTypeArray=ArrayList<ModelMainQuestion>()
    private var questionTypeArrayRandom=ArrayList<ModelMainQuestion>()
    private var randomQuestionType= mutableMapOf<String,Int>()
    private var mainQuestionType= mutableMapOf<String,Int>()

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var btnWriteNow:RadioButton
    private var strImg=""
    private var checkRandom=false
    private var levelQ=0
    private var typeQ=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_create_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        val numberquestion=args.numberQuestion
        val startQuizTime=args.timeStartQuiz
        val endQuizTime=args.timeEndQuiz
        val totalNumberText=args.numTxtQ
        var currentQuestion=0
        val totalNumberQuestion=totalNumberText+numberquestion

        if(!args.randomQ) txt_menu.isVisible=false

        if (totalNumberText==0){
            layout_add_question_question.isVisible=true
        }else if (totalNumberQuestion==1) btn_next_or_finish_question.text="Finish"



        database = FirebaseDatabase.getInstance().getReference("QuizApp")
        databaseuser = FirebaseDatabase.getInstance().getReference("user")


        radioButton.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton.text.toString())
            btnWriteNow=radioButton
        }
        radioButton2.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton2.text.toString())
            btnWriteNow=radioButton2
        }
        radioButton3.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton3.text.toString())
            btnWriteNow=radioButton3
        }
        radioButton4.setOnClickListener {
            layout_answer_enter.isVisible=true
            edt_enter_answer.setText(radioButton4.text.toString())
            btnWriteNow=radioButton4
        }

        img_btn_change_txt_answer.setOnClickListener {
            btnWriteNow.text = edt_enter_answer.text.toString()
            layout_answer_enter.isVisible=false
        }




        btn_next_or_finish_question.setOnClickListener {
            if (typeQ==0&&(levelQ!=0)){
                txt_menu3.error="error"
            }else if (checkRandom&&txt_menu2.text.toString()=="none"){
                 txt_menu2.error
            }
            else {
                if (totalNumberText > currentQuestion) {
                    if (edt_question_quiz.text.toString() == "") {
                        edt_question_quiz.error = "Please Enter Your Question!"
                    } else {
                        addDatatoArray(currentQuestion)
                        Toast.makeText(
                            context,
                            "current question num: ${currentQuestion + 1}",
                            Toast.LENGTH_SHORT
                        ).show()
                        strImg = ""
                        layout_image_put.isVisible = false
                        currentQuestion += 1
                    }
                    if (totalNumberText == currentQuestion) {
                        layout_add_question_question.isVisible = true

                    }
                    edt_question_quiz.setText("")
                } else {

                    if (totalNumberQuestion > 1 + currentQuestion) {
                        addDatatoArray(currentQuestion)
                        currentQuestion += 1
                        strImg = ""
                        layout_image_put.isVisible = false

                        edt_question_quiz.setText("")
                        radioButton.text = ""
                        radioButton2.text = ""
                        radioButton3.text = ""
                        radioButton4.text = ""

                        Toast.makeText(
                            context,
                            "current question num: ${currentQuestion + 1}",
                            Toast.LENGTH_SHORT
                        ).show()

                        if (totalNumberQuestion == currentQuestion + 1) {
                            btn_next_or_finish_question.text = "Finish"
                        }

                    } else {
                        addDatatoArray(currentQuestion)

                        getrandomarray()
                        val randomcode = strArray.random().toString() + strArray.random()
                            .toString() + strArray.random().toString() + strArray.random()
                            .toString() + strArray.random().toString()

                        uploadQuizToFirebase(
                            randomcode,
                            addQuestion,
                            startQuizTime,
                            endQuizTime,
                            currentUser,
                            totalNumberQuestion
                        )


                        val action =
                            CreateQuestionFragmentDirections.actionCreateQuestionFragmentToQuizTemplateFragment(
                                randomcode,
                                MapType(mainQuestionType),
                                MapType(randomQuestionType)
                            )
                        navController.navigate(action)
                    }

                }
            }
        }

        get_img.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

            startActivityForResult(intent,100);

            layout_image_put.isVisible=true

        }

        img_remove_image_edit.setOnClickListener {
            strImg=""
            layout_image_put.isVisible=false
        }




        txt_menu.setOnClickListener {
            val popupMenu = PopupMenu(context, txt_menu)
            popupMenu.menuInflater.inflate(R.menu.menu_item_main, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                txt_menu.text=menuItem.toString()
                checkRandom = menuItem.toString() != "Main"
                true
            }
            popupMenu.show()
        }

        txt_menu2.setOnClickListener {
            val popupMenu = PopupMenu(context, txt_menu)
            popupMenu.menuInflater.inflate(R.menu.menu_item_level, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                txt_menu2.text=menuItem.toString()
                levelQ = if (menuItem.toString()=="Easy") 1
                else if (menuItem.toString()=="Medium") 2
                else if (menuItem.toString()=="Hard") 3
                else 0

                true
            }

            popupMenu.show()
        }

        txt_menu3.setOnClickListener {
            val popupMenu = PopupMenu(context, txt_menu)
            popupMenu.menuInflater.inflate(R.menu.menu_item_type, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                txt_menu3.text=menuItem.toString()

                typeQ = if (menuItem.toString()=="1") 1
                else if (menuItem.toString()=="2") 2
                else if (menuItem.toString()=="3") 3
                else 0

                if (levelQ==0){
                    typeQ=0
                    txt_menu3.text="none"
                }
                true
            }

            popupMenu.show()
        }



    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            val contentResolver: ContentResolver = requireContext().contentResolver
            val uri: Uri? = data?.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver ,uri)

            strImg=convertBitmapToString(bitmap)

            img_question_edit.setImageBitmap(convertStringToBitmap(strImg))


        }
    }

    private fun convertBitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
    fun convertStringToBitmap(imageString: String): Bitmap? {
        val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
    fun incrementKey(map: MutableMap<String, Int>, key: String) {
        // Check if the key exists in the map
        if (map.containsKey(key)) {
            // If the key exists, increment its value by 1
            val value = map[key] ?: 0
            map[key] = value + 1
        } else {
            // If the key doesn't exist, initialize its value to 0
            map[key] = 1
        }
    }

    private fun addDatatoArray(currentQuestion: Int) {
        if (checkRandom){
            questionTypeArrayRandom.add(ModelMainQuestion(currentQuestion.toString(),levelQ.toString()+typeQ.toString()))
                incrementKey(randomQuestionType,levelQ.toString()+typeQ.toString())
        }else{
            questionTypeArray.add(ModelMainQuestion(currentQuestion.toString(),levelQ.toString()+typeQ.toString()))
            incrementKey(mainQuestionType,levelQ.toString()+typeQ.toString())

        }

        val arryAnswer: ArrayList<String> = ArrayList()
        arryAnswer.add(radioButton.text.toString())
        arryAnswer.add(radioButton2.text.toString())
        arryAnswer.add(radioButton3.text.toString())
        arryAnswer.add(radioButton4.text.toString())

        val answerNumber=checkedradio()
        var cheackImg=false
        if (strImg!="") cheackImg=true

        addQuestion.add(QuestionModel(edt_question_quiz.text.toString(),arryAnswer,answerNumber,cheackImg,strImg))
    }

    private fun checkedradio(): Int {

        return when (radio_group_btn_enter_answer.checkedRadioButtonId) {
            R.id.radioButton -> 1
            R.id.radioButton2 -> 2
            R.id.radioButton3 -> 3
            R.id.radioButton4 -> 4
            else -> 0
        }

    }



    private fun uploadQuizToFirebase(
        quizid: String,
        questionQuiz: ArrayList<QuestionModel>,
        startQuizTime: Long,
        endQuizTime: Long,
        currentUser: FirebaseUser?,
        numberQuestion: Int
    ) {

        database.child("Quizzes").child(quizid).child("TypeQuestions").child("Main").setValue(questionTypeArray)
        database.child("Quizzes").child(quizid).child("TypeQuestions").child("Random").setValue(questionTypeArrayRandom)
        database.child("Quizzes").child(quizid).child("TypeQuestions").child("RandomFormat").setValue(randomQuestionType)

        database.child("Quizzes").child(quizid).child("StartTimeQuiz").setValue(startQuizTime)
        database.child("Quizzes").child(quizid).child("QuizName").setValue(args.nameQuiz)
        database.child("Quizzes").child(quizid).child("EndTimeQuiz").setValue(endQuizTime)
        database.child("Quizzes").child(quizid).child("QuizQuestions").setValue(questionQuiz)
        database.child("Quizzes").child(quizid).child("NumberQuestion").setValue(numberQuestion)
        if (currentUser != null) {
            database.child("Quizzes").child(quizid).child("userIdCreatedQuiz").setValue(currentUser.uid)
        }

        if (currentUser != null) {
            databaseuser.child(currentUser.uid).child("QuizCreated").child(quizid).setValue(numberQuestion)

                .addOnCompleteListener{
                    Toast.makeText(context,"QUIZ UPLOAD SUCCESSFULL",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(context,"Error fail upload quiz ${err.message}",Toast.LENGTH_SHORT).show()
                }
        }


    }

    private fun getrandomarray() {
        for (i in 'a'..'z'){
            strArray=(strArray+i)
        }
        for (i in 'A'..'Z'){
            strArray=(strArray+i)
        }
        for (i in '0'..'9'){
            strArray=(strArray+i)
        }

    }

}
