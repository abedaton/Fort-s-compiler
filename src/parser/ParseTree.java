package parser;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * A skeleton class to represent parse trees. The arity is not fixed: a node can
 * have 0, 1 or more children. Trees are represented in the following way: Tree
 * :== Symbol * List In other words, trees are defined recursively: A tree
 * is a root (with a label of type Symbol) and a list of trees children. Thus, a
 * leave is simply a tree with no children (its list of children is empty). This
 * class can also be seen as representing the Node of a tree, in which case a
 * tree is simply represented as its root.
 * 
 * @author Léo Exibard, Sarah Winter
 */
public class ParseTree {
    private Variable label; // The label of the root of the tree
    private List<ParseTree> children; // Its children, which are trees themselves
    private ParseTree father;

    /**
     * Creates a singleton tree with only a root labeled by lbl.
     * 
     * @param lbl The label of the root
     */
    public ParseTree(Variable lbl) {
        this.label = lbl;
        this.children = new ArrayList<>(); // This tree has no children
        this.father = null;
    }

    /**
     * Creates a tree with root labeled by lbl and children
     * 
     * @param lbl  The label of the root
     * @param chdn Its children
     */
    public ParseTree(Variable lbl, List<ParseTree> chdn) {
        this.label = lbl;
        this.children = chdn;
        this.father = null;
    }

    /**
     * Creates a tree with a root, a father and some children
     * @param lbl the label of the node
     * @param chdn the children of the node
     * @param father the father of the node
     */
    public ParseTree(Variable lbl, List<ParseTree> chdn, ParseTree father){
        this.label = lbl;
        this.children = chdn;
        this.father = father;
    }

    /**
     * Creates a node with a father but no children
     * @param lbl the label of the node
     * @param father the father of the node
     */
    public ParseTree(Variable lbl, ParseTree father){
        this.label = lbl;
        this.children = new ArrayList<>();
        this.father = father;
    }

    /**
     * This method will add a child to the child list and set the father of the new node as the current node
     * @param child a ParseTree node that represents the child
     */
    public void addChild(ParseTree child) {
        child.setFather(this);
        this.children.add(child);
    }

    /**
     * Set the father of the current node
     * @param father the father of the current node
     */
    public void setFather(ParseTree father){
        this.father = father;
    }

    /**
     * get the father of the current node
     * @return the father of the current node
     */
    public ParseTree getFather(){
        return this.father;
    }

    /**
     * This method will return the list of children of the current node
     * @return the children of the current node
     */
    public List<ParseTree> getChildren(){
        return this.children;
    }

    /**
     * Set the children of the current node
     * @param children the new children of the current node
     */
    public void setChildren(List<ParseTree> children){
        this.children = children;
    }

    /**
     * This method gets the first child of the current node
     * @return the first child of the current node
     */
    public ParseTree getFirstChild(){
        return this.children.get(0);
    }

    /**
     * Gets the label of the current node
     * @return the label of the current node
     */
    public Variable getLabel(){
        return this.label;
    }

    /**
     * Generates a {@link ListIterator} used too iterates through the children
     * if there is no next children, returns the brother of the father of the current node
     * @return the next child or the brother of the father
     */
    public ParseTree getNext(){
        if (this.father == null) {
            return null;
        }
        ListIterator<ParseTree> it = this.father.getChildren().listIterator();
        while (it.hasNext()){
            if (it.next().equals(this)){
                if (it.hasNext()) {
                    return it.next();
                }
            }
        }
        return this.father.getNext();
    }

    /**
     * Writes the tree as LaTeX code
     */
    public String toLaTexTree() {
        StringBuilder treeTeX = new StringBuilder();
        treeTeX.append("[");
        treeTeX.append(label.toTexString());
        if (this.label.isTerminal()) {
            if (this.label.isVar()){
                treeTeX.append(", var");
            } else {
                treeTeX.append(", terminal");
            }
        }
        treeTeX.append(" ");
        for (ParseTree child : children) {
            treeTeX.append(child.toLaTexTree());
        }
        treeTeX.append("]");
        return treeTeX.toString();
    }

    /**
     * Writes the tree as a forest picture. Returns the tree in forest environment
     * using the latex code of the tree
     */
    public String toForestPicture() {
        return "\\begin{forest}\n\tterminal/.style={top color=green!20, bottom color=green!50},\n\t" +
                "var/.style={top color=blue!35, bottom color=green!50},\n\t" +
                "for tree={\n\t\trounded corners,\n\t\tdraw,\n\t\talign=center,\n\t\ttop color = white,\n\t\t" +
                "bottom color = blue!20,\n\t},\n\tforked edges,\t\n" + toLaTexTree() + "\t\n\\end{forest}";
    }

    /**
     * Writes the tree as a LaTeX document which can be compiled using PDFLaTeX.
     * <br>
     * <br>
     * The result can be used with the command:
     * 
     * <pre>
     * pdflatex some-file.tex
     * </pre>
     */
    public String toLaTeX() {
        return "\\documentclass[border=5pt]{standalone}\n\\usepackage[edges]{forest}\n\\usepackage{mathtools}\n\n" +
                "\\begin{document}\n\n" + toForestPicture() + "\n\n\\end{document}\n%% Local Variables:\n%% TeX-engine: pdflatex\n%% End:";
    }

    /**
     * @return the name of the current node as a latex string
     */
    public String toString(){
        return this.getLabel().toTexString();
    }

    /**
     * @return the root of the tree
     */
    public ParseTree getRoot() {
        ParseTree currentNode = this;
        while (currentNode.getFather() != null) {
            currentNode = currentNode.getFather();
        }
        return currentNode;
    }


    public void setLabel(Variable label){
        this.label = label;
    }

}
