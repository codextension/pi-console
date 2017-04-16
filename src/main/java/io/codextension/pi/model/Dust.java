package io.codextension.pi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by elie on 16.04.17.
 */
@Document(collection = "dust")
public class Dust {
    @Id
    private String id;
    private double valueMeasured;
    private double voltage;
    private double density;
    private Date measuredDate;

    public Dust() {
        Calendar instance = Calendar.getInstance(Locale.GERMANY);
        this.measuredDate = instance.getTime();
    }

    public Dust(double valueMeasured, double voltage, double density) {
        this.valueMeasured = valueMeasured;
        this.voltage = voltage;
        this.density = density;
        Calendar instance = Calendar.getInstance(Locale.GERMANY);
        this.measuredDate = instance.getTime();
    }

    public String getId() {
        return id;
    }

    public double getValueMeasured() {
        return valueMeasured;
    }

    public void setValueMeasured(double valueMeasured) {
        this.valueMeasured = valueMeasured;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public Date getMeasuredDate() {
        return measuredDate;
    }

    public void setMeasuredDate(Date measuredDate) {
        this.measuredDate = measuredDate;
    }
}
