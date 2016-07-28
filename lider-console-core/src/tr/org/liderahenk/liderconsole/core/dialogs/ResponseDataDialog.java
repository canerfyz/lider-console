package tr.org.liderahenk.liderconsole.core.dialogs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
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
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
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
		composite.setLayout(new GridLayout(2, false));

		byte[] responseData = result.getResponseData();
		ContentType contentType = result.getContentType();

		if (responseData == null || responseData.length == 0) {
			Label lblResult = new Label(composite, SWT.NONE);
			lblResult.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
			lblResult.setText(Messages.getString("RESPONSE_DATA"));
			Text txtParams = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL);
			txtParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			txtParams.setText(Messages.getString("RESPONSE_DATA_EMPTY"));
		}
		if (ContentType.APPLICATION_JSON == contentType) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				HashMap<String, Object> resultMap = mapper.readValue(responseData, 0, responseData.length,
						new TypeReference<HashMap<String, Object>>() {
						});
				for (Entry<String, Object> entry : resultMap.entrySet()) {
					// Property name
					Label lblPropName = new Label(composite, SWT.NONE);
					lblPropName.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
					lblPropName.setText(entry.getKey());
					// Property value
					String value = entry.getValue() != null ? entry.getValue().toString() : "";
					int style = SWT.BORDER | SWT.READ_ONLY;
					if (value.length() > 100) {
						style |= SWT.MULTI | SWT.V_SCROLL;
					}
					Text txtPropValue = new Text(composite, style);
					GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
					if (value.length() > 100) {
						gridData.heightHint = 200;
					}
					txtPropValue.setLayoutData(gridData);
					txtPropValue.setText(value);
				}
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
			Label lblResult = new Label(composite, SWT.NONE);
			lblResult.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
			lblResult.setText(Messages.getString("RESPONSE_DATA"));
			Text txtParams = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL);
			txtParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			txtParams.setText(new String(responseData));
		} else if (ContentType.IMAGE_JPEG == contentType || ContentType.IMAGE_PNG == contentType) {
			Label lblResult = new Label(composite, SWT.NONE);
			lblResult.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
			lblResult.setText(Messages.getString("RESPONSE_DATA"));
			Label lblImage = new Label(composite, SWT.BORDER);
			lblImage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			lblImage.setImage(createImage(responseData));
		}
		return composite;
	}

	/**
	 * Create image from given response data, resize if necessary.
	 * 
	 * @param responseData
	 * @return
	 */
	private Image createImage(byte[] responseData) {
		int width = 300;
		int height = 200;
		Image image = new Image(Display.getDefault(), new ByteArrayInputStream(responseData));
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		image.dispose();
		return scaled;
	}

}
