package com.studenthackvii.dave;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RateController {

    @Value("classpath:/peter.json")
    private Resource comicList;

  @GetMapping("/rate")
  public String getCartoons() {
    Logger
      .getAnonymousLogger()
      .info(String.format("resource exists: %s", comicList.exists()));
    return "/test.html";
  }

    @PostMapping("/rate")
    public String postRatings( @ModelAttribute("ratings") Rating[] ratings) {


        return "/index.html";
    }

}