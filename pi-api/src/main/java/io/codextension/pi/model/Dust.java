package io.codextension.pi.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

@Entity
@Table(name = "dust")
public class Dust extends AbstractPersistable<Long> {

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

    @Override
    public String toString() {
        return "Dust{" +
                "valueMeasured=" + valueMeasured +
                ", voltage=" + voltage +
                ", density=" + density +
                ", measuredDate=" + measuredDate +
                '}';
    }
}
