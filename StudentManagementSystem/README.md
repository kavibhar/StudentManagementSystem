# Student Management System (Pure Java)

A console-based Student Management System written entirely in core Java —
no frameworks, no external libraries, no database drivers. Data is stored
in plain CSV files under `data/`, created automatically on first run.

## Features

- **Login & Roles** — Admin and Teacher accounts (`users.csv`)
- **Admin**
  - Full CRUD on students (add / view / update / delete)
  - Full CRUD on courses (add / view / assign teacher / delete)
  - Create / view / delete teacher accounts
  - Enroll / unenroll students in courses
  - All reports (student, course, and overall system summary)
- **Teacher**
  - View their assigned courses
  - Enter / update grades for students in their own courses
  - View students enrolled in their course
  - Student report cards & course reports (scoped to their own courses)
- **Grades & Reports**
  - Marks (0–100) auto-converted to letter grades (A/B/C/D/F)
  - Student report card: all courses, marks, grade, and average
  - Course report: every enrolled student's mark, class average, top student
  - Overall summary: total students/courses/grades, system-wide average

## Project Structure

```
StudentManagementSystem/
├── src/com/sms/
│   ├── Main.java              # entry point, console menus
│   ├── model/                 # Student, Course, Grade, User, Role
│   ├── service/                # AuthService, StudentService, CourseService,
│   │                            GradeService, ReportService
│   └── util/                  # FileManager (CSV I/O), ConsoleUtil (input)
└── data/                      # auto-created CSV "database" (students.csv,
                                  courses.csv, grades.csv, users.csv)
```

## How to Run

Requires only a JDK (Java 17+ recommended). No Maven/Gradle needed.

```bash
# From the StudentManagementSystem/ directory:

# 1. Compile
javac -d bin $(find src -name "*.java")

# 2. Run (run from this directory so it can find/create the data/ folder)
java -cp bin com.sms.Main
```

On Windows (PowerShell), replace step 1 with:
```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | % { $_.FullName })
```

## First Run

No accounts exist yet, so the app auto-creates a default admin:

```
username: admin
password: admin123
```

Log in as admin, then:
1. Go to **Manage Teacher Accounts** to create teacher logins.
2. Go to **Manage Students** to add students.
3. Go to **Manage Courses** to add courses and assign a teacher.
4. Go back to **Manage Students** → **Enroll Student in Course**.
5. Log out and log back in as the teacher to enter grades.
6. Use **Reports** (either role) to generate report cards / summaries.

## Notes & Possible Extensions

- Passwords are stored in plain text in `users.csv` for simplicity — this
  is fine for a learning project but should be hashed in a real system.
- CSV fields strip commas from user input to keep the format simple (no
  quoted-field escaping).
- Easy extensions: attendance tracking, GPA/credit-weighted averages,
  exporting reports to a `.txt`/`.csv` file, or swapping `FileManager`
  for a JDBC-backed implementation without touching the service layer.
