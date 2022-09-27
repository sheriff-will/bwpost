package com.application.iserv.ui.parameters.models;

public class ParametersModel {

    String position;
    Long parameterId;
    double ratePerDay;

    public ParametersModel() {

    }

    public ParametersModel(String position, double ratePerDay) {
        this.position = position;
        this.ratePerDay = ratePerDay;
    }

    public ParametersModel(String position, Long parameterId, double ratePerDay) {
        this.position = position;
        this.parameterId = parameterId;
        this.ratePerDay = ratePerDay;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
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
                ", parameterId=" + parameterId +
                ", ratePerDay=" + ratePerDay +
                '}';
    }

}
