package tr.org.liderahenk.liderconsole.core.utils;

import org.eclipse.swt.widgets.Composite;

public interface IExportableTableViewer {

	Composite getButtonComposite();

	String getSheetName();

	String getReportName();

}
