package easycs.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @author Mmuzafarov
 */
public final class Keyboard {

    private final static Log logger = LogFactory.getLog(Keyboard.class);

    public static String readLine() {
        String in;
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in))) {
            in = br.readLine();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
            in = "";
        }
        return in;
    }

}
