package com.darshan09200.employeemanagementsystem;

import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    private static Database instance;

    private final ArrayList<Employee> employees;

    private boolean isEditActive = false;
    private String viewEmpId = "";

    private Database() {
        employees = new ArrayList<>();
        addEmployee(
                new Manager(
                        "EMP-001",
                        "Darshan Jain",
                        LocalDate.of(2006, 12, 11),
                        7968.0,
                        0.0,
                        98,
                        new Motorcycle(
                                VehicleMake.KAWASAKI,
                                "567-098",
                                VehicleColor.BLACK,
                                VehicleCategory.RACE_MOTORCYCLE,
                                true
                        )
                )
        );
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(0, employee);
        Registration.getInstance().resetFields();
    }

    public Employee getEmployee(String empId) {
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            if (employee.getEmpId().equals(empId)) return employee;
        }
        return null;
    }

    public String getNewEmpId() {
        return String.format("EMP-%03d", (employees.size() + 1));
    }

    public boolean isEditActive() {
        return isEditActive;
    }

    public void setEditActive(boolean editActive) {
        isEditActive = editActive;
    }

    public String getViewEmpId() {
        return viewEmpId;
    }

    public void setViewEmpId(String viewEmpId) {
        this.viewEmpId = viewEmpId;
    }
}
