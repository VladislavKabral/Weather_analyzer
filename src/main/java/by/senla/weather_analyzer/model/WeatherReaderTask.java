package by.senla.weather_analyzer.model;

import by.senla.weather_analyzer.service.WeatherDataService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.TimerTask;

@Component
public class WeatherReaderTask extends TimerTask {

    @Value("${api_uri}")
    private String apiUri;

    @Value("${api_header_key_name}")
    private String apiHeaderKeyName;

    @Value("${api_header_host_name}")
    private String apiHeaderHostName;

    @Value("${api_header_key_value}")
    private String apiHeaderKeyValue;

    @Value("${api_header_host_value}")
    private String apiHeaderHostValue;

    private static final String CURRENT_NODE_NAME = "current";

    private static final String LOCATION_NODE_NAME = "location";

    private static final String CONDITION_NODE_NAME = "condition";

    private static final String TEMPERATURE_FIELD_NAME = "temp_c";

    private static final String WIND_SPEED_FIELD_NAME = "wind_kph";

    private static final String ATMOSPHERIC_PRESSURE_FIELD_NAME = "pressure_mb";

    private static final String AIR_HUMIDITY_FIELD_NAME = "humidity";

    private static final String WEATHER_CONDITIONS_FIELD_NAME = "text";

    private static final String WEATHER_LOCATION_FIELD_NAME = "name";

    private final WeatherDataService weatherDataService;

    @Autowired
    public WeatherReaderTask(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    @Override
    public void run() {
        try {
            getWeatherFromAPI();
        } catch (ParseException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getWeatherFromAPI() throws ParseException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUri))
                .header(apiHeaderKeyName, apiHeaderKeyValue)
                .header(apiHeaderHostName, apiHeaderHostValue)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(response.body());
        JSONObject currentNode = (JSONObject) JSONValue.parseWithException(jsonObject.get(CURRENT_NODE_NAME)
                .toString());
        JSONObject locationNode = (JSONObject) JSONValue.parseWithException(jsonObject.get(LOCATION_NODE_NAME)
                .toString());
        JSONObject conditionNode = (JSONObject) JSONValue.parseWithException(currentNode.get(CONDITION_NODE_NAME)
                .toString());

        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature((Double) currentNode.get(TEMPERATURE_FIELD_NAME));
        weatherData.setWindSpeed((Double) currentNode.get(WIND_SPEED_FIELD_NAME));
        weatherData.setAtmosphericPressure((Double) currentNode.get(ATMOSPHERIC_PRESSURE_FIELD_NAME));
        weatherData.setAirHumidity((Long) currentNode.get(AIR_HUMIDITY_FIELD_NAME));
        weatherData.setWeatherConditions((String) conditionNode.get(WEATHER_CONDITIONS_FIELD_NAME));
        weatherData.setWeatherLocation((String) locationNode.get(WEATHER_LOCATION_FIELD_NAME));
        weatherData.setWeatherDate(new Date());

        weatherDataService.save(weatherData);
    }
}
