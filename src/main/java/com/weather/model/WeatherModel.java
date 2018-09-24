package com.weather.model;

import org.springframework.stereotype.Component;

@Component
public class WeatherModel {
	private int id;
	private int responseid;
	private String location;
	private String actualWeather;
	private String temperature;
	private String dtimeInserted;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getResponseid() {
		return responseid;
	}
	public void setResponseid(int responseid) {
		this.responseid = responseid;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getActualWeather() {
		return actualWeather;
	}
	public void setActualWeather(String actualWeather) {
		this.actualWeather = actualWeather;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getDtimeInserted() {
		return dtimeInserted;
	}
	public void setDtimeInserted(String dtimeInserted) {
		this.dtimeInserted = dtimeInserted;
	}
	
	
}
