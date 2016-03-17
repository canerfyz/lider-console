package tr.org.liderahenk.liderconsole.core.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class AttrText extends Text {

	/**
	 * @param parent
	 * @param style
	 */
	public AttrText(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void checkSubclass() {
		// By default, subclassing is not allowed for many of the SWT Controls
		// This empty method disables the check that prevents subclassing of
		// this class
	}

}
