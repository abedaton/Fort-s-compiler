package parser;

import scanner.LexicalUnit;
import scanner.Symbol;

import java.io.FileReader;
import java.util.*;


public class Parser {
    private final FileReader source;
    private final Stack<String> stack = new Stack<String>();
    private final ActionTable actionTable = new ActionTable();
    private final ArrayList<Symbol> symbols;
    private final Rules rules = new Rules();
    private ParseTree node;

    public Parser(FileReader source, ArrayList<Symbol> symbols){
        this.source = source;
        this.symbols = symbols;
    }

    public ParseTree getParseTree(){
        return this.node;
    }


    public void doTheLL() throws InvalidSyntaxException {
        stack.push("$");
        stack.push("PROGRAM");
        node = new ParseTree(new Variable(LexicalVariable.PROGRAM));
        ParseTree currentNode = node;
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < symbols.size()) {
            String topStack = stack.peek(); // nullable
            LexicalUnit type = symbols.get(i).getType(); // nullable
            Object value = symbols.get(i).getValue();   // nullable
            String currentInput = transform(type.toString());
            Symbol symbol = symbols.get(i);
            if (topStack != null && topStack.equals(currentInput)) {
                currentNode = match(currentNode, currentInput, value);
                i++;
            } else {
                    currentNode = produce(currentNode, currentInput, symbol, value, builder);
            }
        }
        System.out.println(stack);
        if (!stack.peek().equals("$")){
            while (!stack.empty() && !stack.peek().equals("$")) {
                if (actionTable.getData(stack.peek(), "epsi") != null) {
                    Integer ruleNumber = actionTable.getData(stack.peek(), "epsi");
                    builder.append(ruleNumber);
                    currentNode = produceEpsilon(currentNode, ruleNumber);
                    stack.pop();
                }
            }
        }
        System.out.println(builder.toString());
    }

    private void checkIfNull(Object object, Symbol symbol, String topStack, Object value) throws InvalidSyntaxException {
        if (object == null){
            throw new InvalidSyntaxException("Error: Invalid Syntax", symbol.getLine(), topStack, value.toString());
        }
    }


    private void addTree(ParseTree tree, List<String> toAdd){
        for (String stringVariable : toAdd) {
            if (LexicalVariable.contains(stringVariable)){
                tree.addChild(new ParseTree(new Variable(LexicalVariable.valueOf(stringVariable))));
            } else {
                tree.addChild(new ParseTree(new Variable(stringVariable)));

            }
        }
    }

    private ParseTree match(ParseTree tree, String input, Object value){
        stack.pop();
        if (Arrays.asList("VARNAME", "PROGNAME", "NUMBER").contains(input)){
            tree.setLabel(new Variable(input + ": " + value.toString()));
        }
        return tree.getNext();
    }

    private ParseTree produce(ParseTree tree, String input, Symbol symbol, Object value, StringBuilder builder) throws InvalidSyntaxException {
        Integer ruleNumber = actionTable.getData(stack.peek(), input);
        if (ruleNumber != null) {
            stack.pop();
            addStack(tree, ruleNumber);
            tree = tree.getFirstChild();
        } else{
            ruleNumber = actionTable.getData(stack.peek(), "epsi");
            checkIfNull(ruleNumber, symbol, stack.peek(), value);
            stack.pop();
            builder.append(ruleNumber).append(" ");

            tree = produceEpsilon(tree, ruleNumber);
        }
        return tree;
    }

    private ParseTree produceEpsilon(ParseTree tree, Integer ruleNumber){
        if (!rules.getRule(ruleNumber).equals(Collections.singletonList("epsi"))) {
            addStack(tree, ruleNumber);
            tree = tree.getFirstChild();
        } else{
            tree.addChild(new ParseTree(new Variable("epsi")));
            tree = tree.getNext();
        }
        return tree;
    }

    private void addStack(ParseTree tree, int ruleNumber){
        addTree(tree,rules.getRule(ruleNumber));
        stack.addAll(rules.getRuleReversed(ruleNumber));
    }



    private String transform(String columnName){
        switch (columnName) {
            case "LPAREN":
                columnName = "(";
                break;
            case "RPAREN":
                columnName = ")";
                break;
            case "ASSIGN":
                columnName = ":=";
                break;
            case "PLUS":
                columnName = "+";
                break;
            case "MINUS":
                columnName = "-";
                break;
            case "TIMES":
                columnName = "*";
                break;
            case "DIVIDE":
                columnName = "/";
                break;
            case "EQ":
                columnName = "=";
                break;
            case "GT":
                columnName = ">";
                break;
        }
        return columnName;
    }
}


