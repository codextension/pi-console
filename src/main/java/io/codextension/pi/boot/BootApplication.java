package io.codextension.pi.boot;

import io.codextension.pi.component.DHT;
import io.codextension.pi.component.DhtReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.management.timer.Timer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by eelkhour on 24.02.2017.
 */
@SpringBootApplication
@ComponentScan(basePackages = "io.codextension.pi")
public class BootApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootApplication.class, args);
    }

    private void writeToFile(String text) {
        Calendar calendar = Calendar.getInstance();
        try (FileWriter fw = new FileWriter(calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) +".csv", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(text);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                DhtReader reader = new DhtReader();
                writeToFile("date;temperate;humidity");
                while (true) {
                    DHT value = reader.getValue();
                    if (value != null) {
                        writeToFile(value.toString());
                        System.out.println(value.getMeasuredDate() + " --> " + value.getTemperature() + " *C, " + value.getHumidity() + "%");
                    }
                    Thread.sleep(Timer.ONE_MINUTE * 20);
                }
            }
        };
    }
}
