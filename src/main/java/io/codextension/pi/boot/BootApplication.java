package io.codextension.pi.boot;

import com.pi4j.wiringpi.Gpio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

/**
 * Created by eelkhour on 24.02.2017.
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = {"io.codextension.pi.model", "io.codextension.pi.repository"})
@EnableScheduling
@ComponentScan(basePackages = "io.codextension.pi")
public class BootApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BootApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootApplication.class, args);
    }

    @PostConstruct
    public void init() {
        LOG.info("Enabling GPIO ...");
        Gpio.wiringPiSetupGpio();
    }
}
