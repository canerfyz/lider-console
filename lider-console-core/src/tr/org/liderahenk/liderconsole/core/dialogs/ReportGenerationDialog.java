package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplate;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateParameter;
import tr.org.liderahenk.liderconsole.core.rest.requests.ReportGenerationRequest;
import tr.org.liderahenk.liderconsole.core.rest.utils.ReportUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

public class ReportGenerationDialog extends DefaultLiderDialog {

	private static final Logger logger = LoggerFactory.getLogger(ReportGenerationDialog.class);

	private ReportTemplate selectedTemplate;
	private Button btnGenerateReport;

	public ReportGenerationDialog(Shell parentShell, ReportTemplate selectedTemplate) {
		super(parentShell);
		this.selectedTemplate = selectedTemplate;
	}

	/**
	 * Create template input widgets
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		parent.setLayout(new GridLayout(1, false));

		final Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		composite.setLayout(new GridLayout(2, false));

		List<ReportTemplateParameter> params = selectedTemplate.getTemplateParams();
		if (params != null) {
			for (ReportTemplateParameter param : params) {
				// Param label
				Label lbl = new Label(composite, SWT.NONE);
				lbl.setText(param.getLabel());

				// Param input
				Text txt = new Text(composite, SWT.BORDER);
				// Associate parameter key with this input
				txt.setData(param.getKey());
			}
		}

		// Button for generating report table
		btnGenerateReport = new Button(composite, SWT.NONE);
		btnGenerateReport.setText(Messages.getString("GENERATE_REPORT"));
		btnGenerateReport.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/report.png"));
		btnGenerateReport.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnGenerateReport.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!validateInputs()) {
					return;
				}
				// Collect parameter values
				Map<String, Object> paramValues = new HashMap<String, Object>();

				Control[] children = composite.getChildren();
				for (Control control : children) {
					if (control instanceof Text) {
						Text t = (Text) control;
						String key = t.getData().toString();
						paramValues.put(key, t.getText());
					}
				}

				// Send parameter values and template ID to generate report!
				ReportGenerationRequest report = new ReportGenerationRequest();
				report.setTemplateId(selectedTemplate.getId());
				report.setParamValues(paramValues);

				try {
					List<LinkedHashMap<String, String>> list = ReportUtils.generate(report);
					System.out.println(list);
					// TODO return value!!!
				} catch (Exception e1) {
					logger.error(e1.getMessage(), e1);
					Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		applyDialogFont(parent);
		return parent;
	}

	protected boolean validateInputs() {
		// TODO Auto-generated method stub
		return true;
	}

}
