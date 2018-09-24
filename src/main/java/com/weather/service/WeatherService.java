package com.weather.service;

import org.springframework.stereotype.Service;

@Service
public interface WeatherService {
	public String getWeather() throws Exception;
}
