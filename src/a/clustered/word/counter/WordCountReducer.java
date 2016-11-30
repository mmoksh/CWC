/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a.clustered.word.counter;

import java.util.List;

/**
 *
 * @author Mohammed
 */
public class WordCountReducer implements ReduceFuntion {

    @Override
    public Object apply(List l) {
        long counter = 0;
        for (int i = 0; i < l.size(); i++) {
            counter += (long)l.get(i);
        }
        return counter;
    }
}