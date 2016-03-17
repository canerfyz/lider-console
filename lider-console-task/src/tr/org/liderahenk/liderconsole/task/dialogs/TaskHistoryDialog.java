package tr.org.liderahenk.liderconsole.task.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import tr.org.liderahenk.liderconsole.task.i18n.Messages;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class TaskHistoryDialog extends TitleAreaDialog {

	public TaskHistoryDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

//    private TableViewer tblVwrTasks;
//    
//    private final String taskId;
//	
//	private final List<TaskMessage> taskMessages;
//	
//	public TaskHistoryDialog(Shell parentShell, List<TaskMessage> taskMessages, String taskId) {
//		super(parentShell);
//		this.taskMessages = taskMessages;
//		this.taskId = taskId;
//	}
//	
//	/*
//	 * @see org.eclipse.jface.dialogs.Dialog#create()
//	 */
//	@Override
//	public void create() {
//		super.create();
//		setTitle(Messages.getString("TaskHistoryDialog.TASK_HISTORY")); //$NON-NLS-1$
//	    setMessage("Görev Id: " + this.taskId); //$NON-NLS-1$
//	}
//	
//	
//	/*
//	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
//	 */
//	@Override	
//	protected Control createDialogArea(Composite parent) {
//		Composite area = (Composite) super.createDialogArea(parent);
//		
//		Composite comp = new Composite(area, GridData.FILL);		
//		
//		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
//		GridLayout layout = new GridLayout(1, false);
//		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		comp.setLayout(layout);
//		
//		try {
//			createTaskTableArea(comp);
//		} catch (InvocationTargetException | InterruptedException e) {
//			e.printStackTrace();
//		}		
//		return area;
//	}
//	
//	private void createTaskTableArea(Composite container) throws InvocationTargetException, InterruptedException {
//
//		GridData dataSearchGrid = new GridData();
//		dataSearchGrid.grabExcessHorizontalSpace = true;
//		dataSearchGrid.horizontalAlignment = GridData.FILL;
//
//		tblVwrTasks = new TableViewer(container, SWT.SINGLE | SWT.H_SCROLL
//		        							| SWT.V_SCROLL | SWT.FULL_SELECTION 
//		        							| SWT.BORDER);
//		
//		createTableColumns(container, tblVwrTasks);
//		
//	    final Table table = tblVwrTasks.getTable();
//	    table.setHeaderVisible(true);
//	    table.setLinesVisible(true);
//	    table.getVerticalBar().setEnabled(true);
//	    table.getVerticalBar().setVisible(true);
//	    
//	    tblVwrTasks.setContentProvider(new ArrayContentProvider());  
//	    tblVwrTasks.setInput( taskMessages );
//
//	    
//	    GridData gridData = new GridData();
//	    
//	    gridData.verticalAlignment = GridData.FILL;
//	    gridData.horizontalSpan = 2;
//	    gridData.grabExcessHorizontalSpace = true;
//	    gridData.grabExcessVerticalSpace = true;
//	    gridData.heightHint = 160;
//	    gridData.horizontalAlignment = GridData.FILL;
//	    tblVwrTasks.getControl().setLayoutData(gridData);
//
//	}
//	
//	public TableViewer getViewer() { 
//		return this.tblVwrTasks;
//	}
//	
//	private void createTableColumns(final Composite container, final TableViewer scriptViewer) {
//		String[] titles = {Messages.getString("TaskHistoryDialog.DATE"), Messages.getString("TaskHistoryDialog.LEVEL"), Messages.getString("TaskHistoryDialog.MESSAGE")};				 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		int[] bounds = { 100, 100, 400 };
//		
//		// first column: title of the script
//		TableViewerColumn dateColumn = createTableViewerColumn(titles[0], bounds[0], 0);
//		dateColumn.setLabelProvider( new ColumnLabelProvider() {			
//			@Override
//			public String getText(Object element) {
//				if( element instanceof TaskMessage )
//					return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(((TaskMessage)element).getTimestamp()); //$NON-NLS-1$
//				return ""; //$NON-NLS-1$
//			}			
//		});
//		
//		TableViewerColumn levelColumn = createTableViewerColumn(titles[1], bounds[1], 1);
//		levelColumn.setLabelProvider( new ColumnLabelProvider() {			
//			@Override
//			public String getText(Object element) {				
//				if( element instanceof TaskMessage )
//					if (null != ((TaskMessage)element).getMessageLevel()) {
//						return ((TaskMessage)element).getMessageLevel().toString();	
//					}
//					else
//					{
//						return "";
//					}
//					
//				return ""; //$NON-NLS-1$
//			}			
//		});
//		
//		TableViewerColumn messageColumn = createTableViewerColumn(titles[2], bounds[2], 2);
//		messageColumn.setLabelProvider( new ColumnLabelProvider() {			
//			@Override
//			public String getText(Object element) {				
//				if( element instanceof TaskMessage )
//					return ((TaskMessage)element).getMessage();
//				return ""; //$NON-NLS-1$
//			}			
//		});	
//	}
//	
//	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
//	    final TableViewerColumn viewerColumn = new TableViewerColumn( getViewer(), SWT.NONE);
//	    final TableColumn column = viewerColumn.getColumn();
//	    column.setText(title);
//	    column.setWidth(bound);
//	    column.setResizable(true);
//	    column.setMoveable(false);
//	    column.setAlignment(SWT.LEFT);
//	    return viewerColumn;
//	}
//
//	/*
//	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
//	 */
//	@Override
//	protected boolean isResizable() {
//		return true;
//	}
//	
//	/*
//	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
//	 */
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, Messages.getString("TaskHistoryDialog.OK"), true);	 //$NON-NLS-1$
//	}
}
