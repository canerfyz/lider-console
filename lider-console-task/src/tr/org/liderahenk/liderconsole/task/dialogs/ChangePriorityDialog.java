package tr.org.liderahenk.liderconsole.task.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.task.i18n.Messages;
//import tr.org.liderahenk.liderconsole.core.MysMessageDialog;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.task.Task;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class ChangePriorityDialog extends Dialog {

	private Task task;
	private Text txtPriority;
	public ChangePriorityDialog(final Shell parentShell) {
		super(parentShell);		
	}
	
	public ChangePriorityDialog(final Shell parentShell, Task task) {
		super(parentShell);		
		this.task = task;
	}
	
	/* 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override	
	protected Control createDialogArea(final Composite parent) {
		final Composite area = (Composite) super.createDialogArea(parent);
		
		final Composite comp = new Composite(area, GridData.FILL);		
		
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout layout = new GridLayout(1, false);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comp.setLayout(layout);
		createDescriptionArea(comp);
		createPriorityCombo(comp);
		
		return area;
	}

	private void createDescriptionArea(final Composite composite)
	{
		final org.eclipse.swt.widgets.Label lbl = new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
		lbl.setText( Messages.getString("TaskHistoryDialog.DESCRIPTION") );
	}
	
	private void createPriorityCombo(final Composite comp) {		
		txtPriority = new Text(comp, SWT.NONE);
		final GridData dataProfileName = new GridData();
		dataProfileName.grabExcessHorizontalSpace = true;
		dataProfileName.horizontalAlignment = GridData.FILL;
		txtPriority.setLayoutData(dataProfileName);
		if (null != this.task) {
			txtPriority.setText(task.getPriority().toString());
		}		
		
		txtPriority.addListener(SWT.FocusOut, new Listener() {
			
			@Override
			public void handleEvent(Event e) {				
				 String currentText = ((Text)e.widget).getText();
		            try{  
		                int portNum = Integer.valueOf(currentText);  
		                if(portNum < 0 || portNum > 5){  
		                	if (null != task) {
								txtPriority.setText(task.getPriority().toString());
							}else {
								txtPriority.setText("5");
							}
		                }  
		            }  
		            catch(NumberFormatException ex){  
		            	if (null != task) {
							txtPriority.setText(task.getPriority().toString());
						}else {
							txtPriority.setText("5");
						}
		                      
		            }
			}
		});
		
		txtPriority.addVerifyListener(new VerifyListener() {  
	        @Override  
	        public void verifyText(VerifyEvent event) {
	            String currentText = ((Text)event.widget).getText();
	            String port =  currentText.substring(0, event.start) + event.text + currentText.substring(event.end);
	            try{  
	                int portNum = Integer.valueOf(port);  
	                if(portNum < 0 || portNum > 5){  
	                	event.doit = false;  
	                }  
	            }  
	            catch(NumberFormatException ex){  
	                if("".equals(port)){
	                	event.doit = true;
	                }else {
	                	event.doit = false;
	                }
	                      
	            }  
	        }  
	    });
	}
	
	/*
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override	
	protected void okPressed() {
		savePriorityToServer();
		super.okPressed();
	}

	private void savePriorityToServer() {		
		String result = new RestClient().getServerResult("/rest/task/" + task.getId() + "/priority/" + txtPriority.getText()); //$NON-NLS-1$ //$NON-NLS-2$
		
		if ("200".equals(result)) {
//			MysMessageDialog.open(MessageDialog.INFORMATION, Display.getDefault().getActiveShell(), "Information", Messages.getString("TaskHistoryDialog.PRIORITY_CHANGED"), SWT.NONE);
		}
	}
	
	/* 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override	
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("TaskHistoryDialog.OK"), true);	 //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("TaskHistoryDialog.CANCEL"), false);	 //$NON-NLS-1$
	}
	
	public static void main(String[] args) {
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    
	    Text t = new Text(shell, SWT.SINGLE | SWT.BORDER);
	    t.setBounds(10, 85, 100, 32);
	    t.setText("asd");

	    Text t2 = new Text(shell, SWT.SINGLE | SWT.BORDER);
	    t2.setBounds(10, 25, 100, 32);

	    t2.addListener(SWT.FocusIn, new Listener() {
	      public void handleEvent(Event event) {
//	        System.out.println("focus in");
	      }
	    });
	    t2.addListener(SWT.FocusOut, new Listener() {
	      public void handleEvent(Event event) {
//	       System.out.println("focus out");
	      }
	    });
	    
	    shell.setSize(200, 200);
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	  }

}
