package com.gradecalculator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gradecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val students = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter
    private var activeFilter: String? = null

    companion object {
        const val REQUEST_ADD    = 1001
        const val EXTRA_STUDENT  = "extra_student"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        adapter = StudentAdapter(
            onDelete    = { removeStudent(it) },
            onItemClick = { openReportCard(it) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivityForResult(Intent(this, AddStudentActivity::class.java), REQUEST_ADD)
        }

        binding.chipAll.setOnClickListener     { setFilter(null) }
        binding.chipPassing.setOnClickListener { setFilter("passing") }
        binding.chipFailing.setOnClickListener { setFilter("failing") }
        binding.chipGradeA.setOnClickListener  { setFilter("top") }
        binding.chipAll.isChecked = true

        updateUI()
    }

    private fun setFilter(filter: String?) {
        activeFilter = filter
        binding.chipAll.isChecked     = filter == null
        binding.chipPassing.isChecked = filter == "passing"
        binding.chipFailing.isChecked = filter == "failing"
        binding.chipGradeA.isChecked  = filter == "top"
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_demo  -> { startActivity(Intent(this, DemoActivity::class.java)); true }
        R.id.action_clear -> { showClearDialog(); true }
        else -> super.onOptionsItemSelected(item)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            val student = data?.getSerializableExtra(EXTRA_STUDENT) as? Student
            student?.let { students.add(it); updateUI() }
        }
    }

    private fun getFiltered(): List<Student> = when (activeFilter) {
        "passing" -> getPassingStudents(students)
        "failing" -> getFailingStudents(students)
        "top"     -> getTopStudents(students)
        else      -> students.toList()
    }

    private fun removeStudent(student: Student) {
        students.remove(student)
        updateUI()
    }

    private fun openReportCard(student: Student) {
        startActivity(Intent(this, ReportCardActivity::class.java)
            .putExtra(EXTRA_STUDENT, student as java.io.Serializable))
    }

    private fun updateUI() {
        val filtered = getFiltered()
        if (students.isEmpty()) {
            binding.emptyState.visibility    = View.VISIBLE
            binding.contentLayout.visibility = View.GONE
        } else {
            binding.emptyState.visibility    = View.GONE
            binding.contentLayout.visibility = View.VISIBLE
            binding.tvStudentCount.text = students.size.toString()
            binding.tvClassAvg.text     = "${"%.1f".format(classAverage(students))}%"
            binding.tvPassing.text      = getPassingStudents(students).size.toString()
            binding.tvFailing.text      = getFailingStudents(students).size.toString()
            if (activeFilter != null) {
                binding.tvFilterLabel.visibility = View.VISIBLE
                binding.tvFilterLabel.text = "Showing ${filtered.size} of ${students.size} students"
            } else {
                binding.tvFilterLabel.visibility = View.GONE
            }
        }
        adapter.submitList(filtered)
    }

    private fun showClearDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear All?")
            .setMessage("Remove all ${students.size} students?")
            .setPositiveButton("Clear") { _, _ -> students.clear(); updateUI() }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
