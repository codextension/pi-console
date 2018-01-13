package io.codextension.pi.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

@Entity
@Table(name = "dht")
public class Dht extends AbstractPersistable<Long> {

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "humidity")
    private float humidity;

    @Version
    @Column(name = "measured_date")
    private Date measuredDate;

    public Dht(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public Dht() {
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
