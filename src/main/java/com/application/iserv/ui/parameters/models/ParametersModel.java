package com.application.iserv.ui.parameters.models;

public class ParametersModel {

    Long parameterId;
    double dailyRatePerDay;
    String position, duration;

    public ParametersModel() {

    }

    public ParametersModel(double dailyRatePerDay, String position, String duration) {
        this.dailyRatePerDay = dailyRatePerDay;
        this.position = position;
        this.duration = duration;
    }

    public ParametersModel(Long parameterId, double dailyRatePerDay, String position, String duration) {
        this.parameterId = parameterId;
        this.dailyRatePerDay = dailyRatePerDay;
        this.position = position;
        this.duration = duration;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public double getDailyRatePerDay() {
        return dailyRatePerDay;
    }

    public void setDailyRatePerDay(double dailyRatePerDay) {
        this.dailyRatePerDay = dailyRatePerDay;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ParametersModel{" +
                "parameterId=" + parameterId +
                ", dailyRatePerDay=" + dailyRatePerDay +
                ", position='" + position + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

}
