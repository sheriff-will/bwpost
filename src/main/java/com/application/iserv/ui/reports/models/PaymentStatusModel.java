package com.application.iserv.ui.reports.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusModel {

    int statusSize;
    String approved, denied, pending, onHold;

}
