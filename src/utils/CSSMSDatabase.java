package utils;

import java.sql.*;

public class CSSMSDatabase {

    private Connection connection;

    public CSSMSDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cssms", "root", "kiet3011");
            System.out.println("Database connection established.");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        createDatabase();
    }

    private void createDatabase() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("use cssms");
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Employee (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role ENUM('MANAGER', 'STAFF') NOT NULL, expectedHourNumber INT, realHourNumber INT)");
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Campus (campus_id VARCHAR(255) NOT NULL, PRIMARY KEY (campus_id)");
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Shift (shift_id VARCHAR(255) NOT NULL, staff_id VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, campus_id VARCHAR(255) NOT NULL, day INT NOT NULL, month INT NOT NULL, year INT NOT NULL, checkIn TIME NOT NULL, checkOut TIME NOT NULL, userCheckInTime TIME NULL, userCheckOutTime TIME NULL, job VARCHAR(255) NOT NULL, scheduleID VARCHAR(255) NOT NULL, FOREIGN KEY (campus_id) REFERENCES Campus(campus_id), FOREIGN KEY (staff_id) REFERENCES Employee(id), PRIMARY KEY (shift_id, campus_id, staff_id))");
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS schedule (scheduleId INT NOT NULL, shiftId VARCHAR(255) NOT NULL, FOREIGN KEY (shiftId) REFERENCES Shift(shift_id), PRIMARY KEY (scheduleId, shiftId));");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addManager(String id, String name, String password, String role) {
        try {
            String query = "INSERT INTO Employee (id, name, password, role, expectedHourNumber, realHourNumber) VALUES (?, ?, ?, ?, NULL, NULL)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStaff(String id, String name, String password, String role, int expectedHourNumber,
            int realHourNumber) {
        try {
            String query = "INSERT INTO Employee (id, name, password, role, expectedHourNumber, realHourNumber) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
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

    public void addCampus(String campus_id, String staff_id) {
        try {
            String query = "INSERT INTO Campus (campus_id) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, campus_id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addShift(String shift_id, String staff_id, String name, String campus_id, int day, int month, int year,
            Time checkIn,
            Time checkOut, Time userCheckInTime, Time userCheckOutTime, String job) {
        try {
            String query = "INSERT INTO Shift (shift_id, staff_id, name, campus_id, day, month, year, checkIn, checkOut, job) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, shift_id);
            pstmt.setString(2, staff_id);
            pstmt.setString(3, name);
            pstmt.setString(4, campus_id);
            pstmt.setInt(5, day);
            pstmt.setInt(6, month);
            pstmt.setInt(7, year);
            pstmt.setTime(8, checkIn);
            pstmt.setTime(9, checkOut);
            pstmt.setTime(10, checkOut);
            pstmt.setTime(11, checkOut);
            pstmt.setString(12, job);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSchedule(String scheduleID, String shiftID) {
        try {
            String query = "INSERT INTO Schedule (scheduleId, shiftId) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, scheduleID);
            pstmt.setString(2, shiftID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
