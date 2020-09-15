package interfaces;

import java.util.*;


public class NumericVector extends ObjectVector {
    private ArrayList<Double> vec = new ArrayList<Double>();

    public boolean add(Double x) {
        return vec.add(x);
    }

    public double [] toR() {
        double[] x = new double[vec.size()];
        for (int i = 0; i < vec.size(); i++) {
            x[i] = (double) vec.get(i);
        }
        return x;
    }

}