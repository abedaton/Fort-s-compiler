import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;

public class Main {
    /**
    * @param args file in FORTR-S
    * The main method will create a read, read the input file and iterate on it using nextToken
    * The method will print the Lexical Unit and the symbol table in alphabetical order
    */
    public static void main(String[] args) {

      Map<Object, Integer> variables = new HashMap<Object, Integer>();
      ArrayList<Symbol> symbolList = new ArrayList<Symbol>();

        java.io.FileInputStream stream = null;
        try {
            stream = new java.io.FileInputStream(args[0]); // Creation of the Reader
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert stream != null;
        java.io.Reader reader = new java.io.InputStreamReader(stream);


        final LexicalAnalyzer analyzer = new LexicalAnalyzer(reader);
        Symbol readingSymbol;
        while (true) {
            try {
                readingSymbol = analyzer.nextToken();
                if (readingSymbol.getType() == LexicalUnit.EOS) {
                    break;
                }
                symbolList.add(readingSymbol);
                // We know this is a good unit
                if (readingSymbol.getType() == LexicalUnit.VARNAME){
                    variables.putIfAbsent(readingSymbol.getValue(), readingSymbol.getLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("bye");
            }
        }
        printLexicalUnit(symbolList);
        printVariable(variables);
    }
    /**
    * @param LexicalUnit all the lexical unit found in the flex file
    * Print all the lexical unit
    */
    public static void printLexicalUnit(ArrayList<Symbol> LexicalUnit){

      for(Symbol i : LexicalUnit){
        //System.out.println("token: " + ((i.getType() == "ENDLINE") ? "\\n" : i.getyytext()) + "\tlexical unit: " + LexicalUnit.valueOf(unit));
        System.out.println(i);
      }
    }

    /**
    * Print the symbol table in alphabetical order
    * @param variables all variables with the line of the first occurrence
    */
    public static void printVariable(Map<Object,Integer> variables){

      System.out.println("\nVariables");
      TreeMap<Object, Integer> sortedVariables = new TreeMap<Object, Integer>(variables);
      for (Map.Entry<Object,Integer> oneVar : sortedVariables.entrySet()){
          System.out.println(oneVar.getKey() + "\t" + oneVar.getValue());
      }
      System.out.println();
    }
}
