package io.codextension.pi.component;

import io.codextension.pi.model.Dht;
import io.codextension.pi.repository.DhtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by elie on 09.03.17.
 */
@Component
public class NoiseSensorPoller {
    private static final Logger LOG = LoggerFactory.getLogger(NoiseSensorPoller.class);

    @Autowired
    private AnalogSensorReader analogSensorReader;

    @PostConstruct
    public void init() {
    }

    @Scheduled(fixedRate = 100)
    public void pollTemperatureAndHumidity() {
        try {
            Double value = analogSensorReader.getNoiseValue();
            if (value > 0) {
                LOG.debug("Noise value is " + value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
