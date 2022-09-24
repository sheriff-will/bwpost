package com.application.iserv.ui.payments.models;

import java.time.LocalDate;

public class AuthorizeModel {

    LocalDate month;
    Long remunerationHistoryId, participantId;
    String status, statusReason, claimed, bonusAmount,
            bonusReason, deductionAmount, deductionReason, agent, amount;

    public AuthorizeModel() {

    }

    public AuthorizeModel(LocalDate month, Long remunerationHistoryId, Long participantId,
                          String status, String statusReason, String claimed, String bonusAmount,
                          String bonusReason, String deductionAmount, String deductionReason) {
        this.month = month;
        this.remunerationHistoryId = remunerationHistoryId;
        this.participantId = participantId;
        this.status = status;
        this.statusReason = statusReason;
        this.claimed = claimed;
        this.bonusAmount = bonusAmount;
        this.bonusReason = bonusReason;
        this.deductionAmount = deductionAmount;
        this.deductionReason = deductionReason;
    }

    public AuthorizeModel(LocalDate month, Long remunerationHistoryId, Long participantId,
                          String status, String statusReason, String claimed, String bonusAmount,
                          String bonusReason, String deductionAmount, String deductionReason,
                          String agent, String amount) {
        this.month = month;
        this.remunerationHistoryId = remunerationHistoryId;
        this.participantId = participantId;
        this.status = status;
        this.statusReason = statusReason;
        this.claimed = claimed;
        this.bonusAmount = bonusAmount;
        this.bonusReason = bonusReason;
        this.deductionAmount = deductionAmount;
        this.deductionReason = deductionReason;
        this.agent = agent;
        this.amount = amount;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
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

    public String getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(String bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getBonusReason() {
        return bonusReason;
    }

    public void setBonusReason(String bonusReason) {
        this.bonusReason = bonusReason;
    }

    public String getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(String deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public String getDeductionReason() {
        return deductionReason;
    }

    public void setDeductionReason(String deductionReason) {
        this.deductionReason = deductionReason;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RemunerationHistoryModel{" +
                "month=" + month +
                ", remunerationHistoryId=" + remunerationHistoryId +
                ", participantId=" + participantId +
                ", status='" + status + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", claimed='" + claimed + '\'' +
                ", bonusAmount='" + bonusAmount + '\'' +
                ", bonusReason='" + bonusReason + '\'' +
                ", deductionAmount='" + deductionAmount + '\'' +
                ", deductionReason='" + deductionReason + '\'' +
                ", agent='" + agent + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

}