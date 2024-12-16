package com.cinquecento.weathermodel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Room {

    private double temperature;
    private double humidity;
    private PowerLevel airConditionerPower;
    private PowerLevel humidifierPower;

}
