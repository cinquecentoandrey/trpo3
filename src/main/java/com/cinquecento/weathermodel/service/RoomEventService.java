package com.cinquecento.weathermodel.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomEventService {

    private final RoomStateEventProcessor roomStateEventProcessor;
    private final RoomEventGenerator roomEventGenerator;

    @PostConstruct
    public void startProcessing() {
        roomEventGenerator.generateRoomEvents()
                .subscribe(roomStateEventProcessor::processRoomEvent);
    }

}

