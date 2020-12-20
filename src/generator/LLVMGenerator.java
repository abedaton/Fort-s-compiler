package generator;

import org.jetbrains.annotations.NotNull;
import parser.ParseTree;
import parser.Variable;

import java.util.Arrays;
import java.util.List;

public class LLVMGenerator {
    private int nbrReg = 1;
    private final StringBuilder llvmCode = new StringBuilder();
    private final List<String> toTest = Arrays.asList("*", "/", "+", "-");

    public LLVMGenerator(ParseTree tree){
        llvmCode.append(declareReadAndPrintFunction());
        llvmCode.append(start());
        transform(tree);
        llvmCode.append("\tret i32 0\n}");
    }

    public void transform(ParseTree tree){
        if (tree == null){
            return;
        }

        for (int i = 0; i < tree.getChildren().size(); i++){
            transform(tree.getChildren().get(i));
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
                    callProduceExprArithm(tree.getChildren().get(1));
                    llvmCode.append("\tstore i32 %").append(nbrReg-1).append(", i32* ").append(getName(tree.getFirstChild().getLabel())).append("\n");

                    break;
                case "WHILE":
                    break;
                default:
            }
        }
    }

    private void callProduceExprArithm(ParseTree tree) {
        if (tree.hasChildren()) {
            if (toTest.contains(tree.getChildren().get(1).toString())) {
                callProduceExprArithm(tree.getChildren().get(1));
            } else if (toTest.contains(tree.getChildren().get(0).toString())){
                callProduceExprArithm(tree.getChildren().get(0));
            }
        }

        ParseTree right = tree.getChildren().get(1);
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
                appendToBuilder("mul", left_name, right_name, right.hasChildren());
                break;
            case "+":
                appendToBuilder("add", left_name, right_name, right.hasChildren());
                break;
            case "-":
                appendToBuilder("sub", left_name, right_name, right.hasChildren());
                break;
            case "/":
                appendToBuilder("div", left_name, right_name, right.hasChildren());
                break;
            default:
                System.out.println("Not yet implemented");
        }
    }

    public void appendToBuilder(String operation, String left_name, String right_name, boolean reg){
        llvmCode.append("\t%").append(nbrReg++).append(" = ").append(operation).append(" i32 ").append(toTest.contains(left_name) ? "%"+(nbrReg-2) : left_name)
                .append(", ").append(toTest.contains(right_name)? "%"+(nbrReg-2) : right_name).append(" // right = ").append(right_name).append(" left = ").append(left_name).append("\n");
    }



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

    public String callRead(Variable var) {
        String name = getName(var);
        return "\t%" + name + " = call i32 @readInt()\n";
    }

    public String callPrint(Variable var) {
        // maybe wrong get var in a new register then print
        String name = getName(var);
        return "\tcall void @println(i32 " + name + ")\n";
    }

    public String callAdd(Variable var1, Variable var2) {
        //TODO
        String name1 = getName(var1);
        String name2 = getName(var2);
        return "%"+ nbrReg++ + " = add i32 " + name1 + "," + name2 + "\n";
    }

    @NotNull
    private String getName(Variable var1) {
        boolean isVar = var1.getValue().toString().startsWith("V");
        int start1 = var1.getValue().toString().indexOf(" ");
        String tmp = var1.getValue().toString().substring(start1 + 1);
        return isVar ? "%"+ tmp : tmp;
    }

    public String assign(Variable var) {
        String name = getName(var);
        return "\t" + name + " = alloca i32\n";
    }

    public String start() {
        return "define i32 @main() {\n";
    }

    public StringBuilder getLlvmCode() {
        return llvmCode;
    }
}
