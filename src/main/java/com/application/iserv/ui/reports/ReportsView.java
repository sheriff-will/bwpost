package com.application.iserv.ui.reports;

import com.application.iserv.backend.services.ReportsService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.reports.models.AttendanceAverageModel;
import com.application.iserv.ui.reports.models.ContractStatusModel;
import com.application.iserv.ui.reports.models.PaymentStatusModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("iServ | Reports")
@Route(value = "reports", layout = MainLayout.class)
public class ReportsView extends Main {

    // Services
    private final ReportsService reportsService;

    public ReportsView(ReportsService reportsService) {
        this.reportsService = reportsService;

        addClassName("dashboard-view");

        setSizeFull();

        PaymentStatusModel paymentStatusModel = reportsService.getPaymentStatuses();

        double approvedPercentage = Double.parseDouble(paymentStatusModel.getApproved())
                / paymentStatusModel.getStatusSize() * 100;

        double deniedPercentage = Double.parseDouble(paymentStatusModel.getDenied())
                / paymentStatusModel.getStatusSize() * 100;

        double pendingPercentage = Double.parseDouble(paymentStatusModel.getPending())
                / paymentStatusModel.getStatusSize() * 100;

        double onHoldPercentage = Double.parseDouble(paymentStatusModel.getOnHold())
                / paymentStatusModel.getStatusSize() * 100;

        Board board = new Board();
        board.addRow(
                createHighlight(
                        "Approved Payments",
                        paymentStatusModel.getApproved(),
                        approvedPercentage,
                        true),
                createHighlight("Denied Payments",
                        paymentStatusModel.getDenied(),
                        -deniedPercentage,
                        true),
                createHighlight("Pending Payments",
                        paymentStatusModel.getPending(),
                        pendingPercentage,
                        false),
                createHighlight("On Hold Payments",
                        paymentStatusModel.getOnHold(),
                        onHoldPercentage,
                        false)
        );
        board.addRow(createViewEvents());
        board.addRow(createServiceHealth(), createResponseTimes());
        add(board);
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

    private Component createViewEvents() {
        // Header
        Select year = new Select();
        year.setItems("2022");
        year.setValue("2022");
        year.setWidth("100px");

        HorizontalLayout header = createHeader("Payments History", "Transactions");
        header.add(year);

        // Chart
        Chart chart = new Chart(ChartType.AREASPLINE);
        Configuration conf = chart.getConfiguration();
        conf.getChart().setStyledMode(true);

        XAxis xAxis = new XAxis();
        xAxis.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("Amount (pula)");

        PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
        plotOptions.setPointPlacement(PointPlacement.ON);
        plotOptions.setMarker(new Marker(false));
        conf.addPlotOptions(plotOptions);

        conf.addSeries(new ListSeries("Approved", 189, 191, 291, 396, 501, 403, 609, 712, 729, 942, 1044, 1247));
        conf.addSeries(new ListSeries("On Hold", 138, 246, 248, 348, 352, 353, 463, 573, 778, 779, 885, 887));
        conf.addSeries(new ListSeries("Pending", 65, 65, 166, 171, 293, 302, 308, 317, 427, 429, 535, 636));
        conf.addSeries(new ListSeries("Denied", 0, 11, 17, 123, 130, 142, 248, 349, 452, 454, 458, 462));

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName("p-l");
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }

    private Component createServiceHealth() {
        // Header
        HorizontalLayout header = createHeader(
                "Contract Status",
                "Number of all participants contract status"
        );

        // Grid
        Grid<ServiceHealth> grid = new Grid();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setAllRowsVisible(true);

        grid.addColumn(new ComponentRenderer<>(serviceHealth -> {
            Span status = new Span();
            String statusText = getStatusDisplayName(serviceHealth);
            status.getElement().setAttribute("aria-label", "Status: " + statusText);
            status.getElement().setAttribute("title", "Status: " + statusText);
            status.getElement().getThemeList().add(getStatusTheme(serviceHealth));
            return status;
        })).setHeader("").setFlexGrow(0).setAutoWidth(true);
        grid.addColumn(ServiceHealth::getCity).setHeader("Contract").setFlexGrow(1);
        grid.addColumn(ServiceHealth::getInput).setHeader("Number").setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        ContractStatusModel contractStatusModel = reportsService.getContractStatus();

        grid.setItems(new ServiceHealth(
                ServiceHealth.Status.EXCELLENT,
                        "Active",
                        Integer.parseInt(contractStatusModel.getActive())),
                new ServiceHealth(
                        ServiceHealth.Status.OK,
                        "Expired",
                        Integer.parseInt(contractStatusModel.getExpired())),
                new ServiceHealth(
                        ServiceHealth.Status.FAILING,
                        "Terminated",
                        Integer.parseInt(contractStatusModel.getTerminated())));

        // Add it all together
        VerticalLayout serviceHealth = new VerticalLayout(header, grid);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        return serviceHealth;
    }

    private Component createResponseTimes() {
        HorizontalLayout header = createHeader("Attendance", "Average attendance for the month");

        // Chart
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.getChart().setStyledMode(true);
        chart.setThemeName("gradient");

        AttendanceAverageModel attendanceAverageModel = reportsService.getAttendanceAverage();

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Days Worked", attendanceAverageModel.getDaysWorked()));
        series.add(new DataSeriesItem("Days Missed", attendanceAverageModel.getDaysMissed()));
        conf.addSeries(series);

        // Add it all together
        VerticalLayout serviceHealth = new VerticalLayout(header, chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        return serviceHealth;
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

    private String getStatusDisplayName(ServiceHealth serviceHealth) {
        ServiceHealth.Status status = serviceHealth.getStatus();
        if (status == ServiceHealth.Status.OK) {
            return "Ok";
        }
        else if (status == ServiceHealth.Status.FAILING) {
            return "Failing";
        }
        else if (status == ServiceHealth.Status.EXCELLENT) {
            return "Excellent";
        }
        else {
            return status.toString();
        }
    }

    private String getStatusTheme(ServiceHealth serviceHealth) {
        ServiceHealth.Status status = serviceHealth.getStatus();
        String theme = "badge primary small";
        if (status == ServiceHealth.Status.EXCELLENT) {
            theme += " success";
        }
        else if (status == ServiceHealth.Status.FAILING) {
            theme += " error";
        }
        return theme;
    }

}
