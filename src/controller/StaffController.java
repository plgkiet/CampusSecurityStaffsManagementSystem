package controller;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import model.CustomDate;
import model.PersonalShift;
import model.RealDateTime;
import model.Request;
import model.Staff;
import utils.DAO;

public class StaffController {

    Staff staff;
    DAO dao;
    static Calendar c = Calendar.getInstance();

    public StaffController(Staff staff) {
        this.staff = staff;
        this.dao = new DAO();
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public static void sendRequest(String content, Staff staff) {
        Request request = new Request(content, staff.getId());
        staff.getRequestList().add(request);
        ManagerController.receiveRequest(request);
    }

    public boolean checkIn(Staff staff, String id) {
        PersonalShift ps = staff.getPersonalShifts().get(id);
        if (ps == null) {
            System.out.print("ok");
            return false;

        }
        return checkValidCheckInTime(ps);
    }

    public boolean checkOut(Staff staff, String id) {
        PersonalShift ps = staff.getPersonalShifts().get(id);
        if (ps == null) {
            return false;
        }
        if (ps.getUserCheckIn() == null) {
            return false;
        }
        return checkValidCheckOutTime(ps, staff);
    }

    public boolean checkValidCheckInTime(PersonalShift ps) {
        CustomDate date = ps.getDate();
        Time time = ps.getCheckIn();
        String defaultDateTime = getFormattedDateTime(date.getDay(), date.getMonth(), date.getYear(), time.toString());
        RealDateTime rdt = getRealDateTime();
        String realDateTime = getFormattedDateTime(rdt.getDay(), rdt.getMonth(), rdt.getYear(), rdt.getHour(),
                rdt.getMinute(), rdt.getSecond());
        LocalDateTime dt1 = LocalDateTime.parse(defaultDateTime);
        LocalDateTime dt2 = LocalDateTime.parse(realDateTime);
        LocalDateTime beforeDt = dt1.minus(15, ChronoUnit.MINUTES);
        LocalDateTime afterDT = dt1.plusMinutes(50);
//      && dt2.isBefore(afterDT) || dt2.equals(beforeDt) || dt2.equals(afterDT)
        if (dt2.isAfter(beforeDt) || dt2.equals(beforeDt)) {
            ps.setUserCheckIn(Time.valueOf(String.format("%02d:%02d:%02d", rdt.getHour(),
                    rdt.getMinute(), rdt.getSecond())));
            System.out.println(String.valueOf(ps.getUserCheckIn()));
            dao.insertCheckInTime(ps.getUserCheckIn(), staff.getId(), ps.getShiftId());
            return true;
        }
        ps.setUserCheckIn(null);
        return false;

    }

    public boolean checkValidCheckOutTime(PersonalShift ps, Staff staff) {
        CustomDate date = ps.getDate();
        Time time = ps.getCheckOut();
        String defaultDateTime = getFormattedDateTime(date.getDay(), date.getMonth(), date.getYear(), time.toString());
        RealDateTime rdt = getRealDateTime();
        String realDateTime = getFormattedDateTime(rdt.getDay(), rdt.getMonth(), rdt.getYear(), rdt.getHour(),
                rdt.getMinute(), rdt.getSecond());
        LocalDateTime dt1 = LocalDateTime.parse(defaultDateTime);
        LocalDateTime dt2 = LocalDateTime.parse(realDateTime);
        LocalDateTime beforeDT = dt2.plusMinutes(15);
        if (dt2.equals(dt1) || dt2.isBefore(dt1)) {
            ps.setUserCheckOut(Time.valueOf(String.format("%02d:%02d:%02d", rdt.getHour(),
                    rdt.getMinute(), rdt.getSecond())));
            staff.setRealHour(staff.getRealHour() + 6);
            dao.insertCheckOutTime(ps.getUserCheckOut(), staff.getId(), ps.getShiftId());
            dao.updateRealHour(staff.getRealHour(), staff.getId());
            return true;
        }
        ps.setUserCheckOut(null);

        return false;
    }

    public Time getRealTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        Time time = Time.valueOf(String.valueOf(hour) + ":" + String.valueOf(min) + ":" + String.valueOf(sec));
        return time;
    }

    public static RealDateTime getRealDateTime() {
        int realDay = c.get(Calendar.DAY_OF_MONTH);
        int realMonth = c.get(Calendar.MONTH) + 1;
        int realYear = c.get(Calendar.YEAR);
        int realHour = c.get(Calendar.HOUR_OF_DAY);
           int realMinute = c.get(Calendar.MINUTE);
        int realSecond = c.get(Calendar.SECOND);
        return new RealDateTime(realDay, realMonth, realYear, realHour, realMinute, realSecond);
    }

    public static String getFormattedDateTime(int day, int month, int year, String time) {
        return String.format("%04d-%02d-%02d", year, month, day) + "T" + time;
    }

    public static String getFormattedDateTime(int day, int month, int year, int hour, int minute, int second) {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02d", year, month, day, hour, minute, second);
    }
}
