package com.application.iserv.security.Repositories;

import com.application.iserv.security.models.RegisterAgentModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class CredentialsRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Modifying
    public void registerAgent(RegisterAgentModel registerAgentModel) {
        try {

            String insertAgentSQL = "INSERT INTO application_user (firstname, lastname, identity_number, " +
                    "username, password, role, is_account_non_expired, is_account_non_locked," +
                    "is_credentials_non_expired, is_enabled, district, village, service) VALUES(" +
                    "'" + registerAgentModel.getFirstname() + "','" + registerAgentModel.getLastname() + "'," +
                    "'" + registerAgentModel.getIdentity_number() + "','" + registerAgentModel.getUsername() + "'," +
                    "'" + registerAgentModel.getPassword() + "','" + registerAgentModel.getRole() + "'," +
                    "'" + registerAgentModel.getIs_account_expired() + "','" + registerAgentModel.getIs_account_locked() + "'," +
                    "'" + registerAgentModel.getIs_credentials_expired() + "','" + registerAgentModel.getIs_account_disabled() + "'," +
                    "'" + registerAgentModel.getDistrict() + "','" + registerAgentModel.getVillage() + "'," +
                    "'" + registerAgentModel.getService() + "')";

            Query insertAgentQuery = entityManager.createNativeQuery(insertAgentSQL);
            insertAgentQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Object[]> checkForUser(String username) {

        String sql = "SELECT application_user.firstname, application_user.lastname " +
                "FROM application_user " +
                "WHERE application_user.username = '"+username+"'" +
                "AND application_user.password = 'null'";
        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Modifying
    public void updatePassword(String username, String password) {
        try {

            String sql = "UPDATE application_user SET " +
                    "password = '" + password + "' " +
                    "WHERE application_user.username = '"+username+"'";

            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
