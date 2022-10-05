package com.application.iserv.ui.payments.models;


public class AgentPaymentsModel {

    String participant, amount, claimed, ratePerDay, daysWorked,
            baseSalary, bonus, deduction, approval, paymentMode,
            provider, totalSalary;

    public AgentPaymentsModel(String participant, String amount, String claimed, String ratePerDay,
                              String daysWorked, String baseSalary, String bonus, String deduction,
                              String approval, String paymentMode, String provider, String totalSalary) {
        this.participant = participant;
        this.amount = amount;
        this.claimed = claimed;
        this.ratePerDay = ratePerDay;
        this.daysWorked = daysWorked;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deduction = deduction;
        this.approval = approval;
        this.paymentMode = paymentMode;
        this.provider = provider;
        this.totalSalary = totalSalary;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClaimed() {
        return claimed;
    }

    public void setClaimed(String claimed) {
        this.claimed = claimed;
    }

    public String getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(String ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public String getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(String daysWorked) {
        this.daysWorked = daysWorked;
    }

    public String getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(String baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(String totalSalary) {
        this.totalSalary = totalSalary;
    }

}
