package com.nsa.comuty.onboarding.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String name;
    private String dob;
    private String email;
    private String uid;
    private String bio;
    private String profileUrl;
    private String college;
    private String branch;
    private String year;
    private String section;
    private String enrollment;
    private String graduationYear;

    public UserModel() {
    }

    public UserModel(String name, String dob, String email, String uid, String bio, String profileUrl, String college, String branch, String year, String section, String enrollment, String graduationYear) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.uid = uid;
        this.bio = bio;
        this.profileUrl = profileUrl;
        this.college = college;
        this.branch = branch;
        this.year = year;
        this.section = section;
        this.enrollment = enrollment;
        this.graduationYear = graduationYear;
    }

    protected UserModel(Parcel in) {
        name = in.readString();
        dob = in.readString();
        email = in.readString();
        uid=in.readString();
        bio = in.readString();
        profileUrl = in.readString();
        college = in.readString();
        branch = in.readString();
        year = in.readString();
        section = in.readString();
        enrollment = in.readString();
        graduationYear = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(dob);
        parcel.writeString(email);
        parcel.writeString(uid);
        parcel.writeString(bio);
        parcel.writeString(profileUrl);
        parcel.writeString(college);
        parcel.writeString(branch);
        parcel.writeString(year);
        parcel.writeString(section);
        parcel.writeString(enrollment);
        parcel.writeString(graduationYear);
    }
}
