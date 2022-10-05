package com.application.iserv.ui.payments.models;

public class HistoryModel {

    Long remunerationHistoryId, participantId;
    String status, statusReason, claimed, bonusAmount, bonusReason, deductionAmount,
            deductionReason, participant, amount, mobileWalletProvider, bankName, branch,
            accountNumber, ratePerDay, daysWorked, baseSalary, totalSalary, paymentMode, provider;

    public HistoryModel() {

    }

    public HistoryModel(Long remunerationHistoryId, Long participantId, String status, String statusReason,
                        String claimed, String bonusAmount, String bonusReason, String deductionAmount,
                        String deductionReason, String participant, String amount, String mobileWalletProvider,
                        String bankName, String branch, String accountNumber, String ratePerDay,
                        String daysWorked, String baseSalary, String totalSalary, String paymentMode,
                        String provider) {
        this.remunerationHistoryId = remunerationHistoryId;
        this.participantId = participantId;
        this.status = status;
        this.statusReason = statusReason;
        this.claimed = claimed;
        this.bonusAmount = bonusAmount;
        this.bonusReason = bonusReason;
        this.deductionAmount = deductionAmount;
        this.deductionReason = deductionReason;
        this.participant = participant;
        this.amount = amount;
        this.mobileWalletProvider = mobileWalletProvider;
        this.bankName = bankName;
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.ratePerDay = ratePerDay;
        this.daysWorked = daysWorked;
        this.baseSalary = baseSalary;
        this.totalSalary = totalSalary;
        this.paymentMode = paymentMode;
        this.provider = provider;
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

    public String getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(String totalSalary) {
        this.totalSalary = totalSalary;
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

    @Override
    public String toString() {
        return "HistoryModel{" +
                "remunerationHistoryId=" + remunerationHistoryId +
                ", participantId=" + participantId +
                ", status='" + status + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", claimed='" + claimed + '\'' +
                ", bonusAmount='" + bonusAmount + '\'' +
                ", bonusReason='" + bonusReason + '\'' +
                ", deductionAmount='" + deductionAmount + '\'' +
                ", deductionReason='" + deductionReason + '\'' +
                ", agent='" + participant + '\'' +
                ", amount='" + amount + '\'' +
                ", mobileWalletProvider='" + mobileWalletProvider + '\'' +
                ", bankName='" + bankName + '\'' +
                ", branch='" + branch + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", ratePerDay='" + ratePerDay + '\'' +
                ", daysWorked='" + daysWorked + '\'' +
                ", baseSalary='" + baseSalary + '\'' +
                ", totalSalary='" + totalSalary + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }

}
