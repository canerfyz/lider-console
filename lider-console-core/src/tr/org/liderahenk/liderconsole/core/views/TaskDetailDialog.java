package tr.org.liderahenk.liderconsole.core.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.RestRequest;
import tr.org.liderahenk.liderconsole.core.rest.RestResponse;
import tr.org.liderahenk.liderconsole.core.task.Messages;
import tr.org.liderahenk.liderconsole.core.task.Task;

/**
 * 
 * @author <a href="mailto:bm.volkansahin@gmail.com">volkansahin</a>
 *
 */
public class TaskDetailDialog extends TitleAreaDialog {

	private Task selectedTask;
	private TableViewer tblDetail;
	private List<Row> recordList;
	
	Button btn;
	
	public TaskDetailDialog(Shell parentShell, Task selectedTask) {
		super(parentShell);
		this.selectedTask=selectedTask;
	}
	
	@Override
	public void create() {
		super.create();
		
		setTitle(selectedTask.getId());
	    setMessage(Messages.getString("STATE")+":\t"+Messages.getString(selectedTask.getState().toString())+"\n"
	    		+Messages.getString("START_DATE")+":\t"+selectedTask.getCreationDate()); 
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite cmpMain = new Composite(area, GridData.FILL);		
		cmpMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		cmpMain.setLayout(new GridLayout(1, false));
		createTable(cmpMain);
		
		btn = new Button(cmpMain, SWT.None);
		btn.setText(Messages.getString("BUTTON"));
		btn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				getLastTasks(10);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		refresh();
		
		return parent;
		
	}

	private List<Task> getLastTasks(Integer quantity) {
		
		
		final Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("quantity",quantity );
		
		RestRequest request = new RestRequest();
		request.setPluginName("TASK_OBSERVER");
		request.setPluginVersion("1.0.0");
		request.setCommandId("commandId");
		request.setParameterMap(parameterMap);
		
		RestResponse response = RestClient.getInstance().post(request);
		
//		System.out.println(response.getResultMap().isEmpty());;
		
		return null;
	}
	
	private void createTable(Composite cmpMain) {

		tblDetail=new TableViewer(cmpMain, 
				  SWT.SINGLE | SWT.H_SCROLL
			        | SWT.V_SCROLL | SWT.FULL_SELECTION 
			        | SWT.BORDER);	
		
		createTableColumns();
		
		final Table table = tblDetail.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.getVerticalBar().setEnabled(true);
	    table.getVerticalBar().setVisible(true);
	    
	    GridData layoutData = new GridData(SWT.FILL,SWT.FILL,true,true);
	    layoutData.minimumHeight=300;
	    
	    tblDetail.getTable().setLayoutData(layoutData);
	    tblDetail.setContentProvider(new ArrayContentProvider());
		
	}
	
	private void createTableColumns() {

		String[] titles = {Messages.getString("NAME"), Messages.getString("VALUE")};				
		int[] bounds = { 400, 400 };

		TableViewerColumn clmnName = createTableViewerColumn(titles[0], bounds[0]);
		clmnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Row) {
					return ((Row) element).getName();
				}
				return Messages.getString("UNTITLED");
			}
		});
		
		TableViewerColumn clmnValue = createTableViewerColumn(titles[1], bounds[1]);
		clmnValue.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Row) {
					return ((Row) element).getValue();
				}
				return Messages.getString("UNTITLED");
			}
		});
	}
	
	private TableViewerColumn createTableViewerColumn( String title, int bound ) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(this.tblDetail, SWT.NONE);
		final org.eclipse.swt.widgets.TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		column.setAlignment(SWT.LEFT);
		return viewerColumn;
	}
	
	private void refresh() {

		recordList = new ArrayList<TaskDetailDialog.Row>();
		recordList.add(new Row(Messages.getString("ORDER"), ""+selectedTask.getOrder()));
		recordList.add(new Row(Messages.getString("TASK_NAME"), selectedTask.getTaskName()));
		recordList.add(new Row(Messages.getString("PLUGIN_ID"), selectedTask.getPluginId()));
		recordList.add(new Row(Messages.getString("PRIORITY"), ""+selectedTask.getPriority()));
		recordList.add(new Row(Messages.getString("OWNER"), selectedTask.getOwner()));
		recordList.add(new Row(Messages.getString("PARENT_TASK_ID"), selectedTask.getParentTaskId()));
		recordList.add(new Row(Messages.getString("TARGET_JID"), selectedTask.getTargetJID()));
		recordList.add(new Row(Messages.getString("TARGET_OBJECT_DN"), selectedTask.getTargetObjectDN()));

    	if(recordList!=null && !recordList.isEmpty()){
    		tblDetail.getTable().clearAll();
    		tblDetail.setInput(recordList);
    		tblDetail.refresh();
    		tblDetail.getTable().getColumn(0).pack();
    	}
	}

	class Row{
		String name;
		String value;
		
		public Row(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}

}
