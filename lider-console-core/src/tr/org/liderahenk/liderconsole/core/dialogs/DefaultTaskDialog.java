package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestDNType;
import tr.org.liderahenk.liderconsole.core.rest.requests.TaskRequest;
import tr.org.liderahenk.liderconsole.core.rest.utils.TaskUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Default task dialog implementation that can be used by plugins in order to
 * provide task modification capabilities. Plugins should extend this class for
 * task execution.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public abstract class DefaultTaskDialog extends TitleAreaDialog {

	private static final Logger logger = LoggerFactory.getLogger(DefaultTaskDialog.class);

	private Button btnExecuteNow;
	private Button btnExecuteScheduled;

	private Set<String> dnSet;

	public DefaultTaskDialog(Shell parentShell, Set<String> dnSet) {
		super(parentShell);
		this.dnSet = dnSet;
	}

	/**
	 * 
	 * @return dialog title
	 */
	public abstract String createTitle();

	/**
	 * Create task related widgets here!
	 * 
	 * @param parent
	 * @return
	 */
	public abstract Control createTaskDialogArea(Composite parent);

	/**
	 * Validate task data here before sending it to Lider for execution.
	 * 
	 * @return
	 */
	public abstract boolean validateBeforeExecution();

	/**
	 * 
	 * @return parameter map of the task.
	 */
	public abstract Map<String, Object> getParameterMap();

	/**
	 * 
	 * @return command class ID
	 */
	public abstract String getCommandId();

	/**
	 * 
	 * @return plugin name
	 */
	public abstract String getPluginName();

	/**
	 * 
	 * @return plugin version
	 */
	public abstract String getPluginVersion();

	@Override
	public void create() {
		super.create();
		setTitle(createTitle());
		setMessage(generateMsg(dnSet), IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout(1, false));
		createTaskDialogArea(container);
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// Execute task now
		btnExecuteNow = createButton(parent, 5000, Messages.getString("EXECUTE_NOW"), false);
		btnExecuteNow.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/task-play.png"));
		btnExecuteNow.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (validateBeforeExecution()) {
					// TODO confirm box
					try {
						TaskRequest task = new TaskRequest(new ArrayList<String>(dnSet), RestDNType.AHENK,
								getPluginName(), getPluginVersion(), getCommandId(), getParameterMap(), null,
								new Date());
						TaskUtils.execute(task);
					} catch (Exception e1) {
						logger.error(e1.getMessage(), e1);
						Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		// Schedule task to be executed
		btnExecuteScheduled = createButton(parent, 5001, Messages.getString("EXECUTE_SCHEDULED"), false);
		btnExecuteScheduled.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/task-wait.png"));
		GridData gridData = new GridData();
		gridData.widthHint = 140;
		btnExecuteScheduled.setLayoutData(gridData);
		btnExecuteScheduled.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (validateBeforeExecution()) {
					SchedulerDialog dialog = new SchedulerDialog(Display.getDefault().getActiveShell());
					dialog.create();
					if (dialog.open() != Window.OK) {
						return;
					}
					// TODO confirm
					// TODO send
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		// Close
		createButton(parent, IDialogConstants.CLOSE_ID, Messages.getString("CLOSE"), true);
	}

	/**
	 * Generate title message from DN set. Abbreviate DNs if necessary.
	 * 
	 * @param dnSet
	 * @return
	 */
	private String generateMsg(Set<String> dnSet) {
		if (dnSet != null) {
			StringBuilder msg = new StringBuilder("");
			int i = 0;
			for (String dn : dnSet) {
				msg.append(dn).append(" ");
				if (i == 3) {
					break;
				}
				i++;
			}
			return msg.toString();
		}
		return "";
	}

}
