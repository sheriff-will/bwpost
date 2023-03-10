package com.application.iserv.tests.components.navigation.bar;


import com.application.iserv.security.services.SecurityService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.tests.components.FlexBoxLayout;
import com.application.iserv.tests.components.navigation.tab.NaviTab;
import com.application.iserv.tests.components.navigation.tab.NaviTabs;
import com.application.iserv.tests.util.LumoStyles;
import com.application.iserv.tests.util.UIUtils;
import com.application.iserv.tests.views.Home;
import com.application.iserv.ui.utils.ApplicationUserDataModel;
import com.application.iserv.ui.utils.SessionManager;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;

import static com.application.iserv.ui.utils.Constants.*;

@CssImport("./styles/components/app-bar.css")
public class AppBar extends FlexBoxLayout {

	// SessionManager
	SessionManager sessionManager = new SessionManager();

	// ApplicationUser
	ApplicationUserDataModel applicationUserDataModel;

	Tabs viewTabs;
	Tab authorize = new Tab(AUTHORIZE);
	Tab reconcile = new Tab(RECONCILE);
	Tab history = new Tab(HISTORY);
	VerticalLayout layout;

	private String CLASS_NAME = "app-bar";

	private FlexBoxLayout container;

	private Button menuIcon;
	private Button contextIcon;

	private H4 title;
	private FlexBoxLayout actionItems;
	private Image avatar;

	private FlexBoxLayout tabContainer;
	private NaviTabs tabs;
	private ArrayList<Registration> tabSelectionListeners;
	private Button addTab;

	private TextField search;
	private Registration searchRegistration;
	private final SecurityService securityService;

	public enum NaviMode {
		MENU, CONTEXTUAL
	}


	public AppBar(String title, SecurityService securityService, NaviTab... tabs) {
		this.securityService = securityService;
		setClassName(CLASS_NAME);

		initMenuIcon();
		initContextIcon();
		initTitle(title);
		initSearch();
		initAvatar();
		initActionItems();
		initContainer();
		initTabs(tabs);
		configureTabs();
	}

	public void setNaviMode(NaviMode mode) {
		if (mode.equals(NaviMode.MENU)) {
			menuIcon.setVisible(true);
			contextIcon.setVisible(false);
		} else {
			menuIcon.setVisible(false);
			contextIcon.setVisible(true);
		}
	}

	private void initMenuIcon() {
		menuIcon = UIUtils.createTertiaryInlineButton(VaadinIcon.MENU);
		menuIcon.addClassName(CLASS_NAME + "__navi-icon");
		menuIcon.addClickListener(e -> MainLayout.get().getNaviDrawer().toggle());
		UIUtils.setAriaLabel("Menu", menuIcon);
		UIUtils.setLineHeight("1", menuIcon);
	}

	private void initContextIcon() {
		contextIcon = UIUtils
				.createTertiaryInlineButton(VaadinIcon.ARROW_LEFT);
		contextIcon.addClassNames(CLASS_NAME + "__context-icon");
		contextIcon.setVisible(false);
		UIUtils.setAriaLabel("Back", contextIcon);
		UIUtils.setLineHeight("1", contextIcon);
	}

	private void initTitle(String title) {
		this.title = new H4(title);
		this.title.setClassName(CLASS_NAME + "__title");
	}

	private void initSearch() {
		search = new TextField();
		search.setPlaceholder("Search");
		search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		search.setVisible(false);
	}

	private void initAvatar() {
		avatar = new Image();
		avatar.setClassName(CLASS_NAME + "__avatar");
		avatar.setSrc("images/shutdown.png");
		avatar.setAlt("User menu");

		ContextMenu contextMenu = new ContextMenu(avatar);
		contextMenu.setOpenOnClick(true);
		contextMenu.addItem("Log Out", e -> {
			securityService.logout();

			Notification.show("Log Out", 3000,
					Notification.Position.BOTTOM_CENTER);

		});
	}

	private void initActionItems() {
		actionItems = new FlexBoxLayout();
		actionItems.addClassName(CLASS_NAME + "__action-items");
		actionItems.setVisible(false);
	}

	private void initContainer() {

		ComboBox<String> filter = new ComboBox<>();
		filter.setPlaceholder("Filter by place");
		filter.setItems("Oodi", "Modipane");
		filter.getElement().getStyle().set("color", "white");
		filter.getElement().getStyle().set("-webkit-text-fill-color", "white");

		applicationUserDataModel = sessionManager.getApplicationUserData();

		String[] getFirstAndLastName = applicationUserDataModel.getUsername().split(" ");

		String[] getFirstnameChar = getFirstAndLastName[0].split("");
		String lastname = getFirstAndLastName[1];

		String usernameWithAbbreviation = getFirstnameChar[0]+"."+lastname;

		H5 village = new H5(usernameWithAbbreviation);
		village.setClassName(CLASS_NAME + "__village__title");

		village.addClickListener(click -> {
			Notification notification = new Notification(applicationUserDataModel.getUsername());
			notification.setPosition(Notification.Position.BOTTOM_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
			notification.setDuration(5000);
			notification.open();
		});

		container = new FlexBoxLayout(menuIcon, contextIcon, this.title,
				actionItems, village, avatar);
		container.addClassName(CLASS_NAME + "__container");
		container.setAlignItems(Alignment.CENTER);
		add(container);
	}

	public void configureTabs() {
		viewTabs = new Tabs(authorize, reconcile, history);
		layout = new VerticalLayout(viewTabs);
		layout.setAlignItems(Alignment.CENTER);
		layout.setSpacing(false);
		layout.setPadding(false);

		configureRightTabSelection();

		viewTabs.addSelectedChangeListener(tabChangeEvent -> {

			Tab selectedTab = tabChangeEvent.getSelectedTab();

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

		add(layout);
	}

	public void showRightTab(String title) {
		if (title.equalsIgnoreCase("Authorize")) {
			viewTabs.setSelectedTab(authorize);
		}
		else if (title.equalsIgnoreCase("Reconcile")) {
			viewTabs.setSelectedTab(reconcile);
		}
		else if (title.equalsIgnoreCase("History")) {
			viewTabs.setSelectedTab(history);
		}
	}

	public void configureRightTabSelection() {

		UI.getCurrent()
				.getPage()
				.executeJs("return window.location.href")
				.then(jsonValue -> {
					String urlPath = jsonValue.asString();

					if (urlPath.contains(AUTHORIZE_LOWER_CASE)) {
						viewTabs.setSelectedTab(authorize);
					}
					else if (urlPath.contains(RECONCILE_LOWER_CASE)) {
						viewTabs.setSelectedTab(reconcile);
					}
					else if (urlPath.contains(HISTORY_LOWER_CASE)) {
						viewTabs.setSelectedTab(history);
					}
				});

	}

	public void showTabs(boolean isTabsShowing) {
		layout.setVisible(isTabsShowing);
	}

	private void initTabs(NaviTab... tabs) {
		addTab = UIUtils.createSmallButton(VaadinIcon.PLUS);
		addTab.addClickListener(e -> this.tabs
				.setSelectedTab(addClosableNaviTab("New Tab", Home.class)));
		addTab.setVisible(false);

		this.tabs = tabs.length > 0 ? new NaviTabs(tabs) : new NaviTabs();
		this.tabs.setClassName(CLASS_NAME + "__tabs");
		this.tabs.setVisible(false);
		for (NaviTab tab : tabs) {
			configureTab(tab);
		}

		this.tabSelectionListeners = new ArrayList<>();

		tabContainer = new FlexBoxLayout(this.tabs, addTab);
		tabContainer.addClassName(CLASS_NAME + "__tab-container");
		tabContainer.setAlignItems(Alignment.CENTER);

		add(tabContainer);
	}

	/* === MENU ICON === */

	public Button getMenuIcon() {
		return menuIcon;
	}

	/* === CONTEXT ICON === */

	public Button getContextIcon() {
		return contextIcon;
	}

	public void setContextIcon(Icon icon) {
		contextIcon.setIcon(icon);
	}

	/* === TITLE === */

	public String getTitle() {
		return this.title.getText();
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	/* === ACTION ITEMS === */

	public Component addActionItem(Component component) {
		actionItems.add(component);
		updateActionItemsVisibility();
		return component;
	}

	public Button addActionItem(VaadinIcon icon) {
		Button button = UIUtils.createButton(icon, ButtonVariant.LUMO_SMALL,
				ButtonVariant.LUMO_TERTIARY);
		addActionItem(button);
		return button;
	}

	public void removeAllActionItems() {
		actionItems.removeAll();
		updateActionItemsVisibility();
	}

	/* === AVATAR == */

	public Image getAvatar() {
		return avatar;
	}

	/* === TABS === */

	public void centerTabs() {
		tabs.addClassName(LumoStyles.Margin.Horizontal.AUTO);
	}

	private void configureTab(Tab tab) {
		tab.addClassName(CLASS_NAME + "__tab");
		updateTabsVisibility();
	}

	public Tab addTab(String text) {
		Tab tab = tabs.addTab(text);
		configureTab(tab);
		return tab;
	}

	public Tab addTab(String text,
	                  Class<? extends Component> navigationTarget) {
		Tab tab = tabs.addTab(text, navigationTarget);
		configureTab(tab);
		return tab;
	}

	public Tab addClosableNaviTab(String text,
	                              Class<? extends Component> navigationTarget) {
		Tab tab = tabs.addClosableTab(text, navigationTarget);
		configureTab(tab);
		return tab;
	}

	public Tab getSelectedTab() {
		return tabs.getSelectedTab();
	}

	public void setSelectedTab(Tab selectedTab) {
		tabs.setSelectedTab(selectedTab);
	}

	public void updateSelectedTab(String text,
	                              Class<? extends Component> navigationTarget) {
		tabs.updateSelectedTab(text, navigationTarget);
	}

	public void navigateToSelectedTab() {
		tabs.navigateToSelectedTab();
	}

	public void addTabSelectionListener(
			ComponentEventListener<Tabs.SelectedChangeEvent> listener) {
		Registration registration = tabs.addSelectedChangeListener(listener);
		tabSelectionListeners.add(registration);
	}

	public int getTabCount() {
		return tabs.getTabCount();
	}

	public void removeAllTabs() {
		tabSelectionListeners.forEach(registration -> registration.remove());
		tabSelectionListeners.clear();
		tabs.removeAll();
		updateTabsVisibility();
	}

	/* === ADD TAB BUTTON === */

	public void setAddTabVisible(boolean visible) {
		addTab.setVisible(visible);
	}

	/* === SEARCH === */

	public void searchModeOn() {
		menuIcon.setVisible(false);
		title.setVisible(false);
		actionItems.setVisible(false);
		tabContainer.setVisible(false);

		contextIcon.setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
		contextIcon.setVisible(true);
		searchRegistration = contextIcon
				.addClickListener(e -> searchModeOff());

		search.setVisible(true);
		search.focus();
	}

	public void addSearchListener(HasValue.ValueChangeListener listener) {
		search.addValueChangeListener(listener);
	}

	public void setSearchPlaceholder(String placeholder) {
		search.setPlaceholder(placeholder);
	}

	private void searchModeOff() {
		menuIcon.setVisible(true);
		title.setVisible(true);
		tabContainer.setVisible(true);

		updateActionItemsVisibility();
		updateTabsVisibility();

		contextIcon.setVisible(false);
		searchRegistration.remove();

		search.clear();
		search.setVisible(false);
	}

	/* === RESET === */

	public void reset() {
		title.setText("");
		setNaviMode(NaviMode.MENU);
		removeAllActionItems();
		removeAllTabs();
	}

	/* === UPDATE VISIBILITY === */

	private void updateActionItemsVisibility() {
		actionItems.setVisible(actionItems.getComponentCount() > 0);
	}

	private void updateTabsVisibility() {
		tabs.setVisible(tabs.getComponentCount() > 0);
	}

}
