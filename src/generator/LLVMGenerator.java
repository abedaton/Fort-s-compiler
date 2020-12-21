package generator;

import parser.ParseTree;
import parser.Variable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * class that generate the LLVM code from
 * the abstract syntax tree
 */
public class LLVMGenerator {
    private int nbrReg = 1;
    private int nbWhile = 0;
    private int nbIf = 0;
    private final List<String> allocas = new ArrayList<>();
    private final List<String> readvars = new ArrayList<>();
    private final StringBuilder llvmCode = new StringBuilder();
    private final List<String> toTest = Arrays.asList("*", "/", "+", "-");

    /**
     * The constructor which fill the builder with the LLVM code
     * @param tree
     */
    public LLVMGenerator(ParseTree tree){
        llvmCode.append(declareReadAndPrintFunction());
        llvmCode.append(start());
        transform(tree);
        llvmCode.append("\tret i32 0\n}");
    }

    /**
     * method that transform the parse tree into LLVM code
     * for every variable which produce some special code
     * or relauch transform on the tree
     *
     * @param tree the tree that we will transform
     */
    public void transform(ParseTree tree) {
        if (tree == null) {
            return;
        }


        if (tree.getLabel().getVariable() != null) {
            switch (tree.getLabel().getVariable().toString()) {
                case "READVAR":
                    llvmCode.append(callRead(tree.getFirstChild().getLabel()));
                    break;
                case "PRINTVAR":
                    llvmCode.append(callPrint(tree.getFirstChild().getLabel()));
                    break;
                case "ASSIGN":
                    llvmCode.append(assign(tree.getFirstChild().getLabel()));
                    int change = nbrReg;
                    callProduceExprArithm(tree.getChildren().get(1));
                    if (change != nbrReg)
                        llvmCode.append("\tstore i32 %").append(nbrReg - 1).append(", i32* ").append(getName(tree.getFirstChild().getLabel())).append("\n");

                    break;
                case "WHILEVAR":
                    llvmCode.append("\tbr label %while").append(nbWhile).append("\n");
                    llvmCode.append("while").append(nbWhile).append(":\n");
                    calculateCond(tree);
                    llvmCode.append("\tbr i1 %").append(nbrReg - 1).append(", label %inWhile").append(nbWhile).append(", label %endWhile").append(nbWhile).append("\n");
                    llvmCode.append("inWhile").append(nbWhile).append(":\n");
                    transform(tree.getChildren().get(1));
                    llvmCode.append("\tbr label %while").append(nbWhile).append("\n");
                    llvmCode.append("endWhile").append(nbWhile).append(":\n");
                    nbWhile++;

                    break;
                case "IFVAR":
                    llvmCode.append("\tbr label %if").append(nbIf).append("\n");
                    llvmCode.append("if").append(nbIf).append(":\n");
                    calculateCond(tree);
                    if (tree.getChildren().size() == 2) {
                        doIf(tree);
                    } else {
                        doIfElseElseIf(tree, "else");
                    }

                    break;
                default:
                    for (int i = 0; i < tree.getChildren().size(); i++) {
                        transform(tree.getChildren().get(i));
                    }
                    break;

            }
        }
    }

    /** This method creates the if/else code in LLVM
     *
     * @param tree The tree that we will parse
     * @param end if it is the end of if or not
     */
    private void doIfElseElseIf(ParseTree tree, String end) {
        llvmCode.append("\tbr i1 %").append(nbrReg - 1).append(", label %inIf").append(nbIf)
                .append(", label %").append(end).append(nbIf).append("\n");
        llvmCode.append("inIf").append(nbIf).append(":\n");
        transform(tree.getChildren().get(1));

        llvmCode.append("\tbr label %").append(end.equals("else") ? "endIf" : "if").append(nbIf).append("\n");
        llvmCode.append(end.equals("else")?end:"endIf").append(nbIf).append(":\n");
        if (end.equals("else")) {
            transform(tree.getChildren().get(2));
            llvmCode.append("\tbr label %endIf").append(nbIf).append("\n");
        }

        llvmCode.append("endIf").append(nbIf).append(":\n");
    }

    /**
     * this method will create the code of a simple if
     * @param tree that we need to create for
     */
    private void doIf(ParseTree tree){
        llvmCode.append("\tbr i1 %").append(nbrReg - 1).append(", label %inIf").append(nbIf)
                .append(", label %").append("endIf").append(nbIf).append("\n");
        llvmCode.append("inIf").append(nbIf).append(":\n");
        transform(tree.getChildren().get(1));
        llvmCode.append("\tbr label %").append("endIf").append(nbIf).append("\n");
        llvmCode.append("endIf").append(nbIf).append(":\n");
    }

    /**
     * This method will create the code of a expression arithmetic
     * @param tree that we evaluate
     */
    private void callProduceExprArithm(ParseTree tree) {
        if (tree.hasChildren()) {
            if (toTest.contains(tree.getChildren().get(1).toString())) {
                callProduceExprArithm(tree.getChildren().get(1));
            } else if (toTest.contains(tree.getChildren().get(0).toString())){
                callProduceExprArithm(tree.getChildren().get(0));
            }
        } else {
            if (!tree.getLabel().getValue().toString().startsWith("N")) {
                llvmCode.append("\t%")
                        .append(nbrReg++)
                        .append(" = load i32, i32* ")
                        .append(getName(tree.getLabel()))
                        .append("\n");
            } else {
                if (tree.getFather().getLabel().getVariable() != null && tree.getFather().getLabel().getVariable().toString().equals("ASSIGN"))
                    llvmCode.append("\tstore i32 ").append(getName(tree.getLabel())).append(", i32* ").append(getName(tree.getFather().getFirstChild().getLabel())).append("\n");
            }

            return;
        }

        String right_name, left_name;
        if (toTest.contains(tree.getChildren().get(1).getLabel().getValue().toString())){
            right_name = getName(tree.getChildren().get(0).getLabel());
            left_name = getName(tree.getChildren().get(1).getLabel());
        } else {
            right_name = getName(tree.getChildren().get(1).getLabel());
            left_name = getName(tree.getChildren().get(0).getLabel());
        }
        switch (tree.getLabel().getValue().toString()){
            case "*":
                appendToBuilder("mul", left_name, right_name);
                break;
            case "+":
                appendToBuilder("add", left_name, right_name);
                break;
            case "-":
                appendToBuilder("sub", left_name, right_name);
                break;
            case "/":
                appendToBuilder("div", left_name, right_name);
                break;
            case ">":
                break;
            default:
                System.out.println("Not yet implemented : " + tree.getLabel().getValue().toString() + " | " + tree.getLabel().getVariable().toString());
        }
    }


    /**
     * This method will add to the String Builder the
     * instruction in LLVM which make a certain operation
     *
     * @param operation the operation that we found in the tree
     * @param left_name the left operand
     * @param right_name the right operand
     */
    public void appendToBuilder(String operation, String left_name, String right_name){
         if (right_name.contains("%") && left_name.contains("%") && readvars.contains(right_name) && readvars.contains(left_name)){
            llvmCode.append("\t%")
                    .append(nbrReg++)
                    .append("= load i32, i32* ")
                    .append(right_name)
                    .append("\n");
            llvmCode.append("\t%")
                    .append(nbrReg++)
                    .append("= load i32, i32* ")
                    .append(left_name)
                    .append("\n");
            left_name = "%" + (nbrReg-1);
            right_name = "%" + (nbrReg-2);
        } else if (left_name.contains("%") && readvars.contains(left_name)){
            llvmCode.append("\t%")
                    .append(nbrReg++)
                    .append("= load i32, i32* ")
                    .append(left_name)
                    .append("\n");
            left_name = "%" + (nbrReg-1);
        } else if (right_name.contains("%") && readvars.contains(right_name)){
            llvmCode.append("\t%")
                    .append(nbrReg++)
                    .append("= load i32, i32* ")
                    .append(right_name)
                    .append("\n");
            right_name = "%" + (nbrReg-1);
        }
        llvmCode.append("\t%")
                .append(nbrReg++)
                .append(" = ")
                .append(operation)
                .append(" i32 ")
                .append(left_name)
                .append(", ").append(right_name)
                .append("\n");


    }


    /**
     * method which return the string which declare
     * the function read and print
     *
     */
    public String declareReadAndPrintFunction() {
        return "@.strR = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n" +
                "\n" +
                "define i32 @readInt() #0 {\n" +
                "  %1 = alloca i32, align 4\n" +
                "  %2 = call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %1)\n" +
                "  %3 = load i32, i32* %1, align 4\n" +
                "  ret i32 %3\n" +
                "}\n" +
                "\n" +
                "@.strP = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1\n" +
                "@y = constant i32 3\n" +
                "\n" +
                "declare i32 @scanf(i8*, ...) #1\n" +
                "\n" +
                "declare i32 @printf(i8*,...) #1\n" +
                "\n" +
                "\n" +
                "define void @println(i32 %x) {\n" +
                "  %1 = alloca i32, align 4\n" +
                "  store i32 %x, i32* %1, align 4\n" +
                "  %2 = load i32, i32* %1, align 4\n" +
                "  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)\n" +
                "  ret void\n" +
                "}\n";
    }

    /**
     * This method will generate the LLVM code for the read function
     * @param var the variable we found in the tree
     * @return the LLVM code as a String
     */
    public String callRead(Variable var) {
        String name = getName(var);
        readvars.add(name);
        allocas.add(name);
        String result = "\t" + name + " = alloca i32\n";
        result += "\t%" + nbrReg++ + " = call i32 @readInt()\n";
        return result + "\t" + "store i32 %" + (nbrReg -1) + ", i32* " + name + "\n";
    }

    /**
     * This method will generate the LLVM code for the Print function
     * @param var the variable we found in the tree
     * @return the LLVM code as a String
     */
    public String callPrint(Variable var) {
        // maybe wrong get var in a new register then print
        String name = getName(var);
        llvmCode.append("\t%").append(nbrReg++).append(" = load i32, i32* ").append(name).append("\n");
        return "\tcall void @println(i32 %" + (nbrReg-1) + ")\n";
    }

    /**
     * method that to add the LLVM of a condition of a while or a if
     * in the builder
     * @param tree that is go through
     */
    public void calculateCond(ParseTree tree){
        String comp;
        if(tree.getFirstChild().getLabel().getValue().toString().equals("=")){
            comp = "eq";
        } else comp = "sgt";
        int b = nbrReg;
        String val1;
        String val2;
        callProduceExprArithm(tree.getChildren().get(0).getFirstChild());
        if (nbrReg != b) val1 = "%" + (nbrReg-1);
        else val1 = getName(tree.getChildren().get(0).getFirstChild().getLabel());
        int c = nbrReg;
        callProduceExprArithm(tree.getChildren().get(0).getChildren().get(1));
        if (nbrReg != c)  val2 = "%" + (nbrReg-1);
        else val2 = getName(tree.getChildren().get(0).getChildren().get(1).getLabel());
        llvmCode.append("\t%").append(nbrReg++).append(" = icmp ").append(comp).append(" i32 ").append(val1).append(", ").append(val2).append("\n");
    }

    /**
     * This method will parse the variable and get the name of the variable if it is a variable
     * or the value if it is a number
     * @param var1 the variable to parse
     * @return the value/name with "%"
     */
    private String getName(Variable var1) {
        boolean isNumber = var1.getValue().toString().startsWith("N");
        int start1 = var1.getValue().toString().indexOf(" ");
        String tmp = var1.getValue().toString().substring(start1 + 1);
        return !isNumber ? "%"+ tmp : tmp;
    }

    /**
     * This method will generate the assign instruction in LLVM
     * @param var the variable to assign
     * @return The string of the assign instruction in LLVM
     */
    public String assign(Variable var) {
        String name = getName(var);
        if (!allocas.contains(name)) {
            allocas.add(name);
            readvars.add(name);
            return "\t" + name + " = alloca i32\n";
        }
        return "";
    }

    /**
     *  method which return the start of the main function
     *  in LLVM
     * @return
     */
    public String start() {
        return "define i32 @main() {\n";
    }

    /**
     * Method to get the LLVMcode
     * @return the LLVM code as a string
     */
    public StringBuilder getLlvmCode() {
        return llvmCode;
    }
}
