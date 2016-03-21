package tr.org.liderahenk.liderconsole.task.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import tr.org.liderahenk.liderconsole.core.editorinput.GenericEditorInput;
import tr.org.liderahenk.liderconsole.task.i18n.Messages;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class TaskHandler extends AbstractHandler {

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		//Shell shell=window.getShell();
        IWorkbenchPage page = window.getActivePage();
        
        //IPerspectiveDescriptor perspective=page.getPerspective();
        
        //IWorkbench wb = PlatformUI.getWorkbench();
        //IPerspectiveDescriptor perspective=wb.getPerspectiveRegistry().findPerspectiveWithId("org.apache.directory.studio.ldapbrowser.ui.perspective.BrowserPerspective");
        
        /*IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

        IWorkbenchPage page = win.getActivePage();

        IPerspectiveDescriptor perspective = page.getPerspective();*/
        try {
			page.openEditor(new GenericEditorInput("", Messages.getString("TASKS")), "tr.org.liderahenk.liderconsole.task.editors.TaskEditor");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
        /*try {
        	page.showView("tr.org.liderahenk.liderconsole.task.views.TaskView",null,IWorkbenchPage.VIEW_ACTIVATE);
        } catch (PartInitException e) {
        	e.printStackTrace();
        }*/
		
		return null;
	}
	
	/*@Override public boolean isEnabled() {
		return RestSettings.isAVAILABLE();
	};*/
	
}
