package com.application.iserv.tests;

import com.application.iserv.security.services.SecurityService;
import com.application.iserv.tests.components.FlexBoxLayout;
import com.application.iserv.tests.components.navigation.bar.AppBar;
import com.application.iserv.tests.components.navigation.bar.TabBar;
import com.application.iserv.tests.components.navigation.drawer.NaviDrawer;
import com.application.iserv.tests.components.navigation.drawer.NaviItem;
import com.application.iserv.tests.components.navigation.drawer.NaviMenu;
import com.application.iserv.tests.util.css.Overflow;
import com.application.iserv.tests.views.Statistics;
import com.application.iserv.ui.parameters.views.ParametersView;
import com.application.iserv.ui.participants.views.ParticipantsView;
import com.application.iserv.ui.payments.views.AuthorizeView;
import com.application.iserv.ui.payments.views.HistoryView;
import com.application.iserv.ui.payments.views.ReconcileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.application.iserv.ui.utils.Constants.*;

@CssImport(value = "./styles/components/charts.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
@CssImport(value = "./styles/components/floating-action-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/grid.css", themeFor = "vaadin-grid")
@CssImport("./styles/lumo/border-radius.css")
@CssImport("./styles/lumo/icon-size.css")
@CssImport("./styles/lumo/margin.css")
@CssImport("./styles/lumo/padding.css")
@CssImport("./styles/lumo/shadow.css")
@CssImport("./styles/lumo/spacing.css")
@CssImport("./styles/lumo/typography.css")
@CssImport("./styles/misc/box-shadow-borders.css")
@CssImport(value = "./styles/styles.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge")
public class MainLayout extends FlexBoxLayout
		implements RouterLayout, AfterNavigationObserver {

	private static final Logger log = LoggerFactory.getLogger(MainLayout.class);
	private static final String CLASS_NAME = "root";

	private Div appHeaderOuter;

	private FlexBoxLayout row;
	private NaviDrawer naviDrawer;
	private FlexBoxLayout column;

	private Div appHeaderInner;
	private FlexBoxLayout viewContainer;
	private Div appFooterInner;

	private Div appFooterOuter;

	private TabBar tabBar;
	private boolean navigationTabs = false;
	private AppBar appBar;

	private final SecurityService securityService;

	public MainLayout(SecurityService securityService) {
		this.securityService = securityService;
		VaadinSession.getCurrent()
				.setErrorHandler((ErrorHandler) errorEvent -> {
					log.error("Uncaught UI exception",
							errorEvent.getThrowable());
					/*Notification.show(
							"We are sorry, but an internal error occurred");*/
					System.err.println("We are sorry, but an internal error occurred");
				});

		addClassName(CLASS_NAME);
		setFlexDirection(FlexDirection.COLUMN);
		setSizeFull();

		// Initialise the UI building blocks
		initStructure();

		// Populate the navigation drawer
		initNaviItems();

		// Configure the headers and footers (optional)
		initHeadersAndFooters();

	}

	/**
	 * Initialise the required components and containers.
	 */
	private void initStructure() {
		naviDrawer = new NaviDrawer();

		viewContainer = new FlexBoxLayout();
		viewContainer.addClassName(CLASS_NAME + "__view-container");
		viewContainer.setOverflow(Overflow.AUTO);

		column = new FlexBoxLayout(viewContainer);
		column.addClassName(CLASS_NAME + "__column");
		column.setFlexDirection(FlexDirection.COLUMN);
		column.setFlexGrow(1, viewContainer);
		column.setOverflow(Overflow.AUTO);

		row = new FlexBoxLayout(naviDrawer, column);
		row.addClassName(CLASS_NAME + "__row");
		row.setFlexGrow(1, column);
		row.setOverflow(Overflow.AUTO);
		add(row);
		setFlexGrow(1, row);
	}

	/**
	 * Initialise the navigation items.
	 */
	private void initNaviItems() {
		NaviMenu menu = naviDrawer.getMenu();
		menu.addNaviItem(VaadinIcon.USERS, PARTICIPANTS, ParticipantsView.class);

		NaviItem payments = menu.addNaviItem(VaadinIcon.CREDIT_CARD, PAYMENTS, null);
		menu.addNaviItem(payments, AUTHORIZE, AuthorizeView.class);
		menu.addNaviItem(payments, RECONCILE, ReconcileView.class);
		menu.addNaviItem(payments, HISTORY, HistoryView.class);

		menu.addNaviItem(VaadinIcon.TOOLBOX, PARAMETERS, ParametersView.class);
		menu.addNaviItem(VaadinIcon.CHART_3D, REPORTS, Statistics.class);

		payments.setSubItemsVisible(false);

	}

	/**
	 * Configure the app's inner and outer headers and footers.
	 */
	private void initHeadersAndFooters() {
		// setAppHeaderOuter();
		// setAppFooterInner();
		// setAppFooterOuter();

		// Default inner header setup:
		// - When using tabbed navigation the view title, user avatar and main menu button will appear in the TabBar.
		// - When tabbed navigation is turned off they appear in the AppBar.

		appBar = new AppBar("", securityService);

		// Tabbed navigation
		if (navigationTabs) {
			tabBar = new TabBar();
			// TODO Theme here
//			UIUtils.setTheme(Lumo.DARK, tabBar);

			// Shift-click to add a new tab
			for (NaviItem item : naviDrawer.getMenu().getNaviItems(false)) {
				item.addClickListener(e -> {
					if (e.getButton() == 0 && e.isShiftKey()) {
						tabBar.setSelectedTab(tabBar.addClosableTab(item.getText(), item.getNavigationTarget()));
					}
				});
			}
			appBar.getAvatar().setVisible(false);
			setAppHeaderInner(tabBar, appBar);

			// Default navigation
		} else {
			// TODO Theme here
//			UIUtils.setTheme(Lumo.DARK, appBar);
			setAppHeaderInner(appBar);
		}
	}

	private void setAppHeaderOuter(Component... components) {
		if (appHeaderOuter == null) {
			appHeaderOuter = new Div();
			appHeaderOuter.addClassName("app-header-outer");
			getElement().insertChild(0, appHeaderOuter.getElement());
		}
		appHeaderOuter.removeAll();
		appHeaderOuter.add(components);
	}

	private void setAppHeaderInner(Component... components) {
		if (appHeaderInner == null) {
			appHeaderInner = new Div();
			appHeaderInner.addClassName("app-header-inner");
			column.getElement().insertChild(0, appHeaderInner.getElement());
		}
		appHeaderInner.removeAll();
		appHeaderInner.add(components);
	}

	private void setAppFooterInner(Component... components) {
		if (appFooterInner == null) {
			appFooterInner = new Div();
			appFooterInner.addClassName("app-footer-inner");
			column.getElement().insertChild(column.getElement().getChildCount(),
					appFooterInner.getElement());
		}
		appFooterInner.removeAll();
		appFooterInner.add(components);
	}

	private void setAppFooterOuter(Component... components) {
		if (appFooterOuter == null) {
			appFooterOuter = new Div();
			appFooterOuter.addClassName("app-footer-outer");
			getElement().insertChild(getElement().getChildCount(),
					appFooterOuter.getElement());
		}
		appFooterOuter.removeAll();
		appFooterOuter.add(components);
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		this.viewContainer.getElement().appendChild(content.getElement());
	}

	public NaviDrawer getNaviDrawer() {
		return naviDrawer;
	}

	public static MainLayout get() {
		return (MainLayout) UI.getCurrent().getChildren()
				.filter(component -> component.getClass() == MainLayout.class)
				.findFirst().get();
	}

	public AppBar getAppBar() {
		return appBar;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (navigationTabs) {
			afterNavigationWithTabs(event);
		} else {
			afterNavigationWithoutTabs(event);
		}
	}

	private void afterNavigationWithTabs(AfterNavigationEvent e) {
		NaviItem active = getActiveItem(e);
		if (active == null) {
			if (tabBar.getTabCount() == 0) {
				tabBar.addClosableTab("", ParticipantsView.class);
			}
		} else {
			if (tabBar.getTabCount() > 0) {
				tabBar.updateSelectedTab(active.getText(),
						active.getNavigationTarget());
			} else {
				tabBar.addClosableTab(active.getText(),
						active.getNavigationTarget());
			}
		}
		appBar.getMenuIcon().setVisible(false);

	}

	private NaviItem getActiveItem(AfterNavigationEvent e) {
		for (NaviItem item : naviDrawer.getMenu().getNaviItems(false)) {
			if (item.isHighlighted(e)) {
				return item;
			}
		}
		return null;
	}

	private void afterNavigationWithoutTabs(AfterNavigationEvent e) {
		NaviItem active = getActiveItem(e);

		if (active != null) {
			String title = active.getText();
			getAppBar().setTitle(active.getText());

			if (title.contains(PARTICIPANTS)) {
				getAppBar().showTabs(false);
			}
			else if (title.contains(AUTHORIZE)) {
				appBar.showRightTab("Authorize");
				getAppBar().setTitle("Payments");
				getAppBar().showTabs(true);
			}
			else if (title.contains(RECONCILE)) {
				getAppBar().setTitle("Payments");
				appBar.showRightTab("Reconcile");
				getAppBar().showTabs(true);
			}
			else if (title.contains(HISTORY)) {
				getAppBar().setTitle("Payments");
				appBar.showRightTab("History");
				getAppBar().showTabs(true);
			}
			else if (title.contains(PARAMETERS)) {
				getAppBar().showTabs(false);
			}
			else if (title.contains(REPORTS)) {
				getAppBar().showTabs(false);
			}

		}
	}

}
