package com.ruslanburduzhan.weatherbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruslanburduzhan.weatherbot.entity.api.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WeatherService {

    final String KEY;

    public WeatherService(@Value("${weather.key}")String key) {
        KEY = key;
    }

    public Weather getWeatherApi(String city) {
        try {
            String url = "http://api.weatherapi.com/v1/current.json?" + "key=" + KEY + "&q=" + city;
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            if(getResponse.body().startsWith("{\"error\"")) throw new Exception("IncorrectCity");
            return mapper.readValue(getResponse.body(), Weather.class);
        } catch (Exception e) {
            return new Weather(e.getMessage());
        }
    }
}
