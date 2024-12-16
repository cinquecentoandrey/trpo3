package com.cinquecento.weathermodel.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service responsible for coordinating the processing of room events.
 * Subscribes to the event stream and delegates each event for processing.
 */
@Service
@RequiredArgsConstructor
public class RoomEventService {

    /**
     * The processor that handles each room event to adjust device power levels.
     */
    private final RoomStateEventProcessor roomStateEventProcessor;

    /**
     * The generator that creates a stream of room events.
     */
    private final RoomEventGenerator roomEventGenerator;

    /**
     * Initializes the event processing workflow after the service is constructed.
     * Subscribes to the stream of room events and processes them sequentially.
     */
    @PostConstruct
    public void startProcessing() {
        roomEventGenerator.generateRoomEvents()
                .subscribe(roomStateEventProcessor::processRoomEvent);
    }

}

