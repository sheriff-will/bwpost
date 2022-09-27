package com.application.iserv.ui.parameters.models;

public class ParametersModel {

    String position;
    double ratePerDay;

    public ParametersModel() {

    }

    public ParametersModel(String position, double ratePerDay) {
        this.position = position;
        this.ratePerDay = ratePerDay;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    @Override
    public String toString() {
        return "ParametersModel{" +
                "position='" + position + '\'' +
                ", ratePerDay=" + ratePerDay +
                '}';
    }

}
