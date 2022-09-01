package com.application.iserv.tests.components.detailsdrawer;

import com.application.iserv.tests.components.FlexBoxLayout;
import com.application.iserv.tests.layout.size.Horizontal;
import com.application.iserv.tests.layout.size.Right;
import com.application.iserv.tests.layout.size.Vertical;
import com.application.iserv.tests.util.LumoStyles;
import com.application.iserv.tests.util.UIUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.shared.Registration;

public class DetailsDrawerFooter extends FlexBoxLayout {

	private Button save;
	private Button cancel;

	public DetailsDrawerFooter() {
		setBackgroundColor(LumoStyles.Color.Contrast._5);
		setPadding(Horizontal.RESPONSIVE_L, Vertical.S);
		setSpacing(Right.S);
		setWidthFull();

		save = UIUtils.createPrimaryButton("Save");
		cancel = UIUtils.createTertiaryButton("Cancel");
		add(save, cancel);
	}

	public Registration addSaveListener(
			ComponentEventListener<ClickEvent<Button>> listener) {
		return save.addClickListener(listener);
	}

	public Registration addCancelListener(
			ComponentEventListener<ClickEvent<Button>> listener) {
		return cancel.addClickListener(listener);
	}

}
