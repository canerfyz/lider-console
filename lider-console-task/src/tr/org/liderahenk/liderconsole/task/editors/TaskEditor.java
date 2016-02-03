package tr.org.liderahenk.liderconsole.task.editors;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.EventHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.task.CriteriaOperator;
import tr.org.liderahenk.liderconsole.core.task.ImageCombo;
import tr.org.liderahenk.liderconsole.core.task.QueryCriteriaImpl;
import tr.org.liderahenk.liderconsole.core.task.Task;
import tr.org.liderahenk.liderconsole.core.task.TaskState;
import tr.org.liderahenk.liderconsole.core.ui.GenericEditorInput;
import tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter;
import tr.org.liderahenk.liderconsole.task.dialogs.ChangePriorityDialog;
import tr.org.liderahenk.liderconsole.task.dialogs.TaskHistoryDialog;
import tr.org.liderahenk.liderconsole.task.i18n.Messages;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class TaskEditor extends EditorPart {
	private static final String CRITERIA = "criteria";
	private static final String MAX_RESULTS = "maxResults";
	private static final String OFFSET = "offset";
	private static final String REST_URI = "taskdb";
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private IEventBroker eventBroker=(IEventBroker)PlatformUI.getWorkbench().getService(IEventBroker.class);
	private TableViewer tblVwrTasks;
	private TableViewerColumn creationDateColumn;
//	private DateChooserCombo startDate;
//	private DateChooserCombo endDate;
	private ImageCombo cmbTaskState;
	private Combo cmbTaskName;
	private Text txtTargetObjectDn;
	private Task currentTask;
	private Button changePriority;
	private Button btnPrevious;
	private Button btnNext;
	private Button btnFirstPage;
	private Button btnLastPage;
    private static final Image CREATED = getImageFromResource("createdtask.png"); //$NON-NLS-1$
    private static final Image TASK_RECEIVED = getImageFromResource("receivedtask.png"); //$NON-NLS-1$
    private static final Image TASK_PUT_IN_QUEUE = getImageFromResource("queuetask.png"); //$NON-NLS-1$
    private static final Image TASK_PROCESSING = getImageFromResource("processingtask.png"); //$NON-NLS-1$
    private static final Image TASK_PROCESSED = getImageFromResource("processedtask.png"); //$NON-NLS-1$
    private static final Image TASK_TIMEOUT = getImageFromResource("timeouttask.png"); //$NON-NLS-1$
    private static final Image TASK_KILLED = getImageFromResource("killedtask.png"); //$NON-NLS-1$
        
	private Combo cmbTaskCount;
	private boolean filterEnabled;
	private Map<String, Object> filterParams;
	private boolean refreshed = false;	
	private final TaskEditor me;
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "tr.org.liderahenk.liderconsole.task.views.TaskEditor";
	
	private final EventHandler taskCompleteEvent = new EventHandler() {
        public void handleEvent(org.osgi.service.event.Event event) {
      	String body=(String)event.getProperty("org.eclipse.e4.data");
  		GsonBuilder gsonBuilder = new GsonBuilder();
  		Gson gson = gsonBuilder.setDateFormat(DATE_FORMAT).serializeNulls().create(); //$NON-NLS-1$		
  		try {
//  			TaskStatusUpdate taskStatus = null;
//  			try {
//  				taskStatus = (TaskStatusUpdate) gson.fromJson( body, new TypeToken<TaskStatusUpdate>() {}.getType());					
//  			} catch (Exception e) {
//  				e.printStackTrace();
//  			}
  		
//  			if( taskStatus != null || body.equals("")){
//  				Display.getDefault().asyncExec(new Runnable() {
						
//						@Override
//						public void run() {
  						
//		    				tblVwrTasks.setInput( getLogsFromServer( "taskdb" ) );
//		    				tblVwrTasks.refresh();
//							refreshPage(Display.getDefault().getActiveShell());
  						
//  						MysProgressMonitorDialog dialog = new MysProgressMonitorDialog(Display.getDefault().getActiveShell()){
//  							  @Override
//  							  public void createButtonsForButtonBar(Composite parent) {			    
//  							    createButton(parent, IDialogConstants.CANCEL_ID,
//  							    		"Vazgeç", false);
//  							  }
//  							  /*
//  							   *
//  							   * @see org.eclipse.jface.dialogs.ProgressMonitorDialog#configureShell(org.eclipse.swt.widgets.Shell)
//  							   */
//  							  protected void configureShell(final Shell shell) {
//  									super.configureShell(shell);
//  									shell.setText(" "); //$NON-NLS-1$
//  							  }
//  						};
  						
  						try {
//  							dialog.run(true, false, new IRunnableWithProgress(){
//  							     public void run(IProgressMonitor monitor) {
//  							        monitor.beginTask("Yükleniyor...", IProgressMonitor.UNKNOWN);
//  							        Display.getDefault().asyncExec(new Runnable() {					        	
//  										@Override
//  										public void run() {
//  											try {
//													Thread.sleep(5000);
//												} catch (InterruptedException e) {
//													// TODO Auto-generated catch block
//													e.printStackTrace();
//												}
//  											tblVwrTasks.setInput( getLogsFromServer(REST_URI) );
//  											tblVwrTasks.refresh();
//  											changePriority.setEnabled(false);
//  										}
//  									});				        
//  							        monitor.done();
//  							     }
//  								});
  							

  									Job scriptJob = new Job("Görev Yönetimi") {			
  										@Override
  										/*
  										 * (non-Javadoc)
  										 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
  										 */
  										protected IStatus run(IProgressMonitor monitor) {
  											monitor.beginTask("Yükleniyor...", 100);
											try {
												Thread.sleep(5000);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
				  							Display.getDefault().asyncExec(new Runnable() {
				  								
				  								@Override
				  								public void run() {
													tblVwrTasks.setInput( getLogsFromServer(REST_URI) );
													tblVwrTasks.refresh();
													changePriority.setEnabled(false);
												}
				  								});
	  											monitor.worked(100);
	  											monitor.done();
	  											return Status.OK_STATUS;
  											
  										}
  									};
//  									scriptJob.setUser(true);
  									scriptJob.schedule();				
  						} catch (Exception e1) {
  							e1.printStackTrace();					
  						}
							
//						}
//					});
  			}
//  		}
  		catch (Exception e)
  		{
  			e.printStackTrace();	
  		}
  	}
    };
	
	/**
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override	
	public void doSave(IProgressMonitor monitor) {
		/*
		 * Auto-generated.
		 */
	}

	/**
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override	
	public void doSaveAs() {
		/*
		 * Auto-generated.
		 */
	}

	/**
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	public TaskEditor() {
		super();
		me=this;
        eventBroker.subscribe("task", taskCompleteEvent);		
	}
	@Override public void dispose() {
		eventBroker.unsubscribe(taskCompleteEvent);
		super.dispose();
	};
	
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
		filterParams = new HashMap<String, Object>();
		filterParams.put(OFFSET, 0); //$NON-NLS-1$
		filterParams.put(MAX_RESULTS, 100); //$NON-NLS-1$
		filterParams.put(CRITERIA, new ArrayList<Object>()); //$NON-NLS-1$
		refreshed = false;
		parent.setLayout(new GridLayout(1, false));
		
		/* Filter Area */
		
		GridData d1 = new GridData();
		d1.horizontalAlignment = SWT.FILL;
		d1.verticalAlignment = SWT.FILL;
		d1.grabExcessHorizontalSpace = true;		
		d1.grabExcessVerticalSpace = true;
//		filterEnabled = false;
				
		final Group cpm1 = new Group(parent, SWT.NONE);
		cpm1.setText("Filtre");
		cpm1.setLayout(new GridLayout(4, false));
		cpm1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
//		Label startDateLbl = new Label(cpm1, SWT.NONE);
//		startDateLbl.setText(Messages.getString("TaskListPart.START_DATE"));		 //$NON-NLS-1$
//		startDate = new DateChooserCombo(cpm1, SWT.BORDER);
//		startDate.setWeeksVisible(true);
//		startDate.setValue(null);
//		startDate.setFooterVisible(true);	
//				
//		startDate.setLayoutData(d1);		
		
		Label targetObjectDnLbl = new Label(cpm1, SWT.NONE);
		targetObjectDnLbl.setText(Messages.getString("TaskListPart.TARGET_OBJECT_DN")); //$NON-NLS-1$
		txtTargetObjectDn = new Text(cpm1, SWT.BORDER);
		
		txtTargetObjectDn.setLayoutData(d1);
		
		GenericEditorInput input=(GenericEditorInput)this.getEditorInput();
		String inputId=input.getId();
		if (!inputId.equals("")){
			txtTargetObjectDn.setText(inputId);
			txtTargetObjectDn.setEnabled(false);
		}
		
		Label endDateLbl = new Label(cpm1, SWT.NONE);
		endDateLbl.setText(Messages.getString("TaskListPart.END_DATE"));		 //$NON-NLS-1$
//		endDate = new DateChooserCombo(cpm1, SWT.BORDER);
//		
//		endDate.setWeeksVisible(true);
//		endDate.setValue(null);
//		endDate.setFooterVisible(true);		
//		endDate.setLayoutData(d1);
		
		Label taskStateLbl = new Label(cpm1, SWT.NONE);
		taskStateLbl.setText(Messages.getString("TaskListPart.TASK_STATE")); //$NON-NLS-1$
		GridData gridData = new GridData(SWT.BEGINNING, SWT.CENTER, false,false);
		gridData.widthHint = 140;
		cmbTaskState = new ImageCombo(cpm1, SWT.READ_ONLY | SWT.BORDER);
		cmbTaskState.add("", null);
		cmbTaskState.add(Messages.getString("CREATED"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/createdtask.png")));
		cmbTaskState.add(Messages.getString("TASK_RECEIVED"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/receivedtask.png")));
		cmbTaskState.add(Messages.getString("TASK_PUT_IN_QUEUE"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/queuetask.png")));
		cmbTaskState.add(Messages.getString("TASK_PROCESSING"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/processingtask.png")));
		cmbTaskState.add(Messages.getString("TASK_PROCESSED"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/processedtask.png")));
		cmbTaskState.add(Messages.getString("TASK_TIMEOUT"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/timeouttask.png")));
//		cmbTaskState.add(Messages.getString("TASK_KILLED"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/killedtask.png")));
		cmbTaskState.setLayoutData(d1);
		
		Label taskNameLbl = new Label(cpm1, SWT.NONE);
		taskNameLbl.setText(Messages.getString("TaskListPart.TASK_NAME")); //$NON-NLS-1$
		
		cmbTaskName = new Combo(cpm1, SWT.READ_ONLY | SWT.BORDER);
		List<String> cmbTaskNameItems = new ArrayList<String>();
		cmbTaskNameItems.add("");
		
		for (String string : getPluginNamesFromServer()) {
			if (string != null && !string.isEmpty()) {
				if ("NA".equals(string)) {
					cmbTaskNameItems.add("Sistem");
				}
				else {
					cmbTaskNameItems.add(string);					
				}				
			}
		}
		
//		cmbTaskName.add(Messages.getString("TaskListPart.USB_MANAGER_REQ"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/usb.gif")));
//		cmbTaskName.add(Messages.getString("TaskListPart.TAKE_USER_SCREENSHOT"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/screenshot.png")));
//		cmbTaskName.add(Messages.getString("TaskListPart.SCRIPT_RUN_DB"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/script.png")));
//		cmbTaskName.add(Messages.getString("TaskListPart.QUOTA_FILESYSTEM_MANAGE"), new Image(cpm1.getDisplay(), this.getClass().getResourceAsStream("/icons/quota.png")));
//
		String[] arr = new String[cmbTaskNameItems.size()];
		arr = cmbTaskNameItems.toArray(arr);
		cmbTaskName.setItems(arr);
		cmbTaskName.setLayoutData(d1);		
		
		final Composite composite = new Composite (parent, SWT.FILL);
		
		composite.setLayout(new GridLayout(8, false));
	    
		GridData btnData = new GridData();
		btnData.grabExcessHorizontalSpace= true;
		btnData.grabExcessVerticalSpace= true;
		btnData.horizontalAlignment = SWT.FILL;
		btnData.verticalAlignment = SWT.FILL;
		
		composite.setLayoutData(btnData);
		
		Button btnFilter = new Button(composite, SWT.NONE);		
		btnFilter.setImage(new Image(composite.getDisplay(), this.getClass().getResourceAsStream("/icons/filter.png"))); //$NON-NLS-1$
		btnFilter.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnFilter.setText(Messages.getString("TaskListPart.FILTER"));  //$NON-NLS-1$
		
		Button clear = new Button(composite, SWT.NONE);
		clear.setImage(new Image(composite.getDisplay(), this.getClass().getResourceAsStream("/icons/clean.png"))); //$NON-NLS-1$		
		clear.setText(Messages.getString("TaskListPart.CLEAR")); //$NON-NLS-1$
		clear.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		clear.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent event) {
//				startDate.setValue(null);
//				endDate.setValue(null);
				txtTargetObjectDn.setText(""); //$NON-NLS-1$
				cmbTaskState.select(0);
			}
			
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override			
			public void widgetDefaultSelected(SelectionEvent e) {
				/*
				 * Auto-generated.
				 */
			}
		});

		btnFilter.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {				
//				filterAsync();
			}
			
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override			
			public void widgetDefaultSelected(SelectionEvent e) {
				//must override.
			}
		});

		changePriority = new Button(composite, SWT.NONE);
		changePriority.setImage(new Image(composite.getDisplay(), this.getClass().getResourceAsStream("/icons/priority.png"))); //$NON-NLS-1$
		changePriority.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		changePriority.setText(Messages.getString("TaskListPart.CHANGE_PRIORITY"));
		changePriority.setEnabled(false);
		changePriority.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true));
		changePriority.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ChangePriorityDialog dialog = new ChangePriorityDialog(Display.getDefault().getActiveShell(), currentTask);
				dialog.open();
				tblVwrTasks.setInput( getLogsFromServer( REST_URI ) );
				tblVwrTasks.refresh();
			}
			
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
				/*
				 * Auto-generated.
				 */
			}
		});
		
		int recordCount = 0;
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(CRITERIA, new ArrayList<Object>()); //$NON-NLS-1$
		final Long taskCount = getTaskCountFromServer(params, "/taskdb/count");

		btnFirstPage = new Button(composite, SWT.None);
		btnFirstPage.setText("<<");
		btnFirstPage.setEnabled(false);
		
		btnFirstPage.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbTaskCount.select(0);
//				refreshPage(composite);
				if (cmbTaskCount.getSelectionIndex() == 0) {
					btnPrevious.setEnabled(false);					
				}
				else {
					btnPrevious.setEnabled(true);					
				}
				
				if (cmbTaskCount.getSelectionIndex() == cmbTaskCount.getItemCount() - 1) {
					btnNext.setEnabled(false);
					btnLastPage.setEnabled(false);					
				}
				else {
					btnNext.setEnabled(true);
					btnLastPage.setEnabled(true);
				}
				
				btnFirstPage.setEnabled(false);				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				/*
				 * Auto-generated.
				 */
			}
		});
		
		btnPrevious = new Button(composite, SWT.None);
		btnPrevious.setText("<");
		btnPrevious.setEnabled(false);

		btnPrevious.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbTaskCount.select(cmbTaskCount.getSelectionIndex() != 0 ? cmbTaskCount.getSelectionIndex() - 1 : cmbTaskCount.getSelectionIndex());
//				refreshPage(composite);
				if (cmbTaskCount.getSelectionIndex() == 0) {
					btnPrevious.setEnabled(false);
					btnFirstPage.setEnabled(false);
				}
				else {
					btnPrevious.setEnabled(true);
					btnFirstPage.setEnabled(true);
				}
				
				if (cmbTaskCount.getSelectionIndex() == cmbTaskCount.getItemCount() - 1) {
					btnNext.setEnabled(false);
					btnLastPage.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
					btnLastPage.setEnabled(true);
				}				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				/*
				 * Auto-generated.
				 */
			}
		});
		GridData grdTaskCount = new GridData();
		grdTaskCount.widthHint = 100;
		cmbTaskCount = new Combo(composite, SWT.NONE);
		cmbTaskCount.setLayoutData(grdTaskCount);

		cmbTaskCount.setEnabled(true);
		if(null != taskCount)
		{				
			if(taskCount >= 100) {
				recordCount = (int)(taskCount / 100);
			}
				
			String[] items = new String[recordCount + 1];
			for (Integer i = 0; i < recordCount + 1; i++) {	
				Integer k = i + 1;
				items[i] = k.toString();
			}		
			cmbTaskCount.setItems(items);
			if (cmbTaskCount.getSelectionIndex() <= 0) {
				cmbTaskCount.select(0);
			}
		}
		btnNext = new Button(composite, SWT.None);
		btnNext.setText(">");
		if (new Long(1).equals(taskCount)) {
			btnNext.setEnabled(false);			
		}
		btnNext.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbTaskCount.select(cmbTaskCount.getSelectionIndex() + 1);
//				refreshPage(composite);
				if (cmbTaskCount.getSelectionIndex() == 0) {
					btnPrevious.setEnabled(false);
					btnFirstPage.setEnabled(false);					
				}
				else {
					btnPrevious.setEnabled(true);
					btnFirstPage.setEnabled(true);
				}
				
				if (cmbTaskCount.getSelectionIndex() == cmbTaskCount.getItemCount() - 1) {
					btnNext.setEnabled(false);
					btnLastPage.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
					btnLastPage.setEnabled(true);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				/*
				 * Auto-generated.
				 */
			}
		});
		
		btnLastPage = new Button(composite, SWT.None);
		btnLastPage.setText(">>");
		btnLastPage.setEnabled(true);
		
		btnLastPage.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbTaskCount.select(cmbTaskCount.getItemCount() - 1);
//				refreshPage(composite);
				if (cmbTaskCount.getSelectionIndex() == 0) {
					btnPrevious.setEnabled(false);
					btnFirstPage.setEnabled(false);					
				}
				else {
					btnPrevious.setEnabled(true);
					btnFirstPage.setEnabled(true);
				}
				
				if (cmbTaskCount.getSelectionIndex() == cmbTaskCount.getItemCount() - 1) {
					btnNext.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
				}
				btnLastPage.setEnabled(false);				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				/*
				 * Auto-generated.
				 */
			}
		});
		cmbTaskCount.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.character == '\r')
				{
					cmbTaskCount.select( Integer.parseInt(cmbTaskCount.getText()) -1 );
//					refreshPage(composite);
					if (cmbTaskCount.getSelectionIndex() == 0) {
						btnPrevious.setEnabled(false);
						btnFirstPage.setEnabled(false);	
					}
					else {
						btnPrevious.setEnabled(true);
						btnFirstPage.setEnabled(true);
					}
					
					if (cmbTaskCount.getSelectionIndex() == cmbTaskCount.getItemCount() - 1 ) {
						btnNext.setEnabled(false);
						btnLastPage.setEnabled(false);
					}
					else {
						btnNext.setEnabled(true);
						btnLastPage.setEnabled(true);
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		cmbTaskCount.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
//				refreshPage(composite);	
				if (cmbTaskCount.getSelectionIndex() == 0) {
					btnPrevious.setEnabled(false);
					btnFirstPage.setEnabled(false);
				}
				else {
					btnPrevious.setEnabled(true);
					btnFirstPage.setEnabled(true);
				}
				
				if (cmbTaskCount.getSelectionIndex() == cmbTaskCount.getItemCount() - 1) {
					btnNext.setEnabled(false);
					btnLastPage.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
					btnLastPage.setEnabled(true);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
								
			}
		});
		
		
		tblVwrTasks = new TableViewer(composite, SWT.SINGLE | SWT.H_SCROLL
		        							| SWT.V_SCROLL | SWT.FULL_SELECTION 
		        							| SWT.BORDER);
		createTableColumns(composite, tblVwrTasks);
		
		tblVwrTasks.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Task task = ((Task)((StructuredSelection)event.getSelection()).getFirstElement());
				TaskHistoryDialog taskHistoryDialog = new TaskHistoryDialog(parent.getShell(), task.getTaskHistory(), task.getId());
				
				taskHistoryDialog.open();
			}
		});
		
	    final Table table = tblVwrTasks.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.getVerticalBar().setEnabled(true);
	    table.getVerticalBar().setVisible(true);
	    
	    table.setSortColumn(creationDateColumn.getColumn());
	    
	    tblVwrTasks.setComparator(new ViewerComparator() {
	    	  public int compare(Viewer viewer, Object e1, Object e2) {
	    	    Task t1 = (Task) e1;
	    	    Task t2 = (Task) e2;
	    	    return t2.getCreationDate().compareTo(t1.getCreationDate());
	    	  };
	    	}); 
	    
	    tblVwrTasks.setContentProvider(new ArrayContentProvider());
		
	    ColumnViewerToolTipSupport.enableFor(tblVwrTasks, ToolTip.NO_RECREATE);
	    
	    //tblVwrTasks.setInput( getLogsFromServer("taskdb"));	  
	    
	    tblVwrTasks.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				changePriority.setEnabled(true);
				setCurrentTask((Task)((StructuredSelection)event.getSelection()).getFirstElement());
				if(null != getCurrentTask() && null != getCurrentTask().getState() && getCurrentTask().getState().equals(TaskState.TASK_PROCESSED)) {
					changePriority.setEnabled(false);
				}
			}
	    });	    
	   	   
	    GridData gridData2 = new GridData();
	    gridData2.verticalAlignment = GridData.FILL;
	    gridData2.horizontalSpan = 8;
	    gridData2.grabExcessHorizontalSpace = true;
	    gridData2.grabExcessVerticalSpace = true;
	    gridData2.horizontalAlignment = GridData.FILL;
	    tblVwrTasks.getControl().setLayoutData(gridData2);
	    
//	    filterAsync();
	}
	
//	private void refreshPage(Composite composite)
//	{
//		MysProgressMonitorDialog dialog = new MysProgressMonitorDialog(composite.getShell()){
//			  @Override
//			  public void createButtonsForButtonBar(Composite parent) {			    
//			    createButton(parent, IDialogConstants.CANCEL_ID,
//			    		"Vazgeç", false);
//			  }
//			  
//			  /*
//			   *
//			   * @see org.eclipse.jface.dialogs.ProgressMonitorDialog#configureShell(org.eclipse.swt.widgets.Shell)
//			   */
//			  protected void configureShell(final Shell shell) {
//				  super.configureShell(shell);
//				  shell.setText(" "); //$NON-NLS-1$
//			  }
//		};
//		
//		try {
//			dialog.run(true, false, new IRunnableWithProgress(){
//			     public void run(IProgressMonitor monitor) {
//			        monitor.beginTask("Yükleniyor...", IProgressMonitor.UNKNOWN);
//			        Display.getDefault().asyncExec(new Runnable() {					        	
//						@Override
//						public void run() {
//							tblVwrTasks.setInput( getLogsFromServer(REST_URI) );
//							tblVwrTasks.refresh();
//							changePriority.setEnabled(false);
//						}
//					});				        
//			        monitor.done();
//			     }
//				});
//		} catch (Exception e1) {
//			e1.printStackTrace();					
//		}
//	}
	
//	private void filterAsync(){
//		
//		if (startDate.getValue() != null && endDate.getValue() != null && startDate.getValue().after( endDate.getValue())) {
//			MysMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Başlangıç tarihi Bitiş tarihinden büyük olamaz!", SWT.NONE);
//			return;
//		}
//		
//		MysProgressMonitorDialog dialog = new MysProgressMonitorDialog(this.getSite().getShell()){
//			  @Override
//			  public void createButtonsForButtonBar(Composite parent) {			    
//			    createButton(parent, IDialogConstants.CANCEL_ID,
//			    		"Vazgeç", false);
//			  }
//			  /*
//			   *
//			   * @see org.eclipse.jface.dialogs.ProgressMonitorDialog#configureShell(org.eclipse.swt.widgets.Shell)
//			   */
//			  protected void configureShell(final Shell shell) {
//				 super.configureShell(shell);
//				 shell.setText(" "); //$NON-NLS-1$
//			  }
//		};
//		
//		try {
//			dialog.run(true, false, new IRunnableWithProgress(){
//			     public void run(IProgressMonitor monitor) {
//			        monitor.beginTask("Yükleniyor...", IProgressMonitor.UNKNOWN);
//			        Display.getDefault().asyncExec(new Runnable() {					        	
//						@Override
//						public void run() {
//							filter();
//						}
//					});				        
//			        monitor.done();
//			     }
//				});
//		} catch (Exception e1) {
//			e1.printStackTrace();					
//		}		
//	}
	
	private void filter(){
		Task[] tasks = null;
		Map<String, Object> params = new HashMap<String, Object>();
		
		QueryCriteriaImpl criteriaDate = new QueryCriteriaImpl();
		criteriaDate.setField("creationDate"); //$NON-NLS-1$

		List<Object> objectList = new ArrayList<Object>();
		
//		if (null != startDate.getValue() && null != endDate.getValue()) {
//			int oldDate = startDate.getValue().getDate();
//			int oldYear = startDate.getValue().getYear();
//			int oldMonth = startDate.getValue().getMonth();
//			
//			int oldmDate = endDate.getValue().getDate() + 1;
//			int oldmYear = endDate.getValue().getYear();
//			int oldmMonth = endDate.getValue().getMonth();
//			
//			Date date = new Date(oldYear, oldMonth, oldDate);
//			Date mDate = new Date(oldmYear, oldmMonth, oldmDate);
//			criteriaDate.setOperator(CriteriaOperator.BT);
//			criteriaDate.setValues(new Object[]{ date.getTime(), mDate.getTime() });
//			objectList.add(criteriaDate);
//			filterEnabled = true;
//		}
//		else if(null != startDate.getValue() && null == endDate.getValue())
//		{
//			criteriaDate.setOperator(CriteriaOperator.GT);
//			criteriaDate.setValues(new Object[]{ startDate.getValue().getTime()});
//			objectList.add(criteriaDate);
//			filterEnabled = true;
//		}
//		else if(null == startDate.getValue() && null != endDate.getValue())
//		{					
//			int oldDate = endDate.getValue().getDate() + 1;
//			int oldYear = endDate.getValue().getYear();
//			int oldMonth = endDate.getValue().getMonth();
//			
//			Date date = new Date(oldYear, oldMonth, oldDate);
//			criteriaDate.setOperator(CriteriaOperator.LT);
//			criteriaDate.setValues(new Object[]{ date.getTime()});
//			objectList.add(criteriaDate);
//			filterEnabled = true;
//		}
		
		QueryCriteriaImpl criteriaTaskState = new QueryCriteriaImpl();
		criteriaTaskState.setField("state"); //$NON-NLS-1$
		if ( cmbTaskState.getSelectionIndex() > 0) {
			criteriaTaskState.setOperator(CriteriaOperator.EQ);
			criteriaTaskState.setValues(new Object[] { TaskState.values()[ cmbTaskState.getSelectionIndex() -1] });
			objectList.add(criteriaTaskState);
			filterEnabled = true;
		}
		
		
		if ( cmbTaskName.getSelectionIndex() > 0) {
			QueryCriteriaImpl criteriaTaskName_Attribute = new QueryCriteriaImpl();
			criteriaTaskName_Attribute.setField("pluginId"); //$NON-NLS-1$					
			criteriaTaskName_Attribute.setOperator(CriteriaOperator.EQ);
			String taskName  = cmbTaskName.getText();
			criteriaTaskName_Attribute.setValues(new Object[] { !taskName.equals("Sistem") ? taskName : "NA" });
			objectList.add(criteriaTaskName_Attribute);
//			
//			QueryCriteriaImpl criteriaTaskName_Command = new QueryCriteriaImpl();
//			criteriaTaskName_Command.setField("request.command"); //$NON-NLS-1$					
//			criteriaTaskName_Command.setOperator(CriteriaOperator.LIKE);
//			
//			criteriaTaskName_Command.setValues(new Object[] { TaskName.values()[ cmbTaskName.getSelectionIndex() -1].toString().split("_")[1]});
//			objectList.add(criteriaTaskName_Command);
//			
//			QueryCriteriaImpl criteriaTaskName_Action = new QueryCriteriaImpl();
//			criteriaTaskName_Action.setField("request.action"); //$NON-NLS-1$					
//			criteriaTaskName_Action.setOperator(CriteriaOperator.LIKE);
//			
//			criteriaTaskName_Action.setValues(new Object[] { TaskName.values()[ cmbTaskName.getSelectionIndex() -1].toString().split("_")[2]});
//			objectList.add(criteriaTaskName_Action);
			filterEnabled = true;
		}
		
		QueryCriteriaImpl criteriaTargetObjectDn = new QueryCriteriaImpl();
		criteriaTargetObjectDn.setField("targetObjectDN"); //$NON-NLS-1$
		
		if (!txtTargetObjectDn.getText().isEmpty()) {
			criteriaTargetObjectDn.setOperator(CriteriaOperator.LIKE);
			criteriaTargetObjectDn.setValues(new Object[] { txtTargetObjectDn.getText() });
			objectList.add(criteriaTargetObjectDn);
			filterEnabled = true;
		}
		
		params.put( OFFSET, 0 ); //$NON-NLS-1$
		params.put( MAX_RESULTS, 100 ); //$NON-NLS-1$
		params.put( CRITERIA, objectList ); //$NON-NLS-1$
		filterParams = params;
		Long taskCount = getTaskCountFromServer(filterParams, "/taskdb/count");	
		tasks = getLogsFromServer( REST_URI );
		
		tblVwrTasks.setInput( tasks );
		int recodCount = 0;
		
					
		if(null != taskCount)
		{				
			if(taskCount >= 100) {
				recodCount = (int)(taskCount / 100);
			}
				
			String[] items = new String[recodCount + 1];
			for (Integer i = 0; i < recodCount + 1; i++) {	
				Integer k = i + 1;
//				items[i] = k.toString() + "	   ";
				items[i] = k.toString();
			}		
			cmbTaskCount.setItems(items);
			if (cmbTaskCount.getSelectionIndex() <= 0) {
				cmbTaskCount.select(0);
			}
		}
		
		tblVwrTasks.refresh();
	}

	private void createTableColumns(final Composite container, final TableViewer scriptViewer) {
 		String[] titles = {Messages.getString("TaskListPart.CREATION_DATE"),
 				Messages.getString("TaskListPart.TARGET_OBJECT_DN"),
 				Messages.getString("TaskListPart.TASK_NAME"),
 				Messages.getString("TaskListPart.COMMUNICATION_STATE"),
 				Messages.getString("TaskListPart.STATE"),
 				Messages.getString("TaskListPart.PRIORITY"), 				 
 				"",
 				Messages.getString("TaskListPart.OWNER")
 				}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		int[] bounds = { 160, 400, 200, 140, 100, 60, 30, 300 };
		
		TableViewerColumn stateImageColumn = createTableViewerColumn(titles[6], bounds[6]);
		stateImageColumn.setLabelProvider( new ColumnLabelProvider() {			
			@Override
			public String getText(Object element) {
				return null;
			}
			@Override
			public Image getImage(Object element) {
				if( element instanceof Task ){
					TaskState state=((Task)element).getState();
					if (state!=null){
						if(state.equals(TaskState.CREATED)) {
							return CREATED;
						}
						if(state.equals(TaskState.TASK_KILLED)) {
							return TASK_KILLED;
						}
						if(state.equals(TaskState.TASK_PROCESSED)) {
							return TASK_PROCESSED;
						}
						if(state.equals(TaskState.TASK_PROCESSING)) {
							return TASK_PROCESSING;
						}
						if(state.equals(TaskState.TASK_RECEIVED)) {
							return TASK_RECEIVED;
						}
						if(state.equals(TaskState.TASK_TIMEOUT)) {
							return TASK_TIMEOUT;
						}
					}
				}
				return CREATED; 
			  }
			
			  @Override
			  public String getToolTipText(Object element) {
					if(((Task)element).getState().equals(TaskState.CREATED))
						return Messages.getString("CREATED");
					if(((Task)element).getState().equals(TaskState.TASK_KILLED))
						return Messages.getString("TASK_KILLED");
					if(((Task)element).getState().equals(TaskState.TASK_PROCESSED))
						return Messages.getString("TASK_PROCESSED");
					if(((Task)element).getState().equals(TaskState.TASK_PROCESSING))
						return Messages.getString("TASK_PROCESSING");
					if(((Task)element).getState().equals(TaskState.TASK_RECEIVED))
						return Messages.getString("TASK_RECEIVED");
					if(((Task)element).getState().equals(TaskState.TASK_TIMEOUT))
						return Messages.getString("TASK_TIMEOUT");
					
					return Messages.getString("CREATED"); 
			  }			  
			  @Override
			  public Point getToolTipShift(Object object) {
			    return new Point(5, 5);
			  }
			  @Override
			  public int getToolTipDisplayDelayTime(Object object) {
			    return 100;
			  }
			  @Override
			  public int getToolTipTimeDisplayed(Object object) {
			    return 5000;
			  }
		});
		
		creationDateColumn = createTableViewerColumn(titles[0], bounds[0]);
		creationDateColumn.setLabelProvider( new ColumnLabelProvider() {			
			@Override
			public String getText(Object element) {
				if( element instanceof Task )					
					return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(((Task)element).getCreationDate()); //$NON-NLS-1$							
				return "NA"; //$NON-NLS-1$
			}			
		});
		
		ColumnViewerSorter creationDateColumnSorter = new ColumnViewerSorter(getViewer(), creationDateColumn) {
			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Task p1 = (Task) e1;
				Task p2 = (Task) e2;
				return p2.getCreationDate().compareTo(p1.getCreationDate());
			}
		};
		
		creationDateColumnSorter.setSorter(creationDateColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn targetObjectDnColumn = createTableViewerColumn(titles[1], bounds[1]);
		targetObjectDnColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Task )
				{
					String targetObjectDn = ((Task)element).getTargetObjectDN();
					return targetObjectDn == null ? "" : targetObjectDn; //$NON-NLS-1$
				}
				return "NA"; //$NON-NLS-1$
			}
		});
		
		
		ColumnViewerSorter targetObjectDnColumnSorter = new ColumnViewerSorter(getViewer(), targetObjectDnColumn) {
			
			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override			
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Task p1 = (Task) e1;
				Task p2 = (Task) e2;
				return p2.getTargetObjectDN().compareTo(p1.getTargetObjectDN());
			}
		};
		
		targetObjectDnColumnSorter.setSorter(targetObjectDnColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn taskNameColumn = createTableViewerColumn(titles[2], bounds[2]);
		taskNameColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Task )
				{
//					String attribute = ((Task)element).getRequest().getAttribute();
//					String command = ((Task)element).getRequest().getCommand();
//					String action = ((Task)element).getRequest().getAction();					
					String returnValue = ""; //$NON-NLS-1$
					try {
//						returnValue = Messages.getString("TaskListPart." + attribute + "_" + command + "_" + action); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						returnValue = ((Task)element).getPluginId();
					} 
					 catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					return returnValue; //$NON-NLS-1$
				}
				return ""; //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter taskNameColumnSorter = new ColumnViewerSorter(getViewer(), taskNameColumn) {
			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override			
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Task p1 = (Task) e1;
				Task p2 = (Task) e2;
				return p2.getPluginId().compareToIgnoreCase(p1.getPluginId());
			}
		};
		
		taskNameColumnSorter.setSorter(taskNameColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn commStateColumn = createTableViewerColumn(titles[3], bounds[3]);
		commStateColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Task )
				{
					String commState = ((Task)element).getCommState() == null ? "" : ((Task)element).getCommState().toString(); //$NON-NLS-1$
					return Messages.getString(commState); 
				}
				return "NA"; //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter commStateColumnSorter = new ColumnViewerSorter(getViewer(), commStateColumn) {
			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override			
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Task p1 = (Task) e1;
				Task p2 = (Task) e2;
				return Messages.getString(p2.getCommState().toString()).compareTo(Messages.getString(p1.getCommState().toString()));
			}
		};
		
		commStateColumnSorter.setSorter(commStateColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn stateColumn = createTableViewerColumn(titles[4], bounds[4]);
		stateColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Task )
				{
					TaskState state = ((Task)element).getState();
					return null != state ? Messages.getString(state.toString()): ""; //$NON-NLS-1$
				}
				return "NA"; //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter stateColumnSorter = new ColumnViewerSorter(getViewer(), stateColumn) {
			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override			
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Task p1 = (Task) e1;
				Task p2 = (Task) e2;
				return Messages.getString(p2.getState().toString()).compareTo(Messages.getString(p1.getState().toString()));
			}
		};
		
		stateColumnSorter.setSorter(stateColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn priorityColumn = createTableViewerColumn(titles[5], bounds[5]);
		priorityColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Task )
				{
					if (((Task)element).getPriority() != null) {
						return ((Task)element).getPriority().toString();	
					}
					else
					{
						return "[Geçerli Değil]";
					}
				}
				return "N/A"; //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter priorityColumnSorter = new ColumnViewerSorter(getViewer(), priorityColumn) {
			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override			
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Task p1 = (Task) e1;
				Task p2 = (Task) e2;
				return p2.getPriority().compareTo(p1.getPriority());
			}
		};
		
		priorityColumnSorter.setSorter(priorityColumnSorter, ColumnViewerSorter.ASC);
		
		TableViewerColumn ownerColumn = createTableViewerColumn( titles[7], bounds[7] );
		ownerColumn.setLabelProvider( new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof Task )
				{
					return ((Task)element).getOwner();
				}
				return "NA"; //$NON-NLS-1$
			}
		});
		
		ColumnViewerSorter ownerColumnSorter = new ColumnViewerSorter(getViewer(), ownerColumn) {
			/*
			 * @see tr.org.liderahenk.liderconsole.core.utils.ColumnViewerSorter#doCompare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override			
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Task p1 = (Task) e1;
				Task p2 = (Task) e2;
				return p2.getOwner().compareTo(p1.getOwner());
			}
		};
		
		ownerColumnSorter.setSorter(ownerColumnSorter, ColumnViewerSorter.ASC);
	}
	
	@SuppressWarnings("unchecked")
	public Task[] getLogsFromServer(String rest) {		
		String serverResult = null;
		
		if (cmbTaskCount.getItems() != null && cmbTaskCount.getItemCount() > 0 &&  cmbTaskCount.getSelectionIndex() > -1) {
			filterParams.put(OFFSET, cmbTaskCount.getSelectionIndex() * 100);	
		}
		else {
			filterParams.put(OFFSET, 0);
		}
		
		// TODO
		if (!filterParams.isEmpty()) {			
//			serverResult = new RestClient().getServerResult("/rest/" + rest, filterParams); //$NON-NLS-1$			
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		List<Task> itemList = new ArrayList<Task>();
		if (null != serverResult) {
			Gson gson = gsonBuilder.setDateFormat(DATE_FORMAT).serializeNulls().create(); //$NON-NLS-1$
			itemList = (List<Task>) gson.fromJson( serverResult, new TypeToken<List<Task>>() {}.getType());			
		}		
		Task[] arr = new Task[itemList.size()];
		
		if (refreshed) {
			filterParams = new HashMap<String, Object>();
			filterParams.put(OFFSET, 0); //$NON-NLS-1$
			filterParams.put(MAX_RESULTS, 100); //$NON-NLS-1$
			filterParams.put(CRITERIA, new ArrayList<Object>()); //$NON-NLS-1$			
		}
		
 		return  itemList.toArray(arr);
	}
	
	public Long getTaskCountFromServer(Map<String, Object> params, String rest) {		
		String serverResult = null;
		// TODO
		if (!filterParams.isEmpty()) {
//			serverResult = new RestClient().getServerResult( "/rest/" + rest, filterParams ); //$NON-NLS-1$
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Long count = null;
		if (null != serverResult) {
			Gson gson = gsonBuilder.setDateFormat(DATE_FORMAT).serializeNulls().create(); //$NON-NLS-1$
			count = (Long) gson.fromJson( serverResult, new TypeToken<Long>() {}.getType());			
		}		
 		return  count;
	}
	
	public List<String> getPluginNamesFromServer() {		
		String serverResult = null;
		// TODO
//		serverResult = new RestClient().getServerResult("/rest/agent/liderplugins", new HashMap<String, Object>()); //$NON-NLS-1$
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		List<String> pluginNames = new ArrayList<String>();
		if (null != serverResult) {
			Gson gson = gsonBuilder.setDateFormat(DATE_FORMAT).serializeNulls().create(); //$NON-NLS-1$
			pluginNames = (List<String>) gson.fromJson( serverResult, new TypeToken<List<String> >() {}.getType());			
		}		
 		return  pluginNames;
	}
	
	private TableViewerColumn createTableViewerColumn( String title, int bound ) {
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
	
	@Override
	public void setFocus() {
		if(tblVwrTasks != null)
			tblVwrTasks.getControl().setFocus();		
	}
	
	public Task getCurrentTask(){
		return currentTask;
		
	}
	
	public void setCurrentTask(Task task)
	{
		currentTask = task;
	}
	
	private static Image getImageFromResource(String file) {
		Bundle bundle = FrameworkUtil.getBundle(TaskEditor.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null); //$NON-NLS-1$
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}
	
	public void setAgentText(String agent)
	{
		txtTargetObjectDn.setText(agent);		
	}
}