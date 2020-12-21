package generator;

import parser.LexicalVariable;
import parser.ParseTree;
import parser.Variable;


import java.util.ArrayList;
import java.util.Arrays;
/**
 * class representing a abstract syntax tree
 * which we need to create the LLVM code
 *
 *
 */
public class AbstractSyntaxTree {

    private final ParseTree tree;
    private final ArrayList<String> useful = new ArrayList<>(Arrays.asList("+", "-", "/", "*", "=", ">", "VARNAME:", "NUMBER:"));

    /**
     * default constructor which transform the parse tree given into a simple parse tree
     * @param tree parse tree to manipulate
     */
    public AbstractSyntaxTree(ParseTree tree){
        this.tree = tree;
        purge();
    }

    /**
     * method that minimize the parse tree
     */
    public void purge() {
        purgeLeaves(tree, false);
        purgeUselessStates(tree);
        purgeOperations(tree);
        purgeUselessStates(tree);
    }

    public ParseTree getTree() {
        return tree;
    }

    /**
     * method that give the latex of the tree
     */
    public String toLaTex(){
        return tree.toLaTeX();
    }


    /**
     * method that purge the leaves which don't match a useful terminal (+ - / * = > var number)
     * @param tree parse tree which we need to purge
     * @param modified var which said that we have modified the tree
     * @return a boolean that says if the tree was modified or not
     */
    public boolean purgeLeaves(ParseTree tree, boolean modified) {
        int i = 0;
        while (i < tree.getChildren().size()) {
            modified = purgeLeaves(tree.getChildren().get(i), modified);
            if (!modified) {
                i += 1;
            }
        }
        if (!tree.hasChildren() && ((tree.getLabel().getValue() == null) || useful.stream().noneMatch(elem -> tree.getLabel().getValue().toString().contains(elem) && !tree.getLabel().getValue().equals(":=")))){
            tree.getFather().getChildren().remove(tree);
            return true;
        }
        return false;
    }

    /**
     * method that purge the useless States (one father and one child) except PrintVar and Read
     * considering that we still need to know that we will print and read
     * @param tree tree that we need to purge
     */
    public void purgeUselessStates(ParseTree tree) {
        for (int i = 0; i < tree.getChildren().size(); i++){
            purgeUselessStates(tree.getChildren().get(i));
        }
        if (tree.getFather() != null && tree.getChildren().size() == 1 && tree.getLabel().getVariable() != LexicalVariable.PRINTVAR && tree.getLabel().getVariable() != LexicalVariable.READVAR){
            tree.getFather().getChildren().set(tree.getFather().getChildren().indexOf(tree), tree.getChildren().get(0));
            tree.getChildren().get(0).setFather(tree.getFather());
        }
    }

    /**
     * method that transform the tree to make the operators a father of the operands
     * @param tree tree that we need to purge
     * @return boolean if the parse tree is  modified
     */
    public boolean purgeOperations(ParseTree tree) {
        if (tree.getLabel().getValue() != null){
            if (Arrays.asList("*","/","+","-").contains(tree.getLabel().getValue().toString())){
                if (tree.getFather().getLabel().getVariable() == LexicalVariable.ATOM){
                    tree.getFather().setLabel(new Variable("*"));
                    tree.setLabel(new Variable("NUMBER: -1"));
                } else {
                    tree.getFather().getFather().setLabel(tree.getLabel());
                    tree.getFather().getChildren().remove(tree);
                }
                return true;
            }
            if (Arrays.asList("=",">").contains(tree.getLabel().getValue().toString())){
                tree.getFather().setLabel(tree.getLabel());
                tree.getFather().getChildren().remove(tree);
                return true;
            }
        }
        for (int i = 0; i < tree.getChildren().size(); i++){
            if (purgeOperations(tree.getChildren().get(i))){
                i--;
            }
        }
        return false;
    }

    /**
     * method that go through the tree in order and print
     * @param tree tree to go through to be able to recurse
     */
    public void inOrder(ParseTree tree){
        if (tree == null){
            return;
        }

        for (int i = 0; i < tree.getChildren().size(); i++){
            inOrder(tree.getChildren().get(i));
        }
        System.out.print("" + tree + ", ");
    }

    /**
     * method to ask the inOrder recursif over the tree
     */
    public void inOrder(){
        inOrder(tree);
    }
}
