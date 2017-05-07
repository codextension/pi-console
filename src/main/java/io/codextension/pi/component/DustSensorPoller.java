package io.codextension.pi.component;

import io.codextension.pi.model.Dust;
import io.codextension.pi.repository.DustSensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;

/**
 * Created by elie on 09.03.17.
 */
@Component
public class DustSensorPoller {
    private static final Logger LOG = LoggerFactory.getLogger(DustSensorPoller.class);
    @Autowired
    private AnalogSensorReader analogSensorReader;
    private Dust latestValueMeasured;

    @Autowired
    private DustSensorRepository dustSensorRepository;

    @PostConstruct
    public void init() {
        latestValueMeasured = new Dust();
    }

    //@Scheduled(fixedRate = 1000)
    public void pollDustDensity() {
        Dust value = null;
        try {
            value = analogSensorReader.getDustValue();

            if (value != null && value.getDensity() > 0 &&
                    (Math.abs(latestValueMeasured.getValueMeasured() - value.getValueMeasured()) > 99
                            || (new Date().getTime() - latestValueMeasured.getMeasuredDate().getTime()) > 60000)) {
                latestValueMeasured = dustSensorRepository.save(value);
                LOG.debug("Saving new dust density data: " + value.getDensity() + " µg/m3");
            }
        } catch (IOException e) {
            // ingore for now
        }
    }
}
