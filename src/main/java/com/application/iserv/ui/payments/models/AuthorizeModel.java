package com.application.iserv.ui.payments.models;

public class AuthorizeModel {

    double amount, totalNet;
    Long remunerationHistoryId, participantId;
    String bonusAmount, deductionAmount, status, statusReason,
            claimed, bonusReason, deductionReason, employee, identityNumber;

    public AuthorizeModel() {

    }

    public AuthorizeModel(String employee) {
        this.employee = employee;
    }

    public AuthorizeModel(String bonusAmount, String deductionAmount, Long remunerationHistoryId,
                          Long participantId, String status, String statusReason, String claimed,
                          String bonusReason, String deductionReason, String identityNumber) {
        this.bonusAmount = bonusAmount;
        this.deductionAmount = deductionAmount;
        this.remunerationHistoryId = remunerationHistoryId;
        this.participantId = participantId;
        this.status = status;
        this.statusReason = statusReason;
        this.claimed = claimed;
        this.bonusReason = bonusReason;
        this.deductionReason = deductionReason;
        this.identityNumber = identityNumber;
    }

    public AuthorizeModel(double amount, double totalNet, String bonusAmount, String deductionAmount,
                          Long remunerationHistoryId, Long participantId, String status, String statusReason,
                          String claimed, String bonusReason, String deductionReason, String employee,
                          String identityNumber) {
        this.amount = amount;
        this.totalNet = totalNet;
        this.bonusAmount = bonusAmount;
        this.deductionAmount = deductionAmount;
        this.remunerationHistoryId = remunerationHistoryId;
        this.participantId = participantId;
        this.status = status;
        this.statusReason = statusReason;
        this.claimed = claimed;
        this.bonusReason = bonusReason;
        this.deductionReason = deductionReason;
        this.employee = employee;
        this.identityNumber = identityNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(double totalNet) {
        this.totalNet = totalNet;
    }

    public Long getRemunerationHistoryId() {
        return remunerationHistoryId;
    }

    public void setRemunerationHistoryId(Long remunerationHistoryId) {
        this.remunerationHistoryId = remunerationHistoryId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(String bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(String deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public String getClaimed() {
        return claimed;
    }

    public void setClaimed(String claimed) {
        this.claimed = claimed;
    }

    public String getBonusReason() {
        return bonusReason;
    }

    public void setBonusReason(String bonusReason) {
        this.bonusReason = bonusReason;
    }

    public String getDeductionReason() {
        return deductionReason;
    }

    public void setDeductionReason(String deductionReason) {
        this.deductionReason = deductionReason;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    @Override
    public String toString() {
        return "AuthorizeModel{" +
                "amount=" + amount +
                ", totalNet=" + totalNet +
                ", remunerationHistoryId=" + remunerationHistoryId +
                ", participantId=" + participantId +
                ", bonusAmount='" + bonusAmount + '\'' +
                ", deductionAmount='" + deductionAmount + '\'' +
                ", status='" + status + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", claimed='" + claimed + '\'' +
                ", bonusReason='" + bonusReason + '\'' +
                ", deductionReason='" + deductionReason + '\'' +
                ", participant='" + employee + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                '}';
    }
}
