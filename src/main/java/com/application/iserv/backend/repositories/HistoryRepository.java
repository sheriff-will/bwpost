package com.application.iserv.backend.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.APPROVED;
import static com.application.iserv.ui.utils.Constants.DECLINED;

@Repository
@Transactional
public class HistoryRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Object[]> retrieveAllHistory() {
        String sql = "SELECT remuneration_history.remuneration_history_id, remuneration_history.month, " +
                "remuneration_history.status, remuneration_history.status_reason, remuneration_history.claimed," +
                " remuneration_history.bonus_amount, remuneration_history.bonus_reason, " +
                "remuneration_history.deduction_amount, remuneration_history.deduction_reason, " +
                "remuneration_history.participant_id, participants.firstname, participants.lastname, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number " +
                "FROM remuneration_history, participants WHERE " +
                "remuneration_history.participant_id = participants.participant_id" +
                " AND (remuneration_history.status = '"+APPROVED+"' " +
                "OR remuneration_history.status = '"+ DECLINED +"')";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
