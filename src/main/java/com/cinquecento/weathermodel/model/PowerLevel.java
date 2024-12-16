package com.cinquecento.weathermodel.model;

import lombok.Getter;

@Getter
public enum PowerLevel {
    OFF(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    MAX(4);

    private final int level;

    PowerLevel(int level) {
        this.level = level;
    }


    public static PowerLevel increasePower(PowerLevel currentPower) {
        return switch (currentPower) {
            case OFF -> PowerLevel.LOW;
            case LOW -> PowerLevel.MEDIUM;
            case MEDIUM -> PowerLevel.HIGH;
            case HIGH -> PowerLevel.MAX;
            default -> PowerLevel.MAX;
        };
    }

    public static PowerLevel decreasePower(PowerLevel currentPower) {
        return switch (currentPower) {
            case MAX -> PowerLevel.HIGH;
            case HIGH -> PowerLevel.MEDIUM;
            case MEDIUM -> PowerLevel.LOW;
            case LOW -> PowerLevel.OFF;
            default -> PowerLevel.OFF;
        };
    }

}
