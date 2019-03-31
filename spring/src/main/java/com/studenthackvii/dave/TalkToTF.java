package com.studenthackvii.dave;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class TalkToTF {

    private ObjectMapper mapper = new ObjectMapper();

    public Recommendation talk(ConfidenceRatings cr) throws IOException {

        return mapper.readValue(new RestTemplate()
                .postForObject(
                        "localhost:5000/",
                        mapper.writeValueAsString(cr),
                        String.class
                ),
                Recommendation.class);
    }

    public static class Recommendation {
        public float[] recommendation;
    }

    public static class ConfidenceRatings {
        public float[] confidence;
        public float[] rateings;
    }
}
