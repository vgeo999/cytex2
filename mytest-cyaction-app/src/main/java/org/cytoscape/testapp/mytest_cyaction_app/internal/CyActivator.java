package org.cytoscape.testapp.mytest_cyaction_app.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.testapp.mytest_cyaction_app.network.GetNetworkColumnFactory;
import org.cytoscape.testapp.mytest_cyaction_app.network.NetworkNeedsFactory;
import org.cytoscape.testapp.mytest_cyaction_app.panels.ControlPanel;
import org.cytoscape.testapp.mytest_cyaction_app.panels.ControlPanelAction;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;




public class CyActivator extends AbstractCyActivator {
	
		public CyActivator() {
			super();
		};

		
		public void start(BundleContext bc) {
			
			// Menu-Action Stuff:
			CyApplicationManager cyAppMan = getService(bc,CyApplicationManager.class);
			
						
			// ControlPanel Stuff:
			CySwingApplication cytodeskService = getService(bc, CySwingApplication.class);
			MenuAction createVisualStyleAction = new MenuAction( "GTEx", cytodeskService, cyAppMan);
			ControlPanel cp = new ControlPanel(cyAppMan);
			ControlPanelAction cpa = new ControlPanelAction(cytodeskService, cp);
//			
			Properties conProps = new Properties();
			conProps.setProperty("preferredMenu", "Apps.GTEx");
			conProps.setProperty("title", "Control Panel");
			
						
			// stuff I need for Getting NetworkColumns
			GetNetworkColumnFactory getnetcol = new GetNetworkColumnFactory(cyAppMan);
			
			// stuff I need for NetworkBuilding:
			CyNetworkManager cyNetService = getService(bc, CyNetworkManager.class);
			CyNetworkFactory cyNetFacService = getService(bc, CyNetworkFactory.class);
			CyNetworkNaming cyNetNameService = getService(bc, CyNetworkNaming.class);
			
			NetworkNeedsFactory nnf = new NetworkNeedsFactory(cyNetService, cyNetFacService, cyNetNameService);
			
			
			
			
			
			// Register all Services
			
			registerService(bc,createVisualStyleAction,CyAction.class, new Properties());
			registerService(bc, nnf, TaskFactory.class, new Properties());
			registerService(bc, cp, CytoPanelComponent.class, conProps);
			registerService(bc, cpa, CyAction.class, new Properties());
			registerService(bc, getnetcol, TaskFactory.class, new Properties());
		}
	
		/*@Override
	public void start(BundleContext context) throws Exception {
		
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		
		MenuAction action = new MenuAction(cyApplicationManager, "LisasLayout");
		
		Properties properties = new Properties();
		properties.put(ServiceProperties.PREFERRED_MENU, ServiceProperties.APPS_MENU);
		properties.put(ServiceProperties.TITLE, "SomeTitle");
		
		registerAllServices(context, action, properties);
	}
*/
}
