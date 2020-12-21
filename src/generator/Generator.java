package generator;

import parser.ParseTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * class which regroup the class to generate the code llvm
 */
public class Generator {
    private final AbstractSyntaxTree syntaxTree;
    private final LLVMGenerator llvmGenerator;

    /**
     *  Constructor which composed of the abstract tree
     *   and of the LLVMGenerator
     * @param tree parse tree of the code Fortr-S
     */
    public Generator(ParseTree tree) {
        syntaxTree = new AbstractSyntaxTree(tree);
        llvmGenerator = new LLVMGenerator(tree);

        System.out.println(llvmGenerator.getLlvmCode().toString());

    }

    /**
     * method that purge the parseTree
     */
    public void purgeTree() {
        syntaxTree.purge();
    }


    public AbstractSyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    /**
     * method that create a LLVM file
     * @param fileName name of new file to create
     * @throws IOException if error in the creation of the file
     */
    public void createLLVMFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.createNewFile()){
            FileWriter writer = new FileWriter(fileName);
            writer.write(llvmGenerator.getLlvmCode().toString());
            writer.close();
        }
    }

}