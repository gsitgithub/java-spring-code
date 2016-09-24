package dev.gsitgithub.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by gsitgithub on 17/5/2015.
 */
@Controller
public class BaseController {
    @RequestMapping(value = {"/",""})
    public String landingPage() {
        return "index";
    }
}
