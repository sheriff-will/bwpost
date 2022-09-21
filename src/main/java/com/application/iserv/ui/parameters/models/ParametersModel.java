package com.application.iserv.ui.parameters.models;

public class ParametersModel {

    int workingDays;
    double ratePerDay, baseSalary;

    public ParametersModel() {

    }

    public ParametersModel(int workingDays, double ratePerDay, double baseSalary) {
        this.workingDays = workingDays;
        this.ratePerDay = ratePerDay;
        this.baseSalary = baseSalary;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

    public double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Override
    public String toString() {
        return "ParametersModel{" +
                "workingDays=" + workingDays +
                ", ratePerDay=" + ratePerDay +
                ", baseSalary=" + baseSalary +
                '}';
    }

}
