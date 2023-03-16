package com.example.quizproject.Model

class QuestionModel {
    var question:String?=null
    var
            arrayAnswer:Array<String>?=null

    constructor(question:String? ,arrayAnswer:Array<String>?){
        this.question=question
        this.arrayAnswer=arrayAnswer
    }

    constructor()
}