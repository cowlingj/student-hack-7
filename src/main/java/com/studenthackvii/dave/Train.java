package com.studenthackvii.dave;

import java.nio.FloatBuffer;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

public class Train {

  public void name() {
    Graph g = new Graph();
    Tensor<Float> ratings = Tensor.create(new long[]{10}, FloatBuffer.allocate(10));
    Tensor<Float> confidence = Tensor.create(new long[]{10}, FloatBuffer.allocate(10));

    Operation ratingsOp = g.opBuilder("Variable", "ratings")
      .setAttr("dtype", ratings.dataType())
      .setAttr("value", ratings)
      .build();

    Operation confidenceOp = g.opBuilder("Variable", "confidence")
      .setAttr("dtype", confidence.dataType())
      .setAttr("value", confidence)
      .build();
  }
}