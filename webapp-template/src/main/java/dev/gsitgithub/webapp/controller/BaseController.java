package dev.gsitgithub.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by gsitgithub on 17/5/2015.
 */
@Controller
@Slf4j
public class BaseController {
    @RequestMapping(value = {"/",""})
    public String landingPage(HttpServletRequest request) {
        return "index";
    }
    
    @RequestMapping(value = {"/asdf","/asdf/"})
    public String landingPage1(HttpServletRequest request) {
        return "index";
    }
}
