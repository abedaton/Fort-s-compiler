import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Project Part 1: Lexical Analyzer
 *
 * @author Defraene Pierre, Bedaton Antoine
 *
 */

public class Main {
    /**
     * The scanner
     *
     * @param args The argument(s) given to the program
     * @throws IOException           java.io.IOException if an I/O-Error occurs
     * @throws FileNotFoundException java.io.FileNotFoundException if the specified file does not exist
     */

    public static void main(String[] args) throws FileNotFoundException, IOException, SecurityException {

        // Display the usage when the number of arguments is wrong (should be 1)
        if (args.length != 1) {
            System.out.println("Usage:  java -jar Part1.jar file.fs\n"
                    + "or\tjava " + Main.class.getSimpleName() + " file.fs");
            System.exit(0);
        }

        // Open the file given in argument
        FileReader source = new FileReader(args[0]);

        FlexManager manager = new FlexManager(source);
        manager.parseFlex();
    }
}
