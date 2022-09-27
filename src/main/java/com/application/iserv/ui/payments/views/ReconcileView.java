package com.application.iserv.ui.payments.views;


import com.application.iserv.tests.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Payments")
@Route(value = RECONCILE_LOWER_CASE, layout = MainLayout.class)
@PermitAll
public class ReconcileView extends VerticalLayout {

    Upload upload;
    DatePicker datePicker = new DatePicker();

    public ReconcileView() {


        add(
                getToolbar()
        );

    }

    private Component getToolbar() {

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(DATE_FORMAT);

        datePicker.setPlaceholder(DATE);
        datePicker.setI18n(dateFormat);
        datePicker.setHelperText(DATE_TO_RECONCILE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> upload.setVisible(true));

        upload = new Upload();
        upload.setDropAllowed(true);
        upload.setSizeFull();
        upload.setAcceptedFileTypes(CSV_FORMAT);

        upload.addFileRejectedListener(fileRejectedEvent -> {

            Notification notification = Notification.show(
                    CSV_ERROR_MESSAGE,
                    5000,
                    Notification.Position.MIDDLE
            );

            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        });

        upload.setVisible(false);

        return new VerticalLayout(datePicker, upload);

    }

}
