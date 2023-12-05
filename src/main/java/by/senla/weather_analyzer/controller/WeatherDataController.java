package by.senla.weather_analyzer.controller;

import by.senla.weather_analyzer.util.exception.WrongDateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.senla.weather_analyzer.dto.AverageTemperatureDTO;
import by.senla.weather_analyzer.dto.ErrorResponseDTO;
import by.senla.weather_analyzer.dto.RequestForAverageTemperatureCalculationDTO;
import by.senla.weather_analyzer.dto.WeatherDataDTO;
import by.senla.weather_analyzer.model.ErrorResponse;
import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.service.WeatherDataService;
import by.senla.weather_analyzer.util.exception.EntityNotFoundException;
import by.senla.weather_analyzer.util.exception.WrongDateFormatException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/weather/api")
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    private final ModelMapper modelMapper;

    private static final Logger LOGGER = LogManager.getLogger(WeatherDataController.class);

    public WeatherDataController(WeatherDataService weatherDataService, ModelMapper modelMapper) {
        this.weatherDataService = weatherDataService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/latestData")
    public ResponseEntity<WeatherDataDTO> getLatestData() throws EntityNotFoundException {
        LOGGER.debug("Start of getting the latest data from database");
        WeatherData weatherData = weatherDataService.findLatestData();

        LOGGER.debug("End of getting the latest data from database");
        return new ResponseEntity<>(convertToWeatherDataDTO(weatherData), HttpStatus.OK);
    }

    @PostMapping("/averageTemperature")
    public ResponseEntity<AverageTemperatureDTO> getAverageTemperature(
            @RequestBody RequestForAverageTemperatureCalculationDTO request) throws EntityNotFoundException,
            WrongDateFormatException, WrongDateException {

        LOGGER.debug("Start of calculation average temperature");
        AverageTemperatureDTO averageTemperatureDTO = new AverageTemperatureDTO(request.getStartDate(), request.getEndDate(),
                weatherDataService.calculateAverageTemperature(request.getStartDate(), request.getEndDate()));

        LOGGER.debug("End of calculation average temperature, result is: " + averageTemperatureDTO.getAverageTemperature());
        return new ResponseEntity<>(averageTemperatureDTO, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        LOGGER.warn("Caught an exception: " + e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(new Date(), e.getMessage());

        return new ResponseEntity<>(convertToErrorResponseDTO(errorResponse), HttpStatus.BAD_REQUEST);
    }

    private WeatherDataDTO convertToWeatherDataDTO(WeatherData weatherData) {
        return modelMapper.map(weatherData, WeatherDataDTO.class);
    }

    private ErrorResponseDTO convertToErrorResponseDTO(ErrorResponse errorResponse) {
        return modelMapper.map(errorResponse, ErrorResponseDTO.class);
    }
}
