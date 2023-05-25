package com.example.quizproject.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.quizproject.R

class RecyclerViewFormatRandomAdapter constructor(private val itemList: ArrayList<RandomFormat>):
RecyclerView.Adapter<RecyclerViewFormatRandomAdapter.MyViewHolder>()
{
    interface ItemClickListener {
        fun onItemRemoved(position: Int)
    }

    private var itemClickListener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_format_random_list_item , parent , false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
        {
            var typeStr=""
            val r=itemList[position]

            for (n in r.arraySelectTypeQuestion!!){
                when (n) {
                    "11" -> typeStr += "Easy-1 "
                    "12" -> typeStr += "Easy-2 "
                    "13" -> typeStr += "Easy-3 "
                    "21" -> typeStr += "Medium-1 "
                    "22" -> typeStr += "Medium-2 "
                    "23" -> typeStr += "Medium-3 "
                    "31" -> typeStr += "Hard-1 "
                    "32" -> typeStr += "Hard-2 "
                    "33" -> typeStr += "Hard-3 "

                }
            }
            holder.tvTypeSelection.text=typeStr
            holder.txtTotalNum.text=r.totalQ.toString()


        }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val tvTypeSelection : TextView = itemView.findViewById(R.id.txt_type_selection)
        val txtTotalNum:TextView = itemView.findViewById(R.id.txt_total_num_enter)
        private val imgRemove:ImageButton=itemView.findViewById(R.id.img_remove_item)
        init {
            imgRemove.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemRemoved(position)
                }
            }
        }


    }


}