package parser;

/**
 * Enum used to represents the different variables
 */
public enum LexicalVariable {
    PROGRAM,
    SPACE,
    CODE,
    INSTRUCTION,
    ASSIGN,
    EXPRARITH,
    EXPRARITHBIS,
    PROD,
    PRODBIS,
    ATOM,
    IFVAR,
    IFSEQ,
    COND,
    COMP,
    WHILEVAR,
    PRINTVAR,
    READVAR;

    /**
     * A contains method used to detect if the string parameter is in the enum
     * @param variable the string we want to check
     * @return returns true if the string is in the enum otherwise returns false
     */
    public static boolean contains(String variable){
        for (LexicalVariable var : values()){
            if (var.name().equals(variable)){
                return true;
            }
        }
        return false;
    }
}
