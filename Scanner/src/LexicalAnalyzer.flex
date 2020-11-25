/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Projet Compilation                                                      *
 * Defraene Pierre, Bedaton Antoine                                        *
 *                                                                         *
 *                                                                         *
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

    public boolean openComments = true;

  /**
  * @param unit the equivalent of the unit found in String
  * @param yyline the line of the unit found
  * @param yycolumn the column of the unit found
  * @param yytext the actual unit found

  * This method will check if the unit found is in the enum, and if not, we will print an error
  */
    private Symbol ensureGoodUnit(String unit, int yyline, int yycolumn, String yytext){
      try {
          return new Symbol(LexicalUnit.valueOf(unit), yyline, yycolumn,yytext);
      } catch (IllegalArgumentException e){
          System.out.println("Error in this line: " + "\nThis is not a good Unit: " + unit);
          System.exit(1);
          return null;
      }
    }

%}

%eofval{
	return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
%eofval}


%state YYINITIAL, COMMENT_STATE, MULTICOMMENT_STATE

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Num = [0-9]
AlphaNum = {AlphaLowerCase}|{AlphaUpperCase}|{Num}
AlphaLowerNum = {AlphaLowerCase} | {Num}

ProgramName     = {AlphaUpperCase}{AlphaNum}*{AlphaLowerNum}{AlphaNum}*
Variables      = {AlphaLowerCase}{AlphaLowerNum}*
Unit          = {AlphaUpperCase}+

Comment = \/\/
CommentBlock = \/\*
EndOfBlock = \*\/

LineTerminator = \r|\n|\r\n
AnythingButNotEOL = [^\n\r]

Integer        = (([1-9]{Num}*)|0)
Decimal        = \.{Num}*
Real           = {Integer}{Decimal}?
BadNumber      = 0{Real}

%%

<YYINITIAL> {
    {LineTerminator}  {return ensureGoodUnit("ENDLINE", yyline, yycolumn , "\\n");}
    {CommentBlock}    {openComments = true; yybegin(MULTICOMMENT_STATE);}
    {EndOfBlock}      {throw new IllegalArgumentException("*/ without /*");}
    {Comment}         {yybegin(COMMENT_STATE);}
    {Unit}            {return ensureGoodUnit(yytext(), yyline, yycolumn, yytext());}
    {ProgramName}     {return ensureGoodUnit("PROGNAME", yyline, yycolumn, yytext());}
    {Variables}       {return ensureGoodUnit("VARNAME", yyline, yycolumn, yytext());}
    {BadNumber}       {throw new IllegalArgumentException("Error: Bad number:" + yytext());}
    {Real}            {return ensureGoodUnit("NUMBER", yyline, yycolumn, yytext());}
    "("               {return ensureGoodUnit("LPAREN", yyline, yycolumn, yytext());}
    ")"               {return ensureGoodUnit("RPAREN", yyline, yycolumn, yytext());}
    ":="              {return ensureGoodUnit("ASSIGN", yyline, yycolumn, yytext());}
    ">"               {return ensureGoodUnit("GT", yyline, yycolumn, yytext());}
    "="               {return ensureGoodUnit("EQ", yyline, yycolumn, yytext());}
    "/"               {return ensureGoodUnit("DIVIDE", yyline, yycolumn, yytext());}
    "*"               {return ensureGoodUnit("TIMES", yyline, yycolumn, yytext());}
    "+"               {return ensureGoodUnit("PLUS", yyline, yycolumn, yytext());}
    "-"               {return ensureGoodUnit("MINUS", yyline, yycolumn, yytext());}
    ","               {return ensureGoodUnit("COMMA", yyline, yycolumn, yytext());}
    "\t"              {}
    [^]               {if (!yytext().equals(" ")) throw new IllegalArgumentException("Couldn't recognize that symbol: " + yytext());}
}

<COMMENT_STATE> {
    {LineTerminator}        {yybegin(YYINITIAL); return ensureGoodUnit("ENDLINE", yyline, yycolumn, "\\n");}
    {AnythingButNotEOL}     {}
}

<MULTICOMMENT_STATE> {
    {LineTerminator}     {}
    {CommentBlock}         {if (openComments){
                              throw new IllegalArgumentException("You cannot imbricate comments FDP (we need to modify this)");
                            } else {
                              openComments = true;
                            }
                          }
    {EndOfBlock}          {if (!openComments){
                              throw new IllegalArgumentException("You have a closing comment and no opening comment !");
                            } else {
                              openComments = false;
                              yybegin(YYINITIAL);}
                            }
    [^]                    {}
}
