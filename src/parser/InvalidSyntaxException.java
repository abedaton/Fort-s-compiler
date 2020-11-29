package parser;

/**
 * Custom Error class used to represent an invalid Syntax
 * This is thrown when we find an invalid syntax in the FORTR-S file
 */
public class InvalidSyntaxException extends Exception{
    /**
     * The default method, used only if we want to display a message and the traceback
     * @param message the message we want to display
     */
    public InvalidSyntaxException(String message) {
        super(message);
    }

    /**
     * A more advanced function that display the error message, the traceback and the line number
     * @param message the message we want to display
     * @param lineNbr the line number where the error was found
     */
    public InvalidSyntaxException(String message, int lineNbr){
        super(message + " on line " + lineNbr);
    }

    /**
     * A method used to display the invalid string found
     * @param message the message we want to display
     * @param lineNbr the line number where the error was found
     * @param found the string found that was unexpected
     */
    public InvalidSyntaxException(String message, int lineNbr, String found){
        super(message + " on line " + lineNbr + ", " + " unexpected token: " + found);
    }

    /**
     * The most used method, this method displays the wrong string found and what was expected
     * @param message the message we want to display
     * @param lineNbr the line number where the error was found
     * @param expected the string the compiler expected
     * @param found the string found
     */
    public InvalidSyntaxException(String message, int lineNbr, String expected, String found){
        super(message + " on line " + lineNbr + handleEndOfFile(expected, found));
    }

    /**
     * This method will detect if it should be the end of the file or not and print an appropriate message
     * @param expected the expected string
     * @param found the string found
     * @return the appropriate string
     */
    private static String handleEndOfFile(String expected, String found){
        if (expected.equals("$")){
            return ", found code after ENDPROG";
        } else {
            return ", expected: " + expected + " but found: " + found;
        }
    }
}
