package com.studenthackvii.dave;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

@Component
public class TalkToTF {

    private ObjectMapper mapper = new ObjectMapper();

    public Recommendation talk(ConfidenceRatings cr) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Logger.getGlobal().info(mapper.writeValueAsString(cr));
        HttpEntity<String> send = new HttpEntity<>(mapper.writeValueAsString(cr), headers);

        Recommendation recommendation = mapper.readValue(new RestTemplate()
                        .postForObject(
                                "http://localhost:5000/",
                                send,
                                String.class),
                Recommendation.class);

        for (float f: recommendation.recommendation) {

          Logger.getGlobal().info("rec: " + f);
        }

        return recommendation;
    }

    public static class Recommendation {
        public float[] recommendation;
    }

    public static class ConfidenceRatings {
        public float[] confidence;
        public float[] ratings;
    }
}
