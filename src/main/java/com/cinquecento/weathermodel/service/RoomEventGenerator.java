package com.cinquecento.weathermodel.service;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import com.cinquecento.weathermodel.model.Room;
import com.cinquecento.weathermodel.model.state.RoomState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Service responsible for generating periodic room events.
 * Each event represents a new state of temperature and humidity in the room.
 */
@Service
@RequiredArgsConstructor
public class RoomEventGenerator {

    /**
     * The room object that holds the current temperature and humidity values.
     */
    private final Room room;

    /**
     * Configuration properties for room event generation, including the event interval.
     */
    private final RoomSettingProperties roomSettingProperties;

    /**
     * Generates a stream of room state events at a fixed interval.
     * Each event calculates new temperature and humidity values based on the current state.
     *
     * @return a Flux stream emitting RoomState objects representing the updated temperature and humidity.
     */
    public Flux<RoomState> generateRoomEvents() {
        return Flux.interval(Duration.ofSeconds(roomSettingProperties.getEventInterval()))
                .map(i -> {
                    double newTemperature = calculateNextValue(room.getTemperature());
                    double newHumidity = calculateNextValue(room.getHumidity());

                    return new RoomState(newTemperature, newHumidity);
                });
    }

    /**
     * Calculates the next value for a given parameter (temperature or humidity).
     * Adds a random change in the range of ±2 to the current value.
     *
     * @param currentValue the current value of the parameter.
     * @return the next value with a random adjustment.
     */
    private double calculateNextValue(double currentValue) {
        return currentValue + (Math.random() * 2 * (double) 2 - (double) 2);
    }

}
