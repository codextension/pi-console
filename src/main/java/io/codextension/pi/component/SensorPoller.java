package io.codextension.pi.component;

import io.codextension.pi.controller.DustSensorController;
import io.codextension.pi.model.Dht;
import io.codextension.pi.model.Dust;
import io.codextension.pi.repository.DhtRepository;
import io.codextension.pi.repository.DustSensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by elie on 09.03.17.
 */
@Component
public class SensorPoller {
    private DhtReader dhtReader;
    private DustSensorReader dustSensorReader;

    @Autowired
    private DhtRepository dhtRepository;

    @Autowired
    private DustSensorRepository dustSensorRepository;

    @PostConstruct
    public void init(){
        dhtReader = new DhtReader();
        dustSensorReader = new DustSensorReader();
    }

    @Scheduled(fixedRate = 900000)
    public void pollTemperatureAndHumidity() {
        Dht value = dhtReader.getValue();
            if (value != null) {
                dhtRepository.save(value);
            }
    }

    @Scheduled(fixedRate = 15000)
    public void pollDustDensity() {
        Dust value = null;
        try {
            value = dustSensorReader.getValue();
            if (value != null && value.getDensity() > 0) {
                dustSensorRepository.save(value);
            }
        } catch (IOException e) {
            // ingore for now
        }
    }
}
