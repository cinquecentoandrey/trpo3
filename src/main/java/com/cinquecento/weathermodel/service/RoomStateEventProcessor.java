package com.cinquecento.weathermodel.service;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import com.cinquecento.weathermodel.model.PowerLevel;
import com.cinquecento.weathermodel.model.Room;
import com.cinquecento.weathermodel.model.signal.DeviceControlSignal;
import com.cinquecento.weathermodel.model.state.RoomState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsible for processing room state events.
 * Adjusts the power levels of the air conditioner and humidifier based on the new temperature and humidity.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoomStateEventProcessor {

    /**
     * The control signal object that represents the current power levels of the air conditioner and humidifier.
     */
    private final DeviceControlSignal deviceControlSignal = new DeviceControlSignal();

    /**
     * The room object that holds the current temperature, humidity, and device power levels.
     */
    private final Room room;

    /**
     * Configuration properties for device thresholds, such as temperature and humidity limits.
     */
    private final RoomSettingProperties roomSettingProperties;

    /**
     * Processes a new room state event and updates the room's devices accordingly.
     * Adjusts the power levels of the air conditioner and humidifier based on the current state.
     *
     * @param state the new room state, including temperature and humidity.
     */
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

    /**
     * Determines the power level for the air conditioner based on the current and previous temperatures.
     *
     * @param currentTemperature the current temperature.
     * @param previousTemperature the previous temperature.
     * @param previousPower the previous power level of the air conditioner.
     * @param threshold the temperature threshold for turning on the air conditioner.
     * @return the new power level for the air conditioner.
     */
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

    /**
     * Determines the power level for the humidifier based on the current and previous humidity.
     *
     * @param currentHumidity the current humidity level.
     * @param previousHumidity the previous humidity level.
     * @param previousPower the previous power level of the humidifier.
     * @param threshold the humidity threshold for turning on the humidifier.
     * @return the new power level for the humidifier.
     */
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
