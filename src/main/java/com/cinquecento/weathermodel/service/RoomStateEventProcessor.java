package com.cinquecento.weathermodel.service;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import com.cinquecento.weathermodel.model.PowerLevel;
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
        double previousTemperature = room.getTemperature();
        double previousHumidity = room.getHumidity();
        PowerLevel previousAirConditionerPower = room.getAirConditionerPower();
        PowerLevel previousHumidifierPower = room.getHumidifierPower();

        PowerLevel airConditionerLevel = determineAirConditionerPower(
                state.getTemperature(),
                previousTemperature,
                previousAirConditionerPower,
                roomSettingProperties.getTemperatureThreshold()
        );

        PowerLevel humidifierLevel = determineHumidifierPower(
                state.getHumidity(),
                previousHumidity,
                previousHumidifierPower,
                roomSettingProperties.getHumidityThreshold()
        );

        deviceControlSignal.setAirConditionerPower(airConditionerLevel);
        deviceControlSignal.setHumidifierPower(humidifierLevel);

        room.setTemperature(state.getTemperature());
        room.setHumidity(state.getHumidity());
        room.setAirConditionerPower(airConditionerLevel);
        room.setHumidifierPower(humidifierLevel);

        log.info("Изменение состояния: Температура={}°C, Влажность={}%", state.getTemperature(), state.getHumidity());
        log.info("Состояние устройств: Кондиционер={} (мощность: {}), Увлажнитель={} (мощность: {})",
                airConditionerLevel != PowerLevel.OFF ? "Включен" : "Выключен", airConditionerLevel,
                humidifierLevel != PowerLevel.OFF ? "Включен" : "Выключен", humidifierLevel);
    }

    private PowerLevel determineAirConditionerPower(double currentTemperature,
                                                    double previousTemperature,
                                                    PowerLevel previousPower,
                                                    double threshold) {
        if (currentTemperature <= threshold) {
            return PowerLevel.OFF;
        }

        if (currentTemperature > previousTemperature) {
            return PowerLevel.increasePower(previousPower);
        } else if (currentTemperature < previousTemperature) {
            return PowerLevel.decreasePower(previousPower);
        }

        return previousPower;
    }

    private PowerLevel determineHumidifierPower(double currentHumidity,
                                                double previousHumidity,
                                                PowerLevel previousPower,
                                                double threshold) {
        if (currentHumidity >= threshold) {
            return PowerLevel.OFF;
        }

        if (currentHumidity < previousHumidity) {
            return PowerLevel.increasePower(previousPower);
        } else if (currentHumidity > previousHumidity) {
            return PowerLevel.decreasePower(previousPower);
        }

        return previousPower;
    }

}
