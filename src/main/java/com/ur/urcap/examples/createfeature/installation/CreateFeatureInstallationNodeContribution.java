package com.ur.urcap.examples.createfeature.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.InstallationAPI;
import com.ur.urcap.api.domain.UserInterfaceAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.feature.Feature;
import com.ur.urcap.api.domain.feature.FeatureContributionModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.RobotPositionCallback2;
import com.ur.urcap.api.domain.util.Filter;
import com.ur.urcap.api.domain.value.jointposition.JointPositions;
import com.ur.urcap.api.domain.value.robotposition.PositionParameters;
import com.ur.urcap.api.domain.variable.Variable;
import java.awt.EventQueue;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class CreateFeatureInstallationNodeContribution implements InstallationNodeContribution {
	private static final String FEATURE_KEY = "FeatureKey";
	private static final String FEATURE_JOINT_ANGLES_KEY = "jointAngles";
	private static final String SUGGESTED_FEATURE_NAME = "URCapFeature";
	private static final String SELECTED_VAR = "selectedVar";
	
	private final DataModel model;
	private final CreateFeatureInstallationNodeView view;
	private FeatureContributionModel featureContributionModel;
	private UserInterfaceAPI userInterfaceAPI;
	private InstallationAPI installAPI;
	
	private Timer uiTimer;
	private boolean pauseTimer = false;
	
	protected String response = "No response";
	private final VariableCollection vc = new VariableCollection();
  
	CreateFeatureInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, CreateFeatureInstallationNodeView view) {
		this.model = model;
		this.view = view;
		this.installAPI = apiProvider.getInstallationAPI();
		this.featureContributionModel = apiProvider.getInstallationAPI().getFeatureContributionModel();
		this.userInterfaceAPI = apiProvider.getUserInterfaceAPI();
	}
  
	public void openView() {
		this.view.featureIsCreated(isFeatureCreated());
		this.view.updateView(this);
	}
  
	public void closeView() {
		if (this.uiTimer != null)
		this.uiTimer.cancel(); 
	}
  
	public void generateScript(ScriptWriter writer) {}
  
	public boolean isFeatureCreated() {
		return this.model.isSet("FeatureKey");
	}
  
	public Feature getFeature() {
		return this.model.get("FeatureKey", (Feature)null);
	}
  
	public JointPositions getFeatureJointPositions() {
		return this.model.get("jointAngles", (JointPositions)null);
	}
  
	void createFeature() {
		this.userInterfaceAPI.getUserInteraction().getUserDefinedRobotPosition(new RobotPositionCallback2() {
			public void onOk(PositionParameters positionParameters) {
				Feature feature = CreateFeatureInstallationNodeContribution.this.featureContributionModel.addFeature("FeatureKey", "URCapFeature", positionParameters.getPose());
				CreateFeatureInstallationNodeContribution.this.model.set("FeatureKey", feature);
				CreateFeatureInstallationNodeContribution.this.model.set("jointAngles", positionParameters.getJointPositions());
				CreateFeatureInstallationNodeContribution.this.view.featureIsCreated(true);
			}
			});
	}
  
	void updateFeature() {
		this.userInterfaceAPI.getUserInteraction().getUserDefinedRobotPosition(new RobotPositionCallback2() {
			public void onOk(PositionParameters positionParameters) {
				CreateFeatureInstallationNodeContribution.this.featureContributionModel.updateFeature("FeatureKey", positionParameters.getPose());
				CreateFeatureInstallationNodeContribution.this.model.set("jointAngles", positionParameters.getJointPositions());
			}
			});
	}
  
	void deleteFeature() {
		this.featureContributionModel.removeFeature("FeatureKey");
		this.model.remove("FeatureKey");
		this.model.remove("jointAngles");
		this.view.featureIsCreated(false);
	}
  
	public Collection<Variable> getInstallationVars() {
		return this.installAPI.getVariableModel().get(new Filter<Variable>() {
			public boolean accept(Variable element) {
				return element.getType().equals(Variable.Type.VALUE_PERSISTED);
			}
			});
	}
  
	public Variable getSelectedVariable() {
		return this.model.get("selectedVar", (Variable)null);
	}
  
	public void setVariable(Variable variable) {
		this.model.set("selectedVar", variable);
	}
  
	public void removeVariable() {
		this.model.remove("selectedVar");
	}
  
	// public void getVarFromDashboard() {
	// 	this.uiTimer = new Timer(true);
	// 	this.uiTimer.schedule(new TimerTask() {
	// 		public void run() {
	// 			EventQueue.invokeLater(new Runnable() {
	// 				public void run() {
	// 					if (!CreateFeatureInstallationNodeContribution.this.pauseTimer) {
	// 					String response = vc.getVarVal(CreateFeatureInstallationNodeContribution.this.getSelectedVariable().toString());
	// 					CreateFeatureInstallationNodeContribution.this.view.updateVarLabel(response);
	// 					} 
	// 				}
	// 				}  );
	// 		}
	// 		},  0L, 1000L);
	// }

	public void getVarFromDashboard() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						String response = vc.getVarVal(CreateFeatureInstallationNodeContribution.this.getSelectedVariable().toString());
						CreateFeatureInstallationNodeContribution.this.view.updateVarLabel(response);
					}
				});
			}
		}).start();
	}
	
}
