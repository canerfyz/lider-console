package tr.org.liderahenk.liderconsole.core.widgets.ldapSearchAttr;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class AttrValueText extends Text {

	/**
	 * @param parent
	 * @param style
	 */
	public AttrValueText(Composite parent, int style) {
		super(parent, style);
	}
	
	public AttrText getRelatedAttrText() {
		Control[] children = this.getParent().getChildren();
		if (children != null) {
			for (Control child : children) {
				if (child instanceof AttrText) {
					return (AttrText) child;
				}
			}
		}
		return null;
	}
	
	public AttrOperator getRelatedAttrOperator() {
		Control[] children = this.getParent().getChildren();
		if (children != null) {
			for (Control child : children) {
				if (child instanceof AttrOperator) {
					return (AttrOperator) child;
				}
			}
		}
		return null;
	}

	@Override
	protected void checkSubclass() {
		// By default, subclassing is not allowed for many of the SWT Controls
		// This empty method disables the check that prevents subclassing of
		// this class
	}

}
