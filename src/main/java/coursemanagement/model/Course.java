package com.coursemanagement.model;

public class Course {
    private int id;
    private String title;
    private String description;
    private int teacherId;
    private String teacherName;
    
    public Course() {}
    
    public Course(int id, String title, String description, int teacherId, String teacherName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
}
