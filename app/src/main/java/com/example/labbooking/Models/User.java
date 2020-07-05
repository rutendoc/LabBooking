package com.example.labbooking.Models;

public class User {
    private String name, studentNumber, degree, phoneNumber;

    public User() {
    }

    public User(String name, String studentNumber, String degree, String phoneNumber) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.degree = degree;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
