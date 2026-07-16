package com.sms.model;

/**
 * User roles supported by the system.
 * ADMIN   - full access: manage students, courses, teachers, view all reports
 * TEACHER - manage grades for their own courses, view reports for their courses
 */
public enum Role {
    ADMIN,
    TEACHER
}
