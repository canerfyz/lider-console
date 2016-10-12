package tr.org.liderahenk.liderconsole.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.editorinput.DefaultEditorInput;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;

/**
 * Handles operation logs info editor which displays all operation logs with
 * their properties & details.
 * 
 * @author <a href="mailto:cemre.alpsoy@agem.com.tr">Cemre Alpsoy</a>
 *
 */
public class OperationLogsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
		try {
			page.openEditor(new DefaultEditorInput(Messages.getString("OPERATION_LOGS")),
					LiderConstants.EDITORS.OPERATION_LOGS_EDITOR);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

}
