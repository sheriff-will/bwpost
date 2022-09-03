package com.application.iserv.ui.agents.views;


import com.application.iserv.backend.services.AgentsServices;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.agents.forms.AgentForm;
import com.application.iserv.ui.agents.models.AgentsModel;
import com.application.iserv.ui.agents.models.AttendanceModel;
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
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Agents")
@Route(value = AGENTS_LOWER_CASE, layout = MainLayout.class)
@PermitAll
public class AgentsView extends VerticalLayout {

    // Grid
    Grid<AgentsModel> agentsGrid = new Grid<>(AgentsModel.class);

    // booleans
    boolean isAttendanceOpen = false;
    boolean isAttendanceButtonClicked = false;

    // TextFields
    TextField searchAgent = new TextField();

    // Forms
    AgentForm agentForm;

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
    private final AgentsServices agentsServices;

    // Longs
    Long statusValue = 0L;

    // ArrayList
    List<AgentsModel> agentsModelList = new ArrayList<>();

    @Autowired
    public AgentsView(AgentsServices agentsServices) {
        this.agentsServices = agentsServices;

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

        agentForm.setVisible(false);

        removeClassName(ADDING_AGENT);
        removeClassName(EDITING_AGENTS);
    }

    private Component getAgentsContent() {
        agentsHorizontalLayout = new HorizontalLayout(agentsGrid, agentForm);
        agentsHorizontalLayout.setFlexGrow(2, agentsGrid);
        agentsHorizontalLayout.setFlexGrow(1, agentForm);

        agentsHorizontalLayout.setSizeFull();
        agentsHorizontalLayout.addClassName(AGENTS_CONTENT_LAYOUT);

        return agentsHorizontalLayout;
    }

    private void configureAgentsForm() {
        agentForm = new AgentForm(agentsServices);
        agentForm.setWidth("50%");

        agentForm.addListener(AgentForm.CloseEvent.class, e -> {
            agentsGrid.asSingleSelect().clear();
            closeComponents();
        });
        agentForm.addListener(AgentForm.AgentUpdatedEvent.class, e -> {
            Notification notification = new Notification(AGENT_SUCCESSFULLY_UPDATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

        });

        agentForm.addListener(AgentForm.AgentTerminatedEvent.class, e -> {
            Notification notification = new Notification(AGENT_SUCCESSFULLY_TERMINATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

        });

        agentForm.addListener(AgentForm.AgentAddedEvent.class, e -> {
            Notification notification = new Notification(AGENT_SUCCESSFULLY_ADDED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

            closeComponents();

        });

        agentForm.addListener(AgentForm.CloseAttendanceFormEvent.class, e -> {
            agentForm.setVisible(false);

            agentsGrid.asSingleSelect().clear();

            removeClassName(ADDING_AGENT);
            removeClassName(EDITING_AGENTS);

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
                    agentsModelList = new ArrayList<>();
                    agentsModelList = agentsServices.searchAgents(searchAgent.getValue(), statusValue);
                    agentsGrid.setItems(agentsModelList);
                }
                else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                    statusValue = 1L;
                    agentsModelList = new ArrayList<>();
                    agentsModelList = agentsServices.searchAgents(searchAgent.getValue(), statusValue);
                    agentsGrid.setItems(agentsModelList);
                }
            }
        });

        status.setLabel(STATUS);
        status.setItems(ACTIVE, TERMINATED);
        status.setValue(ACTIVE);
        status.addComponents(ACTIVE, new Hr());

        status.addValueChangeListener(statusValueChangeEvent -> {
            if (statusValueChangeEvent.getValue().equalsIgnoreCase(ACTIVE)) {
                agentsGrid.setItems(agentsServices.getAllAgents());
                agentForm.disableButtons(true);
            }
            else if (statusValueChangeEvent.getValue().equalsIgnoreCase(TERMINATED)) {
                agentsGrid.setItems(agentsServices.getAllTerminatedAgents());
                agentForm.disableButtons(false);
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

        addAgentButton = new Button(ADD_AGENT);
        addAgentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addAgentButton.addClickListener(click -> {
            removeClassName(ADDING_AGENT);
            removeClassName(EDITING_AGENTS);

            openAddAgentForm();

        });

        HorizontalLayout statusAttendanceLayout = new HorizontalLayout(status, addAgentButton);
        statusAttendanceLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        statusAttendanceLayout.addClassName(STATUS_ATTENDANCE_LAYOUT);

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_DATE_FORMAT);

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder(DATE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {
            configureAgentsGrid(AGENT_ATTENDANCE);

            addAttendanceToAgents();

            isAttendanceOpen = true;

            addAgentButton.setEnabled(false);

            if (agentForm.isVisible()) {
                agentForm.setVisible(false);
            }

            agentForm.hideAllComponents(true);

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

        HorizontalLayout toolbar = new HorizontalLayout(searchAgent, attendanceButton, dateBackHorizontalLayout);
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
        List<AttendanceModel> attendanceModelList = agentsServices.getAttendance(statusValue);

        for (int i = 0; i < attendanceModelList.size(); i++) {
            String attendance = attendanceModelList.get(i).getStatus();
            Long participantId = attendanceModelList.get(i).getParticipantId();

            for (int j = 0; j < agentsModelList.size(); j++) {
                if (participantId == agentsModelList.get(j).getParticipantId()) {
                    agentsModelList.get(j).setAttendance(attendance);
                }
            }

        }

    }

    private void configureBackButton() {

        removeClassName(EDITING_AGENTS);

        datePicker.clear();

        addAgentButton.setEnabled(true);
        attendanceButton.setEnabled(true);

        dateBackHorizontalLayout.setVisible(false);

        isAttendanceOpen = false;
        isAttendanceButtonClicked = false;

        configureAgentsGrid(AGENT_POSITION);

        agentForm.changeLayout(false);
        agentForm.showTab(IDENTIFICATION);

        agentsGrid.asSingleSelect().clear();

    }

    private void configureAgentsGrid(String columns) {

        agentsGrid.addClassName(AGENTS_GRID);

        if (columns.equalsIgnoreCase(AGENT_POSITION)) {
            agentsGrid.setColumns(AGENT, POSITION);
        }
        else if (columns.equalsIgnoreCase(AGENT_ATTENDANCE)) {
            agentsGrid.setColumns(AGENT, ATTENDANCE_LOWER_CASE);
        }

        agentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

    }

    private void updateAgents() {
        agentsModelList = agentsServices.getAllAgents();
        agentsGrid.setItems(agentsModelList);
    }

    private void editAgent(AgentsModel agentsModel) {
        addClassName(EDITING_AGENTS);

        if (agentsModel == null) {
            closeComponents();
        }
        else {
            if (isAttendanceOpen) {
                agentForm.setAgent(agentsModel);
                agentForm.changeLayout(true);
                agentForm.setVisible(true);
            }
            else {
                agentForm.setButtonText(false);
                agentForm.setAgent(agentsModel);
                agentForm.setVisible(true);
            }
        }
    }

    private void openAddAgentForm() {
        agentsGrid.asSingleSelect().clear();
        editAgent(new AgentsModel());
        agentForm.setButtonText(true);
    }

}
