package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.DAO;

public class Manager extends User {

    List<Request> requestList;
    Map<String, Schedule> scheduleMap;
    public static Map<String, Staff> staffMap;
    DAO dao;

    public Manager(String name, String id, String password) {
        super(name, id, password, USERROLE.MANAGER);
        staffMap = new HashMap<>();
        scheduleMap = new HashMap<>();
        requestList = new ArrayList<>();
        dao = new DAO();
    }

    public void setScheduleMap(Map<String, Schedule> scheduleMap) {
        this.scheduleMap = scheduleMap;
    }

    public Map<String, Staff> getStaffMap() {
        return staffMap;
    }

    public void setStaffMap(Map<String, Staff> staffMap) {
        this.staffMap = staffMap;
    }

    public Map<String, Schedule> getScheduleMap() {
        return scheduleMap;
    }

    public void setScheduleList(Map<String, Schedule> scheduleMap) {
        this.scheduleMap = scheduleMap;
    }

    public Map<String, Staff> getStaffList() {
        this.setStaffMap(dao.loadStaffsFromDatabase());
        return this.staffMap;
    }

    @Override
    public String getId() {

        return super.getId();
    }

    @Override
    public String getName() {

        return super.getName();
    }

    @Override
    public String getPassword() {

        return super.getPassword();
    }

    @Override
    public User.USERROLE getRole() {

        return super.getRole();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setPassword(String password) {

        super.setPassword(password);
    }

    @Override
    public void setRole(User.USERROLE role) {
        super.setRole(role);
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

}
