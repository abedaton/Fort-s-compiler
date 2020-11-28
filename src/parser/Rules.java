package parser;

import java.util.*;

public class Rules {
    Map<Integer, List<String>> rules = new HashMap<Integer, List<String>>();

    public Rules(){
        initRules();
    }


    public List<String> getRule(int ruleNbr){
        return rules.get(ruleNbr);
    }

    public List<String> getRuleReversed(int ruleNbr){
        List<String> rule = new ArrayList<>(getRule(ruleNbr));
        Collections.reverse(rule);
        return rule;
    }

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
        rules.put(13, Arrays.asList("+", "PROD"));
        rules.put(14, Arrays.asList("-", "PROD"));
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
