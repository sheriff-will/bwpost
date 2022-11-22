package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.ReportsRepository;
import com.application.iserv.ui.reports.models.AttendanceAverageModel;
import com.application.iserv.ui.reports.models.ContractStatusModel;
import com.application.iserv.ui.reports.models.PaymentStatusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@Service
public class ReportsService {

    private final ReportsRepository reportsRepository;

    @Autowired
    public ReportsService(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    public PaymentStatusModel getPaymentStatuses() {

        String[] date = LocalDate.now().toString().split("-");
        String month = date[0]+"-"+date[1];

        List<String> paymentStatuses = reportsRepository.getPaymentStatuses(month);

        PaymentStatusModel paymentStatusModel;

        int approved = 0;
        int denied = 0;
        int pending = 0;
        int onHold = 0;

        for (int i = 0; i < paymentStatuses.size(); i++) {
            if (paymentStatuses.get(i).equalsIgnoreCase(APPROVED)) {
                approved++;
            }
            else if (paymentStatuses.get(i).equalsIgnoreCase(DENIED)) {
                denied++;
            }
            else if (paymentStatuses.get(i).equalsIgnoreCase(PENDING)) {
                pending++;
            }
            else if (paymentStatuses.get(i).equalsIgnoreCase(HOLD)) {
                onHold++;
            }

        }

        paymentStatusModel = new PaymentStatusModel(
                paymentStatuses.size(),
                String.valueOf(approved),
                String.valueOf(denied),
                String.valueOf(pending),
                String.valueOf(onHold)
        );

        return paymentStatusModel;
    }

    public ContractStatusModel getContractStatus() {
        List<Object[]> contractStatuses = reportsRepository.getContractStatus();

        ContractStatusModel contractStatusModel;

        int active = 0;
        int expired = 0;
        int terminated = 0;

        for(Object[] row : contractStatuses) {

            if (row[1].toString().equalsIgnoreCase("true")) {
                terminated++;
            }
            else {
                String completionDate_str = row[0].toString();
                String[] getCompletionDate = completionDate_str.split("-");

                LocalDate completionDate = LocalDate.of(
                        Integer.parseInt(getCompletionDate[0]),
                        Integer.parseInt(getCompletionDate[1]),
                        Integer.parseInt(getCompletionDate[2])
                );

                int compare = completionDate.compareTo(LocalDate.now());

                if (compare < 0) {
                    expired++;
                }
                else {
                    active++;
                }
            }
        }

        contractStatusModel = new ContractStatusModel(
                String.valueOf(active),
                String.valueOf(expired),
                String.valueOf(terminated)
        );

        return contractStatusModel;

    }

    public AttendanceAverageModel getAttendanceAverage() {

        String[] date = LocalDate.now().toString().split("-");
        String month = date[0]+"-"+date[1];

        List<String> attendance = reportsRepository.getAttendance(month);

        AttendanceAverageModel attendanceAverageModel;

        double daysWorked = 0;
        double allWorkingDays = 20 * attendance.size();

        for (int i = 0; i < attendance.size(); i++) {

            daysWorked = daysWorked + Double.parseDouble(attendance.get(i));

        }

        double daysMissed = allWorkingDays - daysWorked;

        double averageDaysWorked = daysWorked / allWorkingDays * 100;
        double averageDaysMissed = daysMissed / allWorkingDays * 100;

        attendanceAverageModel = new AttendanceAverageModel(
                averageDaysWorked,
                averageDaysMissed
        );

        return attendanceAverageModel;

    }

}
