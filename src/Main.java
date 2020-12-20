
import parser.InvalidSyntaxException;
import parser.Parser;
import generator.Generator;
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
        //execRight(args);

        FileReader source = new FileReader(args[0]);

        FlexManager flexManager = new FlexManager(source);
        flexManager.parseFlex();
        Parser parser = new Parser(flexManager.getSymbols());
        System.out.println(parser.parse());
        System.out.println(parser.getParseTree().toLaTeX());
        Generator generator = new Generator(parser.getParseTree());
        System.out.println(generator.getSyntaxTree().toLaTex());
        generator.createLLVMFile("llvm.ll");

    }

    /**
     * This method will parse the arguments provided and apply the good functions to it.
     * -v will apply verbose and give a more detailed output
     * -wt will output the derivation tree into a latex file
     * @param args the arguments provided at the start of the program
     * @throws IOException when the input file does exists
     * @throws InvalidSyntaxException when there is a syntax error in the input FS file
     */
    private static void execRight(String[] args) throws InvalidSyntaxException, IOException {
        if (args.length == 1) {
            FileReader source = new FileReader(args[0]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            System.out.println(parser.parse());
        } else if(args.length == 2 && args[0].equals(("-v"))){
            FileReader source = new FileReader(args[1]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            parser.parse();
            System.out.println(parser.prettyPrint());
        } else if(args.length == 3 && args[0].equals(("-wt"))){
            FileReader source = new FileReader(args[2]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            System.out.println(parser.parse());
            //System.out.println(parser.getParseTree().toLaTeX());
            parser.creatLatexFile(args[1]);
        } else if(args.length == 4 && args[0].equals(("-v")) && args[1].equals("-wt")){
            FileReader source = new FileReader(args[3]);

            FlexManager flexManager = new FlexManager(source);
            flexManager.parseFlex();
            Parser parser = new Parser(flexManager.getSymbols());
            parser.parse();
            System.out.println(parser.prettyPrint());
            //System.out.println(parser.getParseTree().toLaTeX());
            parser.creatLatexFile(args[2]);
        } else{
            System.out.println("Usage:  java -jar part2.jar [OPTIONS] file.fs\n"
                    + "or\tjava " + Main.class.getSimpleName() + " [OPTIONS] file.fs\n\n"  +
                    "OPTIONS: \n-v to have a more verbose output\n" +
                    "-wt <filename>.tex to output the derivation tree in a latex file");
            System.exit(0);
            }
    }
}
