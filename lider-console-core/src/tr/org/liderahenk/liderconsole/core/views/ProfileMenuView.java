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
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
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
import org.eclipse.swt.widgets.Label;
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
					final String executionId = e.getAttribute("executionId");
					
					final String icon = e.getAttribute("icon");

					final Command command = commandService.getCommand(commandId);
					final Command execution = commandService.getCommand(executionId);
					
					final String profileDefinition = e.getAttribute("hasProfileDefinition");
					final String profileExecution = e.getAttribute("hasProfileExecution");
					
					String pluginId = e.getContributor().getName();
					final Image img = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, icon).createImage();
					
					GridLayout pluginLayout = new GridLayout(1, true);
					pluginLayout.verticalSpacing = 0;
					pluginLayout.horizontalSpacing = 0;
					pluginLayout.marginWidth = 0;
					
					Composite pluginComp = new Composite(composite, SWT.NONE);
					pluginComp.setLayout(pluginLayout);
					
					GridData data = new GridData();
				    float fontHeight = parent.getShell().getFont().getFontData()[0].height;
				    data.heightHint = img.getImageData().height + (int)fontHeight + 20;
				    data.widthHint = 100;
				    
					final Label pluginImage = ImageButton(pluginComp, img, null);
					pluginImage.setLayoutData(data);
					pluginImage.setToolTipText(command.getName());
					
					GridLayout btnLayout = new GridLayout(2, true);
					btnLayout.horizontalSpacing = 0;
					btnLayout.marginHeight = 0;
					btnLayout.marginWidth = 0;
					
					GridData d = new GridData(SWT.FILL);
					d.grabExcessHorizontalSpace = true;
					d.widthHint = 49;
					
					Composite buttonsComp = new Composite(pluginComp, SWT.NONE);
					buttonsComp.setLayout(btnLayout);
					
					if ("true".equals(profileDefinition) 
							&& commandId != null) {
						final Button profile = new Button(buttonsComp, SWT.PUSH);
						profile.setText("Profile");
						profile.setLayoutData(d);
						
						profile.addSelectionListener(new SelectionAdapter() {
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
					
					if ("true".equals(profileExecution) 
							&& executionId != null) {
						
						final Button action = new Button(buttonsComp, SWT.PUSH);
						action.setText("Action");
						action.setLayoutData(d);
						
						action.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								try {
									ExecutionEvent event = new ExecutionEvent();
									execution.executeWithChecks(event);
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

	/**
	 * @author Caner Feyzullahoğlu <caner.feyzullahoglu@agem.com.tr>
	 * @param parent 
	 * @param image
	 * @param mouseListener
	 * @return Creates a label with a custom image (ImageButton) 
	 * which works like a SWT.PUSH button and changes its display when mouse over. 
	 * <strong><i>mouseOverImage</i></strong> and
	 * <strong>mouseListener</strong> parameters can be passed as <strong>null</strong>
	 * if handling such event is not needed.
	 */
	public static Label ImageButton(Composite parent, final Image image, MouseListener mouseListener) {
		
		final Label imageButton = new Label(parent, SWT.BORDER);
		
		if (imageButton != null) {
			imageButton.setImage(image);
			imageButton.setAlignment(SWT.CENTER);
		}
		
		if (mouseListener != null) {
			imageButton.addMouseListener(mouseListener);
		}
		
		return imageButton;
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
