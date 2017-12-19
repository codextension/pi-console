package io.codextension.pi.controller;

import io.codextension.pi.component.AnalogSensorReader;
import io.codextension.pi.model.Dust;
import io.codextension.pi.repository.DustSensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
@RequestMapping("/dust")
@CrossOrigin
public class DustSensorController {
    private static final Logger LOG = LoggerFactory.getLogger(DustSensorController.class);

    @Autowired
    private DustSensorRepository dustSensorRepository;

    @Autowired
    private AnalogSensorReader analogSensorReader;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-hh:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, true));
    }

    @RequestMapping("/current")
    public Dust getCurrent() throws InterruptedException, IOException {
        return analogSensorReader.getPreviousDustValue();
    }

    @RequestMapping("/range") // ?from=15.09.2012-10:12&to=15.09.2017-10:12
    public List<Dust> getRange(@RequestParam(name = "from", required = true) Date fromDate, @RequestParam(name = "to", required = true) Date toDate) {
        return dustSensorRepository.findByMeasuredDateBetweenOrderByMeasuredDateDesc(fromDate, toDate);
    }

}
