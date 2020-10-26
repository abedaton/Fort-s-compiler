import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
      Map<Object, Integer> variables = new HashMap<Object, Integer>();
      ArrayList<Symbol> symbolList = new ArrayList<Symbol>();

        //take return from the flex files

        java.io.FileInputStream stream = null;
        try {
            stream = new java.io.FileInputStream(args[0]);
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
                symbolList.add(readingSymbol);
                if (readingSymbol.getType() == LexicalUnit.EOS) {
                    break;
                }
                // We know this is a good unit
                if (readingSymbol.getType().equals("VARNAME")){
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
    public static void printLexicalUnit(ArrayList<Symbol> LexicalUnit){
      for(Symbol i : LexicalUnit){
        //System.out.println("token: " + ((i.getType() == "ENDLINE") ? "\\n" : i.getyytext()) + "\tlexical unit: " + LexicalUnit.valueOf(unit));
        System.out.println(i);
      }
    }

    public static void printVariable(Map<Object,Integer> variables){
      System.out.println("\nVariables");
      TreeMap<Object, Integer> sortedVariables = new TreeMap<Object, Integer>(variables);
      for (Map.Entry<Object,Integer> oneVar : sortedVariables.entrySet()){
          System.out.println(oneVar.getKey() + "\t" + oneVar.getValue());
      }
      System.out.println();
    }
}
