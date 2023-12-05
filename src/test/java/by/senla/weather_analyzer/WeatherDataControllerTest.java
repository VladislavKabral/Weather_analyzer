package by.senla.weather_analyzer;

import by.senla.weather_analyzer.controller.WeatherDataController;
import by.senla.weather_analyzer.model.WeatherData;
import by.senla.weather_analyzer.service.WeatherDataService;
import by.senla.weather_analyzer.util.exception.EntityNotFoundException;
import by.senla.weather_analyzer.util.exception.WrongDateException;
import by.senla.weather_analyzer.util.exception.WrongDateFormatException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(WeatherDataController.class)
class WeatherDataControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	WeatherDataService weatherDataService;

	@Test
	void getLatestDataReturnsValidResponse() throws Exception {
		//given
		WeatherData weatherData = new WeatherData();
		weatherData.setTemperature(-5.0);
		weatherData.setWindSpeed(10.9);
		weatherData.setAtmosphericPressure(1007.3);
		weatherData.setAirHumidity(96);
		weatherData.setWeatherConditions("Mist");
		weatherData.setWeatherLocation("Minsk");
		weatherData.setWeatherDate(new Date());

		//when
		Mockito.when(this.weatherDataService.findLatestData()).thenReturn(weatherData);

		//then
		mvc.perform(get("/weather/api/latestData"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.temperature").value(-5.0))
				.andExpect(jsonPath("$.windSpeed").value(10.9))
				.andExpect(jsonPath("$.atmosphericPressure").value(1007.3))
				.andExpect(jsonPath("$.airHumidity").value(96))
				.andExpect(jsonPath("$.weatherConditions").value("Mist"))
				.andExpect(jsonPath("$.weatherLocation").value("Minsk"));

	}

	@Test
	void getLatestDataReturnsErrorResponseWhenLatestDataNotFound() throws Exception {
		//given

		//when
		Mockito.when(this.weatherDataService.findLatestData())
				.thenThrow(new EntityNotFoundException("There isn't any data in database"));

		//then
		mvc.perform(get("/weather/api/latestData"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("There isn't any data in database"));
	}

	@Test
	void getAverageTemperatureReturnsValidResponse() throws Exception {
		//given
		String startDate = "2023-12-01";
		String endDate = "2023-12-31";
		double averageTemperature = -5.5;

		//when
		Mockito.when(this.weatherDataService.calculateAverageTemperature(startDate, endDate))
				.thenReturn(averageTemperature);

		//then
		mvc.perform(post("/weather/api/averageTemperature")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content("{\"startDate\": \"2023-12-01\", \"endDate\": \"2023-12-31\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.startDate").value(startDate))
				.andExpect(jsonPath("$.endDate").value(endDate))
				.andExpect(jsonPath("$.averageTemperature").value(averageTemperature));
	}

	@Test
	void getAverageTemperatureReturnsErrorResponseWhenDataNotFound() throws Exception {
		//given
		String startDate = "2023-12-01";
		String endDate = "2023-12-31";

		//when
		Mockito.when(this.weatherDataService.calculateAverageTemperature(startDate, endDate))
				.thenThrow(new EntityNotFoundException("There isn't any data with these dates in database"));

		//then
		mvc.perform(post("/weather/api/averageTemperature")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content("{\"startDate\": \"2023-12-01\", \"endDate\": \"2023-12-31\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("There isn't any data with these dates in database"));
	}

	@Test
	void getAverageTemperatureReturnsErrorResponseWhenStartDateFormatIsWrong() throws Exception {
		//given
		String startDate = "2023-12-as";
		String endDate = "2023-12-31";

		//when
		Mockito.when(this.weatherDataService.calculateAverageTemperature(startDate, endDate))
				.thenThrow(new WrongDateFormatException("Wrong format of start date. Right format is 'yyyy-MM-dd'"));

		//then
		mvc.perform(post("/weather/api/averageTemperature")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content("{\"startDate\": \"2023-12-as\", \"endDate\": \"2023-12-31\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Wrong format of start date. Right format is 'yyyy-MM-dd'"));
	}

	@Test
	void getAverageTemperatureReturnsErrorResponseWhenEndDateFormatIsWrong() throws Exception {
		//given
		String startDate = "2023-12-01";
		String endDate = "2023-12-33";

		//when
		Mockito.when(this.weatherDataService.calculateAverageTemperature(startDate, endDate))
				.thenThrow(new WrongDateFormatException("Wrong format of end date. Right format is 'yyyy-MM-dd'"));

		//then
		mvc.perform(post("/weather/api/averageTemperature")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content("{\"startDate\": \"2023-12-01\", \"endDate\": \"2023-12-33\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Wrong format of end date. Right format is 'yyyy-MM-dd'"));
	}

	@Test
	void getAverageTemperatureReturnsErrorResponseWhenEndDateIsBeforeStartDate() throws Exception {
		//given
		String startDate = "2023-12-31";
		String endDate = "2023-12-01";

		//when
		Mockito.when(this.weatherDataService.calculateAverageTemperature(startDate, endDate))
				.thenThrow(new WrongDateException("End date is before start date"));

		//then
		mvc.perform(post("/weather/api/averageTemperature")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content("{\"startDate\": \"2023-12-31\", \"endDate\": \"2023-12-01\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("End date is before start date"));
	}
}
