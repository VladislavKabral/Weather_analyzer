package by.senla.weather_analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataDTO {

    private double temperature;

    private double windSpeed;

    private double atmosphericPressure;

    private long airHumidity;

    private String weatherConditions;

    private String weatherLocation;

    private Date weatherDate;
}
