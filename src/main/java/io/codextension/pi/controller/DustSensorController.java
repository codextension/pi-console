package io.codextension.pi.controller;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
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
        final AdcGpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED,
                SpiDevice.DEFAULT_SPI_MODE,
                false);
        GpioPinAnalogInput input = gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH0);
        GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);

        led.high();
        Gpio.delay(280);
        double inValue = input.getValue();
        Gpio.delay(40);
        led.low();
        Gpio.delay(9680);
        gpio.unprovisionPin(led);
        gpio.unprovisionPin(input);
        gpio.shutdown();
        return (inValue * (5 / 1024)) * 0.17 - 0.1;

    }
}
