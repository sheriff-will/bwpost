package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.AuthorizeRepository;
import com.application.iserv.ui.payments.models.AuthorizeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizeService {

    private final AuthorizeRepository authorizeRepository;

    @Autowired
    public AuthorizeService(AuthorizeRepository authorizeRepository) {
        this.authorizeRepository = authorizeRepository;
    }

    public List<AuthorizeModel> getAllRemunerationHistory() {
        List<Object[]> allRemunerationHistory = authorizeRepository.retrieveAllRemunerationHistory();

        List<AuthorizeModel> remunerationHistory = new ArrayList<>();

        for(Object[] row : allRemunerationHistory) {

            String month_str = row[1].toString();
            String[] getMonth = month_str.split("-");

            LocalDate month = LocalDate.of(
                    Integer.parseInt(getMonth[0]),
                    Integer.parseInt(getMonth[1]),
                    Integer.parseInt(getMonth[2])
            );

            AuthorizeModel authorizeModel = new AuthorizeModel(
                    month,
                    Long.parseLong(row[0].toString()),
                    Long.parseLong(row[9].toString()),
                    row[2].toString(),
                    row[3].toString(),
                    row[4].toString(),
                    row[5].toString(),
                    row[6].toString(),
                    row[7].toString(),
                    row[8].toString(),
                    row[10].toString()+" "+row[11].toString(),
                    "auto - generated"
            );

            remunerationHistory.add(authorizeModel);

        }

        return remunerationHistory;
    }

    public void updateRemuneration(AuthorizeModel authorizeModel) {
        authorizeRepository.updateRemunerationDetails(authorizeModel);
    }

    public List<AuthorizeModel> searchRemunerationAgents(String agentNames, Long statusValue) {
        List<Object[]> allRemunerationHistory = authorizeRepository
                .searchForRemunerationAgents(agentNames, statusValue);

        List<AuthorizeModel> remunerationHistory = new ArrayList<>();

        for(Object[] row : allRemunerationHistory) {

            String month_str = row[1].toString();
            String[] getMonth = month_str.split("-");

            LocalDate month = LocalDate.of(
                    Integer.parseInt(getMonth[0]),
                    Integer.parseInt(getMonth[1]),
                    Integer.parseInt(getMonth[2])
            );

            AuthorizeModel authorizeModel = new AuthorizeModel(
                    month,
                    Long.parseLong(row[0].toString()),
                    Long.parseLong(row[9].toString()),
                    row[2].toString(),
                    row[3].toString(),
                    row[4].toString(),
                    row[5].toString(),
                    row[6].toString(),
                    row[7].toString(),
                    row[8].toString(),
                    row[10].toString()+" "+row[11].toString(),
                    "auto - generated"
            );

            remunerationHistory.add(authorizeModel);

        }

        return remunerationHistory;
    }

}
