package com.example.quizproject.model

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizproject.R
import java.text.SimpleDateFormat
import java.util.Date

class AdapterQreatedQuiz constructor(private val itemList: ArrayList<ModelCreatedQuiz>):
    RecyclerView.Adapter<AdapterQreatedQuiz.MyViewHolder>(){


    interface ItemClickListener{
        fun getDataQuiz(position: Int)
    }
    private var clickAddData:ItemClickListener? =null
    fun setItemClickListener(listener: ItemClickListener) {
        clickAddData = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_created_quiz , parent , false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val time=itemList[position].quizTimeStart
        holder.quiTime.text=convertMillisecondsToDate(time.toLong())
        holder.quizName.text=itemList[position].quizName


    }
    @SuppressLint("SimpleDateFormat")
    fun convertMillisecondsToDate(milliseconds: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(milliseconds)
        return sdf.format(date)
    }



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val quizName: TextView =itemView.findViewById(R.id.title_quiz_item)
        val quiTime: TextView =itemView.findViewById(R.id.time_quiz_item)
        private val imgDawnload:ImageButton=itemView.findViewById(R.id.download_quiz_item)
        init {
            imgDawnload.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Log.d("Position =",position.toString())
                    clickAddData?.getDataQuiz(position)
                }
            }
        }


    }

}