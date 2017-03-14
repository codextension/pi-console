package io.codextension.pi.controller;

import com.pi4j.io.gpio.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by elie on 14.03.17.
 */
@RestController
@RequestMapping("/dust")
@CrossOrigin
public class DustSensorController {

    @RequestMapping("/current")
    public double getCurrent() throws InterruptedException {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinAnalogInput input = gpio.provisionAnalogInputPin(RaspiPin.GPIO_16);
        GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);

        output.low();
        Thread.sleep(2);
        double inValue = input.getValue();
        Thread.sleep(1);
        output.high();
        Thread.sleep(10);
        double density = (inValue * (3.3 / 1024)) * 0.17 - 0.1;
        return density;

    }
}
