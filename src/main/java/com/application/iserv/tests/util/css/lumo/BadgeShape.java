package com.application.iserv.tests.util.css.lumo;


public enum BadgeShape {

	NORMAL("normal"), PILL("pill");

	private String style;

	BadgeShape(String style) {
		this.style = style;
	}

	public String getThemeName() {
		return style;
	}

}
