package com.example.healthcareapp;

public class Doctor {
    private String doctorId;
    private String name;
    private String specialization;
    private String hospital;
    private String contactInfo;
    private String email;

    // Constructor
    public Doctor(String doctorId, String name, String specialization, String hospital,
                  String contactInfo, String email) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.hospital = hospital;
        this.contactInfo = contactInfo;
        this.email = email;
    }

    // Getters and setters for accessing and modifying the fields
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
