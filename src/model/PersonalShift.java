package model;

import java.sql.Time;

public class PersonalShift {
    private String shiftId;
    private String name;
    private String staff_id;
    private String campusId;
    private CustomDate date;
    private Time checkIn;
    private Time checkOut;
    private Time userCheckOut;
    private Time userCheckIn;
    private String job; // To distinguish Office Staff and Backup Staff

    public PersonalShift(String shiftId, String staff_id, String name, String campusId, CustomDate date, Time checkIn,
            Time checkOut, String job) {
        this.shiftId = shiftId;
        this.staff_id = staff_id;
        this.campusId = campusId;
        this.name = name;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.job = job;
    }

    public Time getUserCheckOut() {
        return userCheckOut;
    }

    public void setUserCheckOut(Time userCheckOut) {
        this.userCheckOut = userCheckOut;
    }

    public Time getUserCheckIn() {
        return userCheckIn;
    }

    public void setUserCheckIn(Time userCheckIn) {
        this.userCheckIn = userCheckIn;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCampusId() {
        return campusId;
    }

    public CustomDate getDate() {
        return date;
    }

    public void setDate(CustomDate date) {
        this.date = date;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public Time getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Time checkIn) {
        this.checkIn = checkIn;
    }

    public Time getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Time checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return name + " " + shiftId + " " + campusId + " ";
    }

}
