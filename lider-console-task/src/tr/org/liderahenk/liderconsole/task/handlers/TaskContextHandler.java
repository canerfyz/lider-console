package tr.org.liderahenk.liderconsole.task.handlers;

import org.apache.directory.studio.ldapbrowser.core.model.IBookmark;
import org.apache.directory.studio.ldapbrowser.core.model.IEntry;
import org.apache.directory.studio.ldapbrowser.core.model.impl.SearchResult;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import tr.org.liderahenk.liderconsole.core.ui.GenericEditorInput;
import tr.org.liderahenk.liderconsole.task.i18n.Messages;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class TaskContextHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public TaskContextHandler() {
		//Empty constructor.
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        IWorkbenchPage page = window.getActivePage();
        ISelection selection=page.getSelection();
        if ( selection instanceof IStructuredSelection ){
	        IStructuredSelection sselection=(IStructuredSelection)selection;
	        Object selectedItem=sselection.getFirstElement();
	        if (selectedItem instanceof SearchResult)
	        	selectedItem=((SearchResult)selectedItem).getEntry();
			if (selectedItem instanceof IBookmark)
				selectedItem=((IBookmark)selectedItem).getEntry();	        
	        if ( selectedItem instanceof IEntry ){
	        	IEntry selectedEntry=(IEntry)selectedItem;
		        try {
					page.openEditor(new GenericEditorInput(selectedEntry.getDn().getName(), Messages.getString("TASKS")), "tr.org.liderahenk.liderconsole.task.editors.TaskEditor");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
	        }else{
	        	System.out.println("Seçilen öğe desteklenmiyor: "+selectedItem.toString());
	        }
        }
		
		return null;
	}
}
