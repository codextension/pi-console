package io.codextension.pi.controller;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by elie on 14.03.17.
 */
@RestController
@RequestMapping("/dust")
@CrossOrigin
public class DustSensorController {

    @RequestMapping("/current")
    public double getCurrent() throws InterruptedException, IOException {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinAnalogInput input = gpio.provisionAnalogInputPin(RaspiPin.GPIO_16);
        GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);
        SpiDevice spi = SpiFactory.getInstance(SpiChannel.CS0);

        output.low();
        Gpio.delay(280);
        double inValue = input.getValue();
        Gpio.delay(40);
        output.high();
        Gpio.delay(9680);
        double density = (inValue * (3.3 / 1024)) * 0.17 - 0.1;
        return density;

    }
}
