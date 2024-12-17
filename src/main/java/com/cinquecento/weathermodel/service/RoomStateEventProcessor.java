package com.cinquecento.weathermodel.service;

import com.cinquecento.weathermodel.config.properties.RoomSettingProperties;
import com.cinquecento.weathermodel.model.PowerLevel;
import com.cinquecento.weathermodel.model.Room;
import com.cinquecento.weathermodel.model.signal.DeviceControlSignal;
import com.cinquecento.weathermodel.model.state.RoomState;
import com.cinquecento.weathermodel.util.MembershipUtils;
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
     * Processes the current room state, updating temperature, humidity,
     * and device power levels based on fuzzy logic thresholds.
     *
     * @param state the current state of the room, including temperature and humidity
     */
    public void processRoomEvent(RoomState state) {
        double thresholdTemperature = roomSettingProperties.getTemperatureThreshold();
        double thresholdHumidity = roomSettingProperties.getHumidityThreshold();

        PowerLevel airConditionerLevel = determineAirConditionerPower(state.getTemperature(), thresholdTemperature);
        PowerLevel humidifierLevel = determineHumidifierPower(state.getHumidity(), thresholdHumidity);

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
     * Determines the power level for the air conditioner based on fuzzy membership functions
     * for low, medium, and high temperature ranges.
     *
     * @param currentTemperature the current room temperature
     * @param threshold the threshold temperature for device activation
     * @return the calculated {@link PowerLevel} for the air conditioner
     */
    private PowerLevel determineAirConditionerPower(double currentTemperature, double threshold) {
        double low = MembershipUtils.calculateLowMembership(currentTemperature, 0, threshold - 5);
        double medium = MembershipUtils.calculateMediumMembership(currentTemperature, threshold - 5, threshold + 5);
        double high = MembershipUtils.calculateHighMembership(currentTemperature, threshold, threshold + 10);

        if (high > medium && high > low) {
            return PowerLevel.HIGH;
        } else if (medium > low) {
            return PowerLevel.MEDIUM;
        } else if (low > 0) {
            return PowerLevel.LOW;
        } else {
            return PowerLevel.OFF;
        }
    }

    /**
     * Determines the power level for the humidifier based on fuzzy membership functions
     * for low, medium, and high humidity ranges.
     *
     * @param currentHumidity the current room humidity
     * @param threshold the threshold humidity for device activation
     * @return the calculated {@link PowerLevel} for the humidifier
     */
    private PowerLevel determineHumidifierPower(double currentHumidity, double threshold) {
        double low = MembershipUtils.calculateLowMembership(currentHumidity, 0, threshold - 10);
        double medium = MembershipUtils.calculateMediumMembership(currentHumidity, threshold - 10, threshold + 10);
        double high = MembershipUtils.calculateHighMembership(currentHumidity, threshold, threshold + 20);

        if (low > medium && low > high) {
            return PowerLevel.HIGH;
        } else if (medium > high) {
            return PowerLevel.MEDIUM;
        } else if (high > 0) {
            return PowerLevel.LOW;
        } else {
            return PowerLevel.OFF;
        }
    }

}
