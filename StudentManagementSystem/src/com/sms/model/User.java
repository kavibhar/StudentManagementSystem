package com.sms.model;

/**
 * Represents a login account (Admin or Teacher).
 * NOTE: Passwords are stored in plain text for simplicity — this is an
 * educational project. For production use, hash passwords (e.g. BCrypt/SHA-256+salt).
 */
public class User {
    private String username;
    private String password;
    private String fullName;
    private Role role;

    public User(String username, String password, String fullName, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }

    /** Serialize to a single CSV line: username,password,fullName,role */
    public String toCsvLine() {
        return String.join(",", username, password, fullName, role.name());
    }

    /** Parse a CSV line back into a User object. */
    public static User fromCsvLine(String line) {
        String[] p = line.split(",", -1);
        return new User(p[0], p[1], p[2], Role.valueOf(p[3]));
    }

    @Override
    public String toString() {
        return String.format("%-12s %-20s %s", username, fullName, role);
    }
}
