/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a.clustered.word.counter;

/**
 *
 * @author Mohammed
 */
public abstract class KeyValue implements Comparable<KeyValue> {
    Object key;
    Object value;
    
    public KeyValue (Object key, Object Value) {
        this.key = key;
        this.value = value;
    }
}

class Word extends KeyValue {
    
    public Word (String key, int value) {
        super (key, value);
    }

    @Override
    public int compareTo(KeyValue o) {
        return ((String)key).compareTo((String)o.key);
    }
}