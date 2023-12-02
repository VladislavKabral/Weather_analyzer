package by.senla.weather_analyzer.service;

import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherDataService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    @Transactional
    public void save(WeatherData weatherData) {
        weatherDataRepository.save(weatherData);
    }
}
