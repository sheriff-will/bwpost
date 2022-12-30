package com.application.iserv.ui.payments.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReconcileModel {

    String name, identityNumber, amount, totalNet, status, claimed;

}
