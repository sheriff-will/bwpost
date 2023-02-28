package com.application.iserv.backend.repositories;

import com.application.iserv.ui.participants.models.NomineesModel;
import com.application.iserv.ui.participants.models.EmployeesModel;
import com.application.iserv.ui.participants.models.ReferenceModel;
import com.application.iserv.ui.utils.ApplicationUserDataModel;
import com.application.iserv.ui.utils.Commons;
import com.application.iserv.ui.utils.SessionManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class ParticipantsRepository {

    // SessionManager
    SessionManager sessionManager = new SessionManager();

    // ApplicationUser
    ApplicationUserDataModel applicationUserDataModel;

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
                "parameters.village, parameters.service, parameters.duration " +
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
    public void updateAgentDetails(EmployeesModel employeesModel) {
        try {

            List<BigInteger> parameterId = getParameterId(employeesModel.getPosition());

            String updateAgentSQL = "UPDATE participants SET " +
                    "firstname = '" + employeesModel.getFirstname() + "'," +
                    " lastname = '" + employeesModel.getLastname() + "'," +
                    " identity_number = '" + employeesModel.getIdentityNumber() + "'," +
                    " date_of_birth = '" + employeesModel.getDateOfBirth() + "'," +
                    " gender = '" + employeesModel.getGender() + "'," +
                    " marital_status = '" + employeesModel.getMaritalStatus() + "'," +
                    " mobile_number = '" + employeesModel.getMobileNumber() + "'," +
                    " alternate_mobile_number = '" + employeesModel.getAlternateMobileNumber() + "'," +
                    " postal_address = '" + employeesModel.getPostalAddress() + "'," +
                    " residential_address = '" + employeesModel.getResidentialAddress() + "'," +
                    " education = '" + employeesModel.getEducation() + "'," +
                    " placement_officer = '" + employeesModel.getPlacementOfficer() + "'," +
                    " placement_place = '" + employeesModel.getPlacementPlace() + "'," +
                    " placement_date = '" + employeesModel.getPlacementDate() + "'," +
                    " completion_date = '" + employeesModel.getCompletionDate() + "'," +
                    " mobile_wallet_provider = '" + employeesModel.getMobileWalletProvider() + "'," +
                    " bank_name = '" + employeesModel.getBankName() + "'," +
                    " branch = '" + employeesModel.getBranch() + "'," +
                    " account_number = '" + employeesModel.getAccountNumber() + "'," +
                    " timestamp = '" + employeesModel.getTimestamp() + "'," +
                    " is_terminated = '0'," +
                    " parameter_id = '"+parameterId.get(0) + "' "+
                    " WHERE participant_id = '" + employeesModel.getParticipantId() + "'";

            Query updateAgentQuery = entityManager.createNativeQuery(updateAgentSQL);
            updateAgentQuery.executeUpdate();

            String sql = "SELECT remuneration_history.month " +
                    "FROM remuneration_history " +
                    "WHERE remuneration_history.participant_id = '"+ employeesModel.getParticipantId()+"'";

            Query query = entityManager.createNativeQuery(sql);
            List<String> resultList = query.getResultList();

            for (int i = 0; i < resultList.size(); i++) {
                String[] getDate = resultList.get(i).split("-");

                // TODO 1 is magic number
                LocalDate remunerationDate = LocalDate.of(
                        Integer.parseInt(getDate[0]),
                        Integer.parseInt(getDate[1]),
                        LocalDate.now().getDayOfMonth()
                );

                int compare = remunerationDate.compareTo(LocalDate.now());

                if (compare >= 0) {

                    String provider = "";
                    String paymentMode = "";

                    if (!employeesModel.getMobileWalletProvider().equalsIgnoreCase("null")) {
                        provider = Commons.getPhoneNumberCarrier(employeesModel.getMobileWalletProvider());

                        if (provider.equalsIgnoreCase("")) {
                            provider = "No Provider";
                        }

                        paymentMode = "Mobile Wallet";
                    }
                    else if (!employeesModel.getBankName().equalsIgnoreCase("null")) {
                        provider = employeesModel.getBankName();
                        paymentMode = "Bank";
                    }
                    else if (employeesModel.getMobileWalletProvider().equalsIgnoreCase("null")
                            && employeesModel.getBankName().equalsIgnoreCase("null")) {
                        provider = "Botswana Post Office"; // TODO Remove hardcoded Botswana Post Office
                        paymentMode = "Cash";
                    }

                    String updateRemunerationHistorySQL = "UPDATE remuneration_history SET " +
                            "payment_method = '" + paymentMode + "', " +
                            "provider = '" + provider + "' " +
                            "WHERE participant_id = '" + employeesModel.getParticipantId() + "' " +
                            "AND remuneration_history.month = '"+resultList.get(i)+"'";

                    Query updateRemunerationHistoryQuery = entityManager
                            .createNativeQuery(updateRemunerationHistorySQL);
                    updateRemunerationHistoryQuery.executeUpdate();

                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Object[]> checkForAgent(EmployeesModel employeesModel) {

        String sql = "SELECT participants.participant_id " +
                "FROM participants " +
                "WHERE participants.identity_number = '" + employeesModel.getIdentityNumber() + "' " +
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
    public void addNewAgent(EmployeesModel employeesModel,
                            List<String> contractDates, BigInteger parameterId) {
        try {

            // Insert to participants
            String insertAgentSQL = "INSERT INTO participants (firstname, lastname, identity_number, " +
                    "date_of_birth, gender, marital_status, mobile_number, alternate_mobile_number, " +
                    "postal_address, residential_address, education, placement_officer, placement_place, " +
                    "placement_date, completion_date, mobile_wallet_provider, bank_name, branch, " +
                    "account_number, timestamp, is_terminated, parameter_id) VALUES(" +
                    "'" + employeesModel.getFirstname() + "','" + employeesModel.getLastname() + "'," +
                    "'" + employeesModel.getIdentityNumber() + "','" + employeesModel.getDateOfBirth() + "'," +
                    "'" + employeesModel.getGender() + "','" + employeesModel.getMaritalStatus() + "'," +
                    "'" + employeesModel.getMobileNumber() + "','" + employeesModel.getAlternateMobileNumber() + "'," +
                    "'" + employeesModel.getPostalAddress() + "','" + employeesModel.getResidentialAddress() + "'," +
                    "'" + employeesModel.getEducation() + "','" + employeesModel.getPlacementOfficer() + "'," +
                    "'" + employeesModel.getPlacementPlace() + "'," +
                    "'" + employeesModel.getPlacementDate() + "','" + employeesModel.getCompletionDate() + "'," +
                    "'" + employeesModel.getMobileWalletProvider() + "','" + employeesModel.getBankName() + "'," +
                    "'" + employeesModel.getBranch() + "','" + employeesModel.getAccountNumber() + "'," +
                    "'" + employeesModel.getTimestamp() + "','0', '"+parameterId+"')";

            Query insertAgentQuery = entityManager.createNativeQuery(insertAgentSQL);
            insertAgentQuery.executeUpdate();

            // Select participantId
            String sql = "SELECT participants.participant_id " +
                    "FROM participants " +
                    "WHERE participants.identity_number = '"+ employeesModel.getIdentityNumber()+"'";

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

            String provider = "";
            String paymentMode = "";

            if (!employeesModel.getMobileWalletProvider().equalsIgnoreCase("null")) {
                provider = Commons.getPhoneNumberCarrier(employeesModel.getMobileWalletProvider());

                if (provider.equalsIgnoreCase("")) {
                    provider = "No Provider";
                }

                paymentMode = "Mobile Wallet";
            }
            else if (!employeesModel.getBankName().equalsIgnoreCase("null")) {
                provider = employeesModel.getBankName();
                paymentMode = "Bank";
            }
            else if (employeesModel.getMobileWalletProvider().equalsIgnoreCase("null")
                    && employeesModel.getBankName().equalsIgnoreCase("null")) {
                provider = "Botswana Post Office"; // TODO Remove hardcoded Botswana Post Office
                paymentMode = "Cash";
            }

            // Insert to remuneration_history
            for (int i = 0; i < contractDates.size(); i++) {
                String month = contractDates.get(i);

                String insertRemunerationSQL = "INSERT INTO remuneration_history (month, status, " +
                        "status_reason, claimed, bonus_amount, bonus_reason, deduction_amount, " +
                        "deduction_reason, payment_method, provider, participant_id) " +
                        "VALUES('"+month+"', 'Pending', 'null', 'No', '0', 'null', '0', 'null', " +
                        "'"+paymentMode+"', '"+provider+"', '"+query.getResultList().get(0)+"')";

                Query insertRemunerationQuery = entityManager.createNativeQuery(insertRemunerationSQL);
                insertRemunerationQuery.executeUpdate();

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
                "parameters.village, parameters.service, parameters.duration " +
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

    public List<Object[]> filterTerminatedAgentsByPlace(String place) {

        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service, parameters.duration " +
                "FROM participants, parameters" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND parameters.village = '"+place+"' " +
                "AND participants.is_terminated = '1'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> filterAgentsByPlace(String place) {

        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service, parameters.duration " +
                "FROM participants, parameters" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND parameters.village = '"+place+"' " +
                "AND participants.is_terminated = '0'";

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
                "parameters.village, parameters.service, parameters.duration " +
                "FROM participants, parameters" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND (participants.firstname LIKE '%"+agentNames+"%' OR participants.lastname LIKE '%"+agentNames+"%')" +
                " AND participants.is_terminated = '"+statusValue+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> retrieveDuration(String district) {

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

    @Modifying
    public void reinstateParticipant(Long participantId,
                                     List<String> contractDates, EmployeesModel employeesModel) {
        try {

            long duration = Long.parseLong(String.valueOf(contractDates.size()));

            String completionDate = LocalDate.now().plusMonths(duration).toString();

            String updateAgentSQL = "UPDATE participants SET " +
                    "placement_date = '" + LocalDate.now() + "', " +
                    "completion_date = '" + completionDate + "' " +
                    "WHERE participant_id = '" + participantId + "'";

            Query updateAgentQuery = entityManager.createNativeQuery(updateAgentSQL);
            updateAgentQuery.executeUpdate();

            // Insert to attendance_history
            for (int i = 0; i < contractDates.size(); i++) {
                String date = contractDates.get(i);
                String insertAttendanceSQL = "INSERT INTO attendance_history " +
                        "(date, days_worked, participant_id) VALUES(" +
                        "'" + date + "','20', '" + participantId +"')";

                Query insertAttendanceQuery = entityManager.createNativeQuery(insertAttendanceSQL);
                insertAttendanceQuery.executeUpdate();

            }

            String provider = "";
            String paymentMode = "";

            if (!employeesModel.getMobileWalletProvider().equalsIgnoreCase("null")) {
                provider = Commons.getPhoneNumberCarrier(employeesModel.getMobileWalletProvider());

                if (provider.equalsIgnoreCase("")) {
                    provider = "No Provider";
                }

                paymentMode = "Mobile Wallet";
            }
            else if (!employeesModel.getBankName().equalsIgnoreCase("null")) {
                provider = employeesModel.getBankName();
                paymentMode = "Bank";
            }
            else if (employeesModel.getMobileWalletProvider().equalsIgnoreCase("null")
                    && employeesModel.getBankName().equalsIgnoreCase("null")) {
                provider = "Botswana Post Office"; // TODO Remove hardcoded Botswana Post Office
                paymentMode = "Cash";
            }

            // Insert to remuneration_history
            for (int i = 0; i < contractDates.size(); i++) {
                String month = contractDates.get(i);

                String insertRemunerationSQL = "INSERT INTO remuneration_history (month, status, " +
                        "status_reason, claimed, bonus_amount, bonus_reason, deduction_amount, " +
                        "deduction_reason, payment_method, provider, participant_id) " +
                        "VALUES('"+month+"', 'Pending', 'null', 'No', '0', 'null', '0', 'null', " +
                        "'"+paymentMode+"', '"+provider+"', '"+participantId+"')";

                Query insertRemunerationQuery = entityManager.createNativeQuery(insertRemunerationSQL);
                insertRemunerationQuery.executeUpdate();

            }

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
                    "relationship, primary_number, postal_address, participant_id, removed) VALUES(" +
                    "'" + referenceModel.getFirstname() + "'," +
                    "'" + referenceModel.getLastname() + "'," +
                    "'" + referenceModel.getIdentityNumber() + "'," +
                    "'" + referenceModel.getRelationship() + "'," +
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
                    " relationship = '" + referenceModel.getRelationship() + "'," +
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
                "parameters.village, parameters.service, parameters.duration, attendance_history.days_worked " +
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

    public List<Object[]> getAttendanceByPlace(String date, String place) {
        String sql = "SELECT participants.participant_id, participants.firstname, " +
                "participants.lastname, participants.identity_number, participants.date_of_birth, " +
                "participants.gender, participants.marital_status, participants.mobile_number," +
                " participants.alternate_mobile_number, participants.postal_address, " +
                "participants.residential_address, participants.education, participants.placement_officer," +
                " participants.placement_place, participants.placement_date, participants.completion_date, " +
                "participants.mobile_wallet_provider, participants.bank_name, participants.branch, " +
                "participants.account_number, participants.timestamp, participants.is_terminated, " +
                "parameters.parameter_id, parameters.rate_per_day, parameters.position, parameters.district, " +
                "parameters.village, parameters.service, parameters.duration, attendance_history.days_worked " +
                "FROM participants, parameters, attendance_history" +
                " WHERE participants.parameter_id = parameters.parameter_id " +
                "AND participants.is_terminated = '0'" +
                "AND participants.participant_id = attendance_history.participant_id " +
                "AND attendance_history.date = '"+date+"' " +
                "AND parameters.village = '"+place+"'";

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
                "parameters.village, parameters.service, parameters.duration, attendance_history.days_worked " +
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

    @Modifying
    public void updateAttendance(Integer daysWorked, Long participantId, String date) {
        try {

            String updateParticipantSQL = "UPDATE attendance_history SET " +
                    "attendance_history.days_worked = '" + daysWorked + "'" +
                    " WHERE attendance_history.participant_id = '" + participantId + "'" +
                    " AND attendance_history.date = '"+date+"'";

            Query updateParticipantQuery = entityManager.createNativeQuery(updateParticipantSQL);
            updateParticipantQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    // Parameter
    public List<BigInteger> getParameterId(String position) {

        applicationUserDataModel = sessionManager.getApplicationUserData();

        String sql = "SELECT parameter_id " +
                "FROM parameters " +
                "WHERE parameters.position = '"+position+"' " +
                "AND parameters.district = '"+applicationUserDataModel.getDistrict()+"' " +
                "AND parameters.village = '"+applicationUserDataModel.getVillage()+"' " +
                "AND parameters.service = '"+applicationUserDataModel.getService()+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<BigInteger>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> getPositions() {

        applicationUserDataModel = sessionManager.getApplicationUserData();

        String sql = "SELECT parameters.position " +
                "FROM parameters " +
                "WHERE district = '"+applicationUserDataModel.getDistrict()+"' " +
                "AND parameters.service = '"+applicationUserDataModel.getService()+"' " +
                "AND parameters.village = '"+applicationUserDataModel.getVillage()+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<String>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
