package io.codextension.pi.controller;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
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

        AdcGpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE, false);

        provider.export(MCP3008Pin.CH0, PinMode.ANALOG_INPUT);
        Gpio.pinMode(MCP3008Pin.CH0.getAddress(), PinMode.ANALOG_INPUT.getValue());
        GpioUtil.export(12, GpioUtil.DIRECTION_OUT);
        Gpio.pinMode(12, Gpio.OUTPUT);

        Gpio.digitalWrite(12, Gpio.HIGH);
        Gpio.delay(280);
        double inValue = provider.getValue(MCP3008Pin.CH0);
        Gpio.delay(1000);
        Gpio.digitalWrite(12, Gpio.LOW);

        GpioUtil.unexport(12);
        provider.unexport(MCP3008Pin.CH0);

        return (inValue * (5 / 1024)) * 0.17 - 0.1;

    }
}
