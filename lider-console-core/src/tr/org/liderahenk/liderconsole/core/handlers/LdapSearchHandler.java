package tr.org.liderahenk.liderconsole.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import tr.org.liderahenk.liderconsole.core.editorinput.GenericEditorInput;
import tr.org.liderahenk.liderconsole.core.editors.LdapSearchEditor;

public class LdapSearchHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
       	try {
       		page.openEditor(new GenericEditorInput(LdapSearchEditor.ID, "Editor", "none"),LdapSearchEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}       	
       	return null;
	}

}
