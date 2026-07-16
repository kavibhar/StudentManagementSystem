package com.sms.service;

import com.sms.model.Student;
import com.sms.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private static final String FILE = "students.csv";
    private List<Student> students;
    private int nextIdCounter;

    public StudentService() {
        students = load();
        nextIdCounter = computeNextIdCounter();
    }

    private List<Student> load() {
        List<Student> list = new ArrayList<>();
        for (String line : FileManager.readLines(FILE)) {
            list.add(Student.fromCsvLine(line));
        }
        return list;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Student s : students) lines.add(s.toCsvLine());
        FileManager.writeLines(FILE, lines);
    }

    private int computeNextIdCounter() {
        int max = 0;
        for (Student s : students) {
            try {
                int n = Integer.parseInt(s.getId().replaceAll("[^0-9]", ""));
                if (n > max) max = n;
            } catch (NumberFormatException ignored) { }
        }
        return max + 1;
    }

    public String generateNextId() {
        return String.format("STU%03d", nextIdCounter++);
    }

    public Student addStudent(String name, String email, String phone) {
        Student s = new Student(generateNextId(), name, email, phone);
        students.add(s);
        save();
        return s;
    }

    public boolean updateStudent(String id, String name, String email, String phone) {
        Optional<Student> found = findById(id);
        if (found.isEmpty()) return false;
        Student s = found.get();
        if (name != null && !name.isEmpty()) s.setName(name);
        if (email != null && !email.isEmpty()) s.setEmail(email);
        if (phone != null && !phone.isEmpty()) s.setPhone(phone);
        save();
        return true;
    }

    public boolean deleteStudent(String id) {
        boolean removed = students.removeIf(s -> s.getId().equals(id));
        if (removed) save();
        return removed;
    }

    public Optional<Student> findById(String id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public List<Student> getAll() { return students; }

    public boolean enroll(String studentId, String courseId) {
        Optional<Student> s = findById(studentId);
        if (s.isEmpty()) return false;
        s.get().enroll(courseId);
        save();
        return true;
    }

    public boolean unenroll(String studentId, String courseId) {
        Optional<Student> s = findById(studentId);
        if (s.isEmpty()) return false;
        s.get().unenroll(courseId);
        save();
        return true;
    }

    public List<Student> getStudentsInCourse(String courseId) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getCourseIds().contains(courseId)) result.add(s);
        }
        return result;
    }
}
