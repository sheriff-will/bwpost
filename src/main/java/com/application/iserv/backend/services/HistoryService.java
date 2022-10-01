package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.HistoryRepository;
import com.application.iserv.ui.payments.models.HistoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<HistoryModel> getAllHistory(String date) {
        List<Object[]> allHistory = historyRepository.retrieveAllHistory(date);

        List<HistoryModel> historyModelList = new ArrayList<>();

        for(Object[] row : allHistory) {

            double amount = Double.parseDouble(
                    row[15].toString()) * Double.parseDouble(row[16].toString());

            double baseSalary = Double.parseDouble(row[15].toString()) * 20;

            double totalSalary;

            if (!row[4].toString().equalsIgnoreCase("0")) {
                totalSalary = amount + Double.parseDouble(row[4].toString());
            }
            else if (!row[6].toString().equalsIgnoreCase("0")) {
                totalSalary = amount - Double.parseDouble(row[6].toString());
            }
            else {
                totalSalary = amount;
            }

            String provider = "";
            String paymentMode = "";

            if (!row[11].toString().equalsIgnoreCase("null")) {
                provider = "Orange Money"; // TODO Remove hardcode Orange money
                paymentMode = "Mobile Wallet";
            }
            else if (!row[12].toString().equalsIgnoreCase("null")) {
                provider = row[12].toString();
                paymentMode = "Bank";
            }
            else if (row[11].toString().equalsIgnoreCase("null")
                    && row[12].toString().equalsIgnoreCase("null")) {
                provider = "Botswana Post Office"; // TODO Remove hardcoded Botswana Post Office
                paymentMode = "Cash";
            }

            HistoryModel historyModel = new HistoryModel(
                    Long.parseLong(row[0].toString()),
                    Long.parseLong(row[8].toString()),
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[4].toString(),
                    row[5].toString(),
                    row[6].toString(),
                    row[7].toString(),
                    row[9].toString()+" "+row[10].toString(),
                    String.valueOf(amount),
                    row[11].toString(),
                    row[12].toString(),
                    row[13].toString(),
                    row[14].toString(),
                    row[15].toString(),
                    row[16].toString(),
                    String.valueOf(baseSalary),
                    String.valueOf(totalSalary),
                    paymentMode,
                    provider
            );

            historyModelList.add(historyModel);

        }

        return historyModelList;
    }

}
