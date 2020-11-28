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
    private StringBuilder builder;

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
        builder = new StringBuilder();
        int i = 0;
        while (i < symbols.size()) {
            String topStack = stack.peek(); // nullable
            LexicalUnit type = symbols.get(i).getType(); // nullable
            Object value = symbols.get(i).getValue();   // nullable
            String currentInput = transform(type.toString());
            Symbol symbol = symbols.get(i);
            System.out.println("Current input = " + currentInput );
            System.out.println("topStack = " + topStack);
            if (topStack != null && topStack.equals(currentInput)) {
                currentNode = match(currentNode, currentInput, value);
                i++;
            } else {
                    currentNode = produce(currentNode, currentInput, symbol, value);
            }
            System.out.println(stack);
        }
        while (stack.peek() != "$"){
            Integer ruleNumber = actionTable.getData(stack.peek(), "$");
            if (ruleNumber != null) {
                stack.pop();
                currentNode = addStack(currentNode,ruleNumber);
                builder.append(ruleNumber).append(" ");

            } else {
                ;//throw new InvalidSyntaxException("Error: Invalid Syntax", symbol.getLine(), stack.peek(), value.toString());
            }
        }
        System.out.println(builder.toString());
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

    private ParseTree produce(ParseTree tree, String input, Symbol symbol, Object value) throws InvalidSyntaxException {
        Integer ruleNumber = actionTable.getData(stack.peek(), input);
        if (ruleNumber != null) {
            stack.pop();
            tree = addStack(tree, ruleNumber);
            builder.append(ruleNumber).append(" ");

        } else{
            throw new InvalidSyntaxException("Error: Invalid Syntax", symbol.getLine(), stack.peek(), value.toString());
        }
        return tree;
    }

    private ParseTree addStack(ParseTree tree, int ruleNumber){
        List<String> product = rules.getRule(ruleNumber);
        if (product.size()!= 0) {
            addTree(tree, product);
            stack.addAll(rules.getRuleReversed(ruleNumber));
            tree = tree.getFirstChild();
        } else {
            tree.addChild(new ParseTree(new Variable(" ")));
            tree = tree.getNext();
        }
        return tree;
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


