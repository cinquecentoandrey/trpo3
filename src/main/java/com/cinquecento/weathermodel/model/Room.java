package com.cinquecento.weathermodel.model;

import lombok.Data;

@Data
public class Room {

    private double temperature;
    private double humidity;
    private boolean airConditionerOn;
    private boolean humidifierOn;

}
