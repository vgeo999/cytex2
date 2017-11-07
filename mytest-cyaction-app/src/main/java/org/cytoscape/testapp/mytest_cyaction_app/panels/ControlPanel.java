package org.cytoscape.testapp.mytest_cyaction_app.panels;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class ControlPanel extends JPanel implements CytoPanelComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CyApplicationManager cyAppMan;
	
	
	
	public ControlPanel(CyApplicationManager cyAppMan) {
		
		
		// preventing cytoscape to crash because of no initial network/data loaded
		JButton loadData = new JButton("Click me after loading network and attributes!");
		
		loadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ControlPanel)((JButton)e.getSource()).getParent()).something();
				((JButton)e.getSource()).setVisible(false);
			}
		});  // end of loadData-button-ActionEvent
		
		// this.setLayout(new GridLayout(10,0,10,0));    uncomment when needed
		this.add(loadData);
		this.setVisible(true);
		this.cyAppMan = cyAppMan;
	}	// end of constructor
	
	
	// method after clicking the prevention-button
	public void something() {

		// initialize all components needed
		JLabel chooseLabel = new JLabel("Choose tissue of interest: ");
		JLabel chooseExp = new JLabel("Choose threshold for gene-expression: ");
		
		// some f*** hard-coding here - found now other way yet
		String[] tissues = new String[] {"Pancreas", "Adipose_Subcutaneous", "Adipose_Visceral_Omentum",
				"Adrenal_Gland", "Artery_Coronary", "Artery_Tibial", "Brain_Amygdala", "Brain_Cortex",
				"Brain_Hypothalamus", "Cells_Transformed_fi", "Liver", "Lung", "Muscle_Skeletal", 
				"Minor_Salivary_Gland", "Cells_EBV-transforme", "Thyroid", "Ovary"};
		
		// put the hard-coding-stuff in the drop-down-menu 
		JComboBox<String> tissueList = new JComboBox<>(tissues);
		// initialize slider for expressions
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0 , 50, 25);
		
		// this component might later be useful to reduce the network-size
		JCheckBox hideNode = new JCheckBox("Hide unselected nodes");
		// final button to click on so far
		JButton b1 = new JButton("Create View from selection");
		
		
		// set up expression-threshold-slider from data-table?
//		CyTable expTable = cyAppMan.getCurrentNetwork().getDefaultNodeTable();
//		List<Float> flc = expTable.getColumn("mean_expression").getValues(float.class);
//		Float mini = Float.parseFloat(Collections.min(flc).toString());
//		Float maxi = Float.parseFloat(Collections.max(flc).toString());
//		Float midi = mini+maxi/2;
//		Hashtable<Integer, JLabel> labTable = new Hashtable<>();
//		labTable.put(new Integer (0) , new JLabel(mini.toString()));
//		labTable.put(new Integer (5) , new JLabel(midi.toString()));
//		labTable.put(new Integer (10) , new JLabel(maxi.toString()));
//		
		
		slider.setMinorTickSpacing(2);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		//slider.setLabelTable(labTable);
		slider.setLabelTable(slider.createStandardLabels(10));
		
		
		// check-box for hiding unselected nodes to reduce network-size to display
		hideNode.setSelected(true);
		hideNode.addItemListener(new ItemListener() {
		
			// Add an ItemListener to the checkBox for getting the mouseAction 
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					CyNetworkView networkView = cyAppMan.getCurrentNetworkView();
					// do actually hide unselected nodes
					List<CyNode> unselected = CyTableUtil.getNodesInState(cyAppMan.getCurrentNetwork(), "selected", false);
					for(CyNode node : unselected) {
						View<CyNode> sourceView = networkView.getNodeView(node);
						sourceView.setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
					};
				} else {
					// do not hide unselected nodes
					CyNetworkView networkView = cyAppMan.getCurrentNetworkView();
					List<CyNode> unselected = CyTableUtil.getNodesInState(cyAppMan.getCurrentNetwork(), "selected", true);
					for(CyNode node : unselected) {
						View<CyNode> sourceView = networkView.getNodeView(node);
						sourceView.setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, true);
				}
				}; // end of else-block
				cyAppMan.getCurrentNetworkView().updateView();
			}
		}); // end of hideNode-Action-Listener
		
		
		// make some action for the final button that creates the view
		b1.setEnabled(false);
		b1.setToolTipText("This button has to be enabled in the bitter end");
//		b1.addActionListener(new ActionListener() {

//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// enable the button if every selection has been made
//				// that might be helpful for not loading the whole network by mistake too early
//// ---> ADD some network-magic here 			
//				b1.setEnabled(true);
//				cyAppMan.getCurrentNetworkView().updateView();
//			}
			
//		});
		
		
		tissueList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Get input from tissueList (hard-coded above, yeah I know, shame...)
				String input = (String) tissueList.getSelectedItem();
				CyTable table = cyAppMan.getCurrentNetwork().getDefaultNodeTable();
				Collection<CyRow> def = table.getAllRows();
		
				// select matching tissues from user-choice
				for (CyRow row : def) {
					List<String> tissues = row.getList("tissue", String.class);
					boolean matched = false;
					for (String tissue : tissues) {
						if (tissue.equals(input)) {
							row.set("selected", true);
							matched = true;
						}
					}
					if (!matched) row.set("selected", false);
				}
				
				cyAppMan.getCurrentNetworkView().updateView();
			}
		});  // end of tissueList-ActionListener
		
		// add everything to the grid-panel 
		this.setLayout(new GridLayout(10,0,10,10));
		this.add(chooseLabel);
		this.add(tissueList);
		this.add(chooseExp);
		this.add(slider);
		this.add(hideNode);
		this.add(b1);
		this.setVisible(true);
	
	}  // end of something()
	
	public Component getComponent() {
		return this;
	}
	
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}
	
	public String getTitle() {
		return "GTEx App Panel";
	}
	
	public Icon getIcon() {
		return null;
	}
	
	
}
