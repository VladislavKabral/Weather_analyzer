package by.senla.weather_analyzer.controller;

import by.senla.weather_analyzer.dto.WeatherDataDTO;
import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.service.WeatherDataService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather/api")
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    private final ModelMapper modelMapper;

    public WeatherDataController(WeatherDataService weatherDataService, ModelMapper modelMapper) {
        this.weatherDataService = weatherDataService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/latestData")
    public WeatherDataDTO getLatestData() {
        WeatherData weatherData = weatherDataService.getLatestData();

        return convertToWeatherDataDTO(weatherData);
    }

    private WeatherDataDTO convertToWeatherDataDTO(WeatherData weatherData) {
        return modelMapper.map(weatherData, WeatherDataDTO.class);
    }
}
