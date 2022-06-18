package com.nsa.comuty.onboarding.models;

public class UserCollegeModel {
    private String email;
    private String college;

    public UserCollegeModel() {
    }

    public UserCollegeModel(String email, String college) {
        this.email = email;
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}
