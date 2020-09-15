package interfaces;

import java.util.*;


public class IntegerVector extends ObjectVector {
    private ArrayList<Integer> vec = new ArrayList<Integer>();

    public boolean add(Integer x) {
        return vec.add(x);
    }

    public int [] toR() {
        int[] x = new int[vec.size()];
        for (int i = 0; i < vec.size(); i++) {
            x[i] = (int) vec.get(i);
        }
        return x;
    }

}
