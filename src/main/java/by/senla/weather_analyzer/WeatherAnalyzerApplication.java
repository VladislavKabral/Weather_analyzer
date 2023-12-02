package by.senla.weather_analyzer;

import by.senla.weather_analyzer.service.WeatherReaderService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherAnalyzerApplication {

	public static void main(String[] args){
		ApplicationContext context = SpringApplication.run(WeatherAnalyzerApplication.class, args);

		WeatherReaderService weatherReaderService = context.getBean(WeatherReaderService.class);
		weatherReaderService.run();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
