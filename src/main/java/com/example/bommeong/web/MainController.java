package com.example.bommeong.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/healthcheck")
    @ResponseBody
    public String healthcheck() {
        return "healthcheck";
    }
}
