package io.codextension.pi.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dust")
public class Dust {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "value_measured")
    private double valueMeasured;

    @Column(name = "voltage")
    private double voltage;

    @Column(name = "density")
    private double density;

    @Version
    @Column(name = "measured_date")
    private Date measuredDate;

    public Dust() {
    }

    public Dust(double valueMeasured, double voltage, double density) {
        this.valueMeasured = valueMeasured;
        this.voltage = voltage;
        this.density = density;
    }

    public long getId() {
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
}
