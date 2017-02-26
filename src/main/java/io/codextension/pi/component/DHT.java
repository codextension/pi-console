package io.codextension.pi.component;

import java.io.Serializable;

/**
 * Created by elie on 26.02.2017.
 */
public class DHT implements Serializable {

    private float temperature;
    private float humidity;

    public DHT(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
