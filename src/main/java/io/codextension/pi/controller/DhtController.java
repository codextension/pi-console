package io.codextension.pi.controller;

import io.codextension.pi.component.DHT;
import io.codextension.pi.component.DhtReader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by elie on 26.02.2017.
 */
@RestController
@RequestMapping("/dht")
public class DhtController {
    private static long lastCallTimestamp = 0;

    @RequestMapping("/")
    public DHT getTemperatureAndHumidity() {
        if (lastCallTimestamp == 0 || (new Date().getTime() - lastCallTimestamp > 2000)) {
            lastCallTimestamp = new Date().getTime();
            DhtReader reader = new DhtReader();
            return reader.getValue();
        }
        return new DHT(99f, 99f);
    }

}
