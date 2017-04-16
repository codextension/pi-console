package io.codextension.pi.component;

import io.codextension.pi.model.Dht;
import io.codextension.pi.model.Dust;
import io.codextension.pi.repository.DhtRepository;
import io.codextension.pi.repository.DustSensorRepository;
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
public class DustSensorPoller {
    private static final Logger LOG = LoggerFactory.getLogger(DustSensorPoller.class);
    private DustSensorReader dustSensorReader;
    private double latestValueMeasured;

    @Autowired
    private DustSensorRepository dustSensorRepository;

    @PostConstruct
    public void init() {
        dustSensorReader = new DustSensorReader();
    }

    @Scheduled(fixedRate = 1000)
    public void pollDustDensity() {
        Dust value = null;
        try {
            value = dustSensorReader.getValue();
            if (value != null && value.getDensity() > 0 && Math.abs(latestValueMeasured - value.getValueMeasured()) > 99) {
                latestValueMeasured = value.getValueMeasured();
                dustSensorRepository.save(value);
                LOG.debug("Saving new dust density data: " + value.getDensity() + " µg/m3");
            }
        } catch (IOException e) {
            // ingore for now
        }
    }
}
