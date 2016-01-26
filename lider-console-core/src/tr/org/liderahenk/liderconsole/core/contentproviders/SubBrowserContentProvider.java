package tr.org.liderahenk.liderconsole.core.contentproviders;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import tr.org.liderahenk.liderconsole.core.model.ISubBrowserModel;
import tr.org.liderahenk.liderconsole.core.model.SubBrowserItem;

public class SubBrowserContentProvider implements ITreeContentProvider {

	private SubBrowserItem root;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		ISubBrowserModel subBrowserModel = (ISubBrowserModel) newInput;
		if( subBrowserModel != null)
			this.root = subBrowserModel.getRoot();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return root.getChildren().toArray();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object parentElement) {
		Object[] retVal = null;
		if( parentElement instanceof SubBrowserItem ){
			SubBrowserItem parentItem = (SubBrowserItem) parentElement;
			retVal = parentItem.getChildren().toArray();								
		}
		return retVal;
	}

	@Override
	public Object getParent(Object element) {
		Object retVal = null;
		if( element instanceof SubBrowserItem ) {
			retVal = ((SubBrowserItem)element).getParent();
		}
		return retVal;
	}

	@Override
	public boolean hasChildren(Object element) {
		boolean retVal = false;
		if( element instanceof SubBrowserItem )
		{
			final SubBrowserItem item = (SubBrowserItem) element;
			retVal = !item.getChildren().isEmpty();
		}
		return retVal;
	}

	@Override
	public void dispose() {
		// Auto-generated method stub		
	}
}