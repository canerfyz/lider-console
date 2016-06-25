package tr.org.liderahenk.liderconsole.core.contentproviders;

import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import tr.org.liderahenk.liderconsole.core.model.SearchGroup;
import tr.org.liderahenk.liderconsole.core.model.SearchGroupEntry;

public class SearchGroupViewContentProvider implements ITreeContentProvider {

	private List<SearchGroup> rootElements;

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List<?> && !((List<?>) inputElement).isEmpty()
				&& ((List<?>) inputElement).get(0) instanceof SearchGroup) {
			rootElements = (List<SearchGroup>) inputElement;
			return rootElements.toArray(new SearchGroup[rootElements.size()]);
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof SearchGroup) {
			Set<SearchGroupEntry> entries = ((SearchGroup) parentElement).getEntries();
			if (entries != null) {
				return entries.toArray(new SearchGroupEntry[entries.size()]);
			}
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof SearchGroupEntry) {
			if (rootElements != null) {
				for (SearchGroup searchGroup : rootElements) {
					if (searchGroup.getEntries() != null && !searchGroup.getEntries().isEmpty()) {
						for (SearchGroupEntry entry : searchGroup.getEntries()) {
							if (entry.equals((SearchGroupEntry) element)) {
								return searchGroup;
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		Object[] children = getChildren(element);
		return children == null ? false : children.length > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
		// Nothing to dispose
	}

}
