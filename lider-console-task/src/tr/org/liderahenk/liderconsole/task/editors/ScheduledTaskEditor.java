package tr.org.liderahenk.liderconsole.task.editors;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import tr.org.liderahenk.liderconsole.task.i18n.Messages;
//import tr.org.liderahenk.liderconsole.core.MysProgressMonitorDialog;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
//import tr.org.liderahenk.liderconsole.core.schedule.ScheduleRequest;
import tr.org.liderahenk.liderconsole.core.task.Scheduler;
import tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class ScheduledTaskEditor extends EditorPart {
	
	@Inject public EModelService modelService;
	@Inject public MApplication application;
	@Inject public EPartService ePartService;
	
	private TableViewer tblVwrTasks;
	private TableViewerColumn creationDateColumn;
	private List<Scheduler> currentTasks;	
	
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "tr.org.liderahenk.liderconsole.task.views.ScheduledTaskEditor";
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		//must override.
	}

	@Override
	public void doSaveAs() {
		//must override.
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
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
	public void createPartControl(final Composite parent) {
		GridData d1 = new GridData();
		d1.horizontalAlignment = SWT.FILL;
		d1.grabExcessHorizontalSpace = true;

		final Composite composite = new Composite (parent, SWT.NONE);
	    
		composite.setLayout(new GridLayout(3, false));
	    
		Button refresh = new Button(composite, SWT.NONE);
		//final Display display = new Display();
		refresh.setImage(new Image(composite.getDisplay(), this.getClass().getResourceAsStream("/icons/refresh.png"))); //$NON-NLS-1$
		refresh.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		refresh.setText(Messages.getString("TaskListPart.REFRESH")); //$NON-NLS-1$
		
		
		final Button btnStopTask = new Button(composite, SWT.NONE);
		
		final Button btnActivateTask = new Button(composite, SWT.NONE);
		
		btnStopTask.setText(Messages.getString("ScheduledTaskListPart.DEACTIVATE")); //$NON-NLS-1$
		btnStopTask.setImage(new Image(composite.getDisplay(), this.getClass().getResourceAsStream("/icons/deactivate.png"))); //$NON-NLS-1$
		btnStopTask.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO
//				RestClient rest = new RestClient();
//				ScheduleRequest scheduleRequest = new ScheduleRequest();
//				scheduleRequest.setActive(false);
//				
//				if (getCurrentTask().size() == 1) {
//					scheduleRequest.setOperation(tr.org.liderahenk.liderconsole.core.schedule.ScheduleOperation.UPDATE);
//					scheduleRequest.setScheduleId(getCurrentTask().get(0).getId());
//					scheduleRequest.setCronString(getCurrentTask().get(0).getCronFormat());
//
//					rest.getDirectServerResult(getCurrentTask().get(0).getSystemRequest(), new HashMap<String, Object>(), scheduleRequest);
//					btnActivateTask.setEnabled(false);
//					btnStopTask.setEnabled(false);
//				}
//				else
//				{
//					for (Scheduler scheduler : getCurrentTask()) {
//						scheduleRequest.setOperation(tr.org.liderahenk.liderconsole.core.schedule.ScheduleOperation.UPDATE);
//						scheduleRequest.setScheduleId(scheduler.getId());
//						scheduleRequest.setCronString(scheduler.getCronFormat());
//
//						rest.getDirectServerResult(scheduler.getSystemRequest(), new HashMap<String, Object>(), scheduleRequest);						
//					}
//				}
				
				tblVwrTasks.setInput( getLogsFromServer(new HashMap<String, Object>(), "scheduler") ); //$NON-NLS-1$
				tblVwrTasks.refresh();
				
			}
			
			/*
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
				/*
				 * must override.
				 */
			}
		});
		
		
		btnActivateTask.setText(Messages.getString("ScheduledTaskListPart.ACTIVATE")); //$NON-NLS-1$
		btnActivateTask.setImage(new Image(composite.getDisplay(), this.getClass().getResourceAsStream("/icons/check.gif"))); //$NON-NLS-1$
		btnActivateTask.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
//				RestClient rest = new RestClient();
//				ScheduleRequest scheduleRequest = new ScheduleRequest();
//				scheduleRequest.setActive(true);
//				scheduleRequest.setOperation(tr.org.liderahenk.liderconsole.core.schedule.ScheduleOperation.UPDATE);
//				scheduleRequest.setScheduleId(getCurrentTask().getId());
//				scheduleRequest.setCronString(getCurrentTask().getCronFormat());
//
//				rest.getDirectServerResult(getCurrentTask().getSystemRequest(), new HashMap<String, Object>(), scheduleRequest);
//				tblVwrTasks.setInput( getLogsFromServer(new HashMap<String, Object>(), "scheduler") ); //$NON-NLS-1$
//				tblVwrTasks.refresh();
//				btnActivateTask.setEnabled(false);
//				btnStopTask.setEnabled(false);
	// TODO			
//				RestClient rest = new RestClient();
//				ScheduleRequest scheduleRequest = new ScheduleRequest();
//				scheduleRequest.setActive(true);
//				
//				if (getCurrentTask().size() == 1) {
//					scheduleRequest.setOperation(tr.org.liderahenk.liderconsole.core.schedule.ScheduleOperation.UPDATE);
//					scheduleRequest.setScheduleId(getCurrentTask().get(0).getId());
//					scheduleRequest.setCronString(getCurrentTask().get(0).getCronFormat());
//
//					rest.getDirectServerResult(getCurrentTask().get(0).getSystemRequest(), new HashMap<String, Object>(), scheduleRequest);
//					btnActivateTask.setEnabled(false);
//					btnStopTask.setEnabled(false);
//				}
//				else
//				{
//					for (Scheduler scheduler : getCurrentTask()) {
//						scheduleRequest.setOperation(tr.org.liderahenk.liderconsole.core.schedule.ScheduleOperation.UPDATE);
//						scheduleRequest.setScheduleId(scheduler.getId());
//						scheduleRequest.setCronString(scheduler.getCronFormat());
//
//						rest.getDirectServerResult(scheduler.getSystemRequest(), new HashMap<String, Object>(), scheduleRequest);						
//					}
//				}
				
				tblVwrTasks.setInput( getLogsFromServer(new HashMap<String, Object>(), "scheduler") ); //$NON-NLS-1$
				tblVwrTasks.refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}
		});
		
		
		tblVwrTasks = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL
		        							| SWT.V_SCROLL | SWT.FULL_SELECTION 
		        							| SWT.BORDER);
		createTableColumns(composite, tblVwrTasks);
		
		refresh.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
			
			/*
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override			
			public void widgetDefaultSelected(SelectionEvent e) {
				/*
				 * must override. 
				 */
			}
		});
		
	    final Table table = tblVwrTasks.getTable();	    
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
	    table.setSortColumn(creationDateColumn.getColumn());
	    
	    tblVwrTasks.setComparator(new ViewerComparator() {
	    	  public int compare(Viewer viewer, Object e1, Object e2) {
	    	    Scheduler t1 = (Scheduler) e1;
	    	    Scheduler t2 = (Scheduler) e2;
	    	    return t2.getLastUpdate().compareTo(t1.getLastUpdate());
	    	  };
	    	}); 
	    
	    tblVwrTasks.setContentProvider(new ArrayContentProvider());

	    //tblVwrTasks.setInput( getLogsFromServer(new HashMap<String, Object>(), "scheduler") ); //$NON-NLS-1$
	   	   
	    // define layout for the viewer
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 3;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    gridData.heightHint = 420;
	    gridData.horizontalAlignment = GridData.FILL;
	    tblVwrTasks.getControl().setLayoutData(gridData);
   

	    tblVwrTasks.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selections = (StructuredSelection)((IStructuredSelection)event.getSelection());
				@SuppressWarnings("unchecked")
				List<Scheduler> schedulers = (List<Scheduler>)selections.toList();
				
				if (schedulers != null && schedulers.size() == 1) {
					if(schedulers != null && schedulers.get(0) != null && schedulers.get(0).getActive())
					{
						btnStopTask.setEnabled(true);
						btnActivateTask.setEnabled(false);
					}
					else if(schedulers != null && schedulers.get(0) != null && !schedulers.get(0).getActive())
					{
						btnStopTask.setEnabled(false);
						btnActivateTask.setEnabled(true);
					}
				}
				else {
					btnStopTask.setEnabled(true);
					btnActivateTask.setEnabled(true);					
				}
				
				setCurrentTask(schedulers);
			}
		});
		
	    refresh();
	}
	
	private void refresh(){
//		MysProgressMonitorDialog dialog = new MysProgressMonitorDialog(this.getSite().getShell()){
//			  @Override
//			  public void createButtonsForButtonBar(Composite parent) {			    
//			    createButton(parent, IDialogConstants.CANCEL_ID,
//			    		Messages.getString("TaskListPart.CANCEL_BUTTON"), false); //$NON-NLS-1$
//			  }
//			  
//			  /**
//			   * Overriden method.
//			   */
//			  protected void configureShell(final Shell shell) {
//				super.configureShell(shell);
//				shell.setText(" "); //$NON-NLS-1$
//			  }
//		}; 
//		try {
//			dialog.run(true, false, new IRunnableWithProgress(){
//			     public void run(IProgressMonitor monitor) {
//			     monitor.beginTask(Messages.getString("TaskListPart_LOADING"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
//					Display.getDefault().asyncExec(new Runnable() {								
//						@Override
//						public void run() {
//							tblVwrTasks.setInput( getLogsFromServer(new HashMap<String, Object>(), "scheduler") ); //$NON-NLS-1$
//							tblVwrTasks.refresh();
//						}
//					});
//
//					monitor.done();
//			     }});
//		} catch (InvocationTargetException e1) {
//			e1.printStackTrace();
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
	}
	
	private void createTableColumns(final Composite container, final TableViewer scriptViewer) {
 		String[] titles = {Messages.getString("ScheduledTaskListPart.DATE"), Messages.getString("ScheduledTaskListPart.SYSTEM_REQUEST"), Messages.getString("ScheduledTaskListPart.ACTIVE"), Messages.getString("ScheduledTaskListPart.CRON_FORMAT")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		int[] bounds = { 160, 300, 60, 40 };
		
		creationDateColumn = createTableViewerColumn(titles[0], bounds[0], 0);
		creationDateColumn.setLabelProvider( new ColumnLabelProvider() {			
			@Override
			public String getText(Object element) {
				if( element instanceof Scheduler )					
					return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(((Scheduler)element).getLastUpdate()); //$NON-NLS-1$							
				return "NA"; //$NON-NLS-1$
			}			
		});
		
		ColumnViewerSorter creationDateColumnSorter = new ColumnViewerSorter(getViewer(), creationDateColumn) {

			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Scheduler p1 = (Scheduler) e1;
				Scheduler p2 = (Scheduler) e2;
				return p2.getLastUpdate().compareTo(p1.getLastUpdate());
			}

		};
		
		creationDateColumnSorter.setSorter(creationDateColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn systemRequestColumn = createTableViewerColumn(titles[1], bounds[1], 1);
		systemRequestColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Scheduler )
				{
					String systemRequest = ((Scheduler)element).getSystemRequest();
					return systemRequest == null ? "" : systemRequest; //$NON-NLS-1$
				}
				return "NA"; //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter systemRequestColumnSorter = new ColumnViewerSorter(getViewer(), systemRequestColumn) {

			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Scheduler p1 = (Scheduler) e1;
				Scheduler p2 = (Scheduler) e2;
				return p2.getSystemRequest().compareTo(p1.getSystemRequest());
			}

		};
		
		systemRequestColumnSorter.setSorter(systemRequestColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn activeColumn = createTableViewerColumn(titles[2], bounds[2], 2);
		activeColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String active = null != ((Scheduler)element).getActive() ? ((Scheduler)element).getActive().toString() : ""; //$NON-NLS-1$

				if( element instanceof Scheduler )
					return active == "true" ? Messages.getString("TaskListPart.TRUE") : Messages.getString("TaskListPart.FALSE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return Messages.getString("TaskListPart.UNKNOWN"); //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter activeColumnSorter = new ColumnViewerSorter(getViewer(), activeColumn) {

			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Scheduler p1 = (Scheduler) e1;
				Scheduler p2 = (Scheduler) e2;
				return p2.getActive().compareTo(p1.getActive());
			}

		};
		
		activeColumnSorter.setSorter(activeColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn cronFormatColumn = createTableViewerColumn(titles[3], bounds[3], 3);
		cronFormatColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Scheduler )
					return ((Scheduler)element).getCronFormat();
				return "NA"; //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter cronFormatColumnSorter = new ColumnViewerSorter(getViewer(), cronFormatColumn) {

			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Scheduler p1 = (Scheduler) e1;
				Scheduler p2 = (Scheduler) e2;
				return p2.getCronFormat().compareTo(p1.getCronFormat());
			}

		};
		
		cronFormatColumnSorter.setSorter(cronFormatColumnSorter, ColumnViewerSorter.ASC);
	}
	
	@SuppressWarnings("unchecked")
	public Scheduler[] getLogsFromServer(Map<String, Object> params, String rest) {	
		// TODO 
//		String serverResult = null;
//		serverResult = new RestClient().getServerResult("/rest/" + rest, params); //$NON-NLS-1$
//		
//		GsonBuilder gsonBuilder = new GsonBuilder();
//		List<Scheduler> itemList = new ArrayList<Scheduler>();
//		if (null != serverResult) {
//			Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls().create(); //$NON-NLS-1$
//			itemList = (List<Scheduler>) gson.fromJson( serverResult, new TypeToken<List<Scheduler>>() {}.getType());			
//		}		
//		Scheduler[] arr = new Scheduler[itemList.size()];
// 		return  itemList.toArray(arr);
		return null;
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		    final TableViewerColumn viewerColumn = new TableViewerColumn( getViewer(), SWT.NONE);
		    final TableColumn column = viewerColumn.getColumn();
		    column.setText(title);
		    column.setWidth(bound);
		    column.setResizable(true);
		    column.setMoveable(false);
		    column.setAlignment(SWT.LEFT);
		    return viewerColumn;
		  }
	 
    public TableViewer getViewer() { 
		return this.tblVwrTasks;
	}
	
	@Focus
	public void setFocus() {
		if(tblVwrTasks != null)
			tblVwrTasks.getControl().setFocus();		
	}
	
	public List<Scheduler> getCurrentTask(){
		return currentTasks;
		
	}
	
	public void setCurrentTask(List<Scheduler> task)
	{
		currentTasks = task;
	}
}