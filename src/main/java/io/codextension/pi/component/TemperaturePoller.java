package io.codextension.pi.component;

import io.codextension.pi.model.Dht;
import io.codextension.pi.repository.DhtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by elie on 09.03.17.
 */
@Component
public class TemperaturePoller {
    private DhtReader reader;

    @Autowired
    private DhtRepository dhtRepository;

    @PostConstruct
    public void init(){
        reader = new DhtReader();
    }

    @Scheduled(fixedRate = 10000)
    public void pollTemperatureAndHumidity() {
            Dht value = reader.getValue();
            if (value != null) {
                dhtRepository.save(value);
                System.out.println(value.getMeasuredDate() + " --> " + value.getTemperature() + " *C, " + value.getHumidity() + "%");
            }

    }
}
