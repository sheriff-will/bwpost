package com.application.iserv.ui.payments.views;

import com.application.iserv.backend.services.ReconcileService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.payments.models.ReconcileModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@CssImport("./styles/views/statistics.css")
@PageTitle("iServ | Payments")
@RolesAllowed({"ROOT", "ADMIN"})
@Route(value = RECONCILE_LOWER_CASE, layout = MainLayout.class)
public class ReconcileView extends VerticalLayout {

    // Upload
    Upload upload;

    // DatePicker
    DatePicker datePicker = new DatePicker();

    // Rows
    Row row = new Row();

    // Services
    private final ReconcileService reconcileService;

    final List<ReconcileModel>[] reconcileModelList = new List[]{new ArrayList<>()};

    @Autowired
    public ReconcileView(ReconcileService reconcileService) {
        this.reconcileService = reconcileService;

        add(
                getToolbar()
        );

    }

    private Component getToolbar() {

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_MONTH_DATE_FORMAT);

        datePicker.setPlaceholder(DATE);
        datePicker.setI18n(dateFormat);
        datePicker.setHelperText(DATE_TO_RECONCILE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> upload.setVisible(true));

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

        Board board = new Board();

        upload.addSucceededListener(event -> {

            reconcileModelList[0] = new ArrayList<>();

            InputStream fileData = memoryBuffer.getInputStream();

            String line;
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fileData, StandardCharsets.UTF_8))) {

                while ((line = bufferedReader.readLine()) != null) {

                    String[] row = line.split(",");

                    ReconcileModel reconcileModel = new ReconcileModel();
                    reconcileModel.setName(row[0]);
                    reconcileModel.setIdentityNumber(row[1]);
                    reconcileModel.setAmount(row[2]);
                    reconcileModel.setTotalNet(row[3]);
                    reconcileModel.setStatus(row[4]);
                    reconcileModel.setClaimed(row[5]);

                    reconcileModelList[0].add(reconcileModel);

                }

                // Remove Header
                reconcileModelList[0].remove(0);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<String> claimed = new ArrayList<>();
            List<String> unClaimed = new ArrayList<>();

            for (int i = 0; i < reconcileModelList[0].size(); i++) {
                String claimedValue = reconcileModelList[0].get(i).getClaimed();

                if (claimedValue.contains("y") || claimedValue.contains("Y")) {
                    claimed.add(reconcileModelList[0].get(i).getClaimed());
                }
                else if (claimedValue.contains("n") || claimedValue.contains("N")) {
                    unClaimed.add(reconcileModelList[0].get(i).getClaimed());
                }
            }

            double claimedPercentage = (double) claimed.size()
                    / (double) reconcileModelList[0].size() * 100;

            double unClaimedPercentage = (double) unClaimed.size()
                    / (double) reconcileModelList[0].size() * 100;

            board.removeAll();

            row = new Row(
                    createHighlight(
                            "Claimed",
                            String.valueOf(claimed.size()),
                            claimedPercentage,
                            true),
                    createHighlight("Not Claimed",
                            String.valueOf(unClaimed.size()),
                            -unClaimedPercentage,
                            true)
            );

            String date = datePicker.getValue().format(DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT));

            reconcileService.updateRemunerationDetails(date, reconcileModelList[0]);

            board.add(row);

        });

        return new VerticalLayout(datePicker, upload, board);

    }

    private Component createHighlight(String title, String value, Double percentage, boolean showIcon) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";

        if (percentage == 0) {
            prefix = "±";
        } else if (percentage > 0) {
            prefix = "+";
            theme += " success";
        } else if (percentage < 0) {
            icon = VaadinIcon.ARROW_DOWN;
            theme += " error";
        }

        H2 h2 = new H2(title);
        h2.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        Span span = new Span(value);
        span.addClassNames("font-semibold", "text-3xl");

        Icon i = icon.create();
        i.addClassNames("box-border", "p-xs");

        Span badge;

        if (!showIcon) {
            badge = new Span(new Span(percentage.toString()));
            badge.getElement().getThemeList().add("badge");
        }
        else {
            badge = new Span(i, new Span(prefix + percentage));
            badge.getElement().getThemeList().add(theme);
        }

        VerticalLayout layout = new VerticalLayout(h2, span, badge);
        layout.addClassName("p-l");
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    private HorizontalLayout createHeader(String title, String subtitle) {
        H2 h2 = new H2(title);
        h2.addClassNames("text-xl", "m-0");

        Span span = new Span(subtitle);
        span.addClassNames("text-secondary", "text-xs");

        VerticalLayout column = new VerticalLayout(h2, span);
        column.setPadding(false);
        column.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout(column);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setSpacing(false);
        header.setWidthFull();
        return header;
    }

}
