package io.codextension.pi.component;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by elie on 26.02.2017.
 */
public class DHT implements Serializable {

    private float temperature;
    private float humidity;
    private String measuredDate;

    public DHT(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;

        Calendar instance = Calendar.getInstance();
        this.measuredDate = instance.get(Calendar.HOUR_OF_DAY) + ":" + instance.get(Calendar.MINUTE);
    }

    public String getMeasuredDate() {
        return measuredDate;
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

    public String toString(){
        return measuredDate + ";" + temperature + ";" + humidity;
    }
}
