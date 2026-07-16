package com.sms.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;          // e.g. STU001
    private String name;
    private String email;
    private String phone;
    private List<String> courseIds; // courses this student is enrolled in

    public Student(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.courseIds = new ArrayList<>();
    }

    public Student(String id, String name, String email, String phone, List<String> courseIds) {
        this(id, name, email, phone);
        this.courseIds = courseIds;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<String> getCourseIds() { return courseIds; }

    public void enroll(String courseId) {
        if (!courseIds.contains(courseId)) courseIds.add(courseId);
    }

    public void unenroll(String courseId) {
        courseIds.remove(courseId);
    }

    /** id,name,email,phone,course1;course2;course3 */
    public String toCsvLine() {
        String courses = String.join(";", courseIds);
        return String.join(",", id, name, email, phone, courses);
    }

    public static Student fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        Student s = new Student(p[0], p[1], p[2], p[3]);
        if (p.length > 4 && !p[4].isEmpty()) {
            for (String c : p[4].split(";")) s.enroll(c);
        }
        return s;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %-25s %-15s courses=%s",
                id, name, email, phone, courseIds);
    }
}
