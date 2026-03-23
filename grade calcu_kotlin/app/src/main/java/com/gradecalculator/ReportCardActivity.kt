package com.gradecalculator

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gradecalculator.databinding.ActivityReportCardBinding

class ReportCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Report Card"

        @Suppress("DEPRECATION")
        val student = intent.getSerializableExtra(MainActivity.EXTRA_STUDENT) as? Student
        student?.let { bind(it) }
    }

    private fun bind(s: Student) {
        binding.tvEmoji.text   = s.emoji
        binding.tvName.text    = s.name
        binding.tvRemark.text  = "${s.remark} — Grade ${s.grade}"
        binding.tvAverage.text = "Average: ${"%.2f".format(s.average)}%"
        binding.headerCard.setCardBackgroundColor(s.gradeColor)
        binding.tvSummary.text = formatSummary(s)

        // Subject rows
        s.subjectScores.forEach { (subject, score) ->
            val row = layoutInflater.inflate(R.layout.item_subject_row, binding.subjectContainer, false)
            row.findViewById<TextView>(R.id.tvSubjectName).text  = subject
            row.findViewById<TextView>(R.id.tvSubjectScore).text = "$score%"
            val pb = row.findViewById<ProgressBar>(R.id.progressBar)
            pb.progress = score
            pb.progressTintList = android.content.res.ColorStateList.valueOf(scoreColor(score))
            binding.subjectContainer.addView(row)
        }

        binding.tvTotalSubjects.text = s.totalSubjects.toString()
        binding.tvTotalMarks.text    = s.totalMarks.toString()
        binding.tvHighest.text       = "${s.highestScore}%"
        binding.tvBestSubject.text   = s.bestSubject

        // Grade scale rows
        val scale = listOf(
            Triple(binding.rowA, "A", "90-100  Excellent"),
            Triple(binding.rowB, "B", "80-89   Very Good"),
            Triple(binding.rowC, "C", "70-79   Good"),
            Triple(binding.rowD, "D", "60-69   Pass"),
            Triple(binding.rowE, "E", "50-59   Weak Pass"),
            Triple(binding.rowF, "F", "0-49    Fail")
        )
        scale.forEach { (rowBinding, grade, label) ->
            rowBinding.tvGradeLetter.text = grade
            rowBinding.tvGradeRange.text  = label
            if (grade == s.grade) {
                rowBinding.root.setBackgroundColor(Color.parseColor("#1A1565C0"))
                rowBinding.tvMarker.visibility = View.VISIBLE
            }
        }
    }

    private fun scoreColor(score: Int) = when {
        score >= 80 -> Color.parseColor("#2E7D32")
        score >= 60 -> Color.parseColor("#F57F17")
        else        -> Color.parseColor("#B71C1C")
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
