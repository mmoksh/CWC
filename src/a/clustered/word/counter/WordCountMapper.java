/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a.clustered.word.counter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Mohammed
 */
public class WordCountMapper implements MapFunction {

    @Override
    public File apply(File f) {
        ArrayList<Word> words = new ArrayList();
        File outputFile = new File(f.getName() + "_output");
        try {
            Scanner input = new Scanner(f);
            PrintWriter output = new PrintWriter(outputFile);
            while (input.hasNext())
                words.add(new Word(input.next(), 1));
            Word [] array = new Word[words.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = words.get(i);
            }
            Arrays.sort(array);
            for (int i = 0; i < array.length; i++) {
                output.println(array[i].key + " " + array[i].value);
            }
            input.close();
            output.close();
        } catch (FileNotFoundException ex) {
        } finally {
            return outputFile;
        }
    }
}