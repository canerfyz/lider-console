package tr.org.liderahenk.liderconsole.core.editorinput;

import org.apache.directory.studio.ldapbrowser.core.model.ISearch;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * @author Fethi GÜRCAN <fgurcan@innova.com.tr>
 *
 *         GenericEditorInput class for parameter passing to editor classes in
 *         handler class.
 */
public class GenericEditorInput implements IEditorInput {

	private final String Id;
	private final String UserId;
	private final String Type;
	private final ISearch SearchInfo;

	/**
	 * 
	 * @param Id
	 *            - Usually dn of selected tree item. But a unique paremeter can
	 *            passed.
	 * @param Type
	 *            - In order to differentiate editor tabs and prevent multiple
	 *            editor in the center editor area. Should be unique.
	 */
	public GenericEditorInput(String Id, String Type) {
		this.Id = Id;
		this.Type = Type;
		this.UserId = "";
		this.SearchInfo = null;
	}

	/**
	 * 
	 * @param SearchInfo
	 *            - ISearch instance
	 * @param Type
	 *            - In order to differentiate editor tabs and prevent multiple
	 *            editor in the center editor area. Should be unique.
	 */
	public GenericEditorInput(ISearch SearchInfo, String Type) {
		this.Id = null;
		this.Type = Type;
		this.UserId = null;
		this.SearchInfo = SearchInfo;
	}

	/**
	 * 
	 * @param Id
	 *            - Usually dn of selected tree item. But a unique paremeter can
	 *            passed.
	 * @param Type
	 *            - In order to differentiate editor tabs and prevent multiple
	 *            editor in the center editor area. Should be unique.
	 * @param UserId
	 *            - User dn. Usually selected item dn
	 */
	public GenericEditorInput(String Id, String UserId, String Type) {
		this.Id = Id;
		this.Type = Type;
		this.UserId = UserId != null ? UserId : "";
		this.SearchInfo = null;
	}

	public String getId() {
		return this.Id;
	}

	public String getUserId() {
		return this.UserId;
	}

	public ISearch getSearchInfo() {
		return this.SearchInfo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GenericEditorInput) {
			GenericEditorInput gobj = (GenericEditorInput) obj;
			if (Id != null)
				return Type.equals(gobj.Type) && Id.equals(gobj.Id) && UserId.equals(gobj.UserId);
			else if (SearchInfo != null)
				return Type.equals(gobj.Type) && SearchInfo.equals(gobj.SearchInfo);
			else
				return false;
		} else
			return false;
	};

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		if (Id != null)
			return Id.isEmpty() ? UserId.isEmpty() ? Type : UserId : UserId.isEmpty() ? Id : UserId + " : " + Id;
		else if (SearchInfo != null)
			return SearchInfo.getName();
		else
			return "#HATA";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		if (Id != null)
			return Id.isEmpty() ? UserId.isEmpty() ? Type : Type + " - " + UserId
					: UserId.isEmpty() ? Type + " - " + Id : Type + " - " + UserId + " : " + Id;
		else if (SearchInfo != null)
			return "Search: " + SearchInfo.getName();
		else
			return "#HATA";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
