package com.application.iserv.ui.payments.models;

public class AuthorizeModel {

    double amount;
    int bonusAmount, deductionAmount;
    Long remunerationHistoryId, participantId;
    String status, statusReason, claimed, bonusReason, deductionReason, participant;

    public AuthorizeModel() {

    }

    public AuthorizeModel(String participant) {
        this.participant = participant;
    }

    public AuthorizeModel(int bonusAmount, int deductionAmount, Long remunerationHistoryId,
                          Long participantId, String status, String statusReason, String claimed,
                          String bonusReason, String deductionReason) {
        this.bonusAmount = bonusAmount;
        this.deductionAmount = deductionAmount;
        this.remunerationHistoryId = remunerationHistoryId;
        this.participantId = participantId;
        this.status = status;
        this.statusReason = statusReason;
        this.claimed = claimed;
        this.bonusReason = bonusReason;
        this.deductionReason = deductionReason;
    }

    public AuthorizeModel(double amount, int bonusAmount, int deductionAmount, Long remunerationHistoryId,
                          Long participantId, String status, String statusReason, String claimed,
                          String bonusReason, String deductionReason, String participant) {
        this.amount = amount;
        this.bonusAmount = bonusAmount;
        this.deductionAmount = deductionAmount;
        this.remunerationHistoryId = remunerationHistoryId;
        this.participantId = participantId;
        this.status = status;
        this.statusReason = statusReason;
        this.claimed = claimed;
        this.bonusReason = bonusReason;
        this.deductionReason = deductionReason;
        this.participant = participant;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(int bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public int getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(int deductionAmount) {
        this.deductionAmount = deductionAmount;
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

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    @Override
    public String toString() {
        return "AuthorizeModel{" +
                "amount=" + amount +
                ", bonusAmount=" + bonusAmount +
                ", deductionAmount=" + deductionAmount +
                ", remunerationHistoryId=" + remunerationHistoryId +
                ", participantId=" + participantId +
                ", status='" + status + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", claimed='" + claimed + '\'' +
                ", bonusReason='" + bonusReason + '\'' +
                ", deductionReason='" + deductionReason + '\'' +
                ", agent='" + participant + '\'' +
                '}';
    }

}
