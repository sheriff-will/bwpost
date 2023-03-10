package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.HistoryRepository;
import com.application.iserv.ui.payments.models.HistoryModel;
import com.application.iserv.ui.payments.models.HistoryStatementModel;
import com.application.iserv.ui.utils.Commons;
import com.vaadin.flow.server.StreamResource;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.application.iserv.ui.utils.Constants.SIMPLE_MONTH_DATE_FORMAT;

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
                provider = Commons.getPhoneNumberCarrier(row[11].toString());

                if (provider.equalsIgnoreCase("")) {
                    provider = "No Provider";
                }

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

            Locale locale = new Locale("en", "BW");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

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
                    currencyFormatter.format(amount),
                    row[11].toString(),
                    row[12].toString(),
                    row[13].toString(),
                    row[14].toString(),
                    currencyFormatter.format(Double.parseDouble(row[15].toString())),
                    row[16].toString(),
                    currencyFormatter.format(baseSalary),
                    currencyFormatter.format(totalSalary),
                    paymentMode,
                    provider
            );

            historyModelList.add(historyModel);

        }

        return historyModelList;
    }

    public StreamResource exportReport(String names, Long participantId)
            throws IOException, JRException {
        List<Object[]> allHistory = historyRepository.exportStatements(participantId);

        List<HistoryStatementModel> historyStatementModelList = new ArrayList<>();

        for(Object[] row : allHistory) {

            String month_str = row[0].toString();
            String[] getMonth = month_str.split("-");

            // TODO 1 in the magic number
            LocalDate monthDate = LocalDate.of(
                    Integer.parseInt(getMonth[0]),
                    Integer.parseInt(getMonth[1]),
                    1
            );

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(SIMPLE_MONTH_DATE_FORMAT);

            String date = monthDate.format(dateFormatter);

            Double amount = Double.parseDouble(row[3].toString()) * Integer.parseInt(row[4].toString());

            amount = amount + Double.parseDouble(row[1].toString());

            amount = amount - Double.parseDouble(row[2].toString());

            HistoryStatementModel historyStatementModel = new HistoryStatementModel(
                    Double.parseDouble(row[3].toString()),
                    date,
                    String.valueOf(amount),
                    "Name: "+row[5].toString()+" "+row[6].toString(),
                    "Omang: "+row[7].toString(),
                    Double.parseDouble(row[1].toString()),
                    Double.parseDouble(row[2].toString()),
                    Integer.parseInt(row[4].toString())
            );

            historyStatementModelList.add(historyStatementModel);

        }

        File file = ResourceUtils.getFile("classpath:participants_history.jrxml");

        JasperReport jasperReport = JasperCompileManager
                .compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource jrBeanCollectionDataSource
                = new JRBeanCollectionDataSource(historyStatementModelList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Botswana Post");

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                jrBeanCollectionDataSource
        );

        File reportFile = File.createTempFile("report", ".pdf");
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportFile.getAbsolutePath());

        FileInputStream fileInputStream;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm - dd MMMM yyyy");
        StreamResource resource = new StreamResource(
                names+" Statement "+LocalDateTime.now().format(dateFormatter)+".pdf",
                () -> {
                    try {
                        return new FileInputStream(reportFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

        return resource;
    }

    public List<HistoryModel> searchHistory(String agentNames, String date) {
        List<Object[]> allHistory = historyRepository.searchHistory(agentNames, date);

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
                provider = Commons.getPhoneNumberCarrier(row[11].toString());

                if (provider.equalsIgnoreCase("")) {
                    provider = "No Provider";
                }
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

            Locale locale = new Locale("en", "BW");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

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
                    currencyFormatter.format(amount),
                    row[11].toString(),
                    row[12].toString(),
                    row[13].toString(),
                    row[14].toString(),
                    currencyFormatter.format(Double.parseDouble(row[15].toString())),
                    row[16].toString(),
                    currencyFormatter.format(baseSalary),
                    currencyFormatter.format(totalSalary),
                    paymentMode,
                    provider
            );


            historyModelList.add(historyModel);

        }

        return historyModelList;
    }

    public List<HistoryModel> filterHistoryByPlace(String date, String place) {
        List<Object[]> allHistory = historyRepository.filterHistoryByPlace(date, place);

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
                provider = Commons.getPhoneNumberCarrier(row[11].toString());

                if (provider.equalsIgnoreCase("")) {
                    provider = "No Provider";
                }

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

            Locale locale = new Locale("en", "BW");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

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
                    currencyFormatter.format(amount),
                    row[11].toString(),
                    row[12].toString(),
                    row[13].toString(),
                    row[14].toString(),
                    currencyFormatter.format(Double.parseDouble(row[15].toString())),
                    row[16].toString(),
                    currencyFormatter.format(baseSalary),
                    currencyFormatter.format(totalSalary),
                    paymentMode,
                    provider
            );

            historyModelList.add(historyModel);

        }

        return historyModelList;
    }

    public List<HistoryStatementModel> exportReports(Long participantId) {
        List<Object[]> allHistory = historyRepository.exportStatements(participantId);

        List<HistoryStatementModel> historyStatementModelList = new ArrayList<>();

        for(Object[] row : allHistory) {

            String month_str = row[0].toString();
            String[] getMonth = month_str.split("-");

            // TODO 1 in the magic number
            LocalDate monthDate = LocalDate.of(
                    Integer.parseInt(getMonth[0]),
                    Integer.parseInt(getMonth[1]),
                    1
            );

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(SIMPLE_MONTH_DATE_FORMAT);

            String date = monthDate.format(dateFormatter);

            Double amount = Double.parseDouble(row[3].toString()) * Integer.parseInt(row[4].toString());

            amount = amount + Double.parseDouble(row[1].toString());

            amount = amount - Double.parseDouble(row[2].toString());

            HistoryStatementModel historyStatementModel = new HistoryStatementModel(
                    Double.parseDouble(row[3].toString()),
                    date,
                    String.valueOf(amount),
                    "Name: "+row[5].toString()+" "+row[6].toString(),
                    "Omang: "+row[7].toString(),
                    Double.parseDouble(row[1].toString()),
                    Double.parseDouble(row[2].toString()),
                    Integer.parseInt(row[4].toString())
            );

            historyStatementModelList.add(historyStatementModel);

        }

        return historyStatementModelList;
    }

}
