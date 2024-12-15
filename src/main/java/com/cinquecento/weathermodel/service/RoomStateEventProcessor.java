package com.cinquecento.weathermodel.service;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import com.cinquecento.weathermodel.model.Room;
import com.cinquecento.weathermodel.model.signal.DeviceControlSignal;
import com.cinquecento.weathermodel.model.state.RoomState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomStateEventProcessor {

    private final DeviceControlSignal deviceControlSignal = new DeviceControlSignal();
    private final Room room;
    private final RoomSettingProperties roomSettingProperties;

    public void processRoomEvent(RoomState state) {

        if (state.getTemperature() > roomSettingProperties.getTemperatureThreshold()) {
            deviceControlSignal.setAirConditionerOn(true);
        } else {
            deviceControlSignal.setAirConditionerOn(false);
        }

        if (state.getHumidity() < roomSettingProperties.getHumidityThreshold()) {
            deviceControlSignal.setHumidifierOn(true);
        } else {
            deviceControlSignal.setHumidifierOn(false);
        }

        room.setTemperature(state.getTemperature());
        room.setHumidity(state.getHumidity());
        room.setAirConditionerOn(deviceControlSignal.isAirConditionerOn());
        room.setHumidifierOn(deviceControlSignal.isHumidifierOn());

        log.info("Изменение состояния: Температура={}°C, Влажность={}%", state.getTemperature(), state.getHumidity());
        log.info("Состояние устройств: Кондиционер={}, Увлажнитель={}", deviceControlSignal.isAirConditionerOn() ? "Включен" : "Выключен", deviceControlSignal.isHumidifierOn() ? "Включен" : "Выключен");
    }

}
