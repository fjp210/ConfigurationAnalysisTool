package edu.buaa.rse.dotx.worker.digester.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.model.deploy.Deploy;
import edu.buaa.rse.dotx.worker.digester.Digester;

public class BaseDigester extends Digester {
	
	@Override
	public JsonElement degest() {
		JsonElement result = getTestData();
		return result;
	}
	
	private JsonElement getTestData(){
		JsonArray total = new JsonArray();
		
		JsonArray runtime = new JsonArray();
		JsonObject temp = new JsonObject();
		temp.addProperty("1", 0);
		runtime.add(temp);
		temp = new JsonObject();
		temp.addProperty("2", 0);
		runtime.add(temp);
		temp = new JsonObject();
		temp.addProperty("3", 1);
		runtime.add(temp);
		
		JsonObject one = new JsonObject();
		one.add("runtime", runtime);
		one.addProperty("reliability", 43.34);
		one.addProperty("complex", 32.123);
		one.addProperty("schedule", 3.243);
		
		JsonObject result = new JsonObject();
		result.add("1", one);
		total.add(result);
		
		
		one = new JsonObject();
		one.add("runtime", runtime);
		one.addProperty("reliability", 32.34);
		one.addProperty("complex", 35.323);
		one.addProperty("schedule", 2.873);
		
		result = new JsonObject();
		result.add("2", one);
		total.add(result);
		
		return total;
	}
	
}
