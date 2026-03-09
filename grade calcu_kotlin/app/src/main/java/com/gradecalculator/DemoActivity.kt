package com.gradecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gradecalculator.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding

    private val demo = listOf(
        Student("Alice",  mathScore = 95, englishScore = 88, scienceScore = 92),
        Student("Bob",    mathScore = 72, englishScore = 65, scienceScore = 70),
        Student("Carol",  mathScore = 45, englishScore = 52, scienceScore = 38),
        Student("Dave",   mathScore = 88, englishScore = 91, scienceScore = 85),
        Student("Eve",    mathScore = 55, englishScore = 48, scienceScore = 60)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Assignment Demo"
        populate()
    }

    private fun populate() {
        // Function 1
        binding.tvValidate.text = buildString {
            appendLine("validateScore(85)  → ${validateScore(85) ?: "null  ✓ valid"}")
            appendLine("validateScore(-5)  → ${validateScore(-5)}")
            append(    "validateScore(110) → ${validateScore(110)}")
        }

        // Function 2
        binding.tvFormat.text = demo.joinToString("\n") { formatSummary(it) }

        // HOF + lambda
        val high = filterStudents(demo) { it.average >= 80 }
        binding.tvHof.text = buildString {
            appendLine("filterStudents(list) { it.average >= 80 }")
            appendLine("Result:")
            high.forEach { appendLine("  • ${it.name}  (${"%.1f".format(it.average)}%)") }
        }

        // filter
        binding.tvFilter.text = buildString {
            appendLine("Passing → ${getPassingStudents(demo).map { it.name }}")
            appendLine("Failing → ${getFailingStudents(demo).map { it.name }}")
            append(    "Grade A → ${getTopStudents(demo).map { it.name }.ifEmpty { listOf("none") }}")
        }

        // map
        binding.tvMap.text = allSummaries(demo).joinToString("\n")

        // fold
        binding.tvFold.text = buildString {
            appendLine("students.map { it.average }")
            appendLine("        .fold(0.0) { sum, a -> sum + a }")
            appendLine("        .div(students.size)")
            append(    "= ${"%.2f".format(classAverage(demo))}%")
        }

        // forEach
        binding.tvForEach.text = buildString {
            demo.forEach { s -> appendLine("${s.name.padEnd(10)} → ${s.grade}  ${s.emoji}") }
        }

        // OOP checklist
        binding.tvOop.text = buildString {
            appendLine("✅ data class        — Student")
            appendLine("✅ val / var         — immutable vs mutable")
            appendLine("✅ Int? nullable      — null safety")
            appendLine("✅ when expression   — grade/remark/emoji")
            appendLine("✅ Elvis ?:          — null default")
            appendLine("✅ Higher-order fn   — filterStudents()")
            appendLine("✅ Lambda            — { it.average >= 80 }")
            appendLine("✅ Encapsulation     — private logic in class")
            appendLine("✅ Inheritance       — extends AppCompatActivity")
            appendLine("✅ Polymorphism      — override onCreate()")
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
