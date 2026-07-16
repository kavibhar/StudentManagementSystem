package com.sms.service;

import com.sms.model.Grade;
import com.sms.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeService {
    private static final String FILE = "grades.csv";
    private List<Grade> grades;

    public GradeService() {
        grades = load();
    }

    private List<Grade> load() {
        List<Grade> list = new ArrayList<>();
        for (String line : FileManager.readLines(FILE)) {
            list.add(Grade.fromCsvLine(line));
        }
        return list;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Grade g : grades) lines.add(g.toCsvLine());
        FileManager.writeLines(FILE, lines);
    }

    /** Adds a new grade, or updates the mark if one already exists for this student+course. */
    public void recordGrade(String studentId, String courseId, double marks) {
        Optional<Grade> existing = grades.stream()
                .filter(g -> g.getStudentId().equals(studentId) && g.getCourseId().equals(courseId))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setMarks(marks);
        } else {
            grades.add(new Grade(studentId, courseId, marks));
        }
        save();
    }

    public boolean deleteGrade(String studentId, String courseId) {
        boolean removed = grades.removeIf(g -> g.getStudentId().equals(studentId) && g.getCourseId().equals(courseId));
        if (removed) save();
        return removed;
    }

    public List<Grade> getByStudent(String studentId) {
        List<Grade> result = new ArrayList<>();
        for (Grade g : grades) if (g.getStudentId().equals(studentId)) result.add(g);
        return result;
    }

    public List<Grade> getByCourse(String courseId) {
        List<Grade> result = new ArrayList<>();
        for (Grade g : grades) if (g.getCourseId().equals(courseId)) result.add(g);
        return result;
    }

    public List<Grade> getAll() { return grades; }
}
