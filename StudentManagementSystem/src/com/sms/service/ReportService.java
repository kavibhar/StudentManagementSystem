package com.sms.service;

import com.sms.model.Course;
import com.sms.model.Grade;
import com.sms.model.Student;

import java.util.List;
import java.util.Optional;

/** Builds human-readable report text from the other services' data. */
public class ReportService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final GradeService gradeService;

    public ReportService(StudentService studentService, CourseService courseService, GradeService gradeService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
    }

    /** Full report card for one student: every course, mark, letter grade, and overall average. */
    public String studentReportCard(String studentId) {
        Optional<Student> opt = studentService.findById(studentId);
        if (opt.isEmpty()) return "Student " + studentId + " not found.";
        Student s = opt.get();

        StringBuilder sb = new StringBuilder();
        sb.append("=== Report Card: ").append(s.getName()).append(" (").append(s.getId()).append(") ===\n");
        List<Grade> grades = gradeService.getByStudent(studentId);
        if (grades.isEmpty()) {
            sb.append("No grades recorded yet.\n");
            return sb.toString();
        }

        double total = 0;
        sb.append(String.format("%-10s %-25s %-8s %-6s%n", "Course", "Name", "Marks", "Grade"));
        for (Grade g : grades) {
            String courseName = courseService.findById(g.getCourseId()).map(Course::getName).orElse("(unknown)");
            sb.append(String.format("%-10s %-25s %-8.1f %-6s%n",
                    g.getCourseId(), courseName, g.getMarks(), g.getLetterGrade()));
            total += g.getMarks();
        }
        double avg = total / grades.size();
        sb.append(String.format("%nAverage: %.2f  Overall Grade: %s%n", avg, letterFor(avg)));
        return sb.toString();
    }

    /** Report for one course: every enrolled student's mark, class average, and top student. */
    public String courseReport(String courseId) {
        Optional<Course> opt = courseService.findById(courseId);
        if (opt.isEmpty()) return "Course " + courseId + " not found.";
        Course c = opt.get();

        StringBuilder sb = new StringBuilder();
        sb.append("=== Course Report: ").append(c.getName()).append(" (").append(c.getId()).append(") ===\n");
        List<Grade> grades = gradeService.getByCourse(courseId);
        if (grades.isEmpty()) {
            sb.append("No grades recorded yet for this course.\n");
            return sb.toString();
        }

        double total = 0;
        Grade top = grades.get(0);
        sb.append(String.format("%-10s %-25s %-8s %-6s%n", "StudentID", "Name", "Marks", "Grade"));
        for (Grade g : grades) {
            String name = studentService.findById(g.getStudentId()).map(Student::getName).orElse("(unknown)");
            sb.append(String.format("%-10s %-25s %-8.1f %-6s%n",
                    g.getStudentId(), name, g.getMarks(), g.getLetterGrade()));
            total += g.getMarks();
            if (g.getMarks() > top.getMarks()) top = g;
        }
        double avg = total / grades.size();
        String topName = studentService.findById(top.getStudentId()).map(Student::getName).orElse("(unknown)");
        sb.append(String.format("%nClass Average: %.2f%n", avg));
        sb.append(String.format("Top Student: %s (%s) with %.1f marks%n", topName, top.getStudentId(), top.getMarks()));
        return sb.toString();
    }

    /** System-wide summary: total students, courses, and overall average across all grades. */
    public String overallSummary() {
        List<Student> allStudents = studentService.getAll();
        List<Course> allCourses = courseService.getAll();
        List<Grade> allGrades = gradeService.getAll();

        StringBuilder sb = new StringBuilder();
        sb.append("=== Overall System Summary ===\n");
        sb.append("Total Students: ").append(allStudents.size()).append("\n");
        sb.append("Total Courses: ").append(allCourses.size()).append("\n");
        sb.append("Total Grade Records: ").append(allGrades.size()).append("\n");

        if (!allGrades.isEmpty()) {
            double total = 0;
            for (Grade g : allGrades) total += g.getMarks();
            double avg = total / allGrades.size();
            sb.append(String.format("Overall Average Marks: %.2f%n", avg));
        }
        return sb.toString();
    }

    private String letterFor(double marks) {
        if (marks >= 90) return "A";
        if (marks >= 80) return "B";
        if (marks >= 70) return "C";
        if (marks >= 60) return "D";
        return "F";
    }
}
