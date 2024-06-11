package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CustomDate;
import model.PersonalShift;
import model.Staff;
import model.User;
import java.util.*;
import model.Schedule;
import model.Shift;
import model.User.USERROLE;

public class DAO implements AutoCloseable {

    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;

    public DAO() {
        connect();
    }

    public void connect() {
        try {
            // loading MySQL driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            // creating connection with the database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cssms", "root", "kiet3011");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, Staff> loadStaffsFromDatabase() {
        Map<String, Staff> staffMap = new HashMap<>();
        try {
            String query = "SELECT * FROM cssms.employee where role = 'STAFF' ";
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                String staffID = rs.getString("id");
                String staffName = rs.getString("name");
                String staffPassword = rs.getString("password");
                int expectedHour = rs.getInt("expectedHourNumber");
                int realHour = rs.getInt("realHourNumber");
                Staff staff = new Staff(staffName, staffID, staffPassword);
                staff.setExpectedHour(expectedHour);
                staff.setRealHour(realHour);
                staffMap.put(staffID, staff);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResultSetAndStatement();
        }
        return staffMap;
    }

    public Map<String, User> loadUser() {
        Map<String, User> userMap = new HashMap<String, User>();
        User user;
        try {
            String query = "SELECT id, name, password, role FROM cssms.employee";
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");
                if (role.equalsIgnoreCase("Manager")) {
                    user = new User(id, name, password, USERROLE.MANAGER);

                } else {
                    user = new User(id, name, password, USERROLE.STAFF);
                }
                userMap.put(id, user);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResultSetAndStatement();
        }
        return userMap;
    }

    public Map<String, Schedule> loadSchedule() {
        Map<String, Schedule> scheduleMap = new HashMap<>();
        Map<String, Staff> staffMap = loadStaffsFromDatabase();
        try {
            String query = "SELECT shift_id,name,staff_id,password,campus_id,job,scheduleID,weekDay, userCheckInTime, totalStaff from cssms.shift natural join cssms.employee";
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                String shiftID = rs.getString("shift_id");
                String staffID = rs.getString("staff_id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String campusID = rs.getString("campus_id");
                String job = rs.getString("job");
                String scheduleID = rs.getString("scheduleID");
                String weekDay = rs.getString("weekDay");
                int totalStaff = rs.getInt("totalStaff");
                String userCheckInTime = rs.getString("userCheckInTime");
                Staff staff = staffMap.get(staffID);
                Schedule schedule = scheduleMap.getOrDefault(scheduleID, new Schedule(scheduleID));
                scheduleMap.putIfAbsent(scheduleID, schedule);
                Shift shift = schedule.getShiftMap().getOrDefault(shiftID,
                        new Shift(shiftID, Integer.parseInt(weekDay)));
                shift.setTotalStaff(totalStaff);
                if (userCheckInTime != null) {
                    shift.setTotalCheckInStaff(shift.getTotalCheckInStaff()+1);
                }
                schedule.getShiftMap().putIfAbsent(shiftID, shift);
                if (job.equals("Office")) {
                    shift.getOfficeStaffMap().getOrDefault(campusID, new ArrayList<Staff>()).add(staff);
                } else {
                    shift.getBackupStaffMap().getOrDefault(campusID, new ArrayList<Staff>()).add(staff);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResultSetAndStatement();
        }
        return scheduleMap;
    }

    public void insertStaffData(String id, String name, String password, String role, int expectedHourNumber,
            int realHourNumber) {
        try {
            String query = "INSERT INTO cssms.employee (id, name, password, role, expectedHourNumber, realHourNumber) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.setInt(5, expectedHourNumber);
            pstmt.setInt(6, realHourNumber);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalStaff(int totalStaff, String shiftID) {
        try {
            String query = "UPDATE cssms.shift SET totalStaff = ? WHERE shift_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, totalStaff);
            pstmt.setString(2, shiftID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPersonalShift(String shiftid, String staffid, String name, String campusid, CustomDate date, Time checkIn, Time checkOut, Time userCheckInTime, Time userCheckOutTime, String job, String scheduleID, int weekDay) {
        try {
            String query = "INSERT INTO cssms.shift (shift_id, staff_id, name, campus_id, day, month, year, checkIn, checkOut, userCheckInTime, userCheckOutTime, job, scheduleID, weekDay) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, shiftid);
            pstmt.setString(2, staffid);
            pstmt.setString(3, name);
            pstmt.setString(4, campusid);
            pstmt.setInt(5, date.getDay());
            pstmt.setInt(6, date.getMonth());
            pstmt.setInt(7, date.getYear());
            pstmt.setTime(8, checkIn);
            pstmt.setTime(9, checkOut);
            pstmt.setTime(10, userCheckInTime);
            pstmt.setTime(11, userCheckOutTime);
            pstmt.setString(12, job);
            pstmt.setString(13, scheduleID);
            pstmt.setInt(14, weekDay);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertIntoCampusTable(String campusID) {
        try {
            String query = "INSERT INTO cssms.campus (campus_id) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, campusID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCheckInTime(Time userCheckInTime, String staffID, String shiftID) {
        try {
            String query = "UPDATE cssms.shift SET userCheckInTime = ? WHERE staff_id = ? and shift_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, String.valueOf(userCheckInTime));
            pstmt.setString(2, staffID);
            pstmt.setString(3, shiftID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCheckOutTime(Time userCheckOutTime, String staffID, String shiftID) {
        try {
            String query = "UPDATE cssms.shift SET userCheckOutTime = ? WHERE staff_id = ? and shift_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setTime(1, userCheckOutTime);
            pstmt.setString(2, staffID);
            pstmt.setString(3, shiftID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRealHour(int realHour, String staffID) {
        try {
            String query = "UPDATE cssms.employee SET realHourNumber = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, realHour);
            pstmt.setString(2, staffID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExpectedHour(int expectedHour, String staffID) {
        try {
            String query = "UPDATE cssms.employee SET expectedHourNumber = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, expectedHour);
            pstmt.setString(2, staffID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStaffData(String name, String password, String id) {
        try {
            String query = "UPDATE cssms.employee SET name = ?, password = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSetAndStatement();
        }
    }

    public void updateManagerData(String name, String password, String id) {
        try {
            String query = "UPDATE cssms.employee SET name = ?, password = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSetAndStatement();
        }
    }

    public Map<String, PersonalShift> loadPersonalShiftFromDatabase(String staff_id) {
        Map<String, PersonalShift> listOfPersonalShifts = new HashMap<>();
        try {
            String query = "SELECT * FROM cssms.shift WHERE staff_id = ?";
            st = con.prepareStatement(query);
            st.setString(1, staff_id);
            rs = st.executeQuery();

            while (rs.next()) {
                String shiftId = rs.getString("shift_id");
                String staffId = rs.getString("staff_id");
                String name = rs.getString("name");
                String campusId = rs.getString("campus_id");
                int day = rs.getInt("day");
                int month = rs.getInt("month");
                int year = rs.getInt("year");
                Time checkIn = rs.getTime("checkIn");
                Time checkOut = rs.getTime("checkOut");
                Time userCheckInTime = rs.getTime("userCheckInTime");
                Time userCheckOutTime = rs.getTime("userCheckOutTime");
                String job = rs.getString("job");
                PersonalShift personalShift = new PersonalShift(shiftId, staffId, name, campusId, new CustomDate(day, month, year),
                        checkIn,
                        checkOut, job);
                personalShift.setUserCheckIn(userCheckInTime);
                personalShift.setUserCheckOut(userCheckOutTime);
                listOfPersonalShifts.put(shiftId, personalShift);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CSSMSDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResultSetAndStatement();
        }
        return listOfPersonalShifts;
    }

//    public void printAllStaff() {
//        Map<String, Staff> staffs = loadStaffsFromDatabase();
//        for (Staff staff : staffs.values()) {
//            System.out.println(staff);
//        }
//    }
//
//    public void printAllPersonalShifts(String staff_id) {
//        Map<String, PersonalShift> shifts = loadPersonalShiftFromDatabase(staff_id);
//        for (PersonalShift shift : shifts.values()) {
//            System.out.println(shift);
//        }
//    }
    public void closeResultSetAndStatement() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        if (con == null) {
            connect();
        }
        return con;
    }

    @Override
    public void close() throws Exception {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
