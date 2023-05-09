package com.asifrezan.qrcodegenerator.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asifrezan.qrcodegenerator.R
import com.asifrezan.qrcodegenerator.data.MyData
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.util.*

class HomeAdapter(private var dataList: List<MyData>) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var deleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteListener(deleteClickListener: OnDeleteClickListener) {
        this.deleteClickListener = deleteClickListener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    fun setData(newDataList: List<MyData>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]

        val (date, time) = currentItem.date.split("T")
        val (time_s, time_ms) = time.split(".")



        holder.textView.text = currentItem.title
        holder.dateTextView.text = "$date at $time_s"



//
//        holder.deleteIcon.setOnClickListener {
//            deleteClickListener?.onDeleteClick(position)
//        }


    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.titleTextView)
        val dateTextView : TextView = itemView.findViewById(R.id.dateTextView)
       // val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
//            deleteIcon.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    deleteClickListener?.onDeleteClick(position)
//                }
//            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }
}
