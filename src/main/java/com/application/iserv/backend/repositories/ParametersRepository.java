package com.application.iserv.backend.repositories;

import com.application.iserv.ui.parameters.models.ParametersModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class ParametersRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Object[]> checkForParameter(ParametersModel parametersModel) {
        String sql = "SELECT parameters.parameter_id, parameters.position, parameters.rate_per_day " +
                "FROM parameters" +
                " WHERE parameters.position = '"+parametersModel.getPosition()+"' " +
                "AND parameters.village = 'Oodi' AND parameters.service = 'Ipelegeng'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Object[]> checkUpdateParameter(ParametersModel parametersModel) {

        // TODO Remove hardcoded village and service
        String sql = "SELECT parameters.parameter_id, parameters.position, parameters.rate_per_day " +
                "FROM parameters" +
                " WHERE parameters.village = 'Oodi'" +
                " AND parameters.service = 'Ipelegeng'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Modifying
    public void addParameter(ParametersModel parametersModel) {
        try {

            // TODO Replace hardcoded district, village, service

            String insertParameterSQL = "INSERT INTO parameters (rate_per_day, position, district, " +
                    "village, service) VALUES(" +
                    "'" + parametersModel.getRatePerDay() + "','" + parametersModel.getPosition() + "'," +
                    "'Kgatleng','Oodi', 'Ipelegeng')";

            Query insertParameterQuery = entityManager.createNativeQuery(insertParameterSQL);
            insertParameterQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Object[]> getParameters() {

        // TODO Remove hardcoded village, service
        String sql = "SELECT parameters.parameter_id, parameters.position, parameters.rate_per_day " +
                "FROM parameters " +
                "WHERE parameters.village = 'Oodi' AND parameters.service = 'Ipelegeng'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Modifying
    public void updateParameter(ParametersModel parametersModel) {
        try {

            String updateParameterSQL = "UPDATE parameters SET " +
                    "rate_per_day = '" + parametersModel.getRatePerDay() + "'," +
                    " position = '" + parametersModel.getPosition() + "'" +
                    " WHERE parameter_id = '"+parametersModel.getParameterId()+"' " +
                    "AND village = 'Oodi' AND service = 'Ipelegeng'";

            Query updateParameterQuery = entityManager.createNativeQuery(updateParameterSQL);
            updateParameterQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Modifying
    public void deleteParameter(Long parameterId) {
        try {

            String deleteParameterSQL = "DELETE FROM parameters WHERE parameters.parameter_id = '"+parameterId+"'";

            Query deleteParameterQuery = entityManager.createNativeQuery(deleteParameterSQL);
            deleteParameterQuery.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}