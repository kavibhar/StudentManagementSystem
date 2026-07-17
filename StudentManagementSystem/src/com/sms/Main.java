
    private static final AuthService authService = new AuthService();
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final GradeService gradeService = new GradeService();
    private static final ReportService reportService =
            new ReportService(studentService, courseService, gradeService);

    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM (Java)");
        System.out.println("=======================================");

        while (true) {
            User user = loginPrompt();
            if (user == null) {
                System.out.println("Goodbye!");
                break;
            }
            if (user.getRole() == Role.ADMIN) {
                adminMenu(user);
            } else {
                teacherMenu(user);
            }
        }
        sc.close();
    }

    // ---------------------------------------------------------- LOGIN -----

    private static User loginPrompt() {
        System.out.println("\n--- LOGIN ---  (type 'exit' as username to quit)");
        String username = ConsoleUtil.readLine(sc, "Username: ");
        if (username.equalsIgnoreCase("exit")) return null;
        String password = ConsoleUtil.readLine(sc, "Password: ");

        Optional<User> user = authService.login(username, password);
        if (user.isEmpty()) {
            System.out.println("Invalid username or password.");
            return loginPrompt();
        }
        System.out.println("Welcome, " + user.get().getFullName() + " (" + user.get().getRole() + ")");
        return user.get();
    }

    // ---------------------------------------------------------- ADMIN -----

    private static void adminMenu(User admin) {
        while (true) {
            System.out.println("\n=== ADMIN MENU (" + admin.getFullName() + ") ===");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Courses");
            System.out.println("3. Manage Teacher Accounts");
            System.out.println("4. Reports");
            System.out.println("5. Logout");
            int choice = ConsoleUtil.readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1 -> manageStudents();
                case 2 -> manageCourses();
                case 3 -> manageTeachers();
                case 4 -> reportsMenu(admin);
                case 5 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void manageStudents() {
        while (true) {
            System.out.println("\n-- Manage Students --");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Enroll Student in Course");
            System.out.println("6. Unenroll Student from Course");
            System.out.println("7. Back");
            int choice = ConsoleUtil.readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1 -> {
                    String name = ConsoleUtil.readNonEmpty(sc, "Name: ");
                    String email = ConsoleUtil.readNonEmpty(sc, "Email: ");
                    String phone = ConsoleUtil.readNonEmpty(sc, "Phone: ");
                    Student s = studentService.addStudent(name, email, phone);
                    System.out.println("Added student with ID: " + s.getId());
                }
                case 2 -> {
                    List<Student> all = studentService.getAll();
                    if (all.isEmpty()) System.out.println("No students yet.");
                    else all.forEach(System.out::println);
                }
                case 3 -> {
                    String id = ConsoleUtil.readNonEmpty(sc, "Student ID to update: ");
                    if (studentService.findById(id).isEmpty()) {
                        System.out.println("Student not found.");
                        break;
                    }
                    System.out.println("(Leave blank to keep current value)");
                    String name = ConsoleUtil.readLine(sc, "New name: ");
                    String email = ConsoleUtil.readLine(sc, "New email: ");
                    String phone = ConsoleUtil.readLine(sc, "New phone: ");
                    studentService.updateStudent(id, name, email, phone);
                    System.out.println("Updated.");
                }
                case 4 -> {
                    String id = ConsoleUtil.readNonEmpty(sc, "Student ID to delete: ");
                    System.out.println(studentService.deleteStudent(id) ? "Deleted." : "Student not found.");
                }
                case 5 -> {
                    String sid = ConsoleUtil.readNonEmpty(sc, "Student ID: ");
                    String cid = ConsoleUtil.readNonEmpty(sc, "Course ID: ");
                    if (courseService.findById(cid).isEmpty()) {
                        System.out.println("Course not found.");
                        break;
                    }
                    System.out.println(studentService.enroll(sid, cid) ? "Enrolled." : "Student not found.");
                }
                case 6 -> {
                    String sid = ConsoleUtil.readNonEmpty(sc, "Student ID: ");
                    String cid = ConsoleUtil.readNonEmpty(sc, "Course ID: ");
                    System.out.println(studentService.unenroll(sid, cid) ? "Unenrolled." : "Student not found.");
                }
                case 7 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void manageCourses() {
        while (true) {
            System.out.println("\n-- Manage Courses --");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Assign Teacher to Course");
            System.out.println("4. Delete Course");
            System.out.println("5. Back");
            int choice = ConsoleUtil.readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1 -> {
                    String id = ConsoleUtil.readNonEmpty(sc, "Course ID (e.g. CSE101): ");
                    if (courseService.idExists(id)) {
                        System.out.println("A course with that ID already exists.");
                        break;
                    }
                    String name = ConsoleUtil.readNonEmpty(sc, "Course name: ");
                    courseService.addCourse(id, name, "");
                    System.out.println("Course added.");
                }
                case 2 -> {
                    List<Course> all = courseService.getAll();
                    if (all.isEmpty()) System.out.println("No courses yet.");
                    else all.forEach(System.out::println);
                }
                case 3 -> {
                    List<User> teachers = authService.getTeachers();
                    if (teachers.isEmpty()) {
                        System.out.println("No teacher accounts exist yet. Create one first.");
                        break;
                    }
                    String cid = ConsoleUtil.readNonEmpty(sc, "Course ID: ");
                    if (courseService.findById(cid).isEmpty()) {
                        System.out.println("Course not found.");
                        break;
                    }
                    System.out.println("Teachers:");
                    teachers.forEach(System.out::println);
                    String tUsername = ConsoleUtil.readNonEmpty(sc, "Teacher username to assign: ");
                    if (authService.findByUsername(tUsername).isEmpty()) {
                        System.out.println("Teacher not found.");
                        break;
                    }
                    courseService.assignTeacher(cid, tUsername);
                    System.out.println("Teacher assigned.");
                }
                case 4 -> {
                    String id = ConsoleUtil.readNonEmpty(sc, "Course ID to delete: ");
                    System.out.println(courseService.deleteCourse(id) ? "Deleted." : "Course not found.");
                }
                case 5 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void manageTeachers() {
        while (true) {
            System.out.println("\n-- Manage Teacher Accounts --");
            System.out.println("1. Add Teacher Account");
            System.out.println("2. View All Teachers");
            System.out.println("3. Delete Teacher Account");
            System.out.println("4. Back");
            int choice = ConsoleUtil.readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1 -> {
                    String username = ConsoleUtil.readNonEmpty(sc, "New teacher username: ");
                    if (authService.usernameExists(username)) {
                        System.out.println("Username already taken.");
                        break;
                    }
                    String password = ConsoleUtil.readNonEmpty(sc, "Password: ");
                    String fullName = ConsoleUtil.readNonEmpty(sc, "Full name: ");
                    authService.addUser(username, password, fullName, Role.TEACHER);
                    System.out.println("Teacher account created.");
                }
                case 2 -> {
                    List<User> teachers = authService.getTeachers();
                    if (teachers.isEmpty()) System.out.println("No teachers yet.");
                    else teachers.forEach(System.out::println);
                }
                case 3 -> {
                    String username = ConsoleUtil.readNonEmpty(sc, "Teacher username to delete: ");
                    System.out.println(authService.deleteUser(username) ? "Deleted." : "Not found.");
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // -------------------------------------------------------- TEACHER -----

    private static void teacherMenu(User teacher) {
        while (true) {
            System.out.println("\n=== TEACHER MENU (" + teacher.getFullName() + ") ===");
            System.out.println("1. View My Courses");
            System.out.println("2. Enter / Update Grade");
            System.out.println("3. View Students in My Course");
            System.out.println("4. Reports");
            System.out.println("5. Logout");
            int choice = ConsoleUtil.readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1 -> {
                    List<Course> courses = courseService.getCoursesByTeacher(teacher.getUsername());
                    if (courses.isEmpty()) System.out.println("You have no assigned courses.");
                    else courses.forEach(System.out::println);
                }
                case 2 -> enterGrade(teacher);
                case 3 -> viewStudentsInMyCourse(teacher);
                case 4 -> reportsMenu(teacher);
                case 5 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void enterGrade(User teacher) {
        List<Course> myCourses = courseService.getCoursesByTeacher(teacher.getUsername());
        if (myCourses.isEmpty()) {
            System.out.println("You have no assigned courses.");
            return;
        }
        String cid = ConsoleUtil.readNonEmpty(sc, "Course ID: ");
        boolean owns = myCourses.stream().anyMatch(c -> c.getId().equals(cid));
        if (!owns) {
            System.out.println("You are not assigned to that course.");
            return;
        }
        String sid = ConsoleUtil.readNonEmpty(sc, "Student ID: ");
        Optional<Student> student = studentService.findById(sid);
        if (student.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }
        if (!student.get().getCourseIds().contains(cid)) {
            System.out.println("That student is not enrolled in this course.");
            return;
        }
        double marks = ConsoleUtil.readDouble(sc, "Marks (0-100): ");
        gradeService.recordGrade(sid, cid, marks);
        System.out.println("Grade recorded.");
    }

    private static void viewStudentsInMyCourse(User teacher) {
        List<Course> myCourses = courseService.getCoursesByTeacher(teacher.getUsername());
        if (myCourses.isEmpty()) {
            System.out.println("You have no assigned courses.");
            return;
        }
        String cid = ConsoleUtil.readNonEmpty(sc, "Course ID: ");
        boolean owns = myCourses.stream().anyMatch(c -> c.getId().equals(cid));
        if (!owns) {
            System.out.println("You are not assigned to that course.");
            return;
        }
        List<Student> students = studentService.getStudentsInCourse(cid);
        if (students.isEmpty()) System.out.println("No students enrolled in this course.");
        else students.forEach(System.out::println);
    }

    // --------------------------------------------------------- REPORTS ----

    private static void reportsMenu(User user) {
        while (true) {
            System.out.println("\n-- Reports --");
            System.out.println("1. Student Report Card");
            System.out.println("2. Course Report");
            if (user.getRole() == Role.ADMIN) System.out.println("3. Overall System Summary");
            System.out.println("0. Back");
            int choice = ConsoleUtil.readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1 -> {
                    String sid = ConsoleUtil.readNonEmpty(sc, "Student ID: ");
                    System.out.println(reportService.studentReportCard(sid));
                }
                case 2 -> {
                    String cid = ConsoleUtil.readNonEmpty(sc, "Course ID: ");
                    if (user.getRole() == Role.TEACHER) {
                        boolean owns = courseService.getCoursesByTeacher(user.getUsername())
                                .stream().anyMatch(c -> c.getId().equals(cid));
                        if (!owns) {
                            System.out.println("You are not assigned to that course.");
                            break;
                        }
                    }
                    System.out.println(reportService.courseReport(cid));
                }
                case 3 -> {
                    if (user.getRole() == Role.ADMIN) System.out.println(reportService.overallSummary());
                    else System.out.println("Invalid option.");
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
