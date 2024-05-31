package com.ur.urcap.examples.createfeature.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.variable.Variable;
import com.ur.urcap.examples.createfeature.style.Style;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CreateFeatureInstallationNodeView implements SwingInstallationNodeView<CreateFeatureInstallationNodeContribution> {
	private static final String DESCRIPTION_TXT = "<html>Create and modify an installation feature, that will be used in the Use Feature program node.<br/> The created feature can be inspected in PolyScopes feature screen under the installation tab</html>";
  
	private static final String CREATE_FEATURE_TXT = "Create feature";
  
	private static final String UPDATE_FEATURE_TXT = "Update feature";
  
	private static final String DELETE_FEATURE_TXT = "Delete feature";
  
	private JButton createFeatureButton;
	private JButton updateFeatureButton;
	private JButton deleteFeatureButton;
	private JLabel errorLabel = new JLabel();
	private JComboBox variablesComboBox = new JComboBox();
	private JLabel varLabel = new JLabel("Var:");
	private JButton getVarValButton;
	
	private final Style style;
  
	public CreateFeatureInstallationNodeView(Style style) {
    	this.style = style;
	}
  
	public void buildUI(JPanel panel, CreateFeatureInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, 1));
		Box content = Box.createVerticalBox();
		content.setAlignmentX(0.0F);
		content.setAlignmentY(0.0F);
		content.add(createHeaderSection());
		content.add(createVerticalSpacing());
		content.add(createFeatureSection(contribution));
		content.add(createVerticalSpacing());
		content.add(createComboBox(contribution));
		content.add(createVerticalSpacing());
		content.add(this.varLabel);
		content.add(createVerticalSpacing());
		content.add(createGetVarValButton(contribution));
		content.add(createVerticalSpacing());
		panel.add(content);
	}
  
	void featureIsCreated(boolean featureCreated) {
		this.createFeatureButton.setEnabled(!featureCreated);
		this.updateFeatureButton.setEnabled(featureCreated);
		this.deleteFeatureButton.setEnabled(featureCreated);
	}
  
	private Component createFeatureSection(CreateFeatureInstallationNodeContribution contribution) {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(0.0F);
		section.setAlignmentY(0.0F);
		this.createFeatureButton = createCreateFeatureButton(contribution);
		section.add(this.createFeatureButton);
		section.add(createHorizontalSpacing());
		this.updateFeatureButton = createUpdateFeatureButton(contribution);
		section.add(this.updateFeatureButton);
		section.add(createHorizontalSpacing());
		this.deleteFeatureButton = createDeleteFeatureButton(contribution);
		section.add(this.deleteFeatureButton);
		return section;
	}
  
	private JButton createCreateFeatureButton(final CreateFeatureInstallationNodeContribution contribution) {
		JButton btn = new JButton("Create feature");
		btn.setFocusPainted(false);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contribution.createFeature();
			}
			});
		return btn;
	}
  
	private JButton createUpdateFeatureButton(final CreateFeatureInstallationNodeContribution contribution) {
		JButton btn = new JButton("Update feature");
		btn.setFocusPainted(false);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contribution.updateFeature();
			}
			});
		return btn;
	}
  
	private JButton createDeleteFeatureButton(final CreateFeatureInstallationNodeContribution contribution) {
		JButton btn = new JButton("Delete feature");
		btn.setFocusPainted(false);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contribution.deleteFeature();
			}
			});
		return btn;
	}
  
	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(10, 0));
	}
  
	private Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, 15));
	}
  
	private Box createHeaderSection() {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(0.0F);
		JLabel descriptionLabel = new JLabel("<html>Create and modify an installation feature, that will be used in the Use Feature program node.<br/> The created feature can be inspected in PolyScopes feature screen under the installation tab</html>");
		section.add(descriptionLabel);
		return section;
	}
  
	private Box createComboBox(final CreateFeatureInstallationNodeContribution contribution) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(0.0F);
		this.variablesComboBox.setFocusable(false);
		this.variablesComboBox.setPreferredSize(this.style.getComboBoxDimension());
		this.variablesComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == 1)
				if (itemEvent.getItem() instanceof Variable) {
					contribution.setVariable((Variable)itemEvent.getItem());
				} else {
					contribution.removeVariable();
				}  
			}
			});
		JPanel panel = new JPanel(new FlowLayout(0));
		panel.add(this.variablesComboBox, "Center");
		inputBox.add(panel);
		return inputBox;
	}
  
	private JButton createGetVarValButton(final CreateFeatureInstallationNodeContribution contribution) {
		JButton button = new JButton("Get Variable Value");
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contribution.getVarFromDashboard();
			}
			});
		return button;
	}
  
	protected void updateVarLabel(String response) {
		this.varLabel.setText(response);
	}
  

    private void updateVariablesComboBox(CreateFeatureInstallationNodeContribution contribution) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        List<Variable> variables = getVariables(contribution);

        Variable selectedVariable = contribution.getSelectedVariable();
        if (selectedVariable != null) {
            model.setSelectedItem(selectedVariable);
        }
        model.addElement("<Variable>");

        for (Variable variable : variables) {
            model.addElement(variable);
        }

        variablesComboBox.setModel(model);
    }
  
	private List<Variable> getVariables(CreateFeatureInstallationNodeContribution contribution) {
		List<Variable> sortedVariables = new ArrayList<>(contribution.getInstallationVars());
		Collections.sort(sortedVariables, new Comparator<Variable>() {
			public int compare(Variable var1, Variable var2) {
				if (var1.toString().toLowerCase().compareTo(var2.toString().toLowerCase()) == 0)
				return var1.toString().compareTo(var2.toString()); 
				return var1.toString().toLowerCase().compareTo(var2.toString().toLowerCase());
			}
			});
		return sortedVariables;
	}
  
	private void clearErrors() {
    	this.errorLabel.setVisible(false);
	}
  
	public void updateView(CreateFeatureInstallationNodeContribution contribution) {
		clearErrors();
		updateVariablesComboBox(contribution);
	}

}
