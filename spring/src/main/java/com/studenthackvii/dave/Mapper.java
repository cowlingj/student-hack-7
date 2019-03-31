package com.studenthackvii.dave;

public class Mapper {

  public double[] converter(Rating rating, int occurrences){

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