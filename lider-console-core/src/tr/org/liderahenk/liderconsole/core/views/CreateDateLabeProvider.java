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
public class CreateDateLabeProvider extends LabelProvider implements IStyledLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof Task) {
			Task task = (Task) element;
			return new StyledString(task.getCreationDate().toString());
		}
		else if (element instanceof ParentTask) {
			ParentTask parent = (ParentTask) element;
			return new StyledString(parent.getCreationDate());
		}
		return new StyledString();
	}

}
