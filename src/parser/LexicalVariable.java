package parser;

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

    public static boolean contains(String variable){
        for (LexicalVariable var : values()){
            if (var.name().equals(variable)){
                return true;
            }
        }
        return false;
    }
}
