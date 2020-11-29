package parser;

import scanner.LexicalUnit;

public class InvalidSyntaxException extends Exception{
    public InvalidSyntaxException(String message) {
        super(message);
    }

    public InvalidSyntaxException(String message, int lineNbr){
        super(message + " on line " + lineNbr);
    }

    public InvalidSyntaxException(String message, int lineNbr, String expected, String found){
        super(message + " on line " + lineNbr + handleEndOfFile(expected, found));
    }

    public InvalidSyntaxException(String message, int lineNbr, String found){
        super(message + " on line " + lineNbr + ", " + " unexpected token: " + found);
    }


    private static String expected(String expected, String found){
        return ", expected: " + expected + " but found: " + found;
    }

    private static String handleEndOfFile(String peek, String found){
        if (peek.equals("$")){
            return ", found code after ENDPROG";
        } else {
            return expected(peek, found) ;
        }
    }
}
