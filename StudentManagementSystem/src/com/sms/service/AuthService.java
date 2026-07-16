package com.sms.service;

import com.sms.model.Role;
import com.sms.model.User;
import com.sms.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthService {
    private static final String FILE = "users.csv";
    private List<User> users;

    public AuthService() {
        users = load();
        seedDefaultAdminIfEmpty();
    }

    private List<User> load() {
        List<User> list = new ArrayList<>();
        for (String line : FileManager.readLines(FILE)) {
            list.add(User.fromCsvLine(line));
        }
        return list;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (User u : users) lines.add(u.toCsvLine());
        FileManager.writeLines(FILE, lines);
    }

    /** Create a default admin account (admin / admin123) on very first run. */
    private void seedDefaultAdminIfEmpty() {
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123", "System Administrator", Role.ADMIN));
            save();
            System.out.println("[Setup] No users found. Created default admin account -> username: admin, password: admin123");
        }
    }

    public Optional<User> login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public User addUser(String username, String password, String fullName, Role role) {
        User u = new User(username, password, fullName, role);
        users.add(u);
        save();
        return u;
    }

    public boolean deleteUser(String username) {
        boolean removed = users.removeIf(u -> u.getUsername().equals(username));
        if (removed) save();
        return removed;
    }

    public List<User> getAllUsers() { return users; }

    public List<User> getTeachers() {
        List<User> teachers = new ArrayList<>();
        for (User u : users) if (u.getRole() == Role.TEACHER) teachers.add(u);
        return teachers;
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }
}
