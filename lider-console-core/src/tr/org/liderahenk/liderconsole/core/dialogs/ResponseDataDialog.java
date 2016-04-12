package tr.org.liderahenk.liderconsole.core.dialogs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.CommandExecutionResult;
import tr.org.liderahenk.liderconsole.core.xmpp.enums.ContentType;

/**
 * This dialog is used to display details of selected command execution result.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ResponseDataDialog extends DefaultLiderTitleAreaDialog {

	private CommandExecutionResult result;

	public ResponseDataDialog(Shell parentShell, CommandExecutionResult result) {
		super(parentShell);
		this.result = result;
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString("COMMAND_EXECUTION_RESULT"));
		setMessage((result.getResponseMessage() != null ? result.getResponseMessage() + " " : "")
				+ (result.getCreateDate() != null ? result.getCreateDate() + " " : "")
				+ result.getResponseCode().getMessage(), IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));

		byte[] responseData = result.getResponseData();
		ContentType contentType = result.getContentType();

		if (responseData == null || responseData.length == 0) {
			Text txtParams = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
			txtParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			txtParams.setText(Messages.getString("RESPONSE_DATA_EMPTY"));
		}
		if (ContentType.APPLICATION_JSON == contentType) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				String msg = mapper.defaultPrettyPrintingWriter().writeValueAsString(mapper.readValue(responseData, 0,
						responseData.length, new TypeReference<HashMap<String, Object>>() {
						}));
				Text txtParams = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
				txtParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				txtParams.setText(msg);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (ContentType.TEXT_PLAIN == contentType || ContentType.TEXT_HTML == contentType) {
			Text txtParams = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
			txtParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			txtParams.setText(new String(responseData));
		} else if (ContentType.IMAGE_JPEG == contentType || ContentType.IMAGE_PNG == contentType) {
			Label lblImage = new Label(composite.getShell(), SWT.BORDER);
			lblImage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			lblImage.setImage(new Image(Display.getDefault(), new ByteArrayInputStream(responseData)));
		}
		// TODO display file contents!
		return composite;
	}

}
