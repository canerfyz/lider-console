package tr.org.liderahenk.liderconsole.core.dialogs;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Command;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ExecutedTaskParamDialog extends DefaultLiderDialog {

	private Command command;

	protected ExecutedTaskParamDialog(Shell parentShell, Command command) {
		super(parentShell);
		this.command = command;
	}

	/**
	 * Create executed task params widget
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		parent.setLayout(new GridLayout(1, false));

		Text txtParams = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		txtParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtParams.setText(generateMessage(command.getTask().getParameterMap()));

		applyDialogFont(parent);
		return parent;
	}

	private String generateMessage(byte[] parameterMap) {
		if (parameterMap == null || parameterMap.length == 0) {
			Messages.getString("PARAMETER_MAP_EMPTY");
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			String msg = mapper.defaultPrettyPrintingWriter().writeValueAsString(mapper.readValue(parameterMap, 0,
					parameterMap.length, new TypeReference<HashMap<String, Object>>() {
					}));
			return msg;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
