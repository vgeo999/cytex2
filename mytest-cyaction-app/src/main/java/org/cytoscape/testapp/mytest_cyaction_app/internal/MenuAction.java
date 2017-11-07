package org.cytoscape.testapp.mytest_cyaction_app.internal;


import java.awt.event.ActionEvent;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.testapp.mytest_cyaction_app.panels.ControlPanelAction;



/**
 * Creates a new menu item under Apps menu section.
 *
 */
public class MenuAction extends AbstractCyAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CyApplicationManager cyAppMan;
	private CySwingApplication desktopapp;

	public MenuAction(final String menuTitle, CySwingApplication desktopapp, CyApplicationManager cyAppMan) {
		
		
		super(menuTitle, cyAppMan, null, null);
		setPreferredMenu("Apps");
		
		this.cyAppMan = cyAppMan;
		this.desktopapp = desktopapp;
		
	}

	/**
	 *  Some Documentation
	 *
	 * @param e uuuuund Action
	 */
	
	public void actionPerformed(ActionEvent e) {
		CytoPanel cytoPanelWest = desktopapp.getCytoPanel(CytoPanelName.WEST);
		if (cytoPanelWest.getState() == CytoPanelState.HIDE) {
			cytoPanelWest.setState(CytoPanelState.DOCK);
		}	
		
		
		
	}
}
