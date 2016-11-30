package a.clustered.word.counter;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Mohammed Alokshiya
 */
public class Worker {

    private static BufferedReader in;
    private static PrintWriter out;
    private static final String host = "127.0.0.1";
    private static final int port = 4321;

    public static void main(String[] args) throws IOException {
        String line;
        Socket socket = new Socket(host, port);
        Scanner [] resultFiles;
        Object [][] keyValue;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        while (true) {
            line = in.readLine();
            // receiving a new part
            if (line.startsWith("NEW_FILE ")) {
                PrintWriter writer = new PrintWriter(new File(line.substring(9)));
                while (!(line = in.readLine()).startsWith("END_OF_FILE")) {
                    writer.println(line);
                }
                writer.close();
            }
            else if (line.startsWith("MAP_COUNT_WORDS ")) {
                File outputFile = map(new WordCountMapper(), new File("RESULTS_" + line.substring(12)));
                out.println("MAP_COMPLETE " + outputFile.getName());
            }
            else if (line.startsWith("SHUFFLE_SORT_PHASE ")) {
                String[] fileNames = line.split(" ");
                resultFiles = new Scanner[fileNames.length - 1];
                keyValue = new Object[resultFiles.length][2];
                for (int i = 0; i < resultFiles.length; i++) {
                    resultFiles[i] = new Scanner(new File(fileNames[i + 1]));
                    keyValue[i] = resultFiles[i].nextLine().split(" ");
                }
                while (true) {
                    line = in.readLine();
                    if (line.startsWith("END_OF_SHUFFLE_SORT "))
                        break;
                    else if (line.startsWith("GET_KEYS")) {
                        StringBuilder sb = new StringBuilder("KEYS");
                        for (int i = 0; i < resultFiles.length; i++) {
                            if (keyValue[i][0] != null) {
                                sb.append(" ");
                                sb.append(keyValue[i][0]);
                            }
                        }
                        out.println(sb.toString());
                    }
                    else if (line.startsWith("GET_VALUES ")) {
                        String key = line.substring(11);
                        StringBuilder sb = new StringBuilder("VALUES");
                        for (int i = 0; i < resultFiles.length; i++) {
                            while (key.equals(keyValue[i][0])) {
                                sb.append(" ");
                                sb.append(keyValue[i][1]);
                                if (resultFiles[i].hasNextLine())
                                    keyValue[i] = resultFiles[i].nextLine().split(" ");
                                else
                                    keyValue[i][0] = null;
                            }
                        }
                        out.println(sb.toString());
                    }
                }
            }
            else if (line.startsWith("REDUCE_COUNT_WORDS ")) {
                String [] values = line.split(" ");
                ArrayList<String> list = new ArrayList();
                for (int i = 2; i < values.length; i++) {
                    list.add(values[i]);
                }
                Object value = reduce(new WordCountReducer(), list);
                out.println("REDUCE_COMPLETE " + values[1] + " " + value);
            }
        }
    }
    
    public static File map (MapFunction function, File file) {
        return function.apply(file);
    }
    public static Object reduce (ReduceFuntion function, List list) {
        return function.apply(list);
    }
}
