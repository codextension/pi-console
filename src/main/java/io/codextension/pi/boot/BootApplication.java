package io.codextension.pi.boot;

import com.pi4j.io.gpio.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by eelkhour on 24.02.2017.
 */
@SpringBootApplication
@ComponentScan(basePackages = "io.codextension.pi")
public class BootApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            GpioController gpio = GpioFactory.getInstance();
            GpioPinDigitalOutput myLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17,   // PIN NUMBER
                                                                       "My LED",           // PIN FRIENDLY NAME (optional)
                                                                       PinState.LOW);      // PIN STARTUP STATE (optional)
            myLed.high();
        };
    }
}
