/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package tr.org.liderahenk.liderconsole.core.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class creates the colors associated to a given theme
 * 
 */
public class NotifierColorsFactory {

	public enum NotifierTheme {
		ERROR_THEME, WARNING_THEME, SUCCESS_THEME, INFO_THEME 
	};

	/**
	 * Constructor
	 */
	private NotifierColorsFactory() {

	}

	/**
	 * @param theme
	 *            a theme for the notifier widget
	 * @return the color set for the given theme
	 */
	static NotifierColors getColorsForTheme(final NotifierTheme theme) {
		final NotifierColors colors = new NotifierColors();
		switch (theme) {
		case ERROR_THEME:
			colors.textColor = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			colors.titleColor = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			colors.borderColor = new Color(Display.getDefault(), 153, 188, 232);
			colors.leftColor = new Color(Display.getDefault(), 255, 69, 0);
			colors.rightColor = new Color(Display.getDefault(), 255, 127, 80);
			break;
		case WARNING_THEME:
			colors.textColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.titleColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.borderColor = new Color(Display.getDefault(), 208, 208, 208);
			colors.leftColor = new Color(Display.getDefault(), 255, 165, 0);
			colors.rightColor = new Color(Display.getDefault(), 255, 255, 153);
			break;
		case SUCCESS_THEME:
			colors.textColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.titleColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.borderColor = new Color(Display.getDefault(), 208, 208, 208);
			colors.leftColor = new Color(Display.getDefault(), 50, 205, 50);
			colors.rightColor = new Color(Display.getDefault(), 144, 238, 144);
			break;
		case INFO_THEME:
		default:
			colors.textColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.titleColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.borderColor = new Color(Display.getDefault(), 218, 178, 85);
			colors.leftColor = new Color(Display.getDefault(), 30, 144, 255);
			colors.rightColor = new Color(Display.getDefault(), 135, 206, 250);
			break;
		}
		return colors;
	}

}
