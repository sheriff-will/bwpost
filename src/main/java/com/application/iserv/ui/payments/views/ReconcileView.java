package com.application.iserv.ui.payments.views;


import com.application.iserv.StudentModel;
import com.application.iserv.tests.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Payments")
@Route(value = RECONCILE_LOWER_CASE, layout = MainLayout.class)
@PermitAll
public class ReconcileView extends VerticalLayout {

    // Upload
    Upload upload;

    // DatePicker
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


        // TODO Upload not finished
        MemoryBuffer memoryBuffer = new MemoryBuffer();

        upload = new Upload(memoryBuffer);
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

        upload.setMaxFiles(1);

        upload.setVisible(false);

        final List<StudentModel>[] studentModelList = new List[]{new ArrayList<>()};

        upload.addSucceededListener(event -> {

            studentModelList[0] = new ArrayList<>();

            InputStream fileData = memoryBuffer.getInputStream();

            String line;
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fileData, StandardCharsets.UTF_8))) {

                while ((line = bufferedReader.readLine()) != null) {

                    String[] row = line.split(",");

                    StudentModel studentModel = new StudentModel();
                    studentModel.setStudent(row[0]);
                    studentModel.setGrade(row[1]);
                    studentModel.setPass_fail(row[2]);

                    studentModelList[0].add(studentModel);

                }

                // Remove Header
                studentModelList[0].remove(0);
                System.err.println("student: "+ studentModelList[0]);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return new VerticalLayout(datePicker, upload);

    }

}
