/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Projet Compilation                                                      *
 * Defraene Pierre, Bedaton Antoine                                        *
 *                                                                         *
 *                                                                         *                                                                          *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.TreeMap;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

%%

%class LexicalAnalyzer
%function nextToken

%type Symbol
%unicode

%column
%line

%eofclose

%init{

%init}

%{
    public Map<String, Integer> variables = new HashMap<String, Integer>();
    public final LinkedList<Integer> states = new LinkedList<>();
    public int openComments = 0;
    private Symbol ensureGoodUnit(String unit, int yyline, int yycolumn, boolean goBack){
      try {
          System.out.println("token: " + ((unit == "ENDLINE") ? "\\n" : yytext()) + "\tlexical unit: " + LexicalUnit.valueOf(unit));
          // We know this is a good unit
          if (unit.equals("VARNAME")){
              variables.putIfAbsent(yytext(), yyline+1);
          }
          if (goBack){
            yybegin(YYINITIAL);
          }
          return new Symbol(LexicalUnit.valueOf(unit), yyline, yycolumn);
      } catch (IllegalArgumentException e){
          throw new IllegalArgumentException("This is not a good Unit: " + e + ": at line " + yyline);
      }
    }
    private Symbol ensureGoodUnit(String unit, int yyline, int yycolumn){
        return ensureGoodUnit(unit, yyline, yycolumn, true);
    }

    private void newComment(){
        if (openComments == 0){
            openComments++;
            yybegin(MULTICOMMENT_STATE);
        }
    }

    private void endComment(){
        if (openComments > 0){
            openComments--;
            if (openComments == 0){
                yybegin(YYINITIAL);
            }
        } else {
            System.out.println("Erreur: Commentaire fermant mais pas assez d'ouvert");
        }
    }

%}

%eof{
    System.out.println("\nVariables");
    TreeMap<String, Integer> sortedVariables = new TreeMap<String, Integer>(variables);
    for (Map.Entry<String,Integer> oneVar : sortedVariables.entrySet()){
        System.out.println(oneVar.getKey() + "\t" + oneVar.getValue());
    }
    System.out.println();
%eof}

%eofval{
	return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
%eofval}


%state YYINITIAL, COMMENT_STATE, MULTICOMMENT_STATE

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Num = [0-9]
AlphaNum = {AlphaLowerCase}|{AlphaUpperCase}|{Num}
AlphaLowerNum = {AlphaLowerCase} | {Num}

ProgramName     = {AlphaUpperCase}[a-zA-Z0-9]*[a-z0-9][a-zA-Z0-9]*
Variables      = {AlphaLowerCase}[a-z0-9]*
Unit          = {AlphaUpperCase}+

Comment = \/\/  //   // blabla
CommentBlock = \/\*  // /* blabla
EndOfBlock = \*\/    //   blabla */

LineTerminator = \r|\n|\r\n
AnythingButNotEOL = [^\n\r]

Integer        = (([1-9][0-9]*)|0)
Decimal        = \.[0-9]*
Real           = {Integer}{Decimal}?


%%

<YYINITIAL> {
    {LineTerminator}  {ensureGoodUnit("ENDLINE", yyline, yycolumn);}
    {Comment}         {yybegin(COMMENT_STATE);}
    {CommentBlock}    {newComment();}
    {EndOfBlock}      {endComment();}
    {Unit}            {ensureGoodUnit(yytext(), yyline, yycolumn);}
    {ProgramName}     {ensureGoodUnit("PROGNAME", yyline, yycolumn);}
    {Variables}       {ensureGoodUnit("VARNAME", yyline, yycolumn);}
    {Real}            {ensureGoodUnit("NUMBER", yyline, yycolumn);}
    "("               {ensureGoodUnit("LPAREN", yyline, yycolumn);}
    ")"               {ensureGoodUnit("RPAREN", yyline, yycolumn);}
    ":="              {ensureGoodUnit("ASSIGN", yyline, yycolumn);}
    ">"               {ensureGoodUnit("GT", yyline, yycolumn);}
    "="               {ensureGoodUnit("EQ", yyline, yycolumn);}
    "/"               {ensureGoodUnit("DIVIDE", yyline, yycolumn);}
    "*"               {ensureGoodUnit("TIMES", yyline, yycolumn);}
    "+"               {ensureGoodUnit("PLUS", yyline, yycolumn);}
    "-"               {ensureGoodUnit("MINUS", yyline, yycolumn);}
    ","               {ensureGoodUnit("COMMA", yyline, yycolumn);}
    [^]               {}
}

<COMMENT_STATE> {
    {LineTerminator}        {ensureGoodUnit("ENDLINE", yyline, yycolumn);}
    {AnythingButNotEOL}     {}
}

<MULTICOMMENT_STATE> {
    {CommentBlock}         {newComment();}
    {EndOfBlock}           {endComment();}
    //{LineTerminator}|.     {ensureGoodUnit("ENDLINE", yyline, yycolumn, false);}
    {LineTerminator}|.     {System.out.println("token: \\n" + yytext() + "\tlexical unit: " + LexicalUnit.ENDLINE);
                            return new Symbol(LexicalUnit.ENDLINE, yyline, yycolumn);}
    [^]                    {}
}
