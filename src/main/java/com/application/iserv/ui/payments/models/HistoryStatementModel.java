package com.application.iserv.ui.payments.models;

public class HistoryStatementModel {

    Double ratePerDay;
    String date, amount, participant, identityNumber;
    Integer bonusAmount, deductionAmount, daysWorked;

    public HistoryStatementModel() {

    }

    public HistoryStatementModel(Double ratePerDay, String date, String amount, String participant,
                                 String identityNumber, Integer bonusAmount, Integer deductionAmount,
                                 Integer daysWorked) {
        this.ratePerDay = ratePerDay;
        this.date = date;
        this.amount = amount;
        this.participant = participant;
        this.identityNumber = identityNumber;
        this.bonusAmount = bonusAmount;
        this.deductionAmount = deductionAmount;
        this.daysWorked = daysWorked;
    }

    public Double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(Double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public Integer getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Integer bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Integer getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(Integer deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public Integer getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Integer daysWorked) {
        this.daysWorked = daysWorked;
    }

    @Override
    public String toString() {
        return "HistoryStatementModel{" +
                "ratePerDay=" + ratePerDay +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", participant='" + participant + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", bonusAmount=" + bonusAmount +
                ", deductionAmount=" + deductionAmount +
                ", daysWorked=" + daysWorked +
                '}';
    }

}
