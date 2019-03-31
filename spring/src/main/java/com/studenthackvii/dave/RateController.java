package com.studenthackvii.dave;

import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.tensorflow.*;

@Controller
public class RateController {

    private ObjectMapper mapper = new ObjectMapper();

    @Value("classpath:/data/cartoonsToRate.json")
    private Resource cartoonsList;

    CartoonsFile cartoonsFile;

    @PostConstruct
    private void init() throws JsonParseException, JsonMappingException, IOException {
        cartoonsFile = mapper.readValue(cartoonsList.getFile(), CartoonsFile.class);
    }

    @GetMapping("/rate")
    public String getCartoons(Model model) {

        Logger.getAnonymousLogger().info(String.format("cartoonsFile items: %d", cartoonsFile.getCartoons().size()));
        model.addAttribute("ratings", new Rating());
        model.addAttribute("cartoons", cartoonsFile.getCartoons());
        return "/cartoon-list.html";
    }

    @PostMapping(value = "/rate", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public String postRatings(@RequestParam int[] ids, @RequestParam int[] reviews) {

        Rating[] ratingArray = new Rating[ids.length];
        Rating currentRating;

        for (int i = 0; i < ratingArray.length; i++) {
            currentRating = new Rating();
            currentRating.setId(ids[i]);
            currentRating.setReview(reviews[i]);
            ratingArray[i] = currentRating;

            // checking the Rating objects have the ID and Review
            Logger.getAnonymousLogger().info(String.format("rate id: %d", ratingArray[i].getId()));
            Logger.getAnonymousLogger().info(String.format("rate review: %d", ratingArray[i].getReview()));
        }

        return "/recommended.html";
    }

    public int lerp(int value, int a1, int a2, int b1, int b2) {

        if (value <= a1) {
            return b1;
        }

        if (value >= a2) {
            return b2;
        }

        return (int)(((double)(value - a1)) / (a2 - a1) * (b2 - b1) ) + b1;
    }

}