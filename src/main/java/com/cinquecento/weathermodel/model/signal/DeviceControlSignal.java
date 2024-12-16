package com.cinquecento.weathermodel.model.signal;

import com.cinquecento.weathermodel.model.PowerLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceControlSignal {

    private PowerLevel airConditionerPower = PowerLevel.OFF;
    private PowerLevel humidifierPower = PowerLevel.OFF;

}
