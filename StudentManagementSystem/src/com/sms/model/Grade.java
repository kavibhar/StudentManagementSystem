package com.sms.model;

public class Grade {
    private String studentId;
    private String courseId;
    private double marks; // out of 100

    public Grade(String studentId, String courseId, double marks) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
    }

    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    public String getLetterGrade() {
        if (marks >= 90) return "A";
        if (marks >= 80) return "B";
        if (marks >= 70) return "C";
        if (marks >= 60) return "D";
        return "F";
    }

    public String toCsvLine() {
        return String.join(",", studentId, courseId, String.valueOf(marks));
    }

    public static Grade fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        return new Grade(p[0], p[1], Double.parseDouble(p[2]));
    }

    @Override
    public String toString() {
        return String.format("%-8s %-8s marks=%-6.1f grade=%s", studentId, courseId, marks, getLetterGrade());
    }
}
