package by.senla.weather_analyzer.controller;

import by.senla.weather_analyzer.dto.AverageTemperatureDTO;
import by.senla.weather_analyzer.dto.RequestForAverageTemperatureCalculationDTO;
import by.senla.weather_analyzer.dto.WeatherDataDTO;
import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.service.WeatherDataService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

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
        WeatherData weatherData = weatherDataService.findLatestData();

        return convertToWeatherDataDTO(weatherData);
    }

    @PostMapping("/averageTemperature")
    public AverageTemperatureDTO getAverageTemperature(@RequestBody RequestForAverageTemperatureCalculationDTO request) {

        return new AverageTemperatureDTO(request.getStartDate(), request.getEndDate(),
                weatherDataService.calculateAverageTemperature(request.getStartDate(), request.getEndDate()));
    }

    private WeatherDataDTO convertToWeatherDataDTO(WeatherData weatherData) {
        return modelMapper.map(weatherData, WeatherDataDTO.class);
    }
}
