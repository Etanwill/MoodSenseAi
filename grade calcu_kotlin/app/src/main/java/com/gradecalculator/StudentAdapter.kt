package com.gradecalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val onDelete: (Student) -> Unit,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    private val items = mutableListOf<Student>()

    fun submitList(list: List<Student>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card:    CardView    = view.findViewById(R.id.cardView)
        val grade:   TextView   = view.findViewById(R.id.tvGrade)
        val name:    TextView   = view.findViewById(R.id.tvName)
        val summary: TextView   = view.findViewById(R.id.tvSummary)
        val delete:  ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = items[position]
        holder.grade.text = s.grade
        holder.grade.setBackgroundColor(s.gradeColor)
        holder.name.text  = s.name
        holder.summary.text =
            "${s.emoji} ${s.remark}  •  Avg: ${"%.1f".format(s.average)}%  •  ${s.totalSubjects} subjects"
        holder.card.setOnClickListener  { onItemClick(s) }
        holder.delete.setOnClickListener { onDelete(s) }
    }
}
