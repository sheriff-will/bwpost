package com.application.iserv.ui.participants.views;

import com.application.iserv.backend.services.ParticipantsServices;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.participants.forms.ParticipantsForm;
import com.application.iserv.ui.participants.models.ParticipantsModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Participants")
@Route(value = PARTICIPANTS_LOWER_CASE, layout = MainLayout.class)
@PermitAll
public class ParticipantsView extends VerticalLayout {

    // Grid
    Grid<ParticipantsModel> agentsGrid = new Grid<>(ParticipantsModel.class);

    // booleans
    boolean isAttendanceOpen = false;
    boolean isAttendanceButtonClicked = false;

    // TextFields
    TextField searchAgent = new TextField();

    // Forms
    ParticipantsForm participantsForm;

    Select<String> status = new Select<>();

    // DatePicker
    DatePicker datePicker = new DatePicker();

    // Buttons
    Button addAgentButton;
    Button backButton = new Button(BACK);
    Button attendanceButton = new Button(ATTENDANCE);

    // Layouts
    HorizontalLayout agentsHorizontalLayout;
    HorizontalLayout dateBackHorizontalLayout;

    // Services
    private final ParticipantsServices participantsServices;

    // Longs
    Long statusValue = 0L;

    // ArrayList
    List<ParticipantsModel> participantsModelList = new ArrayList<>();

    // Strings
    String date = "";

    @Autowired
    public ParticipantsView(ParticipantsServices participantsServices) {
        this.participantsServices = participantsServices;

        setSizeFull();
        addClassName(AGENTS_LIST_VIEW);

        configureAgentsForm();
        configureAgentsGrid(AGENT_POSITION);

        add(
                getToolbar(),
                getAgentsContent()
        );

        updateAgents();
        closeComponents();

        agentsGrid.asSingleSelect().addValueChangeListener(e -> editAgent(e.getValue()));

    }

    private void closeComponents() {

        if (!isAttendanceButtonClicked) {
            datePicker.setVisible(false);
        }

        participantsForm.setVisible(false);

        removeClassName(ADDING_AGENT);
        removeClassName(EDITING_AGENTS);
    }

    private Component getAgentsContent() {
        agentsHorizontalLayout = new HorizontalLayout(agentsGrid, participantsForm);
        agentsHorizontalLayout.setFlexGrow(2, agentsGrid);
        agentsHorizontalLayout.setFlexGrow(1, participantsForm);

        agentsHorizontalLayout.setSizeFull();
        agentsHorizontalLayout.addClassName(AGENTS_CONTENT_LAYOUT);

        return agentsHorizontalLayout;
    }

    private void configureAgentsForm() {
        participantsForm = new ParticipantsForm(participantsServices);
        participantsForm.setWidth("50%");

        participantsForm.addListener(ParticipantsForm.CloseEvent.class, e -> {
            agentsGrid.asSingleSelect().clear();
            closeComponents();
        });
        participantsForm.addListener(ParticipantsForm.AgentUpdatedEvent.class, e -> {
            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_UPDATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

        });

        participantsForm.addListener(ParticipantsForm.AgentTerminatedEvent.class, e -> {
            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_TERMINATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

        });

        participantsForm.addListener(ParticipantsForm.AgentAddedEvent.class, e -> {
            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_ADDED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

            closeComponents();

        });

        participantsForm.addListener(ParticipantsForm.CloseAttendanceFormEvent.class, e -> {
            participantsForm.setVisible(false);

            agentsGrid.asSingleSelect().clear();

            removeClassName(ADDING_AGENT);
            removeClassName(EDITING_AGENTS);

        });

        participantsForm.addListener(ParticipantsForm.AgentDaysWorkedUpdatedEvent.class, e -> {
            Notification notification = new Notification(UPDATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAttendance();

        });

    }

    private Component getToolbar() {
        searchAgent.setPlaceholder(SEARCH_AGENT_HINT);
        searchAgent.setClearButtonVisible(true);
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName(SEARCH_AGENT);
        searchAgent.addValueChangeListener(searchAgentValueChanged -> {
            if (status.getValue() != null) {
                if (status.getValue().equalsIgnoreCase(ACTIVE)) {
                    statusValue = 0L;
                    participantsModelList = new ArrayList<>();

                    if (isAttendanceOpen && datePicker.getValue() != null) {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                        participantsModelList = participantsServices.searchAttendance(
                                datePicker.getValue().format(dateTimeFormatter),
                                searchAgent.getValue(),
                                statusValue
                        );

                    }
                    else {
                        participantsModelList = participantsServices
                                .searchAgents(
                                        searchAgent.getValue(),
                                        statusValue,
                                        false
                                );
                    }

                    agentsGrid.setItems(participantsModelList);

                }
                else if (status.getValue().equalsIgnoreCase(EXPIRED)) {
                    statusValue = 0L;
                    participantsModelList = new ArrayList<>();
                    participantsModelList = participantsServices.
                            searchAgents(
                                    searchAgent.getValue(),
                                    statusValue,
                                    true
                            );
                    agentsGrid.setItems(participantsModelList);
                }
                else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                    statusValue = 1L;
                    participantsModelList = new ArrayList<>();
                    participantsModelList = participantsServices.
                            searchAgents(
                                    searchAgent.getValue(),
                                    statusValue,
                                    false
                            );
                    agentsGrid.setItems(participantsModelList);
                }
            }
        });

        status.setLabel(CONTRACT_STATUS);
        status.setItems(ACTIVE, EXPIRED, TERMINATED);
        status.setValue(ACTIVE);
        status.addComponents(ACTIVE, new Hr());
        status.addComponents(EXPIRED, new Hr());

        status.addValueChangeListener(statusValueChangeEvent -> {
            if (!isAttendanceOpen) {
                if (statusValueChangeEvent.getValue().equalsIgnoreCase(ACTIVE)) {
                    agentsGrid.setItems(participantsServices.getAllAgents());
                    participantsForm.disableButtons(true);
                }
                else if (statusValueChangeEvent.getValue().equalsIgnoreCase(TERMINATED)) {
                    agentsGrid.setItems(participantsServices.getAllTerminatedAgents());
                    participantsForm.disableButtons(false);
                }
                else if (statusValueChangeEvent.getValue().equalsIgnoreCase(EXPIRED)) {
                    agentsGrid.setItems(participantsServices.getAllExpiredAgents());
                    participantsForm.disableButtons(false);
                }
            }
        });

        attendanceButton.addClassName(ATTENDANCE_BUTTON);
        attendanceButton.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        attendanceButton.setDisableOnClick(true);
        attendanceButton.addClickListener(click -> {
            datePicker.setVisible(true);
            backButton.setVisible(true);
            isAttendanceButtonClicked = true;
            dateBackHorizontalLayout.setVisible(true);
            addClassName(ATTENDANCE_BUTTON_CLICKED);
        });

        backButton.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        backButton.setVisible(false);

        backButton.addClickListener(click -> {

            removeClassNames(
                    ADDING_AGENT,
                    EDITING_AGENTS,
                    SEARCH_AGENT,
                    ATTENDANCE_BUTTON,
                    STATUS_ATTENDANCE_LAYOUT,
                    ATTENDANCE_BUTTON_CLICKED
            );

            configureBackButton();

        });

        addAgentButton = new Button(ADD_PARTICIPANT);
        addAgentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addAgentButton.addClickListener(click -> {
            removeClassName(ADDING_AGENT);
            removeClassName(EDITING_AGENTS);

            openAddAgentForm();

        });

        HorizontalLayout statusAttendanceLayout = new HorizontalLayout(status, attendanceButton);
        statusAttendanceLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        statusAttendanceLayout.addClassName(STATUS_ATTENDANCE_LAYOUT);

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_MONTH_DATE_FORMAT);

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder(DATE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {
            configureAgentsGrid(AGENT_ATTENDANCE);

            addAttendanceToAgents();

            isAttendanceOpen = true;

            addAgentButton.setEnabled(false);

            status.setValue(ACTIVE);
            status.setReadOnly(true);

            if (participantsForm.isVisible()) {
                participantsForm.setVisible(false);
            }

            participantsForm.hideAllComponents(true);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

            if (datePicker.getValue() != null) {
                date = datePicker.getValue().format(dateFormatter);
            }

        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() <= 500) {
                agentsGrid.setHeight("90%");
            }
            else {
                agentsGrid.setSizeFull();
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() <= 500) {
                agentsGrid.setHeight("90%");
            }
            else {
                agentsGrid.setSizeFull();
            }
        });


        dateBackHorizontalLayout = new HorizontalLayout(datePicker, backButton);

        HorizontalLayout toolbar = new HorizontalLayout(searchAgent, addAgentButton, dateBackHorizontalLayout);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolbar.addClassName(TOOLBAR);

        VerticalLayout verticalToolbar = new VerticalLayout(statusAttendanceLayout, toolbar);
        verticalToolbar.addClassName(VERTICAL_TOOLBAR);

        Button terminateButton = new Button(TERMINATE);
        terminateButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout horizontalToolbar = new HorizontalLayout(verticalToolbar, terminateButton);
        horizontalToolbar.addClassNames(HORIZONTAL_TOOLBAR);
        horizontalToolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return verticalToolbar;
    }

    private void addAttendanceToAgents() {

        if (datePicker.getValue() != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

            agentsGrid.setItems(
                    participantsServices.getAttendance(
                            datePicker.getValue().format(dateTimeFormatter)
                    ));

            agentsGrid.asSingleSelect().clear();

        }

    }

    private void configureBackButton() {

        removeClassName(EDITING_AGENTS);

        datePicker.clear();

        addAgentButton.setEnabled(true);
        attendanceButton.setEnabled(true);

        status.setValue(ACTIVE);
        status.setReadOnly(false);

        dateBackHorizontalLayout.setVisible(false);

        isAttendanceOpen = false;
        isAttendanceButtonClicked = false;

        configureAgentsGrid(AGENT_POSITION);

        participantsForm.changeLayout(false);
        participantsForm.showTab(IDENTIFICATION);

        agentsGrid.asSingleSelect().clear();

        participantsForm.resetTabs();

        updateAgents();

    }

    private void configureAgentsGrid(String columns) {

        agentsGrid.addClassName(AGENTS_GRID);

        if (columns.equalsIgnoreCase(AGENT_POSITION)) {
            agentsGrid.setColumns(PARTICIPANT, POSITION);
        }
        else if (columns.equalsIgnoreCase(AGENT_ATTENDANCE)) {
            agentsGrid.setColumns(PARTICIPANT, DAYS_WORKED_CAMEL_CASE);
        }

        agentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

    }

    private void updateAgents() {
        participantsModelList = participantsServices.getAllAgents();
        agentsGrid.setItems(participantsModelList);
    }

    private void updateAttendance() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

        agentsGrid.setItems(participantsServices.getAttendance(
                datePicker.getValue().format(dateTimeFormatter))
        );

    }

    private void editAgent(ParticipantsModel participantsModel) {
        addClassName(EDITING_AGENTS);

        if (participantsModel == null) {
            closeComponents();
        }
        else {
            if (isAttendanceOpen) {
                participantsForm.setAgent(participantsModel, date);
                participantsForm.changeLayout(true);
                participantsForm.setVisible(true);
            }
            else {
                participantsForm.setButtonText(false);
                participantsForm.setAgent(participantsModel, date);
                participantsForm.setVisible(true);
            }
        }
    }

    private void openAddAgentForm() {
        agentsGrid.asSingleSelect().clear();
        editAgent(new ParticipantsModel());
        participantsForm.setButtonText(true);
    }

}
