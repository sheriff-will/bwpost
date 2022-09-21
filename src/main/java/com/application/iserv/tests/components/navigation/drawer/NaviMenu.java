package com.application.iserv.tests.components.navigation.drawer;


import com.application.iserv.ui.payments.views.ReconcileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/components/navi-menu.css")
public class NaviMenu extends Div {

	private String CLASS_NAME = "navi-menu";

	public NaviMenu() {
		setClassName(CLASS_NAME);
	}

	protected void addNaviItem(NaviItem item) {
		add(item);
	}

	protected void addNaviItem(NaviItem parent, NaviItem item) {
		parent.addSubItem(item);
		addNaviItem(item);
	}

	public void filter(String filter) {
		getNaviItems(false).forEach(naviItem -> {
			boolean matches = ((NaviItem) naviItem).getText().toLowerCase()
					.contains(filter.toLowerCase());
			naviItem.setVisible(matches);
		});
	}

	public NaviItem addNaviItem(String text,
	                            Class<? extends Component> navigationTarget) {
		NaviItem item = new NaviItem(text, navigationTarget);
		addNaviItem(item);
		return item;
	}

	public NaviItem addNaviItem(VaadinIcon icon, String text,
								Class<? extends Component> navigationTarget) {
		NaviItem item = new NaviItem(icon, text, navigationTarget);
		addNaviItem(item);
		return item;
	}

	public NaviItem addNaviItem(Image image, String text,
	                            Class<? extends Component> navigationTarget) {
		NaviItem item = new NaviItem(image, text, navigationTarget);
		addNaviItem(item);
		return item;
	}

	public NaviItem addNaviItem(NaviItem parent, String text,
	                            Class<? extends Component> navigationTarget) {
		NaviItem item = new NaviItem(text, navigationTarget);
		addNaviItem(parent, item);
		return item;
	}

	public List<NaviItem> getNaviItems(boolean changeNavigationTarget) {

		List<NaviItem> items = (List) getChildren()
				.collect(Collectors.toList());

		if (changeNavigationTarget) {
			items.remove(1);
			items.add(1, new NaviItem(VaadinIcon.CREDIT_CARD, "Payments", ReconcileView.class));
		}

		return items;
	}

	public List<NaviItem> getNaviItemss() {
		List<NaviItem> items = (List) getChildren()
				.collect(Collectors.toList());
		return items;
	}


	public void replacePayments() {
		List<NaviItem> items = getNaviItems(true);

		System.err.println("Updated list: ");
		for (int i = 0; i < items.size(); i++) {
			System.err.println(items.get(i).getNavigationTarget().getName());
		}
	}
}
