package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.Map;
import java.util.Set;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.SearchGroup;
import tr.org.liderahenk.liderconsole.core.model.SearchGroupEntry;
import tr.org.liderahenk.liderconsole.core.rest.utils.SearchGroupRestUtils;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

public class SearchGroupDialog extends DefaultLiderDialog {

	private static final Logger logger = LoggerFactory.getLogger(SearchGroupDialog.class);

	/**
	 * System-wide event broker
	 */
	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	// Widget
	private Text txtName;
	// Model
	private boolean searchAgents;
	private boolean searchUsers;
	private boolean searchGroups;
	private Map<String, String> criteria;
	private Set<SearchGroupEntry> entries;

	public SearchGroupDialog(Shell parentShell, boolean searchAgents, boolean searchUsers, boolean searchGroups,
			Map<String, String> criteria, Set<SearchGroupEntry> entries) {
		super(parentShell);
		this.searchAgents = searchAgents;
		this.searchUsers = searchUsers;
		this.searchGroups = searchGroups;
		this.criteria = criteria;
		this.entries = entries;
	}

	/**
	 * Create policy input widgets
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		parent.setLayout(new GridLayout(1, false));

		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		composite.setLayout(new GridLayout(2, false));

		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblName.setText(Messages.getString("SEARCH_GROUP_NAME"));

		txtName = new Text(composite, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		applyDialogFont(composite);
		return composite;
	}

	/**
	 * Handle OK button press
	 */
	@Override
	protected void okPressed() {

		setReturnCode(OK);

		// Check if name is empty
		if (txtName.getText().isEmpty()) {
			Notifier.warning(null, Messages.getString("FILL_SEARCH_GROUP_NAME_FIELD"));
			return;
		}

		try {
			SearchGroup sg = new SearchGroup(null, txtName.getText(), searchAgents, searchUsers, searchGroups, criteria,
					entries, null);
			SearchGroup searhGroup = SearchGroupRestUtils.add(sg);
			// Fire an event to refresh search group view
			eventBroker.post(LiderConstants.EVENT_TOPICS.SEARCH_GROUP_CREATED, searhGroup);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}
	}

}
