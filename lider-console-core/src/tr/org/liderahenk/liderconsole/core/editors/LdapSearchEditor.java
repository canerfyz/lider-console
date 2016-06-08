package tr.org.liderahenk.liderconsole.core.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.labelproviders.LdapSearchLabelProvider;
import tr.org.liderahenk.liderconsole.core.ldap.listeners.LdapConnectionListener;
import tr.org.liderahenk.liderconsole.core.ldap.utils.LdapUtils;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.rest.utils.TaskUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.AttrNameCombo;
import tr.org.liderahenk.liderconsole.core.widgets.AttrOperator;
import tr.org.liderahenk.liderconsole.core.widgets.AttrValueText;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * New user-friendly LDAP search editor
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LdapSearchEditor extends EditorPart {

	private static Logger logger = LoggerFactory.getLogger(LdapSearchEditor.class);

	private ScrolledComposite sc;
	private Composite cmpSearchCritera;
	private Composite cmpTable;
	private AttrNameCombo cmbAttribute;
	private AttrOperator cmbOperator;
	private AttrValueText txtAttrValue;
	private Button btnAddCriteria;
	private Button btnSearchAgents;
	private Button btnSearchUsers;
	private Button btnSearch;
	private CheckboxTableViewer viewer;

	/**
	 * LDAP attributes
	 */
	private List<String> attributes;

	/**
	 * Agent properties
	 */
	private Map<String, String> properties;

	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		queryComboItems();
	}

	/**
	 * Populate comboItems array which will be used to populate criteria combos
	 * (AttrCombo)
	 */
	@SuppressWarnings("unchecked")
	private void queryComboItems() {
		try {
			IResponse response = TaskUtils.execute("LIDER-PERSISTENCE", "1.0.0", "GET-LDAP-SEARCH-ATTR");
			// LDAP search attributes (such as uid, liderPrivilege)
			attributes = (List<String>) response.getResultMap().get("attributes");
			// Agent properties (such as hostname, ipAddresses, os)
			properties = (Map<String, String>) response.getResultMap().get("properties");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		// Main composite
		Composite composite = new Composite(sc, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		sc.setContent(composite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		Label lblSearchCriteria = new Label(composite, SWT.NONE);
		lblSearchCriteria.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblSearchCriteria.setText(Messages.getString("LDAP_SEARCH_CRITERIA"));

		cmpSearchCritera = new Composite(composite, SWT.NONE);
		cmpSearchCritera.setLayout(new GridLayout(2, false));
		cmpSearchCritera.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		createSearchCriteria(cmpSearchCritera);

		// Search criterias can be added/removed dynamically
		btnAddCriteria = new Button(cmpSearchCritera, SWT.NONE);
		btnAddCriteria.setImage(
				new Image(cmpSearchCritera.getDisplay(), this.getClass().getResourceAsStream("/icons/16/add.png")));
		btnAddCriteria.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAddGroupButton(e);
			}
		});

		Label lblSearchScope = new Label(composite, SWT.NONE);
		lblSearchScope.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblSearchScope.setText(Messages.getString("LDAP_SEARCH_SCOPE"));

		Composite cmpSearchScope = new Composite(composite, SWT.NONE);
		cmpSearchScope.setLayout(new GridLayout(3, false));
		cmpSearchScope.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		btnSearchAgents = new Button(cmpSearchScope, SWT.RADIO);
		btnSearchAgents.setText(Messages.getString("LDAP_ONLY_AGENTS"));
		btnSearchAgents.setSelection(true);

		btnSearchUsers = new Button(cmpSearchScope, SWT.RADIO);
		btnSearchUsers.setText(Messages.getString("LDAP_ONLY_USERS"));

		btnSearch = new Button(composite, SWT.PUSH);
		btnSearch.setImage(new Image(parent.getDisplay(), this.getClass().getResourceAsStream("/icons/16/filter.png")));
		btnSearch.setText(Messages.getString("LDAP_DO_SEARCH"));
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eventBroker.send("ldap_entry_selected", null);
				doSearch();
			}
		});

		// cmpTable will be populated with table on LDAP search operations.
		// After each operation, current table will be disposed and a new table
		// will be created to mimic dynamically-created table columns in SWT.
		cmpTable = new Composite(composite, SWT.NONE);
		cmpTable.setLayout(new GridLayout(1, false));
		cmpTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer = CheckboxTableViewer.newCheckList(cmpTable, SWT.FULL_SELECTION);
		getSite().setSelectionProvider(viewer);

		viewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {

				ISelection selection = viewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object firstElement = ((IStructuredSelection) selection).getFirstElement();
					if (firstElement instanceof SearchResult) {
						eventBroker.send("ldap_entry_selected", firstElement);
					}
				}
			}
		});

		viewer.setContentProvider(new ArrayContentProvider());
		final Table table = viewer.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		table.getVerticalBar().setEnabled(true);
		table.getVerticalBar().setVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
	}

	/**
	 * Searches LDAP with specified attributes and scope, then prints results on
	 * the table
	 * 
	 */
	protected void doSearch() {

		String filter = computeFilterClause();
		String[] returningAttributes = findReturningAttributes();

		// First, do LDAP search
		List<SearchResult> result = LdapUtils.getInstance().searchAndReturnList(null, filter, returningAttributes,
				SearchControls.SUBTREE_SCOPE, 0, LdapConnectionListener.getConnection(),
				LdapConnectionListener.getMonitor());
		if (result != null && !result.isEmpty()) {
			// Then, filter these results by agent properties
			Map<String, String> propFilter = computePropertyFilter();
			if (propFilter != null && !propFilter.isEmpty()) {
				// TODO
				// TODO
				// TODO
				// TODO
				// TODO

			}
			recreateTable(returningAttributes);
			viewer.setInput(result);
			redraw();
		} else {
			emptyTable(returningAttributes);
		}
	}

	private Map<String, String> computePropertyFilter() {
		return null;
	}

	/**
	 * @param returningAttributes
	 */
	private void emptyTable(String[] returningAttributes) {
		recreateTable(returningAttributes);
		viewer.setInput(new ArrayList<SearchResult>());
		redraw();
	}

	/**
	 * @param returningAttributes
	 */
	private void recreateTable(String[] returningAttributes) {

		viewer.getTable().setRedraw(false);
		viewer.getTable().setHeaderVisible(true);

		disposeTableColumns();
		createTableColumns(viewer, returningAttributes);

		viewer.getTable().setRedraw(true);
	}

	/**
	 * 
	 */
	private void disposeTableColumns() {
		Table table = viewer.getTable();
		while (table.getColumnCount() > 0) {
			table.getColumns()[0].dispose();
		}
	}

	/**
	 * @param viewer
	 * @param returningAttributes
	 */
	private void createTableColumns(TableViewer viewer, String[] returningAttributes) {

		TableViewerColumn dnColumn = createTableViewerColumn(viewer, "Entry", 250);
		dnColumn.setLabelProvider(new LdapSearchLabelProvider());

		if (returningAttributes != null) {
			for (final String attr : returningAttributes) {
				if ("objectClass".equalsIgnoreCase(attr)) {
					continue; // ignore objectClass, show only search
								// parameters.
				}
				TableViewerColumn attrColumn = createTableViewerColumn(viewer, attr, 150);
				attrColumn.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						if (element instanceof SearchResult) {
							try {
								return ((SearchResult) element).getAttributes().get(attr).get().toString();
							} catch (NamingException e) {
								e.printStackTrace();
							}
						}
						return null;
					}
				});
			}
		}
	}

	private TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		column.setAlignment(SWT.LEFT);
		return viewerColumn;
	}

	/**
	 * @return
	 */
	private String computeFilterClause() {

		ArrayList<String> filterExpressions = new ArrayList<String>();
		filterExpressions.add("(objectClass=*)");

		if (btnSearchUsers.getSelection()) {
			String[] userObjClsArr = ConfigProvider.getInstance().getStringArr(LiderConstants.CONFIG.USER_LDAP_OBJ_CLS);
			for (String userObjCls : userObjClsArr) {
				filterExpressions.add("(objectClass=" + userObjCls + ")");
			}
		} else if (btnSearchAgents.getSelection()) {
			String[] agentObjClsArr = ConfigProvider.getInstance()
					.getStringArr(LiderConstants.CONFIG.AGENT_LDAP_OBJ_CLS);
			for (String agentObjCls : agentObjClsArr) {
				filterExpressions.add("(objectClass=" + agentObjCls + ")");
			}
		}

		Control[] children = cmpSearchCritera.getChildren();
		if (children != null) {
			for (Control child : children) {
				if (child instanceof Group) {
					Control[] gChildren = ((Group) child).getChildren();
					if (gChildren != null) {
						for (Control gChild : gChildren) {
							if (isValidAttributeValue(gChild)
									&& isValidAttribute(((AttrValueText) gChild).getRelatedAttrCombo())) {
								AttrNameCombo rAttrText = ((AttrValueText) gChild).getRelatedAttrCombo();
								AttrOperator rAttrOperator = ((AttrValueText) gChild).getRelatedAttrOperator();

								StringBuilder expression = new StringBuilder();
								expression.append("(").append(rAttrText.getText())
										.append(rAttrOperator.getItem(rAttrOperator.getSelectionIndex()))
										.append(((AttrValueText) gChild).getText()).append(")");
								filterExpressions.add(expression.toString());
							}
						}
					}
				}
			}
		}

		if (filterExpressions.size() == 1) {
			return filterExpressions.get(0);
		} else {
			// TODO simdilik n adet expression'i AND islemiyle birlestir.
			// TODO bir sonraki adimda ekrandan AND/OR/NOT operatorlerini de
			// al!
			String fExpr = StringUtils.join(filterExpressions, "");
			StringBuilder filter = new StringBuilder();
			filter.append("(&").append(fExpr).append(")");
			return filter.toString();
		}
	}

	/**
	 * @return an array of returning attributes
	 */
	private String[] findReturningAttributes() {

		ArrayList<String> returningAttributes = new ArrayList<String>();
		// Always add objectClass to returning attributes, to determine if an
		// entry belongs to a user or agent
		returningAttributes.add("objectClass");

		Control[] children = cmpSearchCritera.getChildren();
		if (children != null) {
			for (Control child : children) {
				if (child instanceof Group) {
					Control[] gChildren = ((Group) child).getChildren();
					if (gChildren != null) {
						for (Control gChild : gChildren) {
							if (isValidAttribute(gChild)) {
								returningAttributes.add(((AttrNameCombo) gChild).getText());
							}
						}
					}
				}
			}
		}

		return returningAttributes.toArray(new String[] {});
	}

	/**
	 * @param relatedAttrText
	 * @return true if attribute value is not empty or null and it is a LDAP
	 *         search attribute (not agent property), false otherwise.
	 */
	private boolean isValidAttribute(Control child) {
		return child instanceof AttrNameCombo && ((AttrNameCombo) child).getText() != null
				&& !((AttrNameCombo) child).getText().isEmpty()
				&& attributes.contains(((AttrNameCombo) child).getText());
	}

	/**
	 * @param child
	 * @return true if attribute name is not null or empty
	 */
	private boolean isValidAttributeValue(Control child) {
		return child instanceof AttrValueText && ((AttrValueText) child).getText() != null
				&& !((AttrValueText) child).getText().isEmpty();
	}

	protected void handleAddGroupButton(SelectionEvent e) {

		Composite parent = (Composite) ((Button) e.getSource()).getParent();

		createSearchCriteria(parent);

		Button btnRemoveGroup = new Button(parent, SWT.NONE);
		btnRemoveGroup
				.setImage(new Image(parent.getDisplay(), this.getClass().getResourceAsStream("/icons/16/remove.png")));
		btnRemoveGroup.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleRemoveGroupButton(e);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		redraw();
	}

	private void createSearchCriteria(Composite parent) {

		Group grpSearchCriteria = new Group(parent, SWT.NONE);
		grpSearchCriteria.setLayout(new GridLayout(3, false));
		grpSearchCriteria.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		cmbAttribute = new AttrNameCombo(grpSearchCriteria, SWT.BORDER | SWT.DROP_DOWN);
		cmbAttribute.setToolTipText("Öznitelik");
		cmbAttribute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		cmbAttribute.setItems(generateComboItems());
		cmbAttribute.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AttrNameCombo c = (AttrNameCombo) e.getSource();
				if (properties != null && properties.get(c.getText()) != null) {
					c.getRelatedAttrValue().setAutoCompleteProposals(properties.get(c.getText()).split(","));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		cmbOperator = new AttrOperator(grpSearchCriteria, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbOperator.setItems(new String[] { "=", "<", ">", "!=" });
		cmbOperator.select(0);

		txtAttrValue = new AttrValueText(grpSearchCriteria, SWT.BORDER | SWT.DROP_DOWN);
		txtAttrValue.setToolTipText("Değer");
		txtAttrValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	private String[] generateComboItems() {
		List<String> items = new ArrayList<String>();
		if (attributes != null) {
			items.addAll(attributes);
		}
		if (properties != null) {
			items.addAll(new ArrayList<String>(properties.keySet()));
		}
		return items.toArray(new String[items.size()]);
	}

	protected void handleRemoveGroupButton(SelectionEvent e) {
		Button thisBtn = (Button) e.getSource();
		Composite parent = thisBtn.getParent();
		Control[] children = parent.getChildren();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				if (children[i].equals(thisBtn) && i - 1 > 0) {
					children[i - 1].dispose();
					children[i].dispose();
					redraw();
					break;
				}
			}
		}
	}

	private void redraw() {
		sc.layout(true, true);
		sc.setMinSize(sc.getContent().computeSize(780, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		getSite().setSelectionProvider(null);
		super.dispose();
	}

}
