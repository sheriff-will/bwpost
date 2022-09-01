package com.application.iserv.backend.repositories;

import com.application.iserv.ui.agents.models.AgentsModel;
import com.application.iserv.ui.agents.models.NomineesModel;
import com.application.iserv.ui.agents.models.ReferenceModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class AgentsRepository {

    @PersistenceContext
    EntityManager entityManager;

    // Agent
    public List<Object[]> retrieveAllAgents() {
        String sql = "SELECT * FROM participants WHERE is_terminated = '0'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> retrieveAllNominees() {
        String sql = "SELECT * FROM nominees WHERE nominees.removed = 0";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> retrieveAllReferences() {
        String sql = "SELECT * FROM reference WHERE reference.removed = 0";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Modifying
    public void updateAgentDetails(AgentsModel agentsModel) {
        try {

            String updateAgentSQL = "UPDATE participants SET " +
                    "firstname = '" + agentsModel.getFirstname() + "'," +
                    " lastname = '" + agentsModel.getLastname() + "'," +
                    " identity_number = '" + agentsModel.getIdentityNumber() + "'," +
                    " date_of_birth = '" + agentsModel.getDateOfBirth() + "'," +
                    " gender = '" + agentsModel.getGender() + "'," +
                    " marital_status = '" + agentsModel.getMaritalStatus() + "'," +
                    " mobile_number = '" + agentsModel.getMobileNumber() + "'," +
                    " alternate_mobile_number = '" + agentsModel.getAlternateMobileNumber() + "'," +
                    " postal_address = '" + agentsModel.getPostalAddress() + "'," +
                    " residential_address = '" + agentsModel.getResidentialAddress() + "'," +
                    " education = '" + agentsModel.getEducation() + "'," +
                    " placement_officer = '" + agentsModel.getPlacementOfficer() + "'," +
                    " placement_place = '" + agentsModel.getPlacementPlace() + "'," +
                    " position = '" + agentsModel.getPosition() + "'," +
                    " placement_date = '" + agentsModel.getPlacementDate() + "'," +
                    " completion_date = '" + agentsModel.getCompletionDate() + "'," +
                    " mobile_wallet_provider = '" + agentsModel.getMobileWalletProvider() + "'," +
                    " bank_name = '" + agentsModel.getBankName() + "'," +
                    " branch = '" + agentsModel.getBranch() + "'," +
                    " account_number = '" + agentsModel.getAccountNumber() + "'," +
                    " timestamp = '" + agentsModel.getTimestamp() + "'," +
                    " district = '" + agentsModel.getDistrict() + "'," +
                    " village = '" + agentsModel.getVillage() + "'," +
                    " service = '" + agentsModel.getService() + "'," +
                    " is_terminated = '0'" +
                    " WHERE participant_id = '" + agentsModel.getParticipantId() + "'";

            Query updateAgentQuery = entityManager.createNativeQuery(updateAgentSQL);
            updateAgentQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Object[]> checkForAgent(AgentsModel agentsModel) {

        String sql = "SELECT participants.participant_id " +
                "FROM participants " +
                "WHERE participants.identity_number = '" + agentsModel.getIdentityNumber() + "' " +
                "AND participants.is_terminated = '0'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    // Nominee
    @Modifying
    public void addNewNominee(NomineesModel nomineesModel) {
        try {

            String insertNomineeSQL = "INSERT INTO nominees (firstname, lastname, identity_number, " +
                    "relationship, mobile_number, postal_address, participant_id, removed) VALUES(" +
                    "'" + nomineesModel.getFirstname() + "','" + nomineesModel.getLastname() + "'," +
                    "'" + nomineesModel.getIdentityNumber() + "','" + nomineesModel.getRelationship() + "'," +
                    "'" + nomineesModel.getPrimaryMobile() + "','" + nomineesModel.getPostalAddress() + "'," +
                    "'" + nomineesModel.getParticipantId() + "', '0')";

            Query insertNomineeQuery = entityManager.createNativeQuery(insertNomineeSQL);
            insertNomineeQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Object[]> checkForNominee(NomineesModel nomineesModel) {

        String sql = "SELECT nominees.nominee_id " +
                "FROM nominees " +
                "WHERE nominees.identity_number = '" + nomineesModel.getIdentityNumber() + "' " +
                "AND nominees.participant_id = '" + nomineesModel.getParticipantId() + "'" +
                "AND nominees.removed = '0'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Modifying
    public void updateNomineeDetails(NomineesModel nomineesModel) {
        try {

            String updateNomineeSQL = "UPDATE nominees SET " +
                    "firstname = '" + nomineesModel.getFirstname() + "'," +
                    " lastname = '" + nomineesModel.getLastname() + "'," +
                    " identity_number = '" + nomineesModel.getIdentityNumber() + "'," +
                    " relationship = '" + nomineesModel.getRelationship() + "'," +
                    " mobile_number = '" + nomineesModel.getPrimaryMobile() + "'," +
                    " postal_address = '" + nomineesModel.getPostalAddress() + "'," +
                    " participant_id = '" + nomineesModel.getParticipantId() + "'," +
                    " removed = '0'" +
                    " WHERE nominee_id = '" + nomineesModel.getNomineeId() + "'";

            Query updateNomineeQuery = entityManager.createNativeQuery(updateNomineeSQL);
            updateNomineeQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Modifying
    public void removeNomineeDetails(Long nomineeId) {
        try {

            String updateNomineeSQL = "UPDATE nominees SET " +
                    " removed = '1'" +
                    " WHERE nominee_id = '" + nomineeId + "'";

            Query updateNomineeQuery = entityManager.createNativeQuery(updateNomineeSQL);
            updateNomineeQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    // Reference
    @Modifying
    public void addNewReference(ReferenceModel referenceModel) {
        try {

            String insertReferenceSQL = "INSERT INTO reference (firstname, lastname, identity_number, " +
                    "mobile_number, postal_address, participant_id, removed) VALUES(" +
                    "'" + referenceModel.getFirstname() + "'," +
                    "'" + referenceModel.getLastname() + "'," +
                    "'" + referenceModel.getIdentityNumber() + "'," +
                    "'" + referenceModel.getPrimaryMobile() + "'," +
                    "'" + referenceModel.getPostalAddress() + "'," +
                    "'" + referenceModel.getParticipantId() + "'," +
                    " '0')";

            Query insertReferenceQuery = entityManager.createNativeQuery(insertReferenceSQL);
            insertReferenceQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Object[]> checkForReference(ReferenceModel referenceModel) {

        String sql = "SELECT reference.reference_id " +
                "FROM reference " +
                "WHERE reference.identity_number = '" + referenceModel.getIdentityNumber() + "' " +
                "AND reference.participant_id = '" + referenceModel.getParticipantId() + "'" +
                "AND reference.removed = '0'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Modifying
    public void updateReferenceDetails(ReferenceModel referenceModel) {
        try {

            String updateReferenceSQL = "UPDATE reference SET " +
                    "firstname = '" + referenceModel.getFirstname() + "'," +
                    " lastname = '" + referenceModel.getLastname() + "'," +
                    " identity_number = '" + referenceModel.getIdentityNumber() + "'," +
                    " mobile_number = '" + referenceModel.getPrimaryMobile() + "'," +
                    " postal_address = '" + referenceModel.getPostalAddress() + "'," +
                    " participant_id = '" + referenceModel.getParticipantId() + "'," +
                    " removed = '0'" +
                    " WHERE reference_id = '" + referenceModel.getReferenceId() + "'";

            Query updateReferenceQuery = entityManager.createNativeQuery(updateReferenceSQL);
            updateReferenceQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Modifying
    public void removeReferenceDetails(Long referenceId) {
        try {

            String updateReferenceSQL = "UPDATE reference SET " +
                    " removed = '1'" +
                    " WHERE reference_id = '" + referenceId + "'";

            Query updateReferenceQuery = entityManager.createNativeQuery(updateReferenceSQL);
            updateReferenceQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


}
