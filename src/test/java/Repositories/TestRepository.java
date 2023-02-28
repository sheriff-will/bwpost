package Repositories;

import com.application.iserv.ui.participants.models.EmployeesModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class TestRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Modifying
    public void addNewAgent(EmployeesModel employeesModel, List<String> contractDates) {
        try {

            // Insert to participants
            String insertAgentSQL = "INSERT INTO participants (firstname, lastname, identity_number, " +
                    "date_of_birth, gender, marital_status, mobile_number, alternate_mobile_number," +
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
                    "'" + employeesModel.getTimestamp() + "','0', '1')";

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

            // Insert to remuneration_history
            for (int i = 0; i < contractDates.size(); i++) {
                String month = contractDates.get(i);

                String insertRemunerationSQL = "INSERT INTO remuneration_history (month, status, status_reason, " +
                        "claimed, bonus_amount, bonus_reason, deduction_amount, deduction_reason, participant_id) " +
                        "VALUES('"+month+"', 'Hold', 'null', 'No', '0', 'null', '0', 'null', " +
                        "'"+query.getResultList().get(0)+"')";

                Query insertRemunerationQuery = entityManager.createNativeQuery(insertRemunerationSQL);
                insertRemunerationQuery.executeUpdate();

            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
