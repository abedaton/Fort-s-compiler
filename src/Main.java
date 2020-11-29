
import parser.ActionTable;
import parser.InvalidSyntaxException;
import parser.Parser;
import scanner.FlexManager;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    public static void main(String[] args) throws FileNotFoundException, IOException, SecurityException, InvalidSyntaxException {

        // Display the usage when the number of arguments is wrong (should be 1)

        // Open the file given in argument
        execRight(args);
//        System.out.println(parser.getParseTree().getRoot().toString());

    }

    private static boolean correctCommand(String[] args){
        if (args.length == 1) {
            return true;
        } else if(args.length == 2){
            if (args[0].equals("-v"))
                return true;
        } else if (args.length == 3){
            if (args[0].equals("-wt") )
                return true;
        } else if (args.length == 4){
            if (args[0].equals("-v") && args[1].equals("-wt") )
                return true;
        }
        return false;
    }

    private static void execRight(String[] args) throws IOException, SecurityException, InvalidSyntaxException{
        if (args.length == 1) {
            FileReader source = new FileReader(args[0]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            System.out.println(parser.doTheLL());
        } else if(args.length == 2 && args[0].equals(("-v"))){
            FileReader source = new FileReader(args[1]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            parser.doTheLL();
            System.out.println(parser.prettyPrint());
        } else if(args.length == 3 && args[0].equals(("-wt"))){
            FileReader source = new FileReader(args[2]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            System.out.println(parser.doTheLL());
            //System.out.println(parser.getParseTree().toLaTeX());
            parser.creatLatexFile(args[1]);
        } else if(args.length == 4 && args[0].equals(("-v")) && args[1].equals("-wt")){
            FileReader source = new FileReader(args[3]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            parser.doTheLL();
            System.out.println(parser.prettyPrint());
            //System.out.println(parser.getParseTree().toLaTeX());
            parser.creatLatexFile(args[2]);
        } else{
            System.out.println("Usage:  java -jar part2.jar [OPTIONS] file.fs\n"
                    + "or\tjava " + Main.class.getSimpleName() + "[OPTIONS] file.fs\n"  + "OPTION : -v to have a more verbose output\n -wt latex.tex to obtain the derivation tree in a file latex");
            System.exit(0);
            }
    }
}
