package tr.org.liderahenk.liderconsole.core.handlers;

import org.apache.directory.studio.ldapbrowser.core.model.IEntry;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tr.org.liderahenk.liderconsole.core.views.PolicyMenuDialog;

public class PolicyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
		ISelection selection = page.getSelection();

		if (selection instanceof IStructuredSelection) {

			IStructuredSelection sselection = (IStructuredSelection) selection;
			Object selectedItem = sselection.getFirstElement();

			if (selectedItem instanceof IEntry) {
				IEntry selectedEntry = (IEntry) selectedItem;
				String ahenkDn = selectedEntry.getDn().getName();
				
				EvaluateContributionsHandler handler = new EvaluateContributionsHandler();
				handler.execute(Platform.getExtensionRegistry());
				
				PolicyMenuDialog dialog = new PolicyMenuDialog(Display.getDefault().getActiveShell(), ahenkDn);
				dialog.create();
				dialog.open();
			}

		}

		return null;
	}

}
