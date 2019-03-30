package com.studenthackvii.dave;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;
import org.tensorflow.*;

@Component
public class Train {

    public void train() {
        Graph g = new Graph();

        // vars
        Operation ratingsMulVar = g.opBuilder("Variable", "ratingsVm")
                .setAttr("dtype", DataType.FLOAT)
                .setAttr("shape", Shape.make(10))
                .build();

        Operation confidenceMulVar = g.opBuilder("Variable", "confidenceVm")
                .setAttr("dtype", DataType.FLOAT)
                .setAttr("shape", Shape.make(10))
                .build();

        Operation ratingsAddVar = g.opBuilder("Variable", "ratingsVc")
                .setAttr("dtype", DataType.FLOAT)
                .setAttr("shape", Shape.make(10))
                .build();

        Operation confidenceAddVar = g.opBuilder("Variable", "confidenceVc")
                .setAttr("dtype", DataType.FLOAT)
                .setAttr("shape", Shape.make(10))
                .build();

        // inputs
        Operation ratingsInput = g.opBuilder("Placeholder", "ratingsIn")
                .setAttr("dtype", DataType.FLOAT)
                .setAttr("shape", Shape.make(10))
                .build();

        Operation confidenceInput = g.opBuilder("Placeholder", "confidenceIn").
                setAttr("dtype", DataType.FLOAT)
                .setAttr("shape", Shape.make(10))
                .build();

        // m x
        Operation ratTimesM = g.opBuilder("Mul", "ratingM")
                .addInput(ratingsInput.output(0))
                .addInput(ratingsMulVar.output(0))
                .build();

        Operation conTimesM = g.opBuilder("Mul", "confidenceM")
                .addInput(confidenceInput.output(0))
                .addInput(confidenceMulVar.output(0))
                .build();

        // m x + c
        Operation ratMPlusC = g.opBuilder("Add", "ratingMc")
                .addInput(ratTimesM.output(0))
                .addInput(ratingsAddVar.output(0))
                .build();

        Operation conMPlusC = g.opBuilder("Add", "confidenceMc")
                .addInput(conTimesM.output(0))
                .addInput(confidenceAddVar.output(0))
                .build();


        Operation result = g.opBuilder("Mul", "res")
                .addInput(ratMPlusC.output(0))
                .addInput(conMPlusC.output(0))
                .build();

        Tensor<Float> ratings = Tensor.create(new long[]{10}, FloatBuffer.allocate(10));
        Tensor<Float> confidence = Tensor.create(new long[]{10}, FloatBuffer.allocate(10));

        g.opBuilder("Assign", "assign 1");

        
        Tensor<Float> tensor = (Tensor<Float>) new Session(g)
                .runner()
                .feed("ratingsIn", ratings)
                .feed("confidenceIn", confidence)
                .fetch("res")
                .run()
                .get(0);
        Logger.getAnonymousLogger().info(String.valueOf(tensor.shape()[0]));
    }
}