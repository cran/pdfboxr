package interfaces;

import java.util.*;


public class ObjectVector {
    private ArrayList<Object> vec = new ArrayList<Object>();

    public boolean add(Object x) {
        return vec.add(x);
    }

    public void clear() {
        vec.clear();
    }
}
