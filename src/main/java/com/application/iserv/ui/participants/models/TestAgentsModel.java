package com.application.iserv.ui.participants.models;


public class TestAgentsModel {

    String agent, position, attendance, firstname, lastname, IDNumber, gender, primaryMobile, alternateMobile, postalAddress, residentialAddress;

    public TestAgentsModel() {

    }

    public TestAgentsModel(String agent, String position, String attendance, String firstname,
                           String lastname, String IDNumber, String gender, String primaryMobile,
                           String alternateMobile, String postalAddress, String residentialAddress) {
        this.agent = agent;
        this.position = position;
        this.attendance = attendance;
        this.firstname = firstname;
        this.lastname = lastname;
        this.IDNumber = IDNumber;
        this.gender = gender;
        this.primaryMobile = primaryMobile;
        this.alternateMobile = alternateMobile;
        this.postalAddress = postalAddress;
        this.residentialAddress = residentialAddress;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
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

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
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

    @Override
    public String toString() {
        return "AgentsModel{" +
                "agent='" + agent + '\'' +
                ", position='" + position + '\'' +
                ", attendance='" + attendance + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", IDNumber='" + IDNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", primaryMobile='" + primaryMobile + '\'' +
                ", alternateMobile='" + alternateMobile + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                '}';
    }

}
