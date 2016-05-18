package tr.org.liderahenk.liderconsole.core.widgets;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class AttrNameCombo extends Combo {

	/**
	 * @param parent
	 * @param style
	 */
	public AttrNameCombo(Composite parent, int style) {
		super(parent, style);
	}

	public AttrValueText getRelatedAttrValue() {
		Control[] children = this.getParent().getChildren();
		if (children != null) {
			for (Control child : children) {
				if (child instanceof AttrValueText) {
					return (AttrValueText) child;
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
