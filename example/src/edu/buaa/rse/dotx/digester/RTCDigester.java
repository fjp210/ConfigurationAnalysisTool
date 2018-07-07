package edu.buaa.rse.dotx.digester;

import com.google.gson.JsonElement;

import edu.buaa.rse.analysis.DegestInstance;
import edu.buaa.rse.dotx.worker.digester.Digester;

public class RTCDigester extends Digester {

	@Override
	public JsonElement degest() {
		return DegestInstance.calculation(this.getJobPath(), this.getBasePath());
	}

}
