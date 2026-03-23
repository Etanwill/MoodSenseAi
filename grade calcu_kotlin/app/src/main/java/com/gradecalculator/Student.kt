package com.gradecalculator

import java.io.Serializable

// ============================================================
//  Student.kt
//  OOP: data class, encapsulation, abstraction, computed props
//  Assignment: validateScore(), formatSummary(), filterStudents()
//  Collections: map, filter, forEach, fold, filterNotNull
// ============================================================

data class Student(
    val name:         String,
    val mathScore:    Int? = null,
    val englishScore: Int? = null,
    val scienceScore: Int? = null,
    val ictScore:     Int? = null,
    val frenchScore:  Int? = null
) : Serializable {

    val average: Double get() {
        val scores = listOf(mathScore, englishScore, scienceScore, ictScore, frenchScore)
            .filterNotNull()
        return if (scores.isEmpty()) 0.0 else scores.sum().toDouble() / scores.size
    }

    val grade: String get() = when {
        average >= 90 -> "A"
        average >= 80 -> "B"
        average >= 70 -> "C"
        average >= 60 -> "D"
        average >= 50 -> "E"
        else          -> "F"
    }

    val remark: String get() = when (grade) {
        "A"  -> "Excellent"
        "B"  -> "Very Good"
        "C"  -> "Good"
        "D"  -> "Pass"
        "E"  -> "Weak Pass"
        else -> "Fail"
    }

    val emoji: String get() = when (grade) {
        "A"  -> "🏆"
        "B"  -> "👍"
        "C"  -> "✅"
        "D"  -> "⚠️"
        "E"  -> "📚"
        else -> "❌"
    }

    val gradeColor: Int get() = when (grade) {
        "A"  -> 0xFF2E7D32.toInt()
        "B"  -> 0xFF388E3C.toInt()
        "C"  -> 0xFFF57F17.toInt()
        "D"  -> 0xFFE65100.toInt()
        "E"  -> 0xFFBF360C.toInt()
        else -> 0xFFB71C1C.toInt()
    }

    val totalSubjects: Int get() =
        listOf(mathScore, englishScore, scienceScore, ictScore, frenchScore)
            .filterNotNull().size

    val totalMarks: Int get() =
        listOf(mathScore, englishScore, scienceScore, ictScore, frenchScore)
            .filterNotNull().sum()

    val highestScore: Int get() =
        listOf(mathScore, englishScore, scienceScore, ictScore, frenchScore)
            .filterNotNull().maxOrNull() ?: 0

    val bestSubject: String get() {
        val map = mapOf(
            "Math" to mathScore, "English" to englishScore,
            "Science" to scienceScore, "ICT" to ictScore, "French" to frenchScore
        )
        return map.filterValues { it != null }.maxByOrNull { it.value!! }?.key ?: "—"
    }

    val subjectScores: Map<String, Int> get() =
        mapOf(
            "Mathematics" to mathScore, "English" to englishScore,
            "Science" to scienceScore, "ICT" to ictScore, "French" to frenchScore
        ).filterValues { it != null }.mapValues { it.value!! }
}

// ── FUNCTION 1: Validation ───────────────────────────────────
fun validateScore(score: Int?): String? {
    if (score == null) return null
    if (score < 0)    return "Score cannot be negative"
    if (score > 100)  return "Score cannot exceed 100"
    return null
}

// ── FUNCTION 2: Formatting ───────────────────────────────────
fun formatSummary(student: Student): String {
    return "${student.name} | Avg: ${"%.1f".format(student.average)}% " +
           "| Grade: ${student.grade} (${student.remark}) " +
           "| ${student.totalSubjects} subjects"
}

// ── HIGHER-ORDER FUNCTION ─────────────────────────────────────
fun filterStudents(students: List<Student>, predicate: (Student) -> Boolean): List<Student> =
    students.filter(predicate)

// ── COLLECTION FUNCTIONS ──────────────────────────────────────
fun getPassingStudents(students: List<Student>) =
    filterStudents(students) { it.average >= 50 }

fun getFailingStudents(students: List<Student>) =
    filterStudents(students) { it.average < 50 }

fun getTopStudents(students: List<Student>) =
    filterStudents(students) { it.grade == "A" }

fun classAverage(students: List<Student>): Double {
    if (students.isEmpty()) return 0.0
    return students.map { it.average }.fold(0.0) { sum, a -> sum + a } / students.size
}

fun allSummaries(students: List<Student>): List<String> =
    students.map { formatSummary(it) }
