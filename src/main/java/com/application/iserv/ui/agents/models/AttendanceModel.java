package com.application.iserv.ui.agents.models;

import java.time.LocalDate;

public class AttendanceModel {

    String status;
    LocalDate date;
    Long attendanceId, participantId;

    public AttendanceModel() {

    }

    public AttendanceModel(String status, LocalDate date, Long attendanceId, Long participantId) {
        this.status = status;
        this.date = date;
        this.attendanceId = attendanceId;
        this.participantId = participantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
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
                "status='" + status + '\'' +
                ", date=" + date +
                ", attendanceId=" + attendanceId +
                ", participantId=" + participantId +
                '}';
    }

}
