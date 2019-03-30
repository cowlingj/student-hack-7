package com.studenthackvii.dave;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RateController {

    @Value("classpath:/peter.json")
    private Resource comicList;

  @GetMapping("/rate")
  public String getCartoons(Model model) {
    Logger
      .getAnonymousLogger()
      .info(String.format("resource exists: %s", comicList.exists()));
    model.addAttribute("ratings", new Rating());
    return "/test.html";
  }

    @PostMapping("/rate")
    public String postRatings(@ModelAttribute("ratings") Rating ratings, BindingResult errors) {

        return "/index.html";
    }

}