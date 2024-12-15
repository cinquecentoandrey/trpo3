package com.cinquecento.weathermodel.controller;

import com.cinquecento.weathermodel.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final Room room;

    @GetMapping("/state")
    public ResponseEntity<String> currentState() {
        return ResponseEntity.ok(room.toString());
    }

}
