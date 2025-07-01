package com.agentj.standalone.weather.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public class WeatherAPI {

    private final Random temperatureGenerator = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    public String getWeatherInformation(String city){
        double temperature = temperatureGenerator.nextDouble(1D, 50);
        return asJson(new WeatherInfo(city, temperature + " Celsius"));
    }

    private String asJson(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
