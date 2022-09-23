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
        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service " +
                "FROM participants, parameters" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND participants.is_terminated = '0'";

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
                    " placement_date = '" + agentsModel.getPlacementDate() + "'," +
                    " completion_date = '" + agentsModel.getCompletionDate() + "'," +
                    " mobile_wallet_provider = '" + agentsModel.getMobileWalletProvider() + "'," +
                    " bank_name = '" + agentsModel.getBankName() + "'," +
                    " branch = '" + agentsModel.getBranch() + "'," +
                    " account_number = '" + agentsModel.getAccountNumber() + "'," +
                    " timestamp = '" + agentsModel.getTimestamp() + "'," +
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

    @Modifying
    public void terminateAgentDetails(Long participantId) {
        try {

            String updateParticipantSQL = "UPDATE participants SET " +
                    " is_terminated = '1'" +
                    " WHERE participant_id = '" + participantId + "'";

            Query updateParticipantQuery = entityManager.createNativeQuery(updateParticipantSQL);
            updateParticipantQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Modifying
    public void addNewAgent(AgentsModel agentsModel, List<String> contractDates) {
        try {

            // TODO Replace hardcoded parameterId

            // Insert to participants
            String insertAgentSQL = "INSERT INTO participants (firstname, lastname, identity_number, " +
                    "date_of_birth, gender, marital_status, mobile_number, alternate_mobile_number," +
                    "postal_address, residential_address, education, placement_officer, placement_place, " +
                    "placement_date, completion_date, mobile_wallet_provider, bank_name, branch, " +
                    "account_number, timestamp, is_terminated, parameter_id) VALUES(" +
                    "'" + agentsModel.getFirstname() + "','" + agentsModel.getLastname() + "'," +
                    "'" + agentsModel.getIdentityNumber() + "','" + agentsModel.getDateOfBirth() + "'," +
                    "'" + agentsModel.getGender() + "','" + agentsModel.getMaritalStatus() + "'," +
                    "'" + agentsModel.getMobileNumber() + "','" + agentsModel.getAlternateMobileNumber() + "'," +
                    "'" + agentsModel.getPostalAddress() + "','" + agentsModel.getResidentialAddress() + "'," +
                    "'" + agentsModel.getEducation() + "','" + agentsModel.getPlacementOfficer() + "'," +
                    "'" + agentsModel.getPlacementPlace() + "'," +
                    "'" + agentsModel.getPlacementDate() + "','" + agentsModel.getCompletionDate() + "'," +
                    "'" + agentsModel.getMobileWalletProvider() + "','" + agentsModel.getBankName() + "'," +
                    "'" + agentsModel.getBranch() + "','" + agentsModel.getAccountNumber() + "'," +
                    "'" + agentsModel.getTimestamp() + "','0', '1')";

            Query insertAgentQuery = entityManager.createNativeQuery(insertAgentSQL);
            insertAgentQuery.executeUpdate();

            // Select participantId
            String sql = "SELECT participants.participant_id " +
                    "FROM participants " +
                    "WHERE participants.identity_number = '"+agentsModel.getIdentityNumber()+"'";

            Query query = entityManager.createNativeQuery(sql);

            // Insert to attendance_history
            for (int i = 0; i < contractDates.size(); i++) {
                String date = contractDates.get(i);
                String insertAttendanceSQL = "INSERT INTO attendance_history " +
                        "(date, days_worked, participant_id) VALUES(" +
                        "'" + date + "','20', '" + query.getResultList().get(0)+"')";

                Query insertAttendanceQuery = entityManager.createNativeQuery(insertAttendanceSQL);
                insertAttendanceQuery.executeUpdate();

            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Object[]> retrieveAllTerminatedAgents() {

        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service " +
                "FROM participants, parameters" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND participants.is_terminated = '1'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> searchForAgents(String agentNames, Long statusValue) {
        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service " +
                "FROM participants, parameters" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND (participants.firstname LIKE '%"+agentNames+"%' OR participants.lastname LIKE '%"+agentNames+"%') AND participants.is_terminated = '"+statusValue+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> retrieveDuration(String district) {

        // TODO Remove hardcoded district 'Kgatleng'
        String sql = "SELECT contract_duration.duration " +
                "FROM contract_duration " +
                "WHERE contract_duration.district = '"+district+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<String>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    // Nominee
    @Modifying
    public void addNewNominee(NomineesModel nomineesModel) {
        try {

            String insertNomineeSQL = "INSERT INTO nominees (firstname, lastname, identity_number, " +
                    "relationship, primary_number, postal_address, participant_id, removed) VALUES(" +
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
                    " primary_number = '" + nomineesModel.getPrimaryMobile() + "'," +
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
                    "primary_number, postal_address, participant_id, removed) VALUES(" +
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
                    " primary_number = '" + referenceModel.getPrimaryMobile() + "'," +
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


    // Attendance
    public List<Object[]> retrieveAttendance(String date) {
        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service, attendance_history.days_worked " +
                "FROM participants, parameters, attendance_history" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND participants.is_terminated = '0'" +
                "AND participants.participant_id = attendance_history.participant_id " +
                "AND attendance_history.date = '"+date+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> searchAttendance(String date, String agentNames, Long statusValue) {
        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service, attendance_history.days_worked " +
                "FROM participants, parameters, attendance_history" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND participants.is_terminated = '0'" +
                "AND participants.participant_id = attendance_history.participant_id " +
                "AND attendance_history.date = '"+date+"' " +
                "AND (participants.firstname LIKE '%"+agentNames+"%'" +
                " OR participants.lastname LIKE '%"+agentNames+"%') " +
                "AND participants.is_terminated = '"+statusValue+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
