package com.application.iserv.ui.parameters.views;

import com.application.iserv.backend.services.ParametersService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.parameters.models.ParametersModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import java.text.DecimalFormat;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Parameters")
@Route(value = "parameters", layout = MainLayout.class)
@PermitAll
public class ParametersView extends VerticalLayout {

    // Grids
    Grid<ParametersModel> parametersGrid = new Grid<>(ParametersModel.class);

    // TextField
    TextField position = new TextField(POSITION_UPPER_CASE);

    // IntegerField
    NumberField duration = new NumberField(DURATION);
    NumberField dailyRatePerDay = new NumberField(DAILY_RATE_PER_DAY_CAMEL_CASE);

    // Dialogs
    Dialog parameterDialog = new Dialog();

    // Buttons
    Button saveParameter = new Button();
    Button removeParameter = new Button(REMOVE);
    Button addParameter = new Button(ADD_PARAMETER);

    // Service
    private final ParametersService parametersService;

    // Longs
    Long parameterId;

    @Autowired
    public ParametersView(ParametersService parametersService) {
        this.parametersService = parametersService;

        setSizeFull();

        checkScreenSize();
        configureDialogs();
        configureButtons();
        configureParametersGrid();

        add(
                addParameter,
                parametersGrid,
                parameterDialog
        );

        updateParameters();

        parametersGrid.asSingleSelect().addValueChangeListener(e -> editParameter(e.getValue()));

    }

    private void editParameter(ParametersModel parametersModel) {
        if (parametersModel != null) {
            position.setValue(parametersModel.getPosition());
            dailyRatePerDay.setValue(parametersModel.getDailyRatePerDay());

            String[] getDuration = parametersModel.getDuration().split("");
            duration.setValue(Double.parseDouble(getDuration[0]));

            parameterId = parametersModel.getParameterId();

            saveParameter.setText(UPDATE);
            removeParameter.setVisible(true);
            parameterDialog.open();
        }
    }

    private void checkScreenSize() {

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() > 500) {
                parameterDialog.setWidth("90%");
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() > 500) {
                parameterDialog.setWidth("90%");
            }

        });

    }

    private void configureButtons() {

        addParameter.addClickListener(click -> {
            position.clear();
            dailyRatePerDay.clear();
            duration.clear();
            removeParameter.setVisible(false);

            saveParameter.setText(ADD);
            parameterDialog.open();
        });

        saveParameter.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveParameter.addClickListener(click -> {
            saveParameter.setDisableOnClick(true);
            configureParameterForm();
        });

        removeParameter.addThemeVariants(
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY
        );

        removeParameter.getStyle().set("margin-right", "auto");

        removeParameter.addClickListener(click -> {
            removeParameter.setDisableOnClick(true);
            parametersService.deleteParameter(parameterId);

            updateParameters();

            Notification notification = new Notification(PARAMETER_SUCCESSFULLY_REMOVED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            parametersGrid.asSingleSelect().clear();
            parameterDialog.close();
            removeParameter.setEnabled(true);

        });

    }

    private void configureDialogs() {

        FormLayout addParameterForm = new FormLayout(
                position,
                dailyRatePerDay,
                duration
        );

        duration.setMax(12);
        duration.setMin(1);
        duration.setHasControls(true);
        duration.setHelperText("Number Of Months");

        addParameterForm.setColspan(position, 2);
        addParameterForm.setColspan(dailyRatePerDay, 2);
        addParameterForm.setColspan(duration, 2);

        parameterDialog.getHeader().add(
                new Button(new Icon("lumo", "cross"), (e) -> {
                    parametersGrid.asSingleSelect().clear();
                    parameterDialog.close();
                })
        );

        parameterDialog.addDialogCloseActionListener(dialogCloseActionEvent -> {
            parametersGrid.asSingleSelect().clear();
            parameterDialog.close();
        });

        parameterDialog.setHeaderTitle("Add Parameter");
        parameterDialog.add(addParameterForm);
        parameterDialog.getFooter().add(removeParameter);
        parameterDialog.getFooter().add(saveParameter);

    }

    private void configureParameterForm() {

        if (position.getValue().isEmpty()) {
            position.setInvalid(true);
            Notification notification = new Notification("Enter Position");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveParameter.setEnabled(true);

        }
        else if (dailyRatePerDay.getValue() == null) {
            dailyRatePerDay.setInvalid(true);
            Notification notification = new Notification("Enter Rate Per Day");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveParameter.setEnabled(true);

        }
        else if (duration.isInvalid() || duration.getValue() == null) {
            duration.setInvalid(true);
            Notification notification = new Notification("Enter number of months");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveParameter.setEnabled(true);

        }
        else {

            String response;

            if (saveParameter.getText().equalsIgnoreCase(UPDATE)) {
                response = parametersService.updateParameter(new ParametersModel(
                        parameterId,
                        dailyRatePerDay.getValue(),
                        position.getValue(),
                        duration.getValue().toString()
                ));

            }
            else {
                response = parametersService.addParameter(new ParametersModel(
                        dailyRatePerDay.getValue(),
                        position.getValue(),
                        duration.getValue().toString()
                ));

            }

            if (response.equalsIgnoreCase(PARAMETER_ALREADY_EXIST)) {
                Notification notification = new Notification(PARAMETER_ALREADY_EXIST);
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(5000);
                notification.open();

            }
            else {
                updateParameters();

                if (saveParameter.getText().equalsIgnoreCase(UPDATE)) {
                    Notification notification = new Notification(PARAMETER_SUCCESSFULLY_UPDATED);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(5000);
                    notification.open();
                }
                else {
                    Notification notification = new Notification(PARAMETER_SUCCESSFULLY_ADDED);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(5000);
                    notification.open();
                }

            }

            parametersGrid.asSingleSelect().clear();
            parameterDialog.close();
            saveParameter.setEnabled(true);

        }

    }

    private void updateParameters() {
        parametersGrid.setItems(parametersService.getParameters());
    }

    private void configureParametersGrid() {
        parametersGrid.setColumns(POSITION);
        parametersGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        parametersGrid.addComponentColumn(
                dailyRatePerDay -> createDailyRateLabel(dailyRatePerDay.getDailyRatePerDay()))
                .setHeader(DAILY_RATE_PER_DAY_CAMEL_CASE);

        parametersGrid.addComponentColumn(
                        duration -> createDurationLabel(duration.getDuration()))
                .setHeader(DURATION);

    }

    private Component createDailyRateLabel(double dailyRatePerDay) {
        DecimalFormat decimalFormat = new DecimalFormat("P#.00");
        Label label = new Label();
        label.setText(decimalFormat.format(dailyRatePerDay));

        return label;
    }

    private Component createDurationLabel(String duration) {

        Label label = new Label();
        label.setText(String.valueOf(duration));

        return label;
    }

}
