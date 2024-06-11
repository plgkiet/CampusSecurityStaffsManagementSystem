/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.Calendar;
import java.util.Map;
import model.Manager;
import model.PersonalShift;
import model.Schedule;
import model.Staff;

/**
 *
 * @author phamlegiakiet
 */
public class StaffTableModelController {

    private Map<String, Schedule> scheduleMap;
    private PersonalShift[][] personalShiftArray;
    private Map<String, PersonalShift> personalShiftMap;
    private int startDay;
    private int endDay;
    private int currentMonth;
    private int currentYear;
    private Schedule currentSchedule;
    private boolean nextWeek;
    private boolean previousWeek;
    
    private String dayRange;

    public StaffTableModelController(Manager manager, Staff staff) {
        dayRange = "";
        personalShiftMap = staff.getPersonalShifts();
        nextWeek = true;
        previousWeek = false;
        scheduleMap = manager.getScheduleMap();
        Calendar c = Calendar.getInstance();
        currentMonth = c.get(Calendar.MONTH) + 1;
        currentYear = c.get(Calendar.YEAR);
        currentSchedule = scheduleMap.get(currentMonth + "-" + currentYear);
        startDay = currentSchedule.getShiftMap().get("1-" + currentMonth + "-" + currentYear + "-1").getWeekDay() * -1 + 2;
    }

    public String getDayRange() {
        return dayRange;
    }

    public void setDayRange(String dayRange) {
        this.dayRange = dayRange;
    }

    public PersonalShift[][] getPersonalShiftArray() {
        return personalShiftArray;
    }

    public void setPersonalShiftArray(PersonalShift[][] personalShiftArray) {
        this.personalShiftArray = personalShiftArray;
    }

    public boolean isNextWeek() {
        return nextWeek;
    }

    public void setNextWeek(boolean nextWeek) {
        this.nextWeek = nextWeek;
    }

    public boolean isPreviousWeek() {
        return previousWeek;
    }

    public void setPreviousWeek(boolean previousWeek) {
        this.previousWeek = previousWeek;
    }

    public Schedule getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(Schedule currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public Object[][] getData() {
        Object[][] data = new Object[4][7];
        if (currentSchedule == null) {
            dayRange = currentMonth + "/" + currentYear;
            nextWeek = false;
            previousWeek = false;
            return data;
        }
        personalShiftArray = new PersonalShift[4][7];
        int currentDay = startDay;
        for (int i = 0; i <= 6; i++) {
            for (int j = 1; j <= 4; j++) {
                PersonalShift tempPersonalShift = personalShiftMap.get(currentDay + "-" + currentMonth + "-" + currentYear + "-" + j);
                if (tempPersonalShift != null) {
                    personalShiftArray[j - 1][i] = tempPersonalShift;
                    data[j - 1][i] = tempPersonalShift.getJob();
                }

            }
            currentDay++;
        }
        currentDay--;
        endDay = currentDay > currentSchedule.getShiftMap().keySet().size()/4 ? currentSchedule.getShiftMap().keySet().size()/4 : currentDay;
        dayRange = (startDay < 1 ? 1 : startDay) + "/" + currentMonth + "/" + currentYear + "-" + endDay + "/" + currentMonth + "/" + currentYear;
        if (startDay <= 1) {
            previousWeek = false;
        } else {
            previousWeek = true;
        }
        if (startDay + 7 >= currentSchedule.getShiftMap().keySet().size() / 4) {
            nextWeek = false;
        } else {
            nextWeek = true;
        }
        return data;
    }

    public Object[][] changeData(String t) {
        if (t.equals("forward")) {
            startDay += 7;
        } else if (t.equals("backward")) {
            startDay -= 7;
        }
        return getData();
    }

    public Object[][] changeSchedule(String t) {
        if (t.equals("forward")) {
            currentMonth++;
            if (currentMonth == 13) {
                currentMonth = 1;
                currentYear++;
            }
        } else if (t.equals(("backward"))) {
            currentMonth--;
            if (currentMonth == 0) {
                currentMonth = 1;
                currentYear--;
            }
        }
        currentSchedule = scheduleMap.get(currentMonth + "-" + currentYear);
        if (currentSchedule != null) {
            startDay = currentSchedule.getShiftMap().get("1-" + currentMonth + "-" + currentYear + "-1").getWeekDay() * -1 + 2;
        }
        return getData();
    }
}
