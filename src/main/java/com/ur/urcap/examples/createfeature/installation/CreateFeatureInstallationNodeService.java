package com.ur.urcap.examples.createfeature.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.createfeature.style.Style;
import com.ur.urcap.examples.createfeature.style.V3Style;
import com.ur.urcap.examples.createfeature.style.V5Style;
import java.util.Locale;

public class CreateFeatureInstallationNodeService implements SwingInstallationNodeService<CreateFeatureInstallationNodeContribution, CreateFeatureInstallationNodeView> {
	public void configureContribution(ContributionConfiguration configuration) {}
  
	public String getTitle(Locale locale) {
    	return "Create Feature";
	}
  
	public CreateFeatureInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = (systemAPI.getSoftwareVersion().getMajorVersion() >= 5) ? (Style)new V5Style() : (Style)new V3Style();
		return new CreateFeatureInstallationNodeView(style);
	}
  
	public CreateFeatureInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, CreateFeatureInstallationNodeView view, DataModel model, CreationContext context) {
		return new CreateFeatureInstallationNodeContribution(apiProvider, model, view);
	}
}
