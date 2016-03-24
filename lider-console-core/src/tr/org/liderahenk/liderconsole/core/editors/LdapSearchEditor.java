package tr.org.liderahenk.liderconsole.core.editors;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.labelproviders.LdapSearchLabelProvider;
import tr.org.liderahenk.liderconsole.core.ldap.LdapUtils;
import tr.org.liderahenk.liderconsole.core.listeners.LdapConnectionListener;
import tr.org.liderahenk.liderconsole.core.widgets.AttrOperator;
import tr.org.liderahenk.liderconsole.core.widgets.AttrText;
import tr.org.liderahenk.liderconsole.core.widgets.AttrValueText;

/**
 * New user-friendly LDAP search editor
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LdapSearchEditor extends EditorPart {

	private ScrolledComposite sc;
	private Composite cmpSearchCritera;
	private Composite cmpTable;
	private AttrText txtAttribute;
	private AttrOperator cmbOperator;
	private AttrValueText txtAttrValue;
	private Button btnAddCriteria;
	private Button btnSearchAgents;
	private Button btnSearchUsers;
	private Button btnSearch;
	private CheckboxTableViewer viewer;

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
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		composite.setBackgroundMode(SWT.INHERIT_FORCE);

		sc.setContent(composite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		Label lblSearchCriteria = new Label(composite, SWT.NONE);
		lblSearchCriteria.setText("Arama Kriteri");
		lblSearchCriteria.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

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
		lblSearchScope.setText("Arama Kapsamı");
		lblSearchScope.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));

		Composite cmpSearchScope = new Composite(composite, SWT.NONE);
		cmpSearchScope.setLayout(new GridLayout(3, false));
		cmpSearchScope.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		btnSearchAgents = new Button(cmpSearchScope, SWT.RADIO);
		btnSearchAgents.setText("Sadece Ahenkler");
		btnSearchAgents.setSelection(true);

		btnSearchUsers = new Button(cmpSearchScope, SWT.RADIO);
		btnSearchUsers.setText("Sadece Kullanıcılar");

		btnSearch = new Button(composite, SWT.PUSH);
		btnSearch.setImage(new Image(parent.getDisplay(), this.getClass().getResourceAsStream("/icons/16/filter.png")));
		btnSearch.setText("Aramayı Başlat");
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eventBroker.send("ldap_entry_selected", null);
				doLdapSearch();
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

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO dialogda detay getir
				System.out.println(event);
			}
		});
		viewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {

				ISelection selection = viewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object firstElement = ((IStructuredSelection) selection).getFirstElement();
					if (firstElement instanceof SearchResult) {
						eventBroker.send("ldap_entry_selected", firstElement);
						// TODO sunumdan sonra silinecek
						eventBroker.send("task_service_update_dn_list", ((IStructuredSelection) selection));
					}
				}
			}
		});

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
	protected void doLdapSearch() {

		String[] returningAttributes = findReturningAttributes();
		String filter = computeFilterClause();

		// TODO change countLimit to use paging (research PagedSearch in Control
		// class)
		List<SearchResult> result = LdapUtils.getInstance().searchAndReturnList(null, filter, returningAttributes,
				SearchControls.SUBTREE_SCOPE, 0, LdapConnectionListener.getConnection(),
				LdapConnectionListener.getMonitor());

		if (result != null && !result.isEmpty()) {
			recreateTable(returningAttributes);
			viewer.setInput(result);
			redraw();
		} else {
			emptyTable(returningAttributes);
		}
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
									&& isValidAttribute(((AttrValueText) gChild).getRelatedAttrText())) {
								AttrText rAttrText = ((AttrValueText) gChild).getRelatedAttrText();
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
								returningAttributes.add(((AttrText) gChild).getText());
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
	 * @return
	 */
	private boolean isValidAttribute(Control child) {
		return child instanceof AttrText && ((AttrText) child).getText() != null
				&& !((AttrText) child).getText().isEmpty();
	}

	/**
	 * @param child
	 * @return
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
				.setImage(new Image(parent.getDisplay(), this.getClass().getResourceAsStream("/icons/remove.png")));
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

		txtAttribute = new AttrText(grpSearchCriteria, SWT.NONE);
		txtAttribute.setToolTipText("Öznitelik");
		txtAttribute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		cmbOperator = new AttrOperator(grpSearchCriteria, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbOperator.setItems(new String[] { "=", "<", ">", "!=" });
		cmbOperator.select(0);

		txtAttrValue = new AttrValueText(grpSearchCriteria, SWT.NONE);
		txtAttrValue.setToolTipText("Değer");
		txtAttrValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
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
