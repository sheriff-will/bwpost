package com.application.iserv.backend.repositories;

import com.application.iserv.ui.payments.models.ReconcileModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class ReconcileRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Modifying
    public void updateRemunerationDetails(String date, List<ReconcileModel> reconcileModels) {

        try {

            char value = '"';

            for (int i = 0; i < reconcileModels.size(); i++) {

                String identityNumber =  reconcileModels.get(i).getIdentityNumber();

                if (reconcileModels.get(i).getIdentityNumber().contains(String.valueOf(value))) {
                    identityNumber = identityNumber.substring(1, identityNumber.length() - 1);
                }

                String sql = "SELECT participants.participant_id " +
                        "FROM " +
                        "participants " +
                        "WHERE participants.identity_number = '"+identityNumber+"'";

                Query query = entityManager.createNativeQuery(sql);

                // TODO Make sure participant id is not null encase those jokers tried to add someone

                String updateRemunerationSQL = "UPDATE remuneration_history SET " +
                        "claimed = '" +reconcileModels.get(i).getClaimed()+ "' " +
                        "WHERE participant_id = '"+query.getResultList().get(0)+"' " +
                        "AND month = '"+date+"'";

                Query updateRemunerationQuery = entityManager.createNativeQuery(updateRemunerationSQL);
                updateRemunerationQuery.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
