package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.e4.core.services.events.IEventBroker;
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
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.ldap.enums.DNType;
import tr.org.liderahenk.liderconsole.core.rest.requests.TaskRequest;
import tr.org.liderahenk.liderconsole.core.rest.utils.TaskRestUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.LiderConfirmBox;
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

	private IEventBroker eventBroker;
	private List<EventHandler> handlers;

	private Set<String> dnSet;

	public DefaultTaskDialog(Shell parentShell, Set<String> dnSet) {
		super(parentShell);
		this.dnSet = dnSet;
	}

	public DefaultTaskDialog(Shell parentShell, String dn) {
		super(parentShell);
		this.dnSet = new HashSet<String>();
		dnSet.add(dn);
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
	 * Validate task data here before sending it to Lider for execution. If
	 * validation fails for any of task data, this method should throws a
	 * {@link ValidationException}.
	 * 
	 * @return
	 */
	public abstract void validateBeforeExecution() throws ValidationException;

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
				// Validation of task data
				if (validateTaskData()) {
					if (LiderConfirmBox.open(Display.getDefault().getActiveShell(),
							Messages.getString("TASK_EXEC_TITLE"), Messages.getString("TASK_EXEC_MESSAGE"))) {
						try {
							TaskRequest task = new TaskRequest(new ArrayList<String>(dnSet), DNType.AHENK,
									getPluginName(), getPluginVersion(), getCommandId(), getParameterMap(), null,
									new Date());
							TaskRestUtils.execute(task);
						} catch (Exception e1) {
							logger.error(e1.getMessage(), e1);
							Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
						}
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
				// Validation of task data
				if (validateTaskData()) {
					SchedulerDialog dialog = new SchedulerDialog(Display.getDefault().getActiveShell());
					dialog.create();
					if (dialog.open() != Window.OK) {
						return;
					}
					if (LiderConfirmBox.open(Display.getDefault().getActiveShell(),
							Messages.getString("TASK_EXEC_SCHEDULED_TITLE"),
							Messages.getString("TASK_EXEC_SCHEDULED_MESSAGE"))) {
						try {
							TaskRequest task = new TaskRequest(new ArrayList<String>(dnSet), DNType.AHENK,
									getPluginName(), getPluginVersion(), getCommandId(), getParameterMap(),
									dialog.getCronExpression(), new Date());
							TaskRestUtils.execute(task);
						} catch (Exception e1) {
							logger.error(e1.getMessage(), e1);
							Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		// Close
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("CANCEL"), true);
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

	/**
	 * Handles validation result of task data.
	 */
	protected boolean validateTaskData() {
		try {
			validateBeforeExecution();
			return true;
		} catch (ValidationException e) {
			if (e.getMessage() != null && !"".equals(e.getMessage())) {
				Notifier.warning(null, e.getMessage());
			} else {
				Notifier.error(null, Messages.getString("ERROR_ON_VALIDATE"));
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			Notifier.error(null, Messages.getString("ERROR_ON_VALIDATE"));
			return false;
		}
	}

	public void openWithEventBroker() {
		super.setBlockOnOpen(true);
		super.open();
		unsubscribeEventHandlers();
	}

	public void subscribeEventHandler(EventHandler handler) {
		if (eventBroker == null) {
			eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);
		}
		eventBroker.subscribe(getPluginName().toUpperCase(Locale.ENGLISH), handler);
		if (handlers == null) {
			handlers = new ArrayList<EventHandler>();
		}
		handlers.add(handler);
	}

	public void unsubscribeEventHandlers() {
		if (handlers != null && !handlers.isEmpty() && eventBroker != null) {
			for (EventHandler handler : handlers) {
				eventBroker.unsubscribe(handler);
			}
		}
	}

	public Set<String> getDnSet() {
		return dnSet;
	}

}
