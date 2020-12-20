package generator;

import com.sun.javafx.image.impl.General;
import parser.LexicalVariable;
import parser.ParseTree;
import parser.Variable;


import java.util.ArrayList;
import java.util.Arrays;

public class AbstractSyntaxTree {
    private final ParseTree tree;
    private final ArrayList<String> useful = new ArrayList<>(Arrays.asList("+", "-", "/", "*", "=", ">", "VARNAME:", "NUMBER:"));
    public AbstractSyntaxTree(ParseTree tree){
        this.tree = tree;
        purge();
    }


    public void purge() {
        purgeLeaves(tree, false);
        purgeUselessStates(tree);
        purgeOperations(tree);
        purgeUselessStates(tree);
    }

    public ParseTree getTree() {
        return tree;
    }

    public String toLaTex(){
        return tree.toLaTeX();
    }


    /* rules if - et atom comme frere fusion - et fils de atom
           if operand up to 2 level
           if terminal non operand ou non var ou non number remove
           if var or number up to (exprearithm, print and read,code,if,while) or operand (delete other variable)
           if space delete space
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

    public void purgeUselessStates(ParseTree tree) {
        for (int i = 0; i < tree.getChildren().size(); i++){
            purgeUselessStates(tree.getChildren().get(i));
        }
        if (tree.getFather() != null && tree.getChildren().size() == 1 && tree.getLabel().getVariable() != LexicalVariable.PRINTVAR && tree.getLabel().getVariable() != LexicalVariable.READVAR){
            tree.getFather().getChildren().set(tree.getFather().getChildren().indexOf(tree), tree.getChildren().get(0));
            tree.getChildren().get(0).setFather(tree.getFather());
        }
    }

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

    public void inOrder(ParseTree tree){
        if (tree == null){
            return;
        }

        for (int i = 0; i < tree.getChildren().size(); i++){
            inOrder(tree.getChildren().get(i));
        }
        //System.out.print("" + tree + ", ");
    }

    public void inOrder(){
        inOrder(tree);
    }
}
