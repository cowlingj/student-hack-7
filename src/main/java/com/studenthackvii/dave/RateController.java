package com.studenthackvii.dave;

import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

@Controller
public class RateController {

  private ObjectMapper mapper = new ObjectMapper();

  @Value("classpath:/data/cartoonsToRate.json")
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

      System.out.println("Tensor version" + TensorFlow.version());
  
      final String TRAINING_DATA = "";
      final int NUM_PREDICTIONS = 1;
  
      try (SavedModelBundle b = SavedModelBundle.load(TRAINING_DATA)) {
  
          Session sess = b.session(); // create the session from the Bundle
          // create an input Tensor, value = 2.0f
          Tensor x = Tensor.create(new long[]{NUM_PREDICTIONS},
                  FloatBuffer.wrap(new float[]{2.0f}));
  
          // run the model and get the result, 4.0f.
          float[] y = sess.runner()
                  .feed("x", x)
                  .fetch("y")
                  .run()
                  .get(0)
                  .copyTo(new float[NUM_PREDICTIONS]);
  
          System.out.println(y[0]);
      }
  }
}