package com.sms.model;

public class Course {
    private String id;        // e.g. CSE101
    private String name;      // e.g. "Data Structures"
    private String teacherUsername; // username of assigned teacher, or "" if unassigned

    public Course(String id, String name, String teacherUsername) {
        this.id = id;
        this.name = name;
        this.teacherUsername = teacherUsername == null ? "" : teacherUsername;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeacherUsername() { return teacherUsername; }
    public void setTeacherUsername(String teacherUsername) { this.teacherUsername = teacherUsername; }

    public String toCsvLine() {
        return String.join(",", id, name, teacherUsername);
    }

    public static Course fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        return new Course(p[0], p[1], p.length > 2 ? p[2] : "");
    }

    @Override
    public String toString() {
        String teacher = teacherUsername.isEmpty() ? "(unassigned)" : teacherUsername;
        return String.format("%-8s %-25s teacher=%s", id, name, teacher);
    }
}
