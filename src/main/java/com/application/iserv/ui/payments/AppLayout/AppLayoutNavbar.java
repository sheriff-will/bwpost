package com.application.iserv.ui.payments.AppLayout;

import com.application.iserv.ui.agents.views.AgentsView;
import com.application.iserv.ui.payments.views.AuthorizeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.Optional;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Test Payments")
@Route(value = "testnav")
public class AppLayoutNavbar extends AppLayout {

    // Tabs
    Tabs menu;
    Tabs subViews;

    Tab authorize = new Tab(AUTHORIZE);
    Tab reconcile = new Tab(RECONCILE);
    Tab history = new Tab(HISTORY);
    Tab ipelegeng = new Tab(IPELEGENG);
    Tab tireloSechaba = new Tab(TIRELO_SECHABA);

    // H1
    H2 viewTitle;

    public AppLayoutNavbar() {

        tireloSechaba.setEnabled(false);

        menu = createMenu();

        addToDrawer(createDrawerContent(menu));
        addToNavbar(getHeader());

        setPrimarySection(Section.DRAWER);

        subViews.addSelectedChangeListener(selectedChangeEvent -> {

            Tab selectedTab = subViews.getSelectedTab();

            if (authorize == selectedTab) {
                getUI().ifPresent(ui -> ui.navigate(AUTHORIZE_LOWER_CASE));
            }
            else if (reconcile == selectedTab) {
                getUI().ifPresent(ui -> ui.navigate(RECONCILE_LOWER_CASE));
            }
            else if (history == selectedTab) {
                getUI().ifPresent(ui -> ui.navigate(HISTORY_LOWER_CASE));
            }

        });

    }

    private Tabs createMenu() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[] {
                createTab("Agents", AgentsView.class),
                createTab("Payments", AuthorizeView.class)
        };
    }

    private Tab createTab(String text, Class<? extends Component> navigationTarget) {
        Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private Component getHeader() {

        DrawerToggle toggle = new DrawerToggle();

        viewTitle = new H2();

        subViews = new Tabs(authorize, reconcile, history);

        HorizontalLayout wrapper = new HorizontalLayout(toggle, viewTitle);
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setSpacing(false);

        VerticalLayout subViewsVerticalLayout = new VerticalLayout(subViews);
        subViewsVerticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        subViewsVerticalLayout.setPadding(false);
        subViewsVerticalLayout.setSpacing(false);

        VerticalLayout viewHeader = new VerticalLayout(wrapper, subViewsVerticalLayout);
        viewHeader.setPadding(false);
        viewHeader.setSpacing(false);
        //viewHeader.getThemeList().set(Lumo.DARK, true);
        //   viewHeader.getStyle().set("background", "#1874C3");

        UI.getCurrent()
                .getPage()
                .executeJs("return window.location.href")
                .then(jsonValue -> {
                    String urlPath = jsonValue.asString();

                    if (urlPath.contains(AUTHORIZE_LOWER_CASE)) {
                        subViews.setSelectedTab(authorize);
                    }
                    if (urlPath.contains(RECONCILE_LOWER_CASE)) {
                        subViews.setSelectedTab(reconcile);
                    }
                    if (urlPath.contains(HISTORY_LOWER_CASE)) {
                        subViews.setSelectedTab(history);
                    }
                });

        return viewHeader;
    }

    private Component createDrawerContent(Tabs menu) {

        VerticalLayout layout = new VerticalLayout();

        // Configure styling
        layout.setSizeFull();
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Add logo
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Image image = new Image("images/logo.png", "iServ logo");
        image.setWidth("40%");
        logoLayout.add(image);
        logoLayout.add(new H1("iServ"));

        // Display logo
        layout.add(logoLayout, menu);
        return layout;

    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Select corresponding tab
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // Set view title

        String [] title = getCurrentPageTitle().split(" | ");

        viewTitle.setText(title[2]);
        viewTitle.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab
                -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

}