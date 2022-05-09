package com.example.remindme.online;

public class Reminder {
    private final int id;
    private final String medName;
    private final String medDesc;
    private final String medDate;
    private final String medTime;
    private final String isRepeating;
    private final String medUserEmail;
    private final String medHCWEmail;

    public Reminder(int id, String medName, String medDesc, String medDate, String medTime, String isRepeating, String medUserEmail, String medHCWEmail) {
        this.id = id;
        this.medName = medName;
        this.medDesc = medDesc;
        this.medDate = medDate;
        this.medTime = medTime;
        this.isRepeating = isRepeating;
        this.medUserEmail = medUserEmail;
        this.medHCWEmail = medHCWEmail;
    }

    public int getId() {
        return id;
    }

    public String getMedName() {
        return medName;
    }

    public String getMedDesc() {
        return medDesc;
    }

    public String getMedDate() {
        return medDate;
    }

    public String getMedTime() {
        return medTime;
    }

    public String getIsRepeating() {
        return isRepeating;
    }

    public String getMedUserEmail() {
        return medUserEmail;
    }

    public String getMedHCWEmail() {
        return medHCWEmail;
    }
}