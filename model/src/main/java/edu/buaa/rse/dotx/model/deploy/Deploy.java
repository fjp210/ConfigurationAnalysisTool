package edu.buaa.rse.dotx.model.deploy;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("deploy")
public class Deploy extends Base{
	AppToPartion appToPartion;

	public AppToPartion getAppToPartion() {
		return appToPartion;
	}

	public void setAppToPartion(AppToPartion appToPartion) {
		this.appToPartion = appToPartion;
	}
}
