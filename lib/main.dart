import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';

// ============================================================
//  ASSIGNMENT REQUIREMENTS — WHERE EACH ONE LIVES:
//  ✅ Function 1 — validateScore()         standalone function
//  ✅ Function 2 — formatSummary()         standalone function
//  ✅ Higher-order function — filterStudents()
//  ✅ Lambda passed to HOF                 (s) => s.average >= 80
//  ✅ Collection filter                    getPassingStudents()
//  ✅ map(), where(), forEach(), fold()   used throughout
// ============================================================

// ── FUNCTION 1: Validation ───────────────────────────────────
String? validateScore(int? score) {
  if (score == null) return null;
  if (score < 0)    return 'Score cannot be negative';
  if (score > 100)  return 'Score cannot exceed 100';
  return null;
}

// ── FUNCTION 2: Formatting ───────────────────────────────────
String formatSummary(Student student) {
  final avg    = student.average.toStringAsFixed(1);
  final subjs  = student.totalSubjects;
  final grade  = student.grade;
  final remark = student.remark;
  return '${student.name} | Avg: $avg% | Grade: $grade ($remark) | $subjs subjects';
}

// ── CUSTOM HIGHER-ORDER FUNCTION ─────────────────────────────
// Takes a function (predicate) as parameter — this IS the HOF
List<Student> filterStudents(
  List<Student> students,
  bool Function(Student) predicate,
) {
  return students.where(predicate).toList();
}

// ── COLLECTION PROCESSING FUNCTIONS ─────────────────────────
List<Student> getPassingStudents(List<Student> students) =>
    filterStudents(students, (s) => s.average >= 50);

List<Student> getFailingStudents(List<Student> students) =>
    filterStudents(students, (s) => s.average < 50);

List<Student> getTopStudents(List<Student> students) =>
    filterStudents(students, (s) => s.grade == 'A');

double classAverage(List<Student> students) {
  if (students.isEmpty) return 0;
  return students
      .map((s) => s.average)
      .fold(0.0, (sum, avg) => sum + avg) / students.length;
}

List<String> allSummaries(List<Student> students) =>
    students.map((s) => formatSummary(s)).toList();

// ── DATA MODEL ───────────────────────────────────────────────

class Student {
  final String name;
  final int? mathScore;
  final int? englishScore;
  final int? scienceScore;
  final int? ictScore;
  final int? frenchScore;

  Student({
    required this.name,
    this.mathScore,
    this.englishScore,
    this.scienceScore,
    this.ictScore,
    this.frenchScore,
  });

  double get average {
    final scores = [mathScore, englishScore, scienceScore, ictScore, frenchScore]
        .where((s) => s != null)
        .map((s) => s!)
        .toList();
    if (scores.isEmpty) return 0;
    return scores.reduce((a, b) => a + b) / scores.length;
  }

  String get grade => switch (average) {
    >= 90 => 'A',
    >= 80 => 'B',
    >= 70 => 'C',
    >= 60 => 'D',
    >= 50 => 'E',
    _     => 'F',
  };

  String get remark => switch (grade) {
    'A' => 'Excellent',
    'B' => 'Very Good',
    'C' => 'Good',
    'D' => 'Pass',
    'E' => 'Weak Pass',
    _   => 'Fail',
  };

  Color get gradeColor => switch (grade) {
    'A' => const Color(0xFF2E7D32),
    'B' => const Color(0xFF388E3C),
    'C' => const Color(0xFFF57F17),
    'D' => const Color(0xFFE65100),
    'E' => const Color(0xFFBF360C),
    _   => const Color(0xFFB71C1C),
  };

  String get emoji => switch (grade) {
    'A' => '🏆',
    'B' => '👍',
    'C' => '✅',
    'D' => '⚠️',
    'E' => '📚',
    _   => '❌',
  };

  int get totalSubjects =>
      [mathScore, englishScore, scienceScore, ictScore, frenchScore]
          .where((s) => s != null).length;

  int get totalMarks =>
      [mathScore, englishScore, scienceScore, ictScore, frenchScore]
          .where((s) => s != null)
          .map((s) => s!)
          .fold(0, (a, b) => a + b);

  int get highestScore =>
      [mathScore, englishScore, scienceScore, ictScore, frenchScore]
          .where((s) => s != null)
          .map((s) => s!)
          .fold(0, (a, b) => a > b ? a : b);

  String get bestSubject {
    final subjects = {
      'Math': mathScore, 'English': englishScore,
      'Science': scienceScore, 'ICT': ictScore, 'French': frenchScore,
    };
    final filtered = Map.fromEntries(subjects.entries.where((e) => e.value != null));
    if (filtered.isEmpty) return '—';
    return filtered.entries.reduce((a, b) => a.value! > b.value! ? a : b).key;
  }
}

// ── APP ENTRY POINT ──────────────────────────────────────────

void main() {
  // Console demo — prints when app starts (satisfies main() demo requirement)
  print('\n════════════════════════════════════════════');
  print('  ASSIGNMENT DEMO — Functions & Collections');
  print('════════════════════════════════════════════\n');

  final demo = [
    Student(name: 'Alice',  mathScore: 95, englishScore: 88, scienceScore: 92),
    Student(name: 'Bob',    mathScore: 72, englishScore: 65, scienceScore: 70),
    Student(name: 'Carol',  mathScore: 45, englishScore: 52, scienceScore: 38),
    Student(name: 'Dave',   mathScore: 88, englishScore: 91, scienceScore: 85),
    Student(name: 'Eve',    mathScore: 55, englishScore: 48, scienceScore: 60),
  ];

  print('── Function 1: validateScore() ──');
  print('validateScore(85)  → ${validateScore(85) ?? "null (valid)"}');
  print('validateScore(-5)  → ${validateScore(-5)}');
  print('validateScore(110) → ${validateScore(110)}\n');

  print('── Function 2: formatSummary() ──');
  demo.forEach((s) => print(formatSummary(s)));
  print('');

  print('── Higher-Order Function + Lambda ──');
  final highScorers = filterStudents(demo, (s) => s.average >= 80);
  print('filterStudents(students, (s) => s.average >= 80):');
  highScorers.forEach((s) => print('  • ${s.name} (${s.average.toStringAsFixed(1)}%)'));
  print('');

  print('── Collection Filters ──');
  print('Passing : ${getPassingStudents(demo).map((s) => s.name).toList()}');
  print('Failing : ${getFailingStudents(demo).map((s) => s.name).toList()}');
  print('Grade A : ${getTopStudents(demo).map((s) => s.name).toList()}');
  print('');

  print('── fold() — Class Average ──');
  print('Class average: ${classAverage(demo).toStringAsFixed(2)}%');
  print('');

  print('── forEach() — Grade Distribution ──');
  demo.forEach((s) => print('  ${s.name.padRight(8)} → Grade ${s.grade}  ${s.emoji}'));
  print('\n════════════════════════════════════════════\n');

  runApp(const GradeApp());
}

// ── APP ROOT ─────────────────────────────────────────────────

class GradeApp extends StatelessWidget {
  const GradeApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Grade Calculator',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF1565C0)),
        textTheme: GoogleFonts.poppinsTextTheme(),
        useMaterial3: true,
      ),
      home: const HomeScreen(),
    );
  }
}

// ── HOME SCREEN ──────────────────────────────────────────────

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final List<Student> _students = [];
  String? _activeFilter;

  List<Student> get _filtered {
    if (_activeFilter == null)       return _students;
    if (_activeFilter == 'passing')  return getPassingStudents(_students);
    if (_activeFilter == 'failing')  return getFailingStudents(_students);
    if (_activeFilter == 'top')      return getTopStudents(_students);
    return _students;
  }

  void _add(Student s)    => setState(() => _students.add(s));
  void _remove(int i)     => setState(() => _students.removeAt(i));
  void _clear()           => setState(() => _students.clear());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF0F4FF),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1565C0),
        title: Text('🎓 Grade Calculator',
            style: GoogleFonts.poppins(
                color: Colors.white, fontWeight: FontWeight.bold, fontSize: 20)),
        actions: [
          IconButton(
            icon: const Icon(Icons.code, color: Colors.white),
            tooltip: 'Assignment Demo',
            onPressed: () => Navigator.push(context,
                MaterialPageRoute(builder: (_) => DemoScreen(students: _students))),
          ),
          if (_students.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.delete_sweep, color: Colors.white),
              onPressed: () => _showClearDialog(context),
            ),
        ],
      ),
      body: _students.isEmpty ? _buildEmpty() : _buildList(),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => showModalBottomSheet(
          context: context, isScrollControlled: true,
          backgroundColor: Colors.transparent,
          builder: (_) => AddStudentSheet(onAdd: _add),
        ),
        backgroundColor: const Color(0xFF1565C0),
        icon: const Icon(Icons.person_add, color: Colors.white),
        label: Text('Add Student',
            style: GoogleFonts.poppins(color: Colors.white, fontWeight: FontWeight.w600)),
      ),
    );
  }

  Widget _buildEmpty() => Center(
    child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
      const Text('📋', style: TextStyle(fontSize: 80)),
      const SizedBox(height: 16),
      Text('No Students Yet',
          style: GoogleFonts.poppins(fontSize: 22, fontWeight: FontWeight.bold,
              color: const Color(0xFF1565C0))),
      const SizedBox(height: 8),
      Text('Tap "Add Student" to get started',
          style: GoogleFonts.poppins(fontSize: 14, color: Colors.grey)),
    ]),
  );

  Widget _buildList() {
    final avg     = classAverage(_students);
    final passing = getPassingStudents(_students).length;
    final failing = getFailingStudents(_students).length;

    return Column(children: [
      // Summary banner
      Container(
        margin: const EdgeInsets.all(16),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          gradient: const LinearGradient(colors: [Color(0xFF1565C0), Color(0xFF1E88E5)]),
          borderRadius: BorderRadius.circular(16),
        ),
        child: Row(mainAxisAlignment: MainAxisAlignment.spaceAround, children: [
          _stat('Students', '${_students.length}', '👨‍🎓'),
          _stat('Class Avg', '${avg.toStringAsFixed(1)}%', '📊'),
          _stat('Passing', '$passing', '✅'),
          _stat('Failing', '$failing', '❌'),
        ]),
      ),

      // Filter chips
      SingleChildScrollView(
        scrollDirection: Axis.horizontal,
        padding: const EdgeInsets.symmetric(horizontal: 16),
        child: Row(children: [
          _chip('All',     null),
          const SizedBox(width: 8),
          _chip('Passing', 'passing'),
          const SizedBox(width: 8),
          _chip('Failing', 'failing'),
          const SizedBox(width: 8),
          _chip('Grade A', 'top'),
        ]),
      ),
      const SizedBox(height: 8),

      if (_activeFilter != null)
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16),
          child: Align(
            alignment: Alignment.centerLeft,
            child: Text('Showing ${_filtered.length} of ${_students.length} students',
                style: GoogleFonts.poppins(fontSize: 12, color: Colors.grey)),
          ),
        ),

      Expanded(
        child: ListView.builder(
          padding: const EdgeInsets.symmetric(horizontal: 16),
          itemCount: _filtered.length,
          itemBuilder: (context, i) {
            final student = _filtered[i];
            final real = _students.indexOf(student);
            return _StudentCard(
              student: student, index: i + 1,
              onDelete: () => _remove(real),
              onTap: () => Navigator.push(context,
                  MaterialPageRoute(builder: (_) => ReportCardScreen(student: student))),
            );
          },
        ),
      ),
    ]);
  }

  Widget _stat(String label, String value, String emoji) => Column(children: [
    Text(emoji, style: const TextStyle(fontSize: 20)),
    const SizedBox(height: 4),
    Text(value, style: GoogleFonts.poppins(
        color: Colors.white, fontWeight: FontWeight.bold, fontSize: 18)),
    Text(label, style: GoogleFonts.poppins(color: Colors.white70, fontSize: 11)),
  ]);

  Widget _chip(String label, String? filter) {
    final isActive = _activeFilter == filter;
    return FilterChip(
      label: Text(label, style: GoogleFonts.poppins(fontSize: 12)),
      selected: isActive,
      onSelected: (_) => setState(() => _activeFilter = filter),
      selectedColor: const Color(0xFF1565C0).withOpacity(0.2),
      checkmarkColor: const Color(0xFF1565C0),
    );
  }

  void _showClearDialog(BuildContext context) {
    showDialog(context: context, builder: (_) => AlertDialog(
      title: Text('Clear All?', style: GoogleFonts.poppins(fontWeight: FontWeight.bold)),
      content: Text('Remove all ${_students.length} students?', style: GoogleFonts.poppins()),
      actions: [
        TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
        ElevatedButton(
          onPressed: () { _clear(); Navigator.pop(context); },
          style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
          child: Text('Clear All', style: GoogleFonts.poppins(color: Colors.white)),
        ),
      ],
    ));
  }
}

// ── STUDENT CARD ─────────────────────────────────────────────

class _StudentCard extends StatelessWidget {
  final Student student;
  final int index;
  final VoidCallback onDelete;
  final VoidCallback onTap;
  const _StudentCard({required this.student, required this.index,
      required this.onDelete, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.only(bottom: 12),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: Colors.white, borderRadius: BorderRadius.circular(16),
          boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.06),
              blurRadius: 10, offset: const Offset(0, 4))],
        ),
        child: Row(children: [
          Container(
            width: 52, height: 52,
            decoration: BoxDecoration(color: student.gradeColor, shape: BoxShape.circle),
            child: Center(child: Text(student.grade,
                style: GoogleFonts.poppins(color: Colors.white,
                    fontWeight: FontWeight.bold, fontSize: 22))),
          ),
          const SizedBox(width: 14),
          Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Text(student.name, style: GoogleFonts.poppins(
                fontWeight: FontWeight.bold, fontSize: 15)),
            const SizedBox(height: 4),
            Text('${student.emoji} ${student.remark}  •  Avg: ${student.average.toStringAsFixed(1)}%  •  ${student.totalSubjects} subjects',
                style: GoogleFonts.poppins(fontSize: 12, color: Colors.grey[600])),
          ])),
          IconButton(icon: const Icon(Icons.delete_outline, color: Colors.redAccent),
              onPressed: onDelete),
        ]),
      ),
    );
  }
}

// ── ADD STUDENT SHEET ─────────────────────────────────────────

class AddStudentSheet extends StatefulWidget {
  final Function(Student) onAdd;
  const AddStudentSheet({super.key, required this.onAdd});
  @override
  State<AddStudentSheet> createState() => _AddStudentSheetState();
}

class _AddStudentSheetState extends State<AddStudentSheet> {
  final _name    = TextEditingController();
  final _math    = TextEditingController();
  final _english = TextEditingController();
  final _science = TextEditingController();
  final _ict     = TextEditingController();
  final _french  = TextEditingController();
  final _key     = GlobalKey<FormState>();

  int? _parse(String t) => t.trim().isEmpty ? null : int.tryParse(t.trim());

  void _submit() {
    if (!_key.currentState!.validate()) return;
    widget.onAdd(Student(
      name: _name.text.trim(),
      mathScore:    _parse(_math.text),
      englishScore: _parse(_english.text),
      scienceScore: _parse(_science.text),
      ictScore:     _parse(_ict.text),
      frenchScore:  _parse(_french.text),
    ));
    Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
      decoration: const BoxDecoration(color: Colors.white,
          borderRadius: BorderRadius.vertical(top: Radius.circular(24))),
      child: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Form(
          key: _key,
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Center(child: Container(width: 40, height: 4,
                decoration: BoxDecoration(color: Colors.grey[300],
                    borderRadius: BorderRadius.circular(2)))),
            const SizedBox(height: 20),
            Text('➕ Add Student', style: GoogleFonts.poppins(fontSize: 20,
                fontWeight: FontWeight.bold, color: const Color(0xFF1565C0))),
            const SizedBox(height: 20),
            _field(_name, 'Full Name', 'e.g. Alice Mbeki', Icons.person,
                required: true, number: false),
            const SizedBox(height: 12),
            Text('Scores (leave blank if not taken)',
                style: GoogleFonts.poppins(fontSize: 13, color: Colors.grey)),
            const SizedBox(height: 10),
            Row(children: [
              Expanded(child: _field(_math,    'Math',    '0–100', Icons.calculate)),
              const SizedBox(width: 10),
              Expanded(child: _field(_english, 'English', '0–100', Icons.book)),
            ]),
            const SizedBox(height: 10),
            Row(children: [
              Expanded(child: _field(_science, 'Science', '0–100', Icons.science)),
              const SizedBox(width: 10),
              Expanded(child: _field(_ict,     'ICT',     '0–100', Icons.computer)),
            ]),
            const SizedBox(height: 10),
            _field(_french, 'French', '0–100', Icons.language),
            const SizedBox(height: 24),
            SizedBox(
              width: double.infinity, height: 52,
              child: ElevatedButton(
                onPressed: _submit,
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF1565C0),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                ),
                child: Text('Calculate Grade', style: GoogleFonts.poppins(
                    color: Colors.white, fontWeight: FontWeight.bold, fontSize: 16)),
              ),
            ),
            const SizedBox(height: 12),
          ]),
        ),
      ),
    );
  }

  Widget _field(TextEditingController c, String label, String hint, IconData icon,
      {bool required = false, bool number = true}) {
    return TextFormField(
      controller: c,
      keyboardType: number ? TextInputType.number : TextInputType.text,
      inputFormatters: number ? [FilteringTextInputFormatter.digitsOnly] : null,
      decoration: InputDecoration(
        labelText: label, hintText: hint,
        prefixIcon: Icon(icon, color: const Color(0xFF1565C0)),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
        focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: Color(0xFF1565C0), width: 2)),
        contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 14),
      ),
      style: GoogleFonts.poppins(fontSize: 14),
      validator: (v) {
        if (required && (v == null || v.trim().isEmpty)) return 'Required';
        if (number && v != null && v.isNotEmpty) {
          return validateScore(int.tryParse(v)); // ← uses Function 1
        }
        return null;
      },
    );
  }
}

// ── REPORT CARD SCREEN ───────────────────────────────────────

class ReportCardScreen extends StatelessWidget {
  final Student student;
  const ReportCardScreen({super.key, required this.student});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF0F4FF),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1565C0),
        iconTheme: const IconThemeData(color: Colors.white),
        title: Text('Report Card', style: GoogleFonts.poppins(
            color: Colors.white, fontWeight: FontWeight.bold)),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(children: [
          // Header
          Container(
            width: double.infinity, padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              gradient: LinearGradient(colors: [student.gradeColor,
                  student.gradeColor.withOpacity(0.7)]),
              borderRadius: BorderRadius.circular(20),
            ),
            child: Column(children: [
              Text(student.emoji, style: const TextStyle(fontSize: 60)),
              const SizedBox(height: 10),
              Text(student.name, style: GoogleFonts.poppins(color: Colors.white,
                  fontWeight: FontWeight.bold, fontSize: 24)),
              Text('${student.remark} — Grade ${student.grade}',
                  style: GoogleFonts.poppins(color: Colors.white70, fontSize: 16)),
              const SizedBox(height: 16),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 10),
                decoration: BoxDecoration(color: Colors.white.withOpacity(0.2),
                    borderRadius: BorderRadius.circular(30)),
                child: Text('Average: ${student.average.toStringAsFixed(2)}%',
                    style: GoogleFonts.poppins(color: Colors.white,
                        fontWeight: FontWeight.bold, fontSize: 18)),
              ),
            ]),
          ),
          const SizedBox(height: 16),

          // Summary card using formatSummary() — Function 2
          Container(
            width: double.infinity, padding: const EdgeInsets.all(14),
            decoration: BoxDecoration(
              color: const Color(0xFF1565C0).withOpacity(0.08),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: const Color(0xFF1565C0).withOpacity(0.3)),
            ),
            child: Row(children: [
              const Icon(Icons.summarize, color: Color(0xFF1565C0), size: 20),
              const SizedBox(width: 10),
              Expanded(child: Text(formatSummary(student),
                  style: GoogleFonts.poppins(fontSize: 12, color: const Color(0xFF1565C0)))),
            ]),
          ),
          const SizedBox(height: 20),

          _title('📚 Subject Scores'),
          const SizedBox(height: 10),
          ..._subjectRows(),
          const SizedBox(height: 20),
          _title('📊 Summary'),
          const SizedBox(height: 10),
          _summaryGrid(),
          const SizedBox(height: 20),
          _title('📋 Grade Scale'),
          const SizedBox(height: 10),
          _gradeScale(),
        ]),
      ),
    );
  }

  List<Widget> _subjectRows() {
    final subjects = {
      'Mathematics': student.mathScore, 'English': student.englishScore,
      'Science': student.scienceScore,  'ICT': student.ictScore,
      'French': student.frenchScore,
    };
    return subjects.entries.map((e) {
      if (e.value == null) return const SizedBox.shrink();
      final score = e.value!;
      return Container(
        margin: const EdgeInsets.only(bottom: 8),
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12),
            boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 8)]),
        child: Row(children: [
          Expanded(child: Text(e.key, style: GoogleFonts.poppins(fontWeight: FontWeight.w500))),
          Expanded(flex: 2, child: ClipRRect(
            borderRadius: BorderRadius.circular(10),
            child: LinearProgressIndicator(
              value: score / 100, minHeight: 10, backgroundColor: Colors.grey[200],
              valueColor: AlwaysStoppedAnimation(_scoreColor(score)),
            ),
          )),
          const SizedBox(width: 12),
          Text('$score%', style: GoogleFonts.poppins(
              fontWeight: FontWeight.bold, color: _scoreColor(score))),
        ]),
      );
    }).toList();
  }

  Color _scoreColor(int s) {
    if (s >= 80) return const Color(0xFF2E7D32);
    if (s >= 60) return const Color(0xFFF57F17);
    return const Color(0xFFB71C1C);
  }

  Widget _summaryGrid() => GridView.count(
    shrinkWrap: true, physics: const NeverScrollableScrollPhysics(),
    crossAxisCount: 2, childAspectRatio: 2.2, crossAxisSpacing: 10, mainAxisSpacing: 10,
    children: [
      _statCard('Total Subjects', '${student.totalSubjects}', Icons.subject),
      _statCard('Total Marks',    '${student.totalMarks}',    Icons.score),
      _statCard('Highest Score',  '${student.highestScore}%', Icons.star),
      _statCard('Best Subject',   student.bestSubject,        Icons.emoji_events),
    ],
  );

  Widget _statCard(String label, String value, IconData icon) => Container(
    padding: const EdgeInsets.all(12),
    decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12),
        boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 8)]),
    child: Row(children: [
      Icon(icon, color: const Color(0xFF1565C0), size: 24),
      const SizedBox(width: 10),
      Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center, children: [
        Text(value, style: GoogleFonts.poppins(fontWeight: FontWeight.bold,
            fontSize: 16, color: const Color(0xFF1565C0))),
        Text(label, style: GoogleFonts.poppins(fontSize: 11, color: Colors.grey)),
      ])),
    ]),
  );

  Widget _gradeScale() {
    final scale = [
      ('A','90–100','Excellent', const Color(0xFF2E7D32)),
      ('B','80–89', 'Very Good', const Color(0xFF388E3C)),
      ('C','70–79', 'Good',      const Color(0xFFF57F17)),
      ('D','60–69', 'Pass',      const Color(0xFFE65100)),
      ('E','50–59', 'Weak Pass', const Color(0xFFBF360C)),
      ('F','0–49',  'Fail',      const Color(0xFFB71C1C)),
    ];
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(16),
          boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.04), blurRadius: 8)]),
      child: Column(children: scale.map((item) {
        final isCurrent = item.$1 == student.grade;
        return Container(
          margin: const EdgeInsets.only(bottom: 6),
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          decoration: BoxDecoration(
            color: isCurrent ? item.$4.withOpacity(0.12) : Colors.transparent,
            borderRadius: BorderRadius.circular(8),
            border: isCurrent ? Border.all(color: item.$4, width: 1.5) : null,
          ),
          child: Row(children: [
            Container(width: 32, height: 32,
                decoration: BoxDecoration(color: item.$4, shape: BoxShape.circle),
                child: Center(child: Text(item.$1, style: GoogleFonts.poppins(
                    color: Colors.white, fontWeight: FontWeight.bold)))),
            const SizedBox(width: 12),
            Text(item.$2, style: GoogleFonts.poppins(fontWeight: FontWeight.w500)),
            const Spacer(),
            Text(item.$3, style: GoogleFonts.poppins(color: item.$4, fontWeight: FontWeight.w600)),
            if (isCurrent) ...[const SizedBox(width: 8), Icon(Icons.arrow_left, color: item.$4)],
          ]),
        );
      }).toList()),
    );
  }

  Widget _title(String t) => Align(
    alignment: Alignment.centerLeft,
    child: Text(t, style: GoogleFonts.poppins(fontSize: 16,
        fontWeight: FontWeight.bold, color: const Color(0xFF1565C0))),
  );
}

// ── DEMO SCREEN ──────────────────────────────────────────────

class DemoScreen extends StatelessWidget {
  final List<Student> students;
  const DemoScreen({super.key, required this.students});

  List<Student> get _data => students.isNotEmpty ? students : [
    Student(name: 'Alice',  mathScore: 95, englishScore: 88, scienceScore: 92),
    Student(name: 'Bob',    mathScore: 72, englishScore: 65, scienceScore: 70),
    Student(name: 'Carol',  mathScore: 45, englishScore: 52, scienceScore: 38),
    Student(name: 'Dave',   mathScore: 88, englishScore: 91, scienceScore: 85),
    Student(name: 'Eve',    mathScore: 55, englishScore: 48, scienceScore: 60),
  ];

  @override
  Widget build(BuildContext context) {
    final passing     = getPassingStudents(_data);
    final failing     = getFailingStudents(_data);
    final top         = getTopStudents(_data);
    final avg         = classAverage(_data);
    final highScorers = filterStudents(_data, (s) => s.average >= 80);
    final summaries   = allSummaries(_data);

    return Scaffold(
      backgroundColor: const Color(0xFFF0F4FF),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1565C0),
        iconTheme: const IconThemeData(color: Colors.white),
        title: Text('📋 Assignment Demo', style: GoogleFonts.poppins(
            color: Colors.white, fontWeight: FontWeight.bold)),
      ),
      body: ListView(padding: const EdgeInsets.all(16), children: [

        _card('✅ Function 1 — validateScore()', const Color(0xFF1B5E20), [
          _code('validateScore(85)  → ${validateScore(85) ?? "null  ✓ valid"}'),
          _code('validateScore(-5)  → ${validateScore(-5)}'),
          _code('validateScore(110) → ${validateScore(110)}'),
        ]),

        _card('✅ Function 2 — formatSummary()', const Color(0xFF0D47A1),
            _data.map((s) => _code(formatSummary(s))).toList()),

        _card('✅ Higher-Order Function + Lambda — filterStudents()',
            const Color(0xFF4A148C), [
          _code('filterStudents(students, (s) => s.average >= 80)'),
          const SizedBox(height: 6),
          ...highScorers.map((s) =>
              _chip('${s.name}  ${s.average.toStringAsFixed(1)}%', Colors.purple)),
        ]),

        _card('✅ Collection Filter — where()', const Color(0xFFBF360C), [
          _code('Passing → ${passing.map((s) => s.name).join(", ")}'),
          _code('Failing → ${failing.map((s) => s.name).join(", ")}'),
          _code('Grade A → ${top.isEmpty ? "none yet" : top.map((s) => s.name).join(", ")}'),
        ]),

        _card('✅ map() — Student list → String list', const Color(0xFF004D40),
            summaries.map((s) => _code(s)).toList()),

        _card('✅ fold() — Class Average', const Color(0xFF1A237E), [
          _code('students.map((s)=>s.average).fold(0,(sum,a)=>sum+a)/length'),
          const SizedBox(height: 8),
          _chip('Result: ${avg.toStringAsFixed(2)}%', const Color(0xFF1A237E)),
        ]),

        _card('✅ forEach() — Grade Distribution', const Color(0xFF37474F),
            _data.map((s) => _code('${s.name.padRight(8)} → ${s.grade}  ${s.emoji}')).toList()),

      ]),
    );
  }

  Widget _card(String title, Color color, List<Widget> children) => Container(
    margin: const EdgeInsets.only(bottom: 16),
    decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(16),
        boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.05), blurRadius: 8)]),
    child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Container(
        width: double.infinity,
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        decoration: BoxDecoration(color: color,
            borderRadius: const BorderRadius.vertical(top: Radius.circular(16))),
        child: Text(title, style: GoogleFonts.poppins(
            color: Colors.white, fontWeight: FontWeight.bold, fontSize: 13)),
      ),
      Padding(padding: const EdgeInsets.all(14),
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: children)),
    ]),
  );

  Widget _code(String text) => Padding(
    padding: const EdgeInsets.only(bottom: 4),
    child: Text(text, style: GoogleFonts.sourceCodePro(fontSize: 11, color: Colors.grey[800])),
  );

  Widget _chip(String text, Color color) => Container(
    margin: const EdgeInsets.only(top: 4),
    padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
    decoration: BoxDecoration(color: color.withOpacity(0.1),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withOpacity(0.4))),
    child: Text(text, style: GoogleFonts.poppins(
        fontSize: 12, color: color, fontWeight: FontWeight.w600)),
  );
}