package tr.org.liderahenk.liderconsole.core.comparators;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import tr.org.liderahenk.liderconsole.core.model.Command;

/**
 * This class is used to sort Command records by create date in descending
 * order.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class TaskOverviewComparator extends ViewerComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof Command && e2 instanceof Command) {
			Command c1 = (Command) e1;
			Command c2 = (Command) e2;
			return c2.getCreateDate().compareTo(c1.getCreateDate());
		}
		return super.compare(viewer, e1, e2);
	}

}
