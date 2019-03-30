package com.studenthackvii.dave;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.stream.IntStream;

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
    public String postRatings(@ModelAttribute("ratings") Rating[] ratings) {
        JSONParser parser = new JSONParser();

        try {
            final String FILE_PATH = "C:\\Users\\Peter Boncheff\\IntelliJIDEAProjects\\student-hack-7\\src\\main\\resources\\peter.json" ;
            final String JSON_NAME = "name", JSON_URL = "url", JSON_ID = "id", JSON_GENRE = "genre";

            Object obj = parser.parse(new FileReader(FILE_PATH));

            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);

            String name = (String) jsonObject.get(JSON_NAME);
            int id = (Integer) jsonObject.get(JSON_ID);

            String url = (String) jsonObject.get(JSON_URL);
            JSONArray genre = (JSONArray) jsonObject.get(JSON_GENRE);
            IntStream.range(0, genre.size())
                    .mapToObj(genre::get)
                    .forEach(System.out::println);



        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        peter();
        return "/index.html";
    }


    private void peter(){

        try (Graph g = new Graph()) {
            final String value = "Hello from " + TensorFlow.version();

            // Construct the computation graph with a single operation, a constant
            // named "MyConst" with a value "value".
            try (Tensor t = Tensor.create(value.getBytes(StandardCharsets.UTF_8))) {
                // The Java API doesn't yet include convenience functions for adding operations.
                g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }

            // Execute the "MyConst" operation in a Session.
            try (Session s = new Session(g);
                 // Generally, there may be multiple output tensors,
                 // all of them must be closed to prevent resource leaks.
                 Tensor output = s.runner().fetch("MyConst").run().get(0)) {
                System.out.println("HELLOOOOOOO " + new String(output.bytesValue(), StandardCharsets.UTF_8));


            }
        }
    }

    private double[] converter(Rating rating, int occurrences){

        final int GET_HALF = 2, ARRAY_SIZE = 2, SET_RATING = 0, SET_OCCURRENCES = 1, LOWER_DOWN_NUMBER = 3;
        final int MAXIMUM_OCCURRENCES = 99, MAXIMUM_RATING = 5, MINIMUM_RATING = 1, MINIMUM_OCCURRENCES = 0;
        final double CONVERT_TO_DECIMAL = 0.01;

        if(rating.getId() > MAXIMUM_RATING) rating.setId(MAXIMUM_RATING);
        if(rating.getId() < MINIMUM_RATING) rating.setId(MINIMUM_RATING);

        if(occurrences < MINIMUM_OCCURRENCES) occurrences = MINIMUM_OCCURRENCES;
        if(occurrences > MAXIMUM_OCCURRENCES) occurrences = MAXIMUM_OCCURRENCES;

        final int newRating = rating.getReview() - LOWER_DOWN_NUMBER / GET_HALF; // formula;
        final double newOccurrences = occurrences * CONVERT_TO_DECIMAL;

        double[] array = new double[ARRAY_SIZE];
        array[SET_RATING] = newRating;
        array[SET_OCCURRENCES] = newOccurrences;

        return array;
    }

}