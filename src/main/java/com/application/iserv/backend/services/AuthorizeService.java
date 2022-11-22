package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.AuthorizeRepository;
import com.application.iserv.ui.payments.models.AuthorizeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizeService {

    private final AuthorizeRepository authorizeRepository;

    @Autowired
    public AuthorizeService(AuthorizeRepository authorizeRepository) {
        this.authorizeRepository = authorizeRepository;
    }

    public List<AuthorizeModel> getAllRemunerationHistory(String date) {
        List<Object[]> allRemunerationHistory = authorizeRepository.retrieveAllRemunerationHistory(date);

        List<AuthorizeModel> remunerationHistory = new ArrayList<>();

        for(Object[] row : allRemunerationHistory) {

            if (!row[1].toString().equalsIgnoreCase("null")) {

                double amount = Double.parseDouble(
                        row[11].toString()) * Double.parseDouble(row[12].toString());

                double netTotal = amount;

                if (Double.parseDouble(row[4].toString()) != 0) {
                    netTotal = netTotal + Double.parseDouble(row[4].toString());
                }
                else if (Double.parseDouble(row[6].toString()) != 0) {
                    netTotal = netTotal - Double.parseDouble(row[6].toString());
                }

                AuthorizeModel authorizeModel = new AuthorizeModel(
                        amount,
                        netTotal,
                        row[4].toString(),
                        row[6].toString(),
                        Long.parseLong(row[0].toString()),
                        Long.parseLong(row[8].toString()),
                        row[1].toString(),
                        row[2].toString(),
                        row[3].toString(),
                        row[5].toString(),
                        row[7].toString(),
                        row[9].toString()+" "+row[10].toString()
                );

                remunerationHistory.add(authorizeModel);

            }
            else {
                AuthorizeModel authorizeModel = new AuthorizeModel(
                        row[9].toString()+" "+row[10].toString()
                );

                remunerationHistory.add(authorizeModel);

            }

        }

        return remunerationHistory;
    }

    public List<AuthorizeModel> filterRemunerationHistoryByPlace(String date, String place) {
        List<Object[]> allRemunerationHistory = authorizeRepository
                .filterRemunerationHistoryByPlace(date, place);

        List<AuthorizeModel> remunerationHistory = new ArrayList<>();

        for(Object[] row : allRemunerationHistory) {

            if (!row[1].toString().equalsIgnoreCase("null")) {

                double amount = Double.parseDouble(
                        row[11].toString()) * Double.parseDouble(row[12].toString());

                double netTotal = amount;

                if (Double.parseDouble(row[4].toString()) != 0) {
                    netTotal = netTotal + Double.parseDouble(row[4].toString());
                }
                else if (Double.parseDouble(row[6].toString()) != 0) {
                    netTotal = netTotal - Double.parseDouble(row[6].toString());
                }

                AuthorizeModel authorizeModel = new AuthorizeModel(
                        amount,
                        netTotal,
                        row[4].toString(),
                        row[6].toString(),
                        Long.parseLong(row[0].toString()),
                        Long.parseLong(row[8].toString()),
                        row[1].toString(),
                        row[2].toString(),
                        row[3].toString(),
                        row[5].toString(),
                        row[7].toString(),
                        row[9].toString()+" "+row[10].toString()
                );

                remunerationHistory.add(authorizeModel);

            }
            else {
                AuthorizeModel authorizeModel = new AuthorizeModel(
                        row[9].toString()+" "+row[10].toString()
                );

                remunerationHistory.add(authorizeModel);

            }

        }

        return remunerationHistory;
    }

    public void updateRemuneration(AuthorizeModel authorizeModel) {
        authorizeRepository.updateRemunerationDetails(authorizeModel);
    }

    public List<AuthorizeModel> searchAuthorize(String agentNames, String date) {
        List<Object[]> allRemunerationHistory = authorizeRepository
                .searchAuthorize(agentNames, date);

        List<AuthorizeModel> remunerationHistory = new ArrayList<>();

        for(Object[] row : allRemunerationHistory) {

            double amount = Double.parseDouble(
                    row[11].toString()) * Double.parseDouble(row[12].toString());

            double netTotal = amount;

            if (Double.parseDouble(row[4].toString()) != 0) {
                netTotal = netTotal + Double.parseDouble(row[4].toString());
            }
            else if (Double.parseDouble(row[6].toString()) != 0) {
                netTotal = netTotal - Double.parseDouble(row[6].toString());
            }

            AuthorizeModel authorizeModel = new AuthorizeModel(
                    amount,
                    netTotal,
                    row[4].toString(),
                    row[6].toString(),
                    Long.parseLong(row[0].toString()),
                    Long.parseLong(row[8].toString()),
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[5].toString(),
                    row[7].toString(),
                    row[9].toString()+" "+row[10].toString()
            );

            remunerationHistory.add(authorizeModel);

        }

        return remunerationHistory;
    }

    public void approveAllRemuneration(List<AuthorizeModel> authorizeModelList) {
        authorizeRepository.approveAllRemunerationDetails(authorizeModelList);
    }

}
