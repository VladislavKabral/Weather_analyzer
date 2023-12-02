package by.senla.weather_analyzer.service;

import by.senla.weather_analyzer.model.WeatherReaderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Timer;

@Service
public class WeatherReaderService {

    @Value("${timer_delay}")
    private int timerDelay;

    @Value("${timer_period}")
    private long timerPeriod;

    private final WeatherReaderTask weatherReaderTask;

    @Autowired
    public WeatherReaderService(WeatherReaderTask weatherReaderTask) {
        this.weatherReaderTask = weatherReaderTask;
    }


    public void run() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(weatherReaderTask, timerDelay, timerPeriod);
    }
}
