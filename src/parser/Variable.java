package parser;

import javax.xml.validation.Validator;

public class Variable {
    private boolean terminal;
    private boolean isVar = false;

    private LexicalVariable variable;
    private Object value = null;
    public Variable(LexicalVariable variable, Object value){
        this.variable = variable;
        this.value = value;
        this.terminal = true;
    }

    public Variable(LexicalVariable variable){
        this.variable = variable;
        this.terminal = false;
    }

    public Variable(String terminal){
        this.terminal = true;
        this.value = terminal;
        if (this.value.toString().contains(": ")){
            this.isVar = true;
        }
    }

    /**
     * This method is used to converts raw enums into beautiful latex
     * @return a beautiful string, just like you
     */
    public String toTexString() {
        if (this.value != null) {
            if (this.value.toString().startsWith("VARNAME")){
                return "Var: " + this.value.toString().substring(this.value.toString().indexOf("VARNAME: ")+"VARNAME: ".length());
            }
            if (this.value.toString().startsWith("PROGNAME")){
                return "ProgName: " + this.value.toString().substring(this.value.toString().indexOf("PROGNAME: ")+"PROGNAME: ".length());
            }
            switch (this.value.toString()){
                case "ENDLINE": {return "EndLine";}
                case ">": {return "$>$";}
                case ":=": {return "$\\coloneqq$";}
                case "VARNAME": {return "VarName";}
            }
            if (this.variable == null){
                if (this.value.toString().equals("epsi")) return "$\\epsilon$";
                return this.value.toString().toUpperCase();
            }
        }
        switch (this.variable.toString()){
            case "PRINTVAR":  {return "Print";}
            case "WHILEVAR":  {return "While";}
            case "IFVAR":  {return "If";}
            case "EXPRARITH": {return "ExprArith";}
            case "EXPRARITHBIS": {return "ExprArith'";}
            case "IFSEQ":  {return "IfSeq";}
            case "PRODBIS":  {return "Prod'";}
            case "READVAR": {return "Read";}
        }
        return toTitle(this.variable.toString());
    }

    private String toTitle(String string) {
        string = string.toLowerCase();
        char c =  string.charAt(0);
        String s = new String("" + c);
        String f = s.toUpperCase();
        return f + string.substring(1);
    }

    public boolean isTerminal(){
        return terminal;
    }

    public LexicalVariable getVariable() {
        return variable;
    }

    public Object getValue() {
        return value;
    }

    public boolean isVar() {
        return isVar;
    }

    public void setValue(Object val){
        if (this.value == null){
            this.terminal = true;
        }
        this.value = val;
    }

}
