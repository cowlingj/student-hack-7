package com.studenthackvii.dave;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RateController {

  private ObjectMapper mapper = new ObjectMapper();

  @Value("classpath:/peter.json")
  private Resource cartoonsList;

  @GetMapping("/rate")
  public String getCartoons(Model model) throws JsonParseException, JsonMappingException, IOException {

    CartoonsFile cartoonsFile = mapper.readValue(cartoonsList.getFile(), CartoonsFile.class);

    Logger
      .getAnonymousLogger()
      .info(String.format("cartoonsFile items: %d", cartoonsFile.getCartoons().size()));
    model.addAttribute("ratings", new Rating());
    return "/test.html";
  }

    @PostMapping("/rate")
    public String postRatings(@ModelAttribute("ratings") Rating ratings, BindingResult errors) {

        return "/index.html";
    }

}