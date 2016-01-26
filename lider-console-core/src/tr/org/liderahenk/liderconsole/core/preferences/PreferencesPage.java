package tr.org.liderahenk.liderconsole.core.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import tr.org.liderahenk.liderconsole.core.Activator;
import tr.org.liderahenk.liderconsole.core.i18n.Messages_NLS;

public class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages_NLS.LiderConsolePreferences);
	}

	@Override
	protected void createFieldEditors() {
	}

}
