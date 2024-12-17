package com.cinquecento.weathermodel.model;

import lombok.Getter;

@Getter
public enum PowerLevel {
    OFF(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private final int level;

    PowerLevel(int level) {
        this.level = level;
    }

}
