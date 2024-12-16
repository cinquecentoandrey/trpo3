package com.cinquecento.weathermodel.service;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import com.cinquecento.weathermodel.model.Room;
import com.cinquecento.weathermodel.model.state.RoomState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RoomEventGenerator {

    private final Room room;
    private final RoomSettingProperties roomSettingProperties;

    public Flux<RoomState> generateRoomEvents() {
        return Flux.interval(Duration.ofSeconds(roomSettingProperties.getEventInterval()))
                .map(i -> {
                    double newTemperature = calculateNextValue(room.getTemperature());
                    double newHumidity = calculateNextValue(room.getHumidity());

                    return new RoomState(newTemperature, newHumidity);
                });
    }

    private double calculateNextValue(double currentValue) {
        return currentValue + (Math.random() * 2 * (double) 2 - (double) 2);
    }

}
