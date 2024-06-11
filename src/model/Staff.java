package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.DAO;

public class Staff extends User {

    private int expectedHour;
    private int realHour;
    private long salary;
    Map<String, PersonalShift> personalShifts;
    List<Request> requestList;
    DAO dao;

    public Staff(String name, String id, String password) {
        super(id, name, password, USERROLE.STAFF);
        personalShifts = new HashMap<>();
        this.requestList = new ArrayList<>();
        dao = new DAO();
    }

    public int getExpectedHour() {
        return expectedHour;
    }

    public void setExpectedHour(int expectedHour) {
        this.expectedHour = expectedHour;
    }

    public int getRealHour() {
        return realHour;
    }

    public void setRealHour(int realHour) {
        this.realHour = realHour;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public Map<String, PersonalShift> getPersonalShifts() {
        return personalShifts;
    }

    public void setPersonalShifts(Map<String, PersonalShift> personalShifts) {
        this.personalShifts = personalShifts;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(super.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Staff) {
            return ((Staff) obj).getId().equalsIgnoreCase(super.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Staff{"
                + "name='" + getName() + '\''
                + ", id='" + getId() + '\''
                + ", password='" + getPassword() + '\''
                + ", role='" + getRole() + '\''
                + ", expectedHour=" + expectedHour
                + ", realHour=" + realHour
                + '}';
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

}
