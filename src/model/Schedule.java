package model;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

    private Map<String, Shift> shiftMap;
    private String scheduleID;

    public Schedule(String scheduleID) {
        shiftMap = new HashMap<>();
        this.scheduleID = scheduleID;
    }

    public Map<String, Shift> getShiftMap() {
        return shiftMap;
    }

    public void setShiftList(Map<String, Shift> shiftMap) {
        this.shiftMap = shiftMap;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

}
