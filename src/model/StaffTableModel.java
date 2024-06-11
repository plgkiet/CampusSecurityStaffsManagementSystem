/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import controller.StaffTableModelController;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author phamlegiakiet
 */
public class StaffTableModel extends AbstractTableModel {
    private String[] columnNames = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private Object[][] data;
    private StaffTableModelController tableController;
   
    public StaffTableModel(Manager manager, Staff staff) {
        tableController = new StaffTableModelController(manager, staff);
        data = tableController.getData();
    }

    public StaffTableModelController getTableController() {
        return tableController;
    }

    public void setTableController(StaffTableModelController tableController) {
        this.tableController = tableController;
    }

    public void changeData(String t) {
        data = tableController.changeData(t);
        
    }

    public void changeSchedule(String t) {
        data = tableController.changeSchedule(t);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

//    @Override
//    public boolean isCellEdsitable(int rowIndex, int columnIndex) {
//        return rowIndex != 0;
//    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

}
