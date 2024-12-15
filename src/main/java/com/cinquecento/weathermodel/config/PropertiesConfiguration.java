package com.cinquecento.weathermodel.config;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {RoomSettingProperties.class})
public class PropertiesConfiguration {
}
