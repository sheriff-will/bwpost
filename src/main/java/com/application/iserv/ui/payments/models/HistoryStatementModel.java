package com.application.iserv.ui.payments.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoryStatementModel {

    Double ratePerDay;
    String date;
    String amount;
    String participant, identityNumber;
    double bonusAmount, deductionAmount;
    Integer daysWorked;

}
