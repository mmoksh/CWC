package a.clustered.word.counter.v2.pkg0;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mohammed Alokshiya
 */
public class Worker {

    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static final String host = "127.0.0.1";
    private static final int port = 4321;

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Socket socket = new Socket(host, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            HashMap<String, Integer> hash = new HashMap();
            String [] array;
            StringTokenizer tokenizer;
            String word;
            int count;
            long time;
            int fileID;
            while (true) {
                fileID = (int)in.readObject();
                array = (String []) in.readObject();
                time = System.currentTimeMillis();
                hash.clear();
                for (int i = 0; i < array.length; i++) {
                    if (array[i] == null)
                        break;
                    tokenizer = new StringTokenizer(array[i].toLowerCase());
                    while (tokenizer.hasMoreTokens()) {
                        word = tokenizer.nextToken();
                        if (hash.containsKey(word)) {
                            count = hash.get(word);
                        } else {
                            count = 0;
                        }
                        hash.put(word, count + 1);
                    }
                }
                out.writeObject(fileID);
                out.writeObject(hash);
                out.writeObject(System.currentTimeMillis() - time);
                out.reset();
                hash.clear();
            }
        } catch (SocketException ex) {
            System.out.println("Error: Can not connect to 127.0.0.1!");
        } catch (UnknownHostException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
