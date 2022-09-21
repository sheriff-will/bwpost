package com.application.iserv.ui.payments.models;

import java.time.LocalDate;

public class HistoryModel {

    LocalDate month;
    Long remunerationHistoryId, participantId;
    String status, statusReason, claimed, bonusAmount, bonusReason, deductionAmount,
            deductionReason, agent, amount, mobileWalletProvider, bankName, branch, accountNumber;

    public HistoryModel() {

    }

    public HistoryModel(LocalDate month, Long remunerationHistoryId, Long participantId, String status,
                        String statusReason, String claimed, String bonusAmount, String bonusReason,
                        String deductionAmount, String deductionReason, String agent, String amount,
                        String mobileWalletProvider, String bankName, String branch, String accountNumber) {
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
        this.mobileWalletProvider = mobileWalletProvider;
        this.bankName = bankName;
        this.branch = branch;
        this.accountNumber = accountNumber;
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

    public String getMobileWalletProvider() {
        return mobileWalletProvider;
    }

    public void setMobileWalletProvider(String mobileWalletProvider) {
        this.mobileWalletProvider = mobileWalletProvider;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "HistoryModel{" +
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
                ", mobileWalletProvider='" + mobileWalletProvider + '\'' +
                ", bankName='" + bankName + '\'' +
                ", branch='" + branch + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }

}
