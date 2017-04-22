package io.codextension.pi.boot;

import com.pi4j.wiringpi.Gpio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

/**
 * Created by eelkhour on 24.02.2017.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"io.codextension.pi.model", "io.codextension.pi.repository"})
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "io.codextension.pi")
public class BootApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BootApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootApplication.class, args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    // Of course , you can define the Executor too
    @Bean
    public Executor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @PostConstruct
    public void init() {
        LOG.info("Enabling GPIO ...");
        Gpio.wiringPiSetupGpio();
    }
}
