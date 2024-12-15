package com.cinquecento.weathermodel.config;

import com.cinquecento.weathermodel.model.Room;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Room room() {
        return new Room();
    }

}