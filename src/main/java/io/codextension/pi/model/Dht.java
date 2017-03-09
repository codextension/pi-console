package io.codextension.pi.model;

import org.springframework.data.annotation.Id;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by elie on 09.03.17.
 */
public class Dht {

    @Id
    private String id;
    private float temperature;
    private float humidity;
    private Date measuredDate;

    public Dht(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;

        Calendar instance = Calendar.getInstance(Locale.GERMANY);
        this.measuredDate = instance.getTime();
    }

    public Dht(){
        Calendar instance = Calendar.getInstance(Locale.GERMANY);
        this.measuredDate = instance.getTime();
    }

    public String getId() {
        return id;
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

    public Date getMeasuredDate() {
        return measuredDate;
    }
}
