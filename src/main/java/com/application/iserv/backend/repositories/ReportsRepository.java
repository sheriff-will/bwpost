package com.application.iserv.backend.repositories;

import com.application.iserv.ui.utils.ApplicationUserDataModel;
import com.application.iserv.ui.utils.SessionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class ReportsRepository {

    // SessionManager
    SessionManager sessionManager = new SessionManager();

    // ApplicationUser
    ApplicationUserDataModel applicationUserDataModel;

    @PersistenceContext
    EntityManager entityManager;

    public List<String> getPaymentStatuses(String month) {

        applicationUserDataModel = sessionManager.getApplicationUserData();

        String sql = "SELECT remuneration_history.status " +
                "FROM remuneration_history, participants, parameters " +
                "WHERE participants.participant_id = remuneration_history.participant_id " +
                "AND participants.parameter_id = parameters.parameter_id " +
                "AND parameters.district = '"+applicationUserDataModel.getDistrict()+"' " +
                "AND parameters.village = '"+applicationUserDataModel.getVillage()+"' " +
                "AND parameters.service = '"+applicationUserDataModel.getService()+"' " +
                "AND remuneration_history.month = '"+month+"'";
        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<String>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> getContractStatus() {

        applicationUserDataModel = sessionManager.getApplicationUserData();

        String sql = "SELECT participants.completion_date, participants.is_terminated " +
                "FROM participants, parameters " +
                "WHERE participants.parameter_id = parameters.parameter_id " +
                "AND parameters.district = '"+applicationUserDataModel.getDistrict()+"' " +
                "AND parameters.village = '"+applicationUserDataModel.getVillage()+"' " +
                "AND parameters.service = '"+applicationUserDataModel.getService()+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> getAttendance(String date) {

        applicationUserDataModel = sessionManager.getApplicationUserData();

        String sql = "SELECT attendance_history.days_worked " +
                "FROM attendance_history " +
                "WHERE attendance_history.date = '"+date+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<String>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
