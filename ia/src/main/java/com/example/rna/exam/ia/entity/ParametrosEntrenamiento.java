package com.example.rna.exam.ia.entity;

import lombok.Data;

@Data
public class ParametrosEntrenamiento {
    private double alpha;
    private double[] weights;
    private double bias;
    private int epocas;


}
