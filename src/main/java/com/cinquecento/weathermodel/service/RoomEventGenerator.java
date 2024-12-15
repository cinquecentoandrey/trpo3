package com.cinquecento.weathermodel.service;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import com.cinquecento.weathermodel.model.state.RoomState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RoomEventGenerator {

    private final RoomSettingProperties roomSettingProperties;

    public Flux<RoomState> generateRoomEvents() {
        return Flux.interval(Duration.ofSeconds(roomSettingProperties.getEventInterval()))
                .map(i -> new RoomState(
                        15 + Math.random() * 15,
                        30 + Math.random() * 50
                ));
    }

}
