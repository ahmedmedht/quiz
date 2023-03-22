package com.example.quizproject.Model

class QuestionModel  {
    var question:String?=null
    var arrayAnswer:ArrayList<String> ?= null
    var answernumber:Int?=null

    constructor()
    constructor(question: String?, arrayAnswer: ArrayList<String>?,answernumber:Int?) {
        this.question = question
        this.arrayAnswer = arrayAnswer
        this.answernumber=answernumber
    }

}