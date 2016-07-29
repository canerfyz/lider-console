package tr.org.liderahenk.liderconsole.core.labelproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.model.SearchGroup;
import tr.org.liderahenk.liderconsole.core.model.SearchGroupEntry;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class SearchGroupViewLabelProvider implements ILabelProvider {

	private List<ILabelProviderListener> listeners;

	Image searchGroupImage;
	Image agentImage;
	Image userImage;
	Image groupImage;

	public SearchGroupViewLabelProvider() {
		listeners = new ArrayList<ILabelProviderListener>();
		searchGroupImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/16/list.png"));
		agentImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/16/computer.png"));
		userImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/16/user.png"));
		groupImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/16/users.png"));
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof SearchGroup) {
			return searchGroupImage;
		} else if (element instanceof SearchGroupEntry) {
			switch (((SearchGroupEntry) element).getDnType()) {
			case AHENK:
				return agentImage;
			case USER:
				return userImage;
			case GROUP:
				return groupImage;
			default:
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof SearchGroup) {
			return ((SearchGroup) element).getName() + " " + ((SearchGroup) element).getCreateDate();
		} else if (element instanceof SearchGroupEntry) {
			return ((SearchGroupEntry) element).getDn();
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void dispose() {
		searchGroupImage.dispose();
		agentImage.dispose();
		userImage.dispose();
		groupImage.dispose();
	}

}
