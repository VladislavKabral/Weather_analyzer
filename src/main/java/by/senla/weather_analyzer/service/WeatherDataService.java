package by.senla.weather_analyzer.service;

import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherDataService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    public WeatherData findLatestData() {
        return weatherDataRepository.findFirstByOrderByIdDesc();
    }

    private List<WeatherData> findByDateBetween(String startDate, String endDate) {
        return weatherDataRepository.findByWeatherDateBetween(convertStringToDate(startDate),
                convertStringToDate(endDate));
    }

    public double calculateAverageTemperature(String startDate, String endDate) {
        double averageTemperature = 0.0;
        List<WeatherData> weatherDataList = findByDateBetween(startDate, endDate);

        for (WeatherData weatherData: weatherDataList) {
            averageTemperature += weatherData.getTemperature();
        }

        return averageTemperature / weatherDataList.size();
    }

    @Transactional
    public void save(WeatherData weatherData) {
        weatherDataRepository.save(weatherData);
    }

    private Date convertStringToDate(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate;

        try {
            convertedDate = format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return convertedDate;
    }
}
