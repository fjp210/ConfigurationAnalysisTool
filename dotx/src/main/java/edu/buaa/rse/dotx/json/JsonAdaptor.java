package edu.buaa.rse.dotx.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonAdaptor {
private static JsonAdaptor _instance;
	
	public static JsonAdaptor getInstance(){
		if(JsonAdaptor._instance==null){
			synchronized(JsonAdaptor.class){
				if(JsonAdaptor._instance == null){
					JsonAdaptor instance = new JsonAdaptor();
					JsonAdaptor._instance = instance;
				}
			}
		}
		return JsonAdaptor._instance;
    }
	
	public String objectToFile(JsonElement json, String filePath){
		Writer writer = null;
		try {
			writer = new FileWriter(filePath);
			Gson gson = new GsonBuilder().create();
	        gson.toJson(json, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return filePath;
	}
	
	public JsonElement fileToObject(String filePath){
		JsonParser parse = new JsonParser(); 
		JsonElement json = null;
		try {
			json = parse.parse(new FileReader(filePath));
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return json;
	}
	
}
