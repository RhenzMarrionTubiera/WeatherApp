package com.weather.service;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Calendar;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Connection;

@Component
public class WeatherServiceImpl implements WeatherService {
	@Value("${weather.env}")
	private String env;
	@Value("${weather.appId}")
	private String appId;
	@Value("${weather.countryId}")
	private String countryId;
	
	@Value("${db.myDriver}")
	private String myDriver;
	@Value("${db.myUrl}")
	private String myUrl;
	@Value("${db.username}")
	private String username;
	@Value("${db.password}")
	private String password;
	
	private int responseid;
	private String location;
	private String actualWeather;
	private String temperature;
	
	public String getWeather() throws ClientProtocolException, IOException{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(env+appId+countryId);
		CloseableHttpResponse response = client.execute(httpGet);
		if(response.getStatusLine().getStatusCode()== 200){
			String response2 = EntityUtils.toString(response.getEntity());
			
			JsonParser jsonParser = new JsonParser();
			
			JsonObject responseidOBJ = (JsonObject) jsonParser.parse(response2).getAsJsonObject();
			responseid = responseidOBJ.get("id").getAsInt();
			System.out.println("responseid: "+responseid);
			
			JsonObject locationOBJ = (JsonObject) jsonParser.parse(response2).getAsJsonObject();
			location = locationOBJ.get("name").getAsString();
			System.out.println("location: "+location);
			
			JsonObject actualWeatherOBJ = (JsonObject) jsonParser.parse(response2).getAsJsonObject().getAsJsonArray("weather").get(0);
			actualWeather = actualWeatherOBJ.get("description").getAsString();
			System.out.println("actualWeather: "+actualWeather);
			
			JsonObject temperatureOBJ = (JsonObject) jsonParser.parse(response2).getAsJsonObject().get("main");
			temperature = temperatureOBJ.get("temp").getAsString();
			System.out.println("temperature: "+temperature);
			
			try {
		      Class.forName(myDriver);
		      Connection conn = (Connection) DriverManager.getConnection(myUrl, username, password);
		    
		      Calendar calendar = Calendar.getInstance();
		      java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

		      String query = " INSERT INTO weather ( id, responseid, location, actualWeather, temperature, dtimeInserted )"
		        + " VALUES ( ?, ?, ?, ?, ?, ? )";

		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setInt (1, (Integer) null);
		      preparedStmt.setInt (2, responseid);
		      preparedStmt.setString (3, location);
		      preparedStmt.setString (4, actualWeather);
		      preparedStmt.setString (5, temperature);
		      preparedStmt.setDate (6, startDate);
		      preparedStmt.execute();
		      
		      conn.close();
		    }catch (Exception e){
		      System.err.println("Insert to DB failed!");
		      System.err.println(e.getMessage());
		    }
		}else{
			System.out.println("Failed to get JSON");
		}
		EntityUtils.consume(response.getEntity());
		client.close();
		
		return  "responseid: "+responseid+"\n | "+
				"location: "+location+"\n | "+
				"actualWeather: "+actualWeather+"\n | "+
				"temperature: "+temperature+"\n | "
				;
	}
}
