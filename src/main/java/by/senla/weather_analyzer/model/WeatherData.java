package by.senla.weather_analyzer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "weather_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "wind_speed")
    private double windSpeed;

    @Column(name = "atmospheric_pressure")
    private double atmosphericPressure;

    @Column(name = "air_humidity")
    private long airHumidity;

    @Column(name = "weather_conditions")
    @NotEmpty(message = "Weather conditions must be not empty")
    @Size(min = 4, max = 50, message = "Size of weather conditions must be between 4 and 50 symbols")
    private String weatherConditions;

    @Column(name = "weather_location")
    @NotEmpty(message = "Weather location must be not empty")
    @Size(min = 4, max = 50, message = "Size of weather location must be between 4 and 50 symbols")
    private String weatherLocation;

    @Column(name = "weather_date")
    private Date weatherDate;
}
