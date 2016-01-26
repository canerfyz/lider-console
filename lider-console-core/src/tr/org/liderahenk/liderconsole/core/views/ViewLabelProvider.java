package tr.org.liderahenk.liderconsole.core.views;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;

import tr.org.liderahenk.liderconsole.core.task.ParentTask;
import tr.org.liderahenk.liderconsole.core.task.Task;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		StyledString styledString = new StyledString();
		if (element instanceof ParentTask) {
			ParentTask parent = (ParentTask) element;
			styledString.append(parent.getPluginId() + "-" + parent.getCreationDate());
		}
		else if (element instanceof Task) {
			Task task = (Task) element;
			styledString.append(task.getTargetObjectDN());
		}
		return styledString;
	}

}
