package by.senla.weather_analyzer;

import by.senla.weather_analyzer.controller.WeatherDataController;
import by.senla.weather_analyzer.dto.AverageTemperatureDTO;
import by.senla.weather_analyzer.dto.RequestForAverageTemperatureCalculationDTO;
import by.senla.weather_analyzer.dto.WeatherDataDTO;
import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.service.WeatherDataService;
import by.senla.weather_analyzer.util.exception.EntityNotFoundException;
import by.senla.weather_analyzer.util.exception.WrongDateFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WeatherAnalyzerApplicationTests {

	@Mock
	WeatherDataService weatherDataService;

	@Mock
	ModelMapper modelMapper;

	@Mock
	WeatherDataController weatherDataController;

	@Test
	void getLatestDataReturnsValidResponse() throws EntityNotFoundException {
		//given
		WeatherData weatherData = new WeatherData();
		weatherData.setTemperature(-5.0);
		weatherData.setWindSpeed(10.9);
		weatherData.setAtmosphericPressure(10007.3);
		weatherData.setAirHumidity(96);
		weatherData.setWeatherConditions("Mist");
		weatherData.setWeatherLocation("Minsk");
		weatherData.setWeatherDate(new Date());

		doReturn(weatherData).when(this.weatherDataService).findLatestData();

		//when
		ResponseEntity<WeatherDataDTO> responseEntity = this.weatherDataController.getLatestData();

		//then
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
		assertEquals(modelMapper.map(weatherData, WeatherDataDTO.class), responseEntity.getBody());
	}

	@Test
	void getLatestDataReturnsErrorResponseWhenLatestDataNotFound() throws EntityNotFoundException {
		//given
		doReturn(null).when(weatherDataService).findLatestData();

		//when
		ResponseEntity<WeatherDataDTO> responseEntity = this.weatherDataController.getLatestData();

		//then
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
	}

	@Test
	void getAverageTemperature_ReturnsValidResponse() throws WrongDateFormatException, EntityNotFoundException {
		//given
		String startDate = "2023-12-01";
		String endDate = "2023-12-31";
		double averageTemperature = -5.5;

		RequestForAverageTemperatureCalculationDTO request = new RequestForAverageTemperatureCalculationDTO();
		request.setStartDate(startDate);
		request.setEndDate(endDate);

		AverageTemperatureDTO averageTemperatureDTO = new AverageTemperatureDTO();
		averageTemperatureDTO.setAverageTemperature(averageTemperature);
		averageTemperatureDTO.setStartDate(startDate);
		averageTemperatureDTO.setEndDate(endDate);

		doReturn(averageTemperature).when(this.weatherDataService).calculateAverageTemperature(startDate, endDate);

		//when
		ResponseEntity<AverageTemperatureDTO> responseEntity = this.weatherDataController.getAverageTemperature(request);

		//then
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
		assertEquals(averageTemperatureDTO, responseEntity.getBody());
	}

}
