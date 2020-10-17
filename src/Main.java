import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {
    public static void main(String[] args) {
        //take return from the flex files
        java.io.FileInputStream stream = null;
        try {
            stream = new java.io.FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert stream != null;
        java.io.Reader reader = new java.io.InputStreamReader(stream);
        final LexicalAnalyzer analyzer = new LexicalAnalyzer(reader);
        try {
            analyzer.next_token();
        } catch (IOException e){
            System.out.println("erreur");
        }
    }
}
