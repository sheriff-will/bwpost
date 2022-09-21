package com.application.iserv.tests;

import com.application.iserv.tests.components.navigation.drawer.BrandExpression;
import com.application.iserv.tests.components.navigation.drawer.NaviMenu;
import com.application.iserv.tests.util.UIUtils;
import com.application.iserv.ui.agents.views.AgentsView;
import com.application.iserv.ui.payments.views.AuthorizeView;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import elemental.json.JsonObject;

import java.util.Optional;

import static com.application.iserv.ui.utils.Constants.*;

@CssImport("./styles/components/navi-drawer.css")
@JsModule("./styles/swipe-away.js")
@PageTitle("iServ | Test Payments")
@Route(value = "testmain")
public class TestMainLayout extends AppLayout implements AfterNavigationObserver {

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

    // Buttons
    Button railButton;

    String RAIL = "rail";

    Div scrim;

    private Div mainContent;

    private String CLASS_NAME = "navi-drawer";

    private String OPEN = "open";


    private TextField search;
    private Div scrollableArea;

    private NaviMenu naviMenu;

    public TestMainLayout() {

        addClassName(CLASS_NAME);

        initScrim();
        initMainContent();

        initHeader();
        initSearch();

        initScrollableArea();
        initMenu();

        initFooter();

        addToDrawer(scrim, mainContent);

        //  tireloSechaba.setEnabled(false);

        //  menu = createMenu();

        //addToDrawer(scrim, mainContent);
        //  addToNavbar(getHeader());

        //  setPrimarySection(Section.DRAWER);

       /* subViews.addSelectedChangeListener(selectedChangeEvent -> {

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
*/
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

    /*  @Override
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
  */
  /*  private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
*/
    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab
                        -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }



    // Test

    @ClientCallable
    public void onSwipeAway(JsonObject data) {
        close();
    }

    private void initScrim() {
        // Backdrop on small viewports
        scrim = new Div();
        scrim.addClassName(CLASS_NAME + "__scrim");
        scrim.addClickListener(event -> close());
    }

    private void initMainContent() {
        mainContent = new Div();
        mainContent.addClassName(CLASS_NAME + "__content");
    }

    private void initHeader() {
        mainContent.add(new BrandExpression("iServ"));
    }

    private void initSearch() {

        // Useful keep, To search drawer contents!
		/*search = new TextField();
		search.addValueChangeListener(e -> menu.filter(search.getValue()));
		search.setClearButtonVisible(true);
		search.setPlaceholder("Search");
		search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
*/

        Tab ipelegeng = new Tab(IPELEGENG);
        Tab tireloSechaba = new Tab(TIRELO_SECHABA);

        tireloSechaba.setEnabled(false);
        Tabs services = new Tabs(ipelegeng, tireloSechaba);

        mainContent.add(services);
    }

    private void initScrollableArea() {
        scrollableArea = new Div();
        scrollableArea.addClassName(CLASS_NAME + "__scroll-area");
        mainContent.add(scrollableArea);
    }

    private void initMenu() {
        naviMenu = new NaviMenu();
        scrollableArea.add(naviMenu);
    }

    private void initFooter() {
        railButton = UIUtils.createSmallButton("Collapse", VaadinIcon.CHEVRON_LEFT_SMALL);
        railButton.addClassName(CLASS_NAME + "__footer");
        railButton.addClickListener(event -> toggleRailMode());
        railButton.getElement().setAttribute("aria-label", "Collapse menu");
        mainContent.add(railButton);

    }

    private void toggleRailMode() {
        if (getElement().hasAttribute(RAIL)) {
            getElement().setAttribute(RAIL, false);
            railButton.setIcon(new Icon(VaadinIcon.CHEVRON_LEFT_SMALL));
            railButton.setText("Collapse");
            UIUtils.setAriaLabel("Collapse menu", railButton);

        } else {
            getElement().setAttribute(RAIL, true);
            railButton.setIcon(new Icon(VaadinIcon.CHEVRON_RIGHT_SMALL));
            railButton.setText("Expand");

            UIUtils.setAriaLabel("Expand menu", railButton);
            getUI().get().getPage().executeJavaScript(
                    "var originalStyle = getComputedStyle($0).pointerEvents;" //
                            + "$0.style.pointerEvents='none';" //
                            + "setTimeout(function() {$0.style.pointerEvents=originalStyle;}, 170);",
                    getElement());
        }
    }

    public void toggle() {
        if (getElement().hasAttribute(OPEN)) {
            close();
        } else {
            open();
        }
    }

    private void open() {
        getElement().setAttribute(OPEN, true);
    }

    private void close() {
        getElement().setAttribute(OPEN, false);
        applyIOS122Workaround();
    }

    private void applyIOS122Workaround() {
        // iOS 12.2 sometimes fails to animate the menu away.
        // It should be gone after 240ms
        // This will make sure it disappears even when the browser fails.
        getUI().get().getPage().executeJavaScript(
                "var originalStyle = getComputedStyle($0).transitionProperty;" //
                        + "setTimeout(function() {$0.style.transitionProperty='padding'; requestAnimationFrame(function() {$0.style.transitionProperty=originalStyle})}, 250);",
                mainContent.getElement());
    }

    public NaviMenu getMenu() {
        return naviMenu;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        close();
    }
}