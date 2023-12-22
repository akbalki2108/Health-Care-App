package com.example.healthcareapp;

import java.io.Serializable;

public class Patient implements Serializable {
    private String patientId;
    private String pName;
    private int pAge;
    private String pGender;
    private String pAddress;
    private String medicalHistory;
    private String pContactInfo;

    private String email;

    // Constructor
    public Patient(String patientId, String name, int age, String gender, String address,
                   String medicalHistory, String contactInfo,String email) {
        this.patientId = patientId;
        this.pName = name;
        this.pAge = age;
        this.pGender = gender;
        this.pAddress = address;
        this.medicalHistory = medicalHistory;
        this.pContactInfo = contactInfo;
        this.email = email;
    }

    // Getters and setters for accessing and modifying the fields
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return pName;
    }

    public void setName(String name) {
        this.pName = name;
    }

    public int getAge() {
        return pAge;
    }

    public void setAge(int age) {
        this.pAge = age;
    }

    public String getGender() {
        return pGender;
    }

    public void setGender(String gender) {
        this.pGender = gender;
    }

    public String getAddress() {
        return pAddress;
    }

    public void setAddress(String address) {
        this.pAddress = address;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getContactInfo() {
        return pContactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.pContactInfo = contactInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
