package com.cinquecento.weathermodel.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.room-settings")
public class RoomSettingProperties {

    private Long eventInterval;
    private Long humidityThreshold;
    private Long temperatureThreshold;
    private Long initialTemp;
    private Long initialHumidity;

}
