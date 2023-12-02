package by.senla.weather_analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestForAverageTemperatureCalculationDTO {

    private String startDate;

    private String endDate;
}
