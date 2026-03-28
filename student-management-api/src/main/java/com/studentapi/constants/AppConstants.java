package com.studentapi.constants;

public class AppConstants {

    // Pagination defaults
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    // Messages
    public static final String STUDENT_CREATED = "Student created successfully";
    public static final String STUDENT_UPDATED = "Student updated successfully";
    public static final String STUDENT_DELETED = "Student deleted successfully";
    public static final String STUDENT_FOUND = "Student retrieved successfully";
    public static final String STUDENTS_FOUND = "Students retrieved successfully";
    public static final String STUDENT_NOT_FOUND = "Student not found with ID: ";
    public static final String EMAIL_EXISTS = "Email already exists: ";

    // Private constructor to prevent instantiation
    private AppConstants() {
        throw new IllegalStateException("Constants class");
    }
}