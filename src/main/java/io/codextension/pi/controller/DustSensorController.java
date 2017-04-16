package io.codextension.pi.controller;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(DustSensorController.class);

    @RequestMapping("/current")
    public double getCurrent() throws InterruptedException, IOException {

        MCP3008GpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE, false);

        provider.export(MCP3008Pin.CH0, PinMode.ANALOG_INPUT);
        Gpio.pinMode(12, Gpio.OUTPUT);

        Gpio.digitalWrite(12, Gpio.LOW);
        Gpio.delayMicroseconds(280);
        double inValue = provider.getImmediateValue(MCP3008Pin.CH0);
        Gpio.delayMicroseconds(40);
        Gpio.digitalWrite(12, Gpio.HIGH);

        GpioUtil.unexport(12);
        provider.unexport(MCP3008Pin.CH0);
        LOG.debug("Raw value read is " + inValue);
        return (inValue * 3.3) / 1024.0;

    }
}
