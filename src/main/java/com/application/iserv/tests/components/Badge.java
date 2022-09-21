package com.application.iserv.tests.components;


import com.application.iserv.tests.util.UIUtils;
import com.application.iserv.tests.util.css.lumo.BadgeColor;
import com.application.iserv.tests.util.css.lumo.BadgeShape;
import com.application.iserv.tests.util.css.lumo.BadgeSize;
import com.vaadin.flow.component.html.Span;

import java.util.StringJoiner;

import static com.application.iserv.tests.util.css.lumo.BadgeShape.PILL;

public class Badge extends Span {

	public Badge(String text) {
		this(text, BadgeColor.NORMAL);
	}

	public Badge(String text, BadgeColor color) {
		super(text);
		UIUtils.setTheme(color.getThemeName(), this);
	}

	public Badge(String text, BadgeColor color, BadgeSize size, BadgeShape shape) {
		super(text);
		StringJoiner joiner = new StringJoiner(" ");
		joiner.add(color.getThemeName());
		if (shape.equals(PILL)) {
			joiner.add(shape.getThemeName());
		}
		if (size.equals(BadgeSize.S)) {
			joiner.add(size.getThemeName());
		}
		UIUtils.setTheme(joiner.toString(), this);
	}

}
