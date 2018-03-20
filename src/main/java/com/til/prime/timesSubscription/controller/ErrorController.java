package com.til.prime.timesSubscription.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
    @RequestMapping(value = { "/blocked" }, method = RequestMethod.GET)
    public ModelAndView blockedPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("blocked");
        return model;
    }

    @RequestMapping(value = { "/error" }, method = RequestMethod.GET)
    public ModelAndView errorPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("error");
        return model;
    }
}
