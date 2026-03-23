package com.gradecalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gradecalculator.databinding.ActivityAddStudentBinding

class AddStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Student"
        binding.btnCalculate.setOnClickListener { submit() }
    }

    private fun submit() {
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) { binding.tilName.error = "Name is required"; return }
        binding.tilName.error = null

        val math    = parse(binding.etMath.text.toString(),    "Math")
        val english = parse(binding.etEnglish.text.toString(), "English")
        val science = parse(binding.etScience.text.toString(), "Science")
        val ict     = parse(binding.etIct.text.toString(),     "ICT")
        val french  = parse(binding.etFrench.text.toString(),  "French")

        // -999 means invalid input was found — stop here
        if (listOf(math, english, science, ict, french).any { it == -999 }) return

        val student = Student(
            name         = name,
            mathScore    = math.takeIf { it != null && it >= 0 },
            englishScore = english.takeIf { it != null && it >= 0 },
            scienceScore = science.takeIf { it != null && it >= 0 },
            ictScore     = ict.takeIf { it != null && it >= 0 },
            frenchScore  = french.takeIf { it != null && it >= 0 }
        )

        if (student.totalSubjects == 0) {
            Toast.makeText(this, "Enter at least one subject score", Toast.LENGTH_SHORT).show()
            return
        }

        setResult(RESULT_OK, Intent().putExtra(MainActivity.EXTRA_STUDENT, student as java.io.Serializable))
        finish()
    }

    // Returns: null = blank (not taken), -999 = invalid, actual value = valid
    private fun parse(text: String, label: String): Int? {
        val t = text.trim()
        if (t.isEmpty()) return null
        val n = t.toIntOrNull()
        val err = validateScore(n ?: -1)
        return if (n == null || err != null) {
            Toast.makeText(this, "$label: ${err ?: "Invalid number"}", Toast.LENGTH_SHORT).show()
            -999
        } else n
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
