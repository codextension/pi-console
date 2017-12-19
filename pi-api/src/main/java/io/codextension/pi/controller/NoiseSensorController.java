package io.codextension.pi.controller;

import io.codextension.pi.component.AnalogSensorReader;
import io.codextension.pi.model.Dust;
import io.codextension.pi.repository.DustSensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by elie on 14.03.17.
 */
@RestController
@RequestMapping("/noise")
@CrossOrigin
public class NoiseSensorController {
    private static final Logger LOG = LoggerFactory.getLogger(NoiseSensorController.class);
    private Double polledValue = 0.0;

    @Autowired
    private AnalogSensorReader analogSensorReader;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-hh:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, true));
    }

    @RequestMapping("/current")
    public double getCurrent() throws IOException {
        return polledValue;
    }

    @Scheduled(fixedRate = 1)
    public void pollNoise() {
        try {
            polledValue = analogSensorReader.getNoiseValue();
            Double voltage = (polledValue * 5) / 1024.0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
