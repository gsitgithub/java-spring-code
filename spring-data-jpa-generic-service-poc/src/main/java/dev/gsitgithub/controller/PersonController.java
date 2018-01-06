package dev.gsitgithub.controller;


import java.util.List;

import org.boon.json.ObjectMapper;
import org.boon.json.implementation.ObjectMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.gsitgithub.generic.repo.specification.Filter;
import dev.gsitgithub.service.PersonService;

@RestController
public class PersonController {
    @Autowired
    PersonService service;
    ObjectMapper objectMapper = new ObjectMapperImpl();

    @RequestMapping(value = "/persons", method = RequestMethod.POST)
    @ResponseBody
    public String filter(@RequestBody Filter filter){
        return objectMapper.toJson(service.search(filter));
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    @ResponseBody
    public String join(@RequestParam(value = "filter",required = false) String queryString){
        return objectMapper.toJson(service.search(queryString));
    }


}
