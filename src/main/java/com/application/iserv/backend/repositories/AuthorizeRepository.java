package com.application.iserv.backend.repositories;

import com.application.iserv.ui.payments.models.AuthorizeModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class AuthorizeRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Object[]> retrieveAllRemunerationHistory() {
        String sql = "SELECT remuneration_history.remuneration_history_id, remuneration_history.month, " +
                "remuneration_history.status, remuneration_history.status_reason, remuneration_history.claimed," +
                " remuneration_history.bonus_amount, remuneration_history.bonus_reason, " +
                "remuneration_history.deduction_amount, remuneration_history.deduction_reason, " +
                "remuneration_history.participant_id, participants.firstname, participants.lastname " +
                "FROM remuneration_history, participants WHERE " +
                "remuneration_history.participant_id = participants.participant_id";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> searchForRemunerationAgents(String agentNames, Long statusValue) {
        String sql = "SELECT remuneration_history.remuneration_history_id, remuneration_history.month, " +
                "remuneration_history.status, remuneration_history.status_reason, remuneration_history.claimed," +
                " remuneration_history.bonus_amount, remuneration_history.bonus_reason, " +
                "remuneration_history.deduction_amount, remuneration_history.deduction_reason, " +
                "remuneration_history.participant_id, participants.firstname, participants.lastname " +
                "FROM remuneration_history, participants WHERE " +
                "(participants.firstname LIKE '%"+agentNames+"%' OR participants.lastname LIKE '%"+agentNames+"%') " +
                "AND remuneration_history.participant_id = participants.participant_id";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Modifying
    public void updateRemunerationDetails(AuthorizeModel authorizeModel) {
        try {

            String updateAgentSQL = "UPDATE remuneration_history SET " +
                    "month = '" + authorizeModel.getMonth() + "'," +
                    " status = '" + authorizeModel.getStatus() + "'," +
                    " status_reason = '" + authorizeModel.getStatusReason() + "'," +
                    " claimed = '" + authorizeModel.getClaimed() + "'," +
                    " bonus_amount = '" + authorizeModel.getBonusAmount() + "'," +
                    " bonus_reason = '" + authorizeModel.getBonusReason() + "'," +
                    " deduction_amount = '" + authorizeModel.getDeductionAmount() + "'," +
                    " deduction_reason = '" + authorizeModel.getDeductionReason() + "'," +
                    " participant_id = '" + authorizeModel.getParticipantId() + "'" +
                    " WHERE remuneration_history_id = '" + authorizeModel.getRemunerationHistoryId() + "'";

            Query updateAgentQuery = entityManager.createNativeQuery(updateAgentSQL);
            updateAgentQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
