package io.codextension.pi.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by elie on 09.03.17.
 */
@Component
@Scope("singleton")
public class NoiseSensorPoller {
    private static final Logger LOG = LoggerFactory.getLogger(NoiseSensorPoller.class);

    @Autowired
    private AnalogSensorReader analogSensorReader;

    //@Scheduled(fixedRate = 1)
    @PostConstruct
    public void pollNoise() {
        try {
            Double value = analogSensorReader.getNoiseValue();
                Double voltage = (value * 5) / 1024.0;
            pollNoise();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
