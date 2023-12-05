package by.senla.weather_analyzer.service;

import by.senla.weather_analyzer.util.exception.WrongDateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOGGER = LogManager.getLogger(WeatherDataService.class);

    @Autowired
    public WeatherDataService(WeatherDataRepository weatherDataRepository, DateValidator dateValidator) {
        this.weatherDataRepository = weatherDataRepository;
        this.dateValidator = dateValidator;
    }

    public WeatherData findLatestData() throws EntityNotFoundException {
        WeatherData weatherData = weatherDataRepository.findFirstByOrderByIdDesc();

        if (weatherData == null) {
            LOGGER.error("There isn't any data in database");
            throw new EntityNotFoundException("There isn't any data in database");
        }

        LOGGER.info("Latest data from database was found");
        return weatherData;
    }

    private List<WeatherData> findByDateBetween(String startDate, String endDate) throws EntityNotFoundException {
        List<WeatherData> weatherDataList = weatherDataRepository.findByWeatherDateBetween(convertStringToDate(startDate),
                convertStringToDate(endDate));

        if (weatherDataList.isEmpty()) {
            LOGGER.error("There isn't any data from " + startDate + " to " + endDate);
            throw new EntityNotFoundException("There isn't any data with these dates in database");
        }

        return weatherDataList;
    }

    public double calculateAverageTemperature(String startDate, String endDate) throws EntityNotFoundException,
            WrongDateFormatException, WrongDateException {

        double averageTemperature = 0.0;

        if (dateValidator.validate(startDate)) {
            LOGGER.error(startDate + "is wrong format of start date");
            throw new WrongDateFormatException("Wrong format of start date. Right format is 'yyyy-MM-dd'");
        }

        if (dateValidator.validate(endDate)) {
            LOGGER.error(endDate + "is wrong format of end date");
            throw new WrongDateFormatException("Wrong format of end date. Right format is 'yyyy-MM-dd'");
        }

        if (convertStringToDate(endDate).before(convertStringToDate(startDate))) {
            LOGGER.error("End date is before start date");
            throw new WrongDateException("End date is before start date");
        }

        LOGGER.info("Start and end dates are correct");
        List<WeatherData> weatherDataList = findByDateBetween(startDate, endDate);

        for (WeatherData weatherData: weatherDataList) {
            averageTemperature += weatherData.getTemperature();
        }

        LOGGER.info("Average temperature is " + averageTemperature / weatherDataList.size());
        return averageTemperature / weatherDataList.size();
    }

    @Transactional
    public void save(WeatherData weatherData) {
        LOGGER.info("New entity was saved");
        weatherDataRepository.save(weatherData);
    }

    private Date convertStringToDate(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate;

        try {
            convertedDate = format.parse(date);
        } catch (ParseException e) {
            LOGGER.error("Exception in converting date from String");
            throw new RuntimeException(e);
        }

        return convertedDate;
    }
}
