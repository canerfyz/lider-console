package tr.org.liderahenk.liderconsole.core.views;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class ProfileMenuView extends ViewPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {

		ICommandService commandService = (ICommandService) getViewSite()
				.getService(ICommandService.class);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, true));
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		// Find ProfileMenu contributions
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(LiderConstants.ExtensionPoints.PROFILE_MENU);
		IConfigurationElement[] config = extensionPoint
				.getConfigurationElements();

		if (config != null) {
			for (IConfigurationElement e : config) {
				try {
					final String commandId = e.getAttribute("commandId");
					final String icon = e.getAttribute("icon");

					final Command command = commandService
							.getCommand(commandId);
					if (command != null) {
						// Add new button and its listener for the menu
						// contribution!
						final Button btn = new Button(composite, SWT.PUSH);
						btn.setToolTipText(command.getName());
						String pluginId = e.getContributor().getName();
						AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, icon).createImage();
						
						final Image img = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, icon).createImage();
						final String text =command.getName();
						
						GridData data = new GridData();
					    float fontHeight = parent.getShell().getFont().getFontData()[0].height;
					    data.heightHint = img.getImageData().height + (int)fontHeight + 20;
					    data.widthHint = 100;
						
					    btn.setLayoutData(data);
						btn.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
						
						FontData[] fD = btn.getFont().getFontData();
						fD[0].setHeight(7);
						btn.setFont( new Font(parent.getDisplay(),fD[0]));
						
						btn.addListener(SWT.Paint, new Listener() {

					        @Override
					        public void handleEvent(Event event) {
					            GC gc = event.gc;

					            int width = ((GridData)btn.getLayoutData()).widthHint;
					            int height = ((GridData)btn.getLayoutData()).heightHint;
					            Point textSize = gc.textExtent(text);

					            gc.drawText(text, width / 2 - textSize.x / 2, img.getImageData().height + textSize.y/2, true);
					            gc.drawImage(img, width / 2 - img.getImageData().width / 2, height / 2 - img.getImageData().height / 2 - textSize.y / 2);
					        }
					    });
						
						btn.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								try {
									command.executeWithChecks(new ExecutionEvent());
								} catch (ExecutionException e1) {
									e1.printStackTrace();
								} catch (NotDefinedException e1) {
									e1.printStackTrace();
								} catch (NotEnabledException e1) {
									e1.printStackTrace();
								} catch (NotHandledException e1) {
									e1.printStackTrace();
								}
							}
						});
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

}
