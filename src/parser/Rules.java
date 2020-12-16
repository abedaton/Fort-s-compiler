package parser;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;


/**
 * This class represents the rules
 * It contains all the rules of the grammar
 */
public class Rules {
    private final Map<Integer, List<String>> rules = new HashMap<>();
    private final List<String> variables = new ArrayList<>();

    public Rules(){
        initRules();
        initVariable();
    }

    public String getRuleVariable(int ruleNbr) {
        return variables.get(ruleNbr-1);
    }

    public List<String> getRule(int ruleNbr){
        return rules.get(ruleNbr);
    }

    /**
     * Method use to reverse the result of the rule
     * @param ruleNbr The number of the rule
     * @return the result of the rule in reversed
     */
    public List<String> getRuleReversed(int ruleNbr){
        List<String> rule = new ArrayList<>(getRule(ruleNbr));
        Collections.reverse(rule);
        return rule;
    }

    /**
     * This method will add all elements to the variables list
     */
    private void initVariable(){
        variables.add("PROGRAM");
        variables.add("SPACE");
        variables.add("SPACE");
        variables.add("CODE");
        variables.add("CODE");
        variables.add("INSTRUCTION");
        variables.add("INSTRUCTION");
        variables.add("INSTRUCTION");
        variables.add("INSTRUCTION");
        variables.add("INSTRUCTION");
        variables.add("ASSIGN");
        variables.add("EXPRARITH");
        variables.add("EXPRARITHBIS");
        variables.add("EXPRARITHBIS");
        variables.add("EXPRARITHBIS");
        variables.add("PROD");
        variables.add("PROD'");
        variables.add("PRODBIS");
        variables.add("PRODBIS'");
        variables.add("ATOM'");
        variables.add("ATOM'");
        variables.add("ATOM");
        variables.add("ATOM");
        variables.add("IFVAR'");
        variables.add("IFSEQ'");
        variables.add("IFSEQ'");
        variables.add("COND");
        variables.add("COMP");
        variables.add("COMP");
        variables.add("WHILEVAR");
        variables.add("PRINTVAR");
        variables.add("READVAR");
    }

    /**
     * This method will add the rule and the result of the rules in the HashMap
     */
    private void initRules() {
        rules.put(1, Arrays.asList("SPACE", "BEGINPROG", "PROGNAME", "SPACE", "CODE", "ENDPROG", "SPACE"));
        rules.put(2, Arrays.asList("ENDLINE", "SPACE"));
        rules.put(3, new ArrayList<>());
        rules.put(4, Arrays.asList("INSTRUCTION", "SPACE", "CODE"));
        rules.put(5, new ArrayList<>());
        rules.put(6, Collections.singletonList("ASSIGN"));
        rules.put(7, Collections.singletonList("IFVAR"));
        rules.put(8, Collections.singletonList("WHILEVAR"));
        rules.put(9, Collections.singletonList("PRINTVAR"));
        rules.put(10, Collections.singletonList("READVAR"));
        rules.put(11, Arrays.asList("VARNAME", ":=", "EXPRARITH"));
        rules.put(12, Arrays.asList("PROD", "EXPRARITHBIS"));
        rules.put(13, Arrays.asList("+", "PROD","EXPRARITHBIS"));
        rules.put(14, Arrays.asList("-", "PROD","EXPRARITHBIS"));
        rules.put(15, new ArrayList<>());
        rules.put(16, Arrays.asList("ATOM", "PRODBIS"));
        rules.put(17, Arrays.asList("*", "ATOM", "PRODBIS"));
        rules.put(18, Arrays.asList("/", "ATOM", "PRODBIS"));
        rules.put(19, new ArrayList<>());
        rules.put(20, Arrays.asList("-", "ATOM"));
        rules.put(21, Collections.singletonList("VARNAME"));
        rules.put(22, Collections.singletonList("NUMBER"));
        rules.put(23, Arrays.asList("(", "EXPRARITH", ")"));
        rules.put(24, Arrays.asList("IF", "(", "COND", ")", "THEN", "SPACE", "CODE", "IFSEQ"));
        rules.put(25, Collections.singletonList("ENDIF"));
        rules.put(26, Arrays.asList("ELSE", "SPACE", "CODE", "ENDIF"));
        rules.put(27, Arrays.asList("EXPRARITH", "COMP", "EXPRARITH"));
        rules.put(28, Collections.singletonList("="));
        rules.put(29, Collections.singletonList(">"));
        rules.put(30, Arrays.asList("WHILE", "(", "COND", ")", "DO", "ENDLINE", "CODE", "ENDWHILE"));
        rules.put(31, Arrays.asList("PRINT", "(", "VARNAME", ")"));
        rules.put(32, Arrays.asList("READ", "(", "VARNAME", ")"));
    }
}
