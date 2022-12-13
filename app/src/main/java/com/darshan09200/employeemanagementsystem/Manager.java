package com.darshan09200.employeemanagementsystem;

import java.time.LocalDate;

public class Manager extends Employee {
    static final double GAIN_FACTOR_CLIENT = 500;
    private int nbClients;

    public Manager(String profileImage, String empId, String firstName, String lastName,  LocalDate dob, double occupationRate, double monthlySalary, int nbClients, Vehicle vehicle) {
        super(profileImage, empId, firstName, lastName, dob, occupationRate, monthlySalary, EmployeeType.MANAGER, vehicle);
        this.nbClients = nbClients;
    }

    public int getNbClients() {
        return nbClients;
    }

    public void setNbClients(int nbClients) {
        this.nbClients = nbClients;
    }

    @Override
    public double getAnnualIncome() {
        return super.getAnnualIncome() + (nbClients * GAIN_FACTOR_CLIENT);
    }

    @Override
    public String toString() {
        return "Manager{" +
                super.toString() +
                "nbClients=" + nbClients +
                '}';
    }
}
