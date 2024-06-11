package model;

import java.util.HashMap;
import java.util.*;

public class Shift {

    private Map<Integer, List<Staff>> officeStaffMap = new HashMap<>();
    private Map<Integer, List<Staff>> backupStaffMap = new HashMap<>();
    private int totalCheckInStaff;
    private int totalStaff;
    private String shiftID;
    private int weekDay;

    public Shift(String shiftID, int weekDay) {
        this.totalCheckInStaff = 0;
        this.weekDay = weekDay;
        this.shiftID = shiftID;
    }

    public int getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(int totalStaff) {
        this.totalStaff = totalStaff;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public Map<Integer, List<Staff>> getOfficeStaffMap() {
        return officeStaffMap;
    }

    public void setOfficeStaffMap(Map<Integer, List<Staff>> officeStaffMap) {
        this.officeStaffMap = officeStaffMap;
    }

    public int getTotalCheckInStaff() {
        return totalCheckInStaff;
    }

    public void setTotalCheckInStaff(int totalCheckInStaff) {
        this.totalCheckInStaff = totalCheckInStaff;
    }

    public String getShiftID() {
        return shiftID;
    }

    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }

    public Map<Integer, List<Staff>> getBackupStaffMap() {
        return backupStaffMap;
    }

    public void setBackupStaffMap(Map<Integer, List<Staff>> backupStaffMap) {
        this.backupStaffMap = backupStaffMap;
    }

}
