package com.darshan09200.employeemanagementsystem;

import java.time.LocalDate;

public class Programmer extends Employee {
    static final double GAIN_FACTOR_PROJECTS = 200;
    private int nbProjects;

    public Programmer(String empId, String name, LocalDate dob, double occupationRate, double monthlySalary, int nbProjects, Vehicle vehicle) {
        super(empId, name, dob, occupationRate, monthlySalary, EmployeeType.PROGRAMMER, vehicle);
        this.nbProjects = nbProjects;
    }

    public int getNbProjects() {
        return nbProjects;
    }

    public void setNbProjects(int nbProjects) {
        this.nbProjects = nbProjects;
    }

    public double getAnnualIncome() {
        return super.getAnnualIncome() + (nbProjects * GAIN_FACTOR_PROJECTS);
    }

    @Override
    public String toString() {
        return "Programmer{" +
                super.toString() +
                "nbProjects=" + nbProjects +
                '}';
    }
}
