package com.example.remindme.offline;

public class Reminders {
    private int id;
    private String medicationName;
    private String medDesc;
    private String medDate;
    private String medTime;
    private String isRepeating;

    Reminders(String medName, String medDesc, String medDate, String medTime, String isRepeating) {
        this.medicationName = medName;
        this.medDesc = medDesc;
        this.medDate = medDate;
        this.medTime = medTime;
        this.isRepeating = isRepeating;
    }

    Reminders(int id, String medName, String medDesc, String medDate, String medTime, String isRepeating) {
        this.id = id;
        this.medicationName = medName;
        this.medDesc = medDesc;
        this.medDate = medDate;
        this.medTime = medTime;
        this.isRepeating = isRepeating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return medicationName;
    }

    public void setName(String medName) {
        this.medicationName = medName;
    }

    public String getDate() {
        return medDate;
    }

    public void setDate(String sDate) {
        this.medDate = sDate;
    }

    public String getTime() {
        return medTime;
    }

    public void setTime(String mTime) {
        this.medTime = mTime;
    }

    public String getIsRepeating() {
        return isRepeating;
    }

    public void setIsRepeating(String isRepeating) {
        this.isRepeating = isRepeating;
    }

    public String getMedDesc() {
        return medDesc;
    }

    public void setMedDesc(String medDesc) {
        this.medDesc = medDesc;
    }


}