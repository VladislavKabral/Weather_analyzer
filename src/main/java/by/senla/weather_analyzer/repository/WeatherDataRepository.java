package by.senla.weather_analyzer.repository;

import by.senla.weather_analyzer.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Integer> {
    WeatherData findFirstByOrderByIdDesc();

    List<WeatherData> findByWeatherDateBetween(Date startDate, Date endDate);
}
