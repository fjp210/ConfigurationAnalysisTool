package edu.buaa.rse.dotx.json;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TestJson {
	public static void main(String[] args) {
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
		
		
		Gson gson = new Gson();
		gson.toJson(total, System.out);
		
		
		
	}
}
