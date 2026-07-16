package com.sms.service;

import com.sms.model.Course;
import com.sms.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseService {
    private static final String FILE = "courses.csv";
    private List<Course> courses;

    public CourseService() {
        courses = load();
    }

    private List<Course> load() {
        List<Course> list = new ArrayList<>();
        for (String line : FileManager.readLines(FILE)) {
            list.add(Course.fromCsvLine(line));
        }
        return list;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Course c : courses) lines.add(c.toCsvLine());
        FileManager.writeLines(FILE, lines);
    }

    public boolean idExists(String id) {
        return courses.stream().anyMatch(c -> c.getId().equals(id));
    }

    public Course addCourse(String id, String name, String teacherUsername) {
        Course c = new Course(id, name, teacherUsername);
        courses.add(c);
        save();
        return c;
    }

    public boolean assignTeacher(String courseId, String teacherUsername) {
        Optional<Course> c = findById(courseId);
        if (c.isEmpty()) return false;
        c.get().setTeacherUsername(teacherUsername);
        save();
        return true;
    }

    public boolean deleteCourse(String id) {
        boolean removed = courses.removeIf(c -> c.getId().equals(id));
        if (removed) save();
        return removed;
    }

    public Optional<Course> findById(String id) {
        return courses.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public List<Course> getAll() { return courses; }

    public List<Course> getCoursesByTeacher(String teacherUsername) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (c.getTeacherUsername().equals(teacherUsername)) result.add(c);
        }
        return result;
    }
}
