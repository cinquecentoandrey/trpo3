package com.cinquecento.weathermodel.model.signal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceControlSignal {

    private boolean airConditionerOn;
    private boolean humidifierOn;

}
