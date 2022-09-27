package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.HistoryRepository;
import com.application.iserv.ui.payments.models.HistoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<HistoryModel> getAllHistory() {
        List<Object[]> allHistory = historyRepository.retrieveAllHistory();

        List<HistoryModel> historyModelList = new ArrayList<>();

        for(Object[] row : allHistory) {

            String month_str = row[1].toString();
            String[] getMonth = month_str.split("-");

            LocalDate month = LocalDate.of(
                    Integer.parseInt(getMonth[0]),
                    Integer.parseInt(getMonth[1]),
                    Integer.parseInt(getMonth[2])
            );

            HistoryModel historyModel = new HistoryModel(
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
                    "auto - generated",
                    row[12].toString(),
                    row[13].toString(),
                    row[14].toString(),
                    row[15].toString()

            );

            historyModelList.add(historyModel);

        }

        return historyModelList;
    }

}
