package com.application.iserv.ui.participants.models;

public class AttendanceModel {

    int daysWorked;
    Long participantId;

    public AttendanceModel() {

    }

    public AttendanceModel(int daysWorked, Long participantId) {
        this.daysWorked = daysWorked;
        this.participantId = participantId;
    }

    public int getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(int daysWorked) {
        this.daysWorked = daysWorked;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    @Override
    public String toString() {
        return "AttendanceModel{" +
                "daysWorked=" + daysWorked +
                ", participantId=" + participantId +
                '}';
    }

}
