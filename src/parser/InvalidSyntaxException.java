package parser;

public class InvalidSyntaxException extends Exception{
    public InvalidSyntaxException(String message, int lineNbr){
        super(message + " on line " + lineNbr);
    }

    public InvalidSyntaxException(String message, int lineNbr, String expected, String found){
        super(message + " on line " + lineNbr + approxSting(expected, found));
    }

    public InvalidSyntaxException(String message, int lineNbr, String found){
        super(message + " on line " + lineNbr + ", " + " unexpected token: " + found);
    }

    public static String approxSting(String expected, String found){
        // ", expected: " + expected + " but found: " + found
        if (expected.startsWith(found.substring(found.length()/2))) {
            return ", got: " + found + " did you mean " + expected + " ?";
        }else{
            return ", expected: " + expected + " but found: " + found;
        }
    }
}
