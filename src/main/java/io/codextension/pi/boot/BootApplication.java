package io.codextension.pi.boot;

import io.codextension.pi.component.DHT11;
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
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {

                DHT11 dht = new DHT11();

                for (int i = 0; i < 10; i++) {
                    Thread.sleep(2000);
                    dht.getTemperature();
                }

                System.out.println("Done!!");

            }
        };
    }
}
