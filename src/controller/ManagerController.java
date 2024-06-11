package controller;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import model.*;
import utils.DAO;

public class ManagerController {

    private static Manager manager;
    private static DAO dao;

    public ManagerController(Manager manager) {
        this.manager = manager;
        this.dao = new DAO();
    }

    public boolean registerStaff(String name, String id,String password,UserManager userManager) {
        Staff staff = manager.getStaffList().get(id);
        if (staff != null) {
            return false;
        }
        staff = new Staff(name, id, password);
        manager.getStaffList().put(id, staff);
        userManager.addUser(staff);
        return true;
    }

    public static void receiveRequest(Request request) {
        manager.getRequestList().add(request);
    }

    public static Manager getManager() {
        return manager;
    }

    public static void setManager(Manager manager) {
        ManagerController.manager = manager;
    }

    public void createSchedule() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int dayInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        Schedule schedule = new Schedule(month + "-" + year);
        Time checkInTime;
        Time checkOutTime;
        PriorityQueue<Staff> queue = new PriorityQueue<Staff>((s1, s2) -> {
            int compare = s1.getExpectedHour() - s2.getExpectedHour();
            if (compare == 0) {
                compare = s1.getId().compareToIgnoreCase(s2.getId());
            }
            return compare;
        });
        queue.addAll(manager.getStaffList().values());
        for (int i = 1; i <= dayInMonth; i++) {
            for (int j = 1; j <= 4; j++) {
                String shiftID = i + "-" + month + "-" + year + "-" + j;
                LocalDate date = LocalDate.of(year, month, i);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                int dayOfWeekInt = dayOfWeek.getValue();
                Shift shift = new Shift(shiftID, dayOfWeekInt);
                switch (j) {
                    case 1:
                        checkInTime = Time.valueOf("00:00:00");
                        checkOutTime = Time.valueOf("06:00:00");
                        break;
                    case 2:
                        checkInTime = Time.valueOf("06:00:00");
                        checkOutTime = Time.valueOf("12:00:00");
                        break;
                    case 3:
                        checkInTime = Time.valueOf("12:00:00");
                        checkOutTime = Time.valueOf("18:00:00");
                        break;
                    case 4:
                        checkInTime = Time.valueOf("18:00:00");
                        checkOutTime = Time.valueOf("00:00:00");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + j);
                }

                for (int campus = 1; campus <= 5; campus++) {
                    shift.getOfficeStaffMap().put(campus, new ArrayList<Staff>());
                    for (int staff = 1; staff <= 2; staff++) {
                        Staff tempStaff = queue.poll();
                        tempStaff.setExpectedHour(tempStaff.getExpectedHour() + 4);
                        dao.updateExpectedHour(tempStaff.getExpectedHour(), tempStaff.getId());
                        shift.getOfficeStaffMap().get(campus).add(tempStaff);
                        PersonalShift personalShift = new PersonalShift(shiftID, tempStaff.getId(), tempStaff.getName(),
                                String.valueOf(campus), new CustomDate(i, month, year), checkInTime, checkOutTime, "Office");
                        dao.insertPersonalShift(shiftID, tempStaff.getId(), tempStaff.getName(), String.valueOf(campus), new CustomDate(i, month, year), checkInTime, checkOutTime, null, null, "Office", schedule.getScheduleID(), shift.getWeekDay());
                        tempStaff.getPersonalShifts().put(shiftID, personalShift);
                        queue.add(tempStaff);
                        shift.setTotalStaff(shift.getTotalStaff() + 1);
                    }
                }

                for (int campus = 1; campus <= 5; campus++) {
                    List<Staff> polledStaff = new ArrayList<>();
                    shift.getBackupStaffMap().put(campus, new ArrayList<>());
                    for (int staff = 1; staff <= 2; staff++) {
                        while (true) {
                            Staff tempStaff = queue.poll();
                            polledStaff.add(tempStaff);
                            if (!shift.getOfficeStaffMap().get(campus).contains(tempStaff) && !shift.getBackupStaffMap().get(campus).contains(tempStaff)) {
                                tempStaff.setExpectedHour(tempStaff.getExpectedHour() + 4);
                                dao.updateExpectedHour(tempStaff.getExpectedHour(), tempStaff.getId());
                                shift.getBackupStaffMap().get(campus).add(tempStaff);
                                PersonalShift personalShift = new PersonalShift(shiftID, tempStaff.getId(), tempStaff.getName(),
                                        String.valueOf(campus), new CustomDate(i, month, year), checkInTime, checkOutTime, "Backup");
                                dao.insertPersonalShift(shiftID, tempStaff.getId(), tempStaff.getName(), String.valueOf(campus), new CustomDate(i, month, year), checkInTime, checkOutTime, null, null, "Backup", schedule.getScheduleID(), shift.getWeekDay());
                                tempStaff.getPersonalShifts().put(shiftID, personalShift);
                                shift.setTotalStaff(shift.getTotalStaff() + 1);
                                break;
                            }
                        }
                    }
                    for (Staff tempPolledStaff : polledStaff) {
                        queue.add(tempPolledStaff);
                    }
                }
                dao.updateTotalStaff(shift.getTotalStaff(), shiftID);
                schedule.getShiftMap().put(shiftID, shift);
            }
        }
        manager.getScheduleMap().put(month + "-" + year, schedule);
    }

    public void calculateAndSetSalary(Staff staff, double hourlyRate) {
        int expectedHour = staff.getExpectedHour();
        int realHour = staff.getRealHour();
        long salary = 0;
        int caseType = 0;
        if (expectedHour > realHour) { 
            caseType = 2;
        } else {
            if (expectedHour == realHour) {
                caseType = 2;
            } else {
                caseType = 3;
            }
        }
        switch (caseType) {
//            case 1:
//                salary = (long) (realHour * 80000 + (expectedHour - realHour) * 0.2 * 80000);
//                break;
            case 2:
                salary = (long) (realHour * 80000);
                break;
            case 3:
                salary = (long) ((expectedHour + (realHour - expectedHour) * 1.5) * 80000);
                break;
        }
        staff.setSalary(salary);
    }
}
