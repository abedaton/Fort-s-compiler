package generator;

import parser.ParseTree;


import java.util.ArrayList;
import java.util.List;

public class AbstractSyntaxTree {
    public ParseTree tree;

    public AbstractSyntaxTree(ParseTree tree){
        this.tree = tree;
    }


    public void purge() {
        ParseTree root = this.tree.getRoot();
        List<ParseTree> children = root.getChildren();
        List<ParseTree> goodChildren = new ArrayList<>();
        for (ParseTree tree : children) {
            if (!tree.getLabel().isTerminal() && tree.getLabel().getVariable().toString().equalsIgnoreCase("CODE")) {
                System.out.println("goodbye");
                goodChildren.add(tree);
            }
        }
        root.setChildren(goodChildren);
    }

    public ParseTree getTree() {
        return tree;
    }

    public String toLaTex(){
        return tree.toLaTeX();
    }
}
