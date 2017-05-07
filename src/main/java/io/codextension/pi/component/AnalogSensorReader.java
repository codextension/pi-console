package io.codextension.pi.component;

import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.event.PinEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import io.codextension.pi.model.Dust;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by elie on PIN_NB.04.17.
 */
@Component
@Scope("singleton")
public class AnalogSensorReader {
    private static final Logger LOG = LoggerFactory.getLogger(AnalogSensorReader.class);

    private static final int PIN_NB = 16;
    private MCP3008GpioProvider provider;
    private Dust previousValue;

    @PostConstruct
    public void init() {
        try {
            provider = new MCP3008GpioProvider(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE, false);
            provider.export(MCP3008Pin.CH0, PinMode.ANALOG_INPUT);
            Gpio.pinMode(PIN_NB, Gpio.OUTPUT);
            Gpio.digitalWrite(PIN_NB, Gpio.HIGH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        GpioUtil.unexport(PIN_NB);
        provider.unexport(MCP3008Pin.CH0);
    }

    /**
     * For info
     * 3000 +     = VERY POOR
     * 1050-3000  = POOR
     * 300-1050   = FAIR
     * 150-300    = GOOD
     * 75-150     = VERY GOOD
     * 0-75       = EXCELLENT
     *
     * @return
     * @throws IOException
     */
    public synchronized Dust getDustValue() throws IOException {


        Gpio.digitalWrite(PIN_NB, Gpio.LOW);
        Gpio.delayMicroseconds(280);
        double inValue = provider.getImmediateValue(MCP3008Pin.CH0);
        double voltage = ((inValue * 5) / 1024.0);
        double dustDensity = (voltage * 0.17 - 0.1) * 1000;
        Gpio.delayMicroseconds(40);
        Gpio.digitalWrite(PIN_NB, Gpio.HIGH);


        previousValue = new Dust(inValue, voltage, dustDensity);
        return previousValue;
    }

    public synchronized double getNoiseValue() throws IOException {
        double inValue = provider.getValue(MCP3008Pin.CH7);
        Gpio.delayMicroseconds(280);
        return inValue;
    }

    public Dust getPreviousDustValue() {
        return previousValue;
    }
}
