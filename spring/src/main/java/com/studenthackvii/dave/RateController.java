package com.studenthackvii.dave;

import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        List<String> allGenresList = new ArrayList<String>();
        // go through all cartoons and add all genres to a list
        for (int i = 0; i < ratingArray.length; i++) {
            String[] currentGenres = cartoonsFile.getCartoons().get(i).getGenre();
            for(String genre : currentGenres)
            {
              if (!allGenresList.contains(genre))
              {
                allGenresList.add(genre);
              }
            }
        }
        String[] genreArray = allGenresList.toArray(new String[0]);
        int[] cumulativeGenreRating = new int[genreArray.length];
        int[] occurences = new int[genreArray.length];
        
        for (int i = 0; i < ratingArray.length; i++) {
            currentRating = new Rating();
            currentRating.setId(ids[i]);
            currentRating.setReview(reviews[i]);
            ratingArray[i] = currentRating;

            // checking the Rating objects have the ID and Review
            Logger.getAnonymousLogger().info(String.format("rate id: %d", ratingArray[i].getId()));
            Logger.getAnonymousLogger().info(String.format("rate review: %d", ratingArray[i].getReview()));

            // get genres of current cartoon
            String[] currentGenres = cartoonsFile.getCartoons().get(ratingArray[i].getId()).getGenre();

            for (int index = 0; index < genreArray.length; index++)
            {
                for(int j=0; j<currentGenres.length; j++)
                {
                    if (genreArray[index] == currentGenres[j]) {
                      cumulativeGenreRating[index] += reviews[i];
                      occurences[index] ++;
                    }
                }
            }

        }

        for(int i = 0; i < genreArray.length; i++)
        {
            double avg = (double)cumulativeGenreRating[i]/occurences[i];
            double adjustedAvg = lerp(avg, 1, 5, -1, 1);
            Logger.getAnonymousLogger().info(String.format("lerp for genre: %d", i));
            Logger.getAnonymousLogger().info(String.format("lerp: %f", adjustedAvg));
        }


        return "/recommended.html";
    }

    public double lerp(double value, double a1, double a2, double b1, double b2) {
        Logger.getAnonymousLogger().info(String.format("average value: %f", value));
        if (value <= a1) {
            return b1;
        }

        if (value >= a2) {
            return b2;
        }

        return (((value - a1) * (b2 - b1)) / (a2 - a1) ) + b1;
    }

}