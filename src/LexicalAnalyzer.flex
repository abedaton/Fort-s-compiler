/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Projet Compilation                                                      *
 * Defraene Pierre, Bedaton Antoine                                        *
 *                                                                         *
 *                                                                         *                                                                          *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


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
    public java.util.Map<String, Integer> variables = new java.util.LinkedHashMap<String, Integer>();
    private Symbol ensureGoodUnit(String unit, int yyline, int yycolumn){
      try {
          System.out.println("token: " + ((unit == "ENDLINE") ? "\\n" : yytext()) + "\tlexical unit: " + LexicalUnit.valueOf(unit));
          // We know this is a good unit
          if (unit.equals("VARNAME")){
              variables.putIfAbsent(yytext(), yyline+1);
          }
          yybegin(YYINITIAL);
          return new Symbol(LexicalUnit.valueOf(unit), yyline, yycolumn);
      } catch (IllegalArgumentException e){
          throw new IllegalArgumentException("This is not a good Unit: " + e + ": at line " + yyline);
      }
    }
%}

%eof{
    System.out.println("\nVariables");
    for (String var : variables.keySet()){
        System.out.println(var + "\t" + variables.get(var));
    }
    System.out.println();
%eof}

%eofval{
	return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
%eofval}


%state YYINITIAL, COMMENT_STATE, MULTICOMMENT_STATE

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]

ProgramName     = [A-Z][a-zA-Z0-9]*[a-z0-9][a-zA-Z0-9]*
Variables      = {AlphaLowerCase}[a-z0-9]*
Unit          = {AlphaUpperCase}+

Comment = \/\/
CommentBlock = \/\*
EndOfBlock = \*\/

LineTerminator = \r|\n|\r\n
AnythingButNotEOL = [^\n\r]

Integer        = (([1-9][0-9]*)|0)
Decimal        = \.[0-9]*
Real           = {Integer}{Decimal}?


%%

<YYINITIAL> {
    {LineTerminator}  {ensureGoodUnit("ENDLINE", yyline, yycolumn);}
    {Comment}         {yybegin(COMMENT_STATE);}
    {CommentBlock}    {yybegin(MULTICOMMENT_STATE);}
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
    {EndOfBlock}           {yybegin(YYINITIAL);}
    {LineTerminator}|.     {}
}