package by.senla.weather_analyzer.repository;

import by.senla.weather_analyzer.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Integer> {
    WeatherData findFirstByOrderByIdDesc();
}
