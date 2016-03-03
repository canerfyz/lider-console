package tr.org.liderahenk.liderconsole.core.views;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.policy.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.policy.IPolicy;

/**
 * 
 * @author <a href="mailto:mine.dogan@agem.com.tr">Mine Doğan</a>
 *
 */
public class PolicyMenuDialog extends TitleAreaDialog {
	
	private String ahenkDn;
	
	private Combo combo;
	private Button applyButton;
	private Button addButton;

	public PolicyMenuDialog(Shell parentShell, String ahenkDn) {
		super(parentShell);
		this.ahenkDn = ahenkDn;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(area, GridData.FILL);
		composite.setLayout(new GridLayout(3, true));
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));
		
		// Find PolicyMenu contributions
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(LiderConstants.ExtensionPoints.POLICY_MENU);
		IConfigurationElement[] config = extensionPoint
				.getConfigurationElements();
		
		if (config != null) {
			for (IConfigurationElement e : config) {
				
				try {
					String label = e.getAttribute("label");
					Object listClass = e.createExecutableExtension("listClass");
					
			        Label pluginLabel = new Label(composite, SWT.NONE);
					pluginLabel.setText(label);
					
					combo = new Combo(composite, SWT.DROP_DOWN);
					combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
					
					if (listClass instanceof IPolicy) {
				          executeExtension(listClass);
				    }
					
					addButton = new Button(composite, SWT.NONE);
					addButton.setText(Messages.getString("AddButton"));
					
					addButton.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
						}
					});
					
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("CloseButton"), true);
		
		applyButton = createButton(parent, 5000, Messages.getString("ApplyButton"), false);
		applyButton.setText(Messages.getString("ApplyButton"));
		
		applyButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
	}
	
	private void executeExtension(final Object o) {
		ISafeRunnable runnable = new ISafeRunnable() {
		      @Override
		      public void handleException(Throwable e) {
		      }

		      @Override
		      public void run() throws Exception {
		    	  String[] items = ((IPolicy) o).listProfiles();
		    	  try {
		    		  combo.setItems(items);
		    	  } catch (NullPointerException e1) {
						e1.printStackTrace();
		    	  }
		      }
		    };
		SafeRunner.run(runnable);
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString("ApplyPolicy"));
		setMessage(ahenkDn);
	}
}

