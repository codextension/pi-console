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
public class DhtSensorPoller {
    private static final Logger LOG = LoggerFactory.getLogger(DhtSensorPoller.class);
    private DhtReader dhtReader;

    @Autowired
    private DhtRepository dhtRepository;

    @Autowired
    private DustSensorRepository dustSensorRepository;

    @PostConstruct
    public void init(){
        dhtReader = new DhtReader();
    }

    @Scheduled(fixedRate = 600000)
    public void pollTemperatureAndHumidity() {
        LOG.debug("Attempting to read temp and humidity...");
        Dht value = dhtReader.getValue();
            if (value != null) {
                dhtRepository.save(value);
                LOG.debug("Saving new temp/humidity data: " + value.getHumidity() + "% , " + value.getTemperature() + " °C");
            } else {
                try {
                    Thread.sleep(2000);
                    pollTemperatureAndHumidity();
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage());
                }
            }
    }
}
