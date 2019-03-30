package com.studenthackvii.dave;

import org.springframework.stereotype.Component;

@Component
public class Rating {
    private int id;
    private int review;

    public Rating() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public int getReview() {
        return review;
    }
}
