package interfaces;

import java.util.*;


public class VectorList {
    private Map<String, ObjectVector> vec = new HashMap<String, ObjectVector>();

    public void append(String key, Integer val) {
        if (vec.get(key) == null) {
            IntegerVector x = new IntegerVector();
            x.add(val);
            vec.put(key, x);
        } else {
            IntegerVector x = (IntegerVector) vec.get(key);
            x.add(val);
        }
    }

    public void append(String key, Double val) {
        if (vec.get(key) == null) {
            NumericVector x = new NumericVector();
            x.add(val);
            vec.put(key, x);
        } else {
            NumericVector x = (NumericVector) vec.get(key);
            x.add(val);
        }
    }

    public void append(String key, Float val) {
        if (vec.get(key) == null) {
            NumericVector x = new NumericVector();
            x.add(val.doubleValue());
            vec.put(key, x);
        } else {
            NumericVector x = (NumericVector) vec.get(key);
            x.add(val.doubleValue());
        }
    }

    public void append(String key, String val) {
        if (vec.get(key) == null) {
            CharacterVector x = new CharacterVector();
            x.add(val);
            vec.put(key, x);
        } else {
            CharacterVector x = (CharacterVector) vec.get(key);
            x.add(val);
        }
    }

    public String [] names() {
        String[] x = new String[vec.size()];
        int it = 0;
        for (Map.Entry<String, ObjectVector> entry : vec.entrySet()) {
            x[it] = entry.getKey();
            it = it + 1;
        }
        return x;
    }

    public ObjectVector get(String key) {
        return vec.get(key);
    }

} 
