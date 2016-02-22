package tr.org.liderahenk.liderconsole.core.views;

import org.apache.directory.studio.ldapbrowser.ui.views.connection.Messages;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import tr.org.liderahenk.liderconsole.core.task.ParentTask;
import tr.org.liderahenk.liderconsole.core.task.Task;

/**
 * 
 * @author <a href="mailto:bm.volkansahin@gmail.com">volkansahin</a>
 *
 */

public class ParentTaskDetailDialog extends TitleAreaDialog{

	private TableViewer tblTaskDetail;
	private ParentTask parentTask;
	
	public ParentTaskDetailDialog(Shell activeShell,ParentTask parentTask) {
		super(activeShell);
		this.parentTask=parentTask;
		
	}
	
	@Override
	public void create() {
		super.create();
	
		setTitle(parentTask.getTaskName()); 
	    setMessage(Messages.getString("STATE")+":\t"+Messages.getString(parentTask.getState().toString())+"\n"
	    		+Messages.getString("START_DATE")+":\t"+parentTask.getCreationDate()); 
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite cmpMain = new Composite(area, GridData.FILL);		
		cmpMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cmpMain.setLayout(new GridLayout(1, false));
		createTable(cmpMain);
		refresh();
		
		return parent;
	}

	private void createTable(Composite cmpMain) {
		
		tblTaskDetail=new TableViewer(cmpMain, 
					SWT.SINGLE | SWT.H_SCROLL
			        | SWT.V_SCROLL | SWT.FULL_SELECTION 
			        | SWT.BORDER);	
		
		createTableColumns();
		
		final Table table = tblTaskDetail.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.getVerticalBar().setEnabled(true);
	    table.getVerticalBar().setVisible(true);
	    
	    GridData layoutData = new GridData(SWT.FILL,SWT.FILL,true,true);
	    layoutData.minimumHeight=300;
	    
	    tblTaskDetail.getTable().setLayoutData(layoutData);
	    tblTaskDetail.setContentProvider(new ArrayContentProvider());
	    
	}


	private void createTableColumns() {

		String[] titles = {
				Messages.getString("ID")
				,Messages.getString("ACTIVE")
				,Messages.getString("CREATION_DATE")
				,Messages.getString("CHANGED_DATE")
				,Messages.getString("VERSION")
				,Messages.getString("ORDER")
				,Messages.getString("PRIORITY")
				,Messages.getString("STATE")
				,Messages.getString("OWNER")
				};	
		
		int defaultSize =tblTaskDetail.getControl().getShell().getSize().x/titles.length;
		
		TableViewerColumn clmnId = createTableViewerColumn(titles[0], defaultSize);
		clmnId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getId();
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnActive = createTableViewerColumn(titles[1], defaultSize);
		clmnActive.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getActive().toString();
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnCreationDate = createTableViewerColumn(titles[2], defaultSize);
		clmnCreationDate.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getCreationDate();
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnChangedDate = createTableViewerColumn(titles[3], defaultSize);
		clmnChangedDate.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getChangedDate();
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnVersion = createTableViewerColumn(titles[4], defaultSize);
		clmnVersion.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getVersion().toString();
				}
				return Messages.getString("UNTITLED");
			}
		});

		
		TableViewerColumn clmnOrder = createTableViewerColumn(titles[5], defaultSize);
		clmnOrder.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getOrder().toString();
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnPriority = createTableViewerColumn(titles[6],defaultSize);
		clmnPriority.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getPriority().toString();
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnState = createTableViewerColumn(titles[7], defaultSize);
		clmnState.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return Messages.getString(((Task) element).getState().toString());
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnOwner = createTableViewerColumn(titles[8], defaultSize);
		clmnOwner.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Task) {
					return ((Task) element).getOwner();
				}
				return Messages.getString("UNTITLED");
			}
		});

		tblTaskDetail.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection selectedItem = (IStructuredSelection)event.getSelection();
			
				if(selectedItem.getFirstElement() instanceof Task){
					Task selectedTask = (Task)selectedItem.getFirstElement();
					TaskDetailDialog detailDialog = new TaskDetailDialog(Display.getDefault().getActiveShell(),selectedTask);
					detailDialog.create();
					detailDialog.open();
				}
			}
		});
	}

	private TableViewerColumn createTableViewerColumn( String title, int bound ) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(this.tblTaskDetail, SWT.NONE);
		final org.eclipse.swt.widgets.TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.setAlignment(SWT.LEFT);
		return viewerColumn;
	}

	private void refresh() {
		
	   	if(parentTask.getTasks()!=null && !parentTask.getTasks().isEmpty()){
	   		tblTaskDetail.getTable().clearAll();
	   		tblTaskDetail.setInput(parentTask.getTasks());
	   		tblTaskDetail.refresh();
	   		tblTaskDetail.getTable().getColumn(0).pack();
    	}
	}

}
