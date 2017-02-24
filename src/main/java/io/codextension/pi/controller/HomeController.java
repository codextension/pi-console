package io.codextension.pi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by eelkhour on 24.02.2017.
 */
@RestController
public class HomeController {

    @RequestMapping({"/"})
    public String home(){
        return "Home page is empty";
    }
}
