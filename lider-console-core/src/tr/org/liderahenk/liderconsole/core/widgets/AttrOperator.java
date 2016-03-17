package tr.org.liderahenk.liderconsole.core.widgets;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class AttrOperator extends Combo {

	/**
	 * @param parent
	 * @param style
	 */
	public AttrOperator(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void checkSubclass() {
		// By default, subclassing is not allowed for many of the SWT Controls
		// This empty method disables the check that prevents subclassing of
		// this class
	}

}
