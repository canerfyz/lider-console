/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation 
 *******************************************************************************/
package tr.org.liderahenk.liderconsole.core.widgets;

import org.eclipse.swt.graphics.Color;

import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;

/**
 * This class is a simple POJO that holds colors used by the Notifier widget.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * 
 */
public class NotifierColors {
	Color titleColor;
	Color textColor;
	Color borderColor;
	Color leftColor;
	Color rightColor;

	void dispose() {
		// DO NOT dispose titleColor and textColor since we are not the ones
		// allocating them in the first place! They will be disposed by the
		// system if necessary.
		SWTResourceManager.safeDispose(this.borderColor);
		SWTResourceManager.safeDispose(this.leftColor);
		SWTResourceManager.safeDispose(this.rightColor);
	}
}
