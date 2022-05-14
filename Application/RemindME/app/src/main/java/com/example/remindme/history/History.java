package com.example.remindme.history;

public class History {
    private String medicationName;
    private String medDate;
    private String medTime;
    private String status;

    public History(String medicationName, String medDate, String medTime, String status) {
        this.medicationName = medicationName;
        this.medDate = medDate;
        this.medTime = medTime;
        this.status = status;
    }

    String getMedName() {
        return medicationName;
    }

    public void setMedName(String medicationName) {
        this.medicationName = medicationName;
    }

    String getMedDate() {
        return medDate;
    }

    public void setMedDate(String medDate) {
        this.medDate = medDate;
    }

    String getMedTime() {
        return medTime;
    }

    public void setMedTime(String medTime) {
        this.medTime = medTime;
    }

    String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
