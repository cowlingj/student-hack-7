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

    private void peter() {
        try (Graph g = new Graph()) {
            final String value = "Hello from " + TensorFlow.version();

            // Construct the computation graph with a single operation, a constant
            // named "MyConst" with a value "value".
            try (Tensor t = Tensor.create(value.getBytes(StandardCharsets.UTF_8))) {
                // The Java API doesn't yet include convenience functions for adding operations.
                g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }

            try (Session s = new Session(g); Tensor output = s.runner().fetch("MyConst").run().get(0)) {
                System.out.println(new String(output.bytesValue(), StandardCharsets.UTF_8));
            }
        }
    }

    private double[] converter(Rating rating, int occurrences) {

        final int GET_HALF = 2, ARRAY_SIZE = 2, SET_RATING = 0, SET_OCCURRENCES = 1, LOWER_DOWN_NUMBER = 3;
        final int MAXIMUM_OCCURRENCES = 99, MAXIMUM_RATING = 5, MINIMUM_RATING = 1, MINIMUM_OCCURRENCES = 0;
        final double CONVERT_TO_DECIMAL = 0.01;

        if (rating.getId() > MAXIMUM_RATING)
            rating.setId(MAXIMUM_RATING);
        if (rating.getId() < MINIMUM_RATING)
            rating.setId(MINIMUM_RATING);

        if (occurrences < MINIMUM_OCCURRENCES)
            occurrences = MINIMUM_OCCURRENCES;
        if (occurrences > MAXIMUM_OCCURRENCES)
            occurrences = MAXIMUM_OCCURRENCES;

        final int newRating = rating.getReview() - LOWER_DOWN_NUMBER / GET_HALF; // formula;
        final double newOccurrences = occurrences * CONVERT_TO_DECIMAL;

        double[] array = new double[ARRAY_SIZE];
        array[SET_RATING] = newRating;
        array[SET_OCCURRENCES] = newOccurrences;

        return array;
    }

}