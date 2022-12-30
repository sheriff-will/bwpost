package com.application.iserv.backend.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.APPROVED;
import static com.application.iserv.ui.utils.Constants.DENIED;

@Repository
@Transactional
public class HistoryRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Object[]> retrieveAllHistory(String date) {

        String sql = "SELECT remuneration_history.remuneration_history_id, remuneration_history.status, " +
                "remuneration_history.status_reason, remuneration_history.claimed, " +
                "remuneration_history.bonus_amount, remuneration_history.bonus_reason, " +
                "remuneration_history.deduction_amount, remuneration_history.deduction_reason, " +
                "remuneration_history.participant_id, participants.firstname, participants.lastname, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, " +
                "parameters.rate_per_day, attendance_history.days_worked " +
                "FROM remuneration_history, participants, parameters, attendance_history " +
                "WHERE remuneration_history.participant_id = participants.participant_id " +
                "AND participants.participant_id = attendance_history.participant_id " +
                "AND participants.parameter_id = parameters.parameter_id " +
                "AND (remuneration_history.status = '"+APPROVED+"' " +
                "OR remuneration_history.status = '"+ DENIED +"') " +
                "AND remuneration_history.month = '"+date+"' " +
                "AND attendance_history.date = '"+date+"' " +
                "AND participants.is_terminated = '0'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> exportStatements(Long participantId) {

        String sql = "SELECT remuneration_history.month," +
                " remuneration_history.bonus_amount, remuneration_history.deduction_amount," +
                " parameters.rate_per_day, attendance_history.days_worked, " +
                "participants.firstname, participants.lastname, participants.identity_number " +
                "FROM remuneration_history, parameters, participants, attendance_history " +
                "WHERE remuneration_history.participant_id = '"+participantId+"' " +
                "AND participants.participant_id = remuneration_history.participant_id " +
                "AND parameters.parameter_id = participants.parameter_id " +
                "AND remuneration_history.participant_id = attendance_history.participant_id " +
                "AND attendance_history.date = remuneration_history.month";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> searchHistory(String agentNames, String date) {

        String sql = "SELECT remuneration_history.remuneration_history_id, remuneration_history.status, " +
                "remuneration_history.status_reason, remuneration_history.claimed, " +
                "remuneration_history.bonus_amount, remuneration_history.bonus_reason, " +
                "remuneration_history.deduction_amount, remuneration_history.deduction_reason, " +
                "remuneration_history.participant_id, participants.firstname, participants.lastname, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, " +
                "parameters.rate_per_day, attendance_history.days_worked " +
                "FROM remuneration_history, participants, parameters, attendance_history " +
                "WHERE (participants.firstname LIKE '%"+agentNames+"%' " +
                "OR participants.lastname LIKE '%"+agentNames+"%') " +
                "AND remuneration_history.participant_id = participants.participant_id " +
                "AND participants.participant_id = attendance_history.participant_id " +
                "AND participants.parameter_id = parameters.parameter_id " +
                "AND (remuneration_history.status = '"+APPROVED+"' " +
                "OR remuneration_history.status = '"+ DENIED +"') " +
                "AND remuneration_history.month = '"+date+"' " +
                "AND attendance_history.date = '"+date+"' " +
                "AND participants.is_terminated = '0'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> filterHistoryByPlace(String date, String place) {

        String sql = "SELECT remuneration_history.remuneration_history_id, remuneration_history.status, " +
                "remuneration_history.status_reason, remuneration_history.claimed, " +
                "remuneration_history.bonus_amount, remuneration_history.bonus_reason, " +
                "remuneration_history.deduction_amount, remuneration_history.deduction_reason, " +
                "remuneration_history.participant_id, participants.firstname, participants.lastname, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, " +
                "parameters.rate_per_day, attendance_history.days_worked " +
                "FROM remuneration_history, participants, parameters, attendance_history " +
                "WHERE remuneration_history.participant_id = participants.participant_id " +
                "AND participants.participant_id = attendance_history.participant_id " +
                "AND participants.parameter_id = parameters.parameter_id " +
                "AND (remuneration_history.status = '"+APPROVED+"' " +
                "OR remuneration_history.status = '"+ DENIED +"') " +
                "AND remuneration_history.month = '"+date+"' " +
                "AND attendance_history.date = '"+date+"' " +
                "AND parameters.village = '"+place+"' " +
                "AND participants.is_terminated = '0'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
