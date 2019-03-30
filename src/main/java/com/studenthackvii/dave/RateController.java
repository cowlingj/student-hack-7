package com.studenthackvii.dave;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RateController {

  @Value("classpath:/peter.json")
  private Resource comicList;

  @GetMapping("/rate")
  public String getCartoons() {
    Logger
      .getAnonymousLogger()
      .info(String.format("resource exists: %s", comicList.exists()));
    return "/index.html";
  }

  @PostMapping("/rate")
  public String postRatings() {
    return "/index.html";
  }
}