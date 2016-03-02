package tr.org.liderahenk.liderconsole.core.handlers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Execute;

import tr.org.liderahenk.liderconsole.core.policy.IPolicy;

public class EvaluateContributionsHandler {
	
	private static final String IPOLICY_ID = 
		      "tr.org.liderahenk.liderconsole.core.policymenu";
	
	@Execute
	  public void execute(IExtensionRegistry registry) {
	    IConfigurationElement[] config =
	        registry.getConfigurationElementsFor(IPOLICY_ID);
	    try {
	      for (IConfigurationElement e : config) {
	        final Object o =
	            e.createExecutableExtension("listClass");
	        if (o instanceof IPolicy) {
	          executeExtension(o);
	        }
	      }
	    } catch (CoreException ex) {
	      System.out.println(ex.getMessage());
	    }
	  }

	  private void executeExtension(final Object o) {
	    ISafeRunnable runnable = new ISafeRunnable() {
	      @Override
	      public void handleException(Throwable e) {
	      }

	      @Override
	      public void run() throws Exception {
	        ((IPolicy) o).listProfiles();
	      }
	    };
	    SafeRunner.run(runnable);
	  }

}
