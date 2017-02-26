package io.codextension.pi.controller;

import com.pi4j.io.gpio.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by eelkhour on 24.02.2017.
 */
@RestController
@RequestMapping("/lights")
public class LightController {

    @RequestMapping(value = "/{state}", method = RequestMethod.GET)
    public String switchLights(@PathVariable(required = true, name = "state") String state, Model model) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput myLed_1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "My LED 1");
        GpioPinDigitalOutput myLed_2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "My LED 2");

        if (state.equalsIgnoreCase("off")) {
            myLed_1.low();
            myLed_2.low();
        } else if (state.equalsIgnoreCase("on")) {
            myLed_1.high();
            myLed_2.high();
        }

        gpio.unprovisionPin(myLed_1);
        gpio.unprovisionPin(myLed_2);
        gpio.shutdown();
        return "Setting light state to: " + state;
    }


}
