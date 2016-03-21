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
public class ScheduledTaskHandler extends AbstractHandler {
	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        IWorkbenchPage page = window.getActivePage();

        try {
			page.openEditor(new GenericEditorInput("", Messages.getString("SCHEDULED_TASKS")), "tr.org.liderahenk.liderconsole.task.editors.ScheduledTaskEditor");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*@Override public boolean isEnabled() {
		return RestSettings.isAVAILABLE();
	};*/
}
