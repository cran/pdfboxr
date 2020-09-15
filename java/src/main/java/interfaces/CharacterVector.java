package interfaces;

import java.util.*;


public class CharacterVector extends ObjectVector {
    private ArrayList<String> vec = new ArrayList<String>();

    public boolean add(String x) {
        return vec.add(x);
    }

    public String [] toR() {
        return vec.toArray(new String[vec.size()]);
    }

}
