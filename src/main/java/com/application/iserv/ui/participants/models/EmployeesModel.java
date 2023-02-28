package com.application.iserv.ui.participants.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeesModel {

    Integer daysWorked;
    Long participantId;
    LocalDateTime timestamp;
    LocalDate dateOfBirth, placementDate, completionDate;
    String firstname, lastname, identityNumber, gender, maritalStatus, mobileNumber,
            alternateMobileNumber, postalAddress, residentialAddress, education, placementOfficer,
            placementPlace, position, mobileWalletProvider, bankName,
            branch, accountNumber, district, village, service, duration, employee;

    public EmployeesModel() {

    }

    public EmployeesModel(LocalDateTime timestamp, LocalDate dateOfBirth, LocalDate placementDate,
                          LocalDate completionDate, String firstname, String lastname,
                          String identityNumber, String gender, String maritalStatus, String mobileNumber,
                          String alternateMobileNumber, String postalAddress, String residentialAddress,
                          String education, String placementOfficer, String placementPlace, String position,
                          String mobileWalletProvider, String bankName, String branch, String accountNumber,
                          String district, String village, String service, String duration) {
        this.timestamp = timestamp;
        this.dateOfBirth = dateOfBirth;
        this.placementDate = placementDate;
        this.completionDate = completionDate;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.mobileNumber = mobileNumber;
        this.alternateMobileNumber = alternateMobileNumber;
        this.postalAddress = postalAddress;
        this.residentialAddress = residentialAddress;
        this.education = education;
        this.placementOfficer = placementOfficer;
        this.placementPlace = placementPlace;
        this.position = position;
        this.mobileWalletProvider = mobileWalletProvider;
        this.bankName = bankName;
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.district = district;
        this.village = village;
        this.service = service;
        this.duration = duration;
    }

    public EmployeesModel(Long participantId, LocalDateTime timestamp, LocalDate dateOfBirth,
                          LocalDate placementDate, LocalDate completionDate, String firstname, String lastname,
                          String identityNumber, String gender, String maritalStatus, String mobileNumber,
                          String alternateMobileNumber, String postalAddress, String residentialAddress,
                          String education, String placementOfficer, String placementPlace, String position,
                          String mobileWalletProvider, String bankName, String branch, String accountNumber,
                          String district, String village, String service, String duration) {
        this.participantId = participantId;
        this.timestamp = timestamp;
        this.dateOfBirth = dateOfBirth;
        this.placementDate = placementDate;
        this.completionDate = completionDate;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.mobileNumber = mobileNumber;
        this.alternateMobileNumber = alternateMobileNumber;
        this.postalAddress = postalAddress;
        this.residentialAddress = residentialAddress;
        this.education = education;
        this.placementOfficer = placementOfficer;
        this.placementPlace = placementPlace;
        this.position = position;
        this.mobileWalletProvider = mobileWalletProvider;
        this.bankName = bankName;
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.district = district;
        this.village = village;
        this.service = service;
        this.duration = duration;
    }

    public EmployeesModel(Long participantId, LocalDateTime timestamp, LocalDate dateOfBirth,
                          LocalDate placementDate, LocalDate completionDate, String firstname, String lastname,
                          String identityNumber, String gender, String maritalStatus, String mobileNumber,
                          String alternateMobileNumber, String postalAddress, String residentialAddress,
                          String education, String placementOfficer, String placementPlace, String position,
                          String mobileWalletProvider, String bankName, String branch, String accountNumber,
                          String district, String village, String service, String duration, String employee) {
        this.participantId = participantId;
        this.timestamp = timestamp;
        this.dateOfBirth = dateOfBirth;
        this.placementDate = placementDate;
        this.completionDate = completionDate;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.mobileNumber = mobileNumber;
        this.alternateMobileNumber = alternateMobileNumber;
        this.postalAddress = postalAddress;
        this.residentialAddress = residentialAddress;
        this.education = education;
        this.placementOfficer = placementOfficer;
        this.placementPlace = placementPlace;
        this.position = position;
        this.mobileWalletProvider = mobileWalletProvider;
        this.bankName = bankName;
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.district = district;
        this.village = village;
        this.service = service;
        this.duration = duration;
        this.employee = employee;
    }

    public EmployeesModel(Integer daysWorked, Long participantId, LocalDateTime timestamp,
                          LocalDate dateOfBirth, LocalDate placementDate, LocalDate completionDate,
                          String firstname, String lastname, String identityNumber, String gender,
                          String maritalStatus, String mobileNumber, String alternateMobileNumber,
                          String postalAddress, String residentialAddress, String education,
                          String placementOfficer, String placementPlace, String position,
                          String mobileWalletProvider, String bankName, String branch, String accountNumber,
                          String district, String village, String service, String duration, String employee) {
        this.daysWorked = daysWorked;
        this.participantId = participantId;
        this.timestamp = timestamp;
        this.dateOfBirth = dateOfBirth;
        this.placementDate = placementDate;
        this.completionDate = completionDate;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.mobileNumber = mobileNumber;
        this.alternateMobileNumber = alternateMobileNumber;
        this.postalAddress = postalAddress;
        this.residentialAddress = residentialAddress;
        this.education = education;
        this.placementOfficer = placementOfficer;
        this.placementPlace = placementPlace;
        this.position = position;
        this.mobileWalletProvider = mobileWalletProvider;
        this.bankName = bankName;
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.district = district;
        this.village = village;
        this.service = service;
        this.duration = duration;
        this.employee = employee;
    }

    public Integer getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Integer daysWorked) {
        this.daysWorked = daysWorked;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(LocalDate placementDate) {
        this.placementDate = placementDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAlternateMobileNumber() {
        return alternateMobileNumber;
    }

    public void setAlternateMobileNumber(String alternateMobileNumber) {
        this.alternateMobileNumber = alternateMobileNumber;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPlacementOfficer() {
        return placementOfficer;
    }

    public void setPlacementOfficer(String placementOfficer) {
        this.placementOfficer = placementOfficer;
    }

    public String getPlacementPlace() {
        return placementPlace;
    }

    public void setPlacementPlace(String placementPlace) {
        this.placementPlace = placementPlace;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "ParticipantsModel{" +
                "daysWorked=" + daysWorked +
                ", participantId=" + participantId +
                ", timestamp=" + timestamp +
                ", dateOfBirth=" + dateOfBirth +
                ", placementDate=" + placementDate +
                ", completionDate=" + completionDate +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", alternateMobileNumber='" + alternateMobileNumber + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                ", education='" + education + '\'' +
                ", placementOfficer='" + placementOfficer + '\'' +
                ", placementPlace='" + placementPlace + '\'' +
                ", position='" + position + '\'' +
                ", mobileWalletProvider='" + mobileWalletProvider + '\'' +
                ", bankName='" + bankName + '\'' +
                ", branch='" + branch + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", district='" + district + '\'' +
                ", village='" + village + '\'' +
                ", service='" + service + '\'' +
                ", duration='" + duration + '\'' +
                ", participant='" + employee + '\'' +
                '}';
    }

}