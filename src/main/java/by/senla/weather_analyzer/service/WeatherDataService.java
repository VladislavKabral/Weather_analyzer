package by.senla.weather_analyzer.service;

import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.repository.WeatherDataRepository;
import by.senla.weather_analyzer.util.exception.EntityNotFoundException;
import by.senla.weather_analyzer.util.exception.WrongDateFormatException;
import by.senla.weather_analyzer.util.validation.DateValidator;
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

    private final DateValidator dateValidator;

    @Autowired
    public WeatherDataService(WeatherDataRepository weatherDataRepository, DateValidator dateValidator) {
        this.weatherDataRepository = weatherDataRepository;
        this.dateValidator = dateValidator;
    }

    public WeatherData findLatestData() throws EntityNotFoundException {
        WeatherData weatherData = weatherDataRepository.findFirstByOrderByIdDesc();

        if (weatherData == null) {
            throw new EntityNotFoundException("There isn't any data in database");
        }

        return weatherData;
    }

    private List<WeatherData> findByDateBetween(String startDate, String endDate) throws EntityNotFoundException {
        List<WeatherData> weatherDataList = weatherDataRepository.findByWeatherDateBetween(convertStringToDate(startDate),
                convertStringToDate(endDate));

        if (weatherDataList.isEmpty()) {
            throw new EntityNotFoundException("There isn't any data with these dates in database");
        }

        return weatherDataList;
    }

    public double calculateAverageTemperature(String startDate, String endDate) throws EntityNotFoundException,
            WrongDateFormatException {

        double averageTemperature = 0.0;

        if (dateValidator.validate(startDate)) {
            throw new WrongDateFormatException("Wrong format of start date. Right format is 'yyyy-MM-dd'");
        }

        if (dateValidator.validate(endDate)) {
            throw new WrongDateFormatException("Wrong format of end date. Right format is 'yyyy-MM-dd'");
        }

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
