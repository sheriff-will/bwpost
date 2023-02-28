package com.application.iserv.tests.components.navigation.drawer;

import com.application.iserv.backend.services.ParametersService;
import com.application.iserv.backend.services.ParticipantsServices;
import com.application.iserv.tests.util.UIUtils;
import com.application.iserv.ui.participants.views.ParticipantsView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;

@CssImport("./styles/components/navi-drawer.css")
@JsModule("./styles/swipe-away.js")
public class NaviDrawer extends Div
		implements AfterNavigationObserver {

	private String CLASS_NAME = "navi-drawer";
	private String RAIL = "rail";
	private String OPEN = "open";
	private Div scrim;

	ComboBox<String> filter = new ComboBox<>();

	private Div mainContent;
	private TextField search;
	private Div scrollableArea;

	private Button railButton;
	private NaviMenu menu;

	private final ParametersService parametersService;
	private final ParticipantsServices participantsServices;

	ParticipantsView participantsView;

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		UI ui = attachEvent.getUI();
		ui.getPage().executeJavaScript("window.addSwipeAway($0,$1,$2,$3)",
				mainContent.getElement(), this, "onSwipeAway",
				scrim.getElement());
	}

	@ClientCallable
	public void onSwipeAway(JsonObject data) {
		close();
	}

	public NaviDrawer(ParametersService parametersService, ParticipantsServices participantsServices) {

		setClassName(CLASS_NAME);

		initScrim();
		initMainContent();

		initHeader();
		initSearch();

		initScrollableArea();
		initMenu();

		initFooter();

		this.parametersService = parametersService;
		this.participantsServices = participantsServices;

	}

	private void initScrim() {
		// Backdrop on small viewports
		scrim = new Div();
		scrim.addClassName(CLASS_NAME + "__scrim");
		scrim.addClickListener(event -> close());
		add(scrim);
	}

	private void initMainContent() {
		mainContent = new Div();
		mainContent.addClassName(CLASS_NAME + "__content");
		add(mainContent);
	}

	private void initHeader() {
		mainContent.add(new BrandExpression("BotswanaPost"));
	}

	private void initSearch() {

		// Useful keep, To search drawer contents!
	/*	search = new TextField();
		search.addValueChangeListener(e -> menu.filter(search.getValue()));
		search.setClearButtonVisible(true);
		search.setPlaceholder("Search");
		search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
*/
		Tab botswanaPost = new Tab("BotswanaPost");
		Tab posoMoney = new Tab("PosoMoney");

		TextField names = new TextField();
		names.setValue("Will Mangwa");
		names.setReadOnly(true);

		TextField district = new TextField();
		district.setValue("Kgatleng");
		district.setReadOnly(true);

		names.getStyle()
				.set("padding-top", "0")
				.set("padding-bottom", "0");

		district.getStyle()
				.set("padding-top", "0")
				.set("padding-bottom", "0");

		botswanaPost.getStyle()
				.set("font-weight", "400");

		posoMoney.getStyle()
				.set("font-weight", "400");


		posoMoney.setEnabled(false);
		Tabs tabs = new Tabs(botswanaPost, posoMoney);
		tabs.addThemeVariants(
				TabsVariant.LUMO_SMALL
		);
		tabs.getStyle()
				.set("margin-bottom", "var(--lumo-space-l)");

		VerticalLayout verticalLayout = new VerticalLayout(names, district);
		verticalLayout.setMargin(false);
		verticalLayout.setPadding(false);

		filter.setClearButtonVisible(true);
		filter.setPlaceholder("Filter by village");
		filter.setItems("Oodi", "Modipane");

		filter.getStyle()
				.set("padding-top", "0")
				.set("padding-left", "var(--lumo-space-m)")
				.set("padding-right", "var(--lumo-space-m)");

		filter.setValue("Oodi");
		filter.setLabel("Kgatleng");

		filter.addValueChangeListener(filterValueChangeEvent -> {
			//fireEvent(new VillageEvent(this));
		//	EventBus.getDefault().postSticky(new StringModel("Hello"));
	//		EventBus.getDefault().register(this);

	//		EventBus.getDefault().post(new MessageEvent("Hello"));
			participantsView = new ParticipantsView(parametersService, participantsServices);

			participantsView.filterParticipantsByPlace(filterValueChangeEvent.getValue());

		});

		mainContent.add(tabs);

	}

	private void initScrollableArea() {
		scrollableArea = new Div();
		scrollableArea.addClassName(CLASS_NAME + "__scroll-area");
		mainContent.add(scrollableArea);
	}

	private void initMenu() {
		menu = new NaviMenu();
		scrollableArea.add(menu);
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
			filter.setVisible(true);

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
			filter.setVisible(false);

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
		return menu;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
		close();
	}

	// Events
	public static abstract class NaviDrawerFormEvent extends ComponentEvent<NaviDrawer> {

		protected NaviDrawerFormEvent(NaviDrawer source) {
			super(source, false);
		}

	}

	public static class VillageEvent extends NaviDrawerFormEvent {
		VillageEvent(NaviDrawer source) {
			super(source);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
																  ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
