package tr.org.liderahenk.liderconsole.core.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import tr.org.liderahenk.liderconsole.core.task.ParentTask;
import tr.org.liderahenk.liderconsole.core.task.Task;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class ViewContentProvider implements ITreeContentProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ParentTask[]) {
			return (ParentTask[]) inputElement;
		}
		else if (inputElement instanceof Task[]) {
			return (Task[]) inputElement;
		}
		else if (inputElement instanceof ArrayList<?>) {
			return ((ArrayList<?>) inputElement).toArray();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		return hasChildren(parentElement) ? (((ParentTask) parentElement).getTasks()).toArray() : null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof Task) {
			return ((Task) element).getParent();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return element instanceof ParentTask && ((ParentTask) element).getTasks() != null;
	}
	
}
