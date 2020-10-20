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


%eofval{
	return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
%eofval}


%init{
  System.out.println("Starting parssing... ");
%init}

%state YYINITIAL, PROGNAMESTATE, VARIABLE_STATE

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]

ProgramName     = [A-Z][a-zA-Z0-9]*[a-z0-9][a-zA-Z0-9]*
Variables      = {AlphaLowerCase}[a-z0-9]*
Unit          = {AlphaUpperCase}+

/* comments */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
newline = [\n?\r?]
// Comment can be the last line of the file, without line terminator.
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

Sign           = [+-]
Integer        = {Sign}?(([1-9][0-9]*)|0)
Decimal        = \.[0-9]*
Real           = {Integer}{Decimal}?
%%

<YYINITIAL> {
    {Comment}    {}
    {Unit}       {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.valueOf(yytext())); yybegin(YYINITIAL); return new Symbol(LexicalUnit.valueOf(yytext()), yyline, yycolumn);}
    {ProgramName} {System.out.println("token: " + yytext() + "\tlexical unit: " + LexicalUnit.PROGNAME); yybegin(YYINITIAL); return new Symbol(LexicalUnit.PROGNAME, yyline, yycolumn);}
    {Variables}  {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.VARNAME); yybegin(YYINITIAL); return new Symbol(LexicalUnit.VARNAME, yyline, yycolumn);}
    {Real}       {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.NUMBER); yybegin(YYINITIAL); return new Symbol(LexicalUnit.NUMBER, yyline, yycolumn);}
    "("          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.LPAREN); yybegin(YYINITIAL); return new Symbol(LexicalUnit.LPAREN, yyline, yycolumn);}
    ")"          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.RPAREN); yybegin(YYINITIAL); return new Symbol(LexicalUnit.RPAREN, yyline, yycolumn);}
    ":="         {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.ASSIGN); yybegin(YYINITIAL); return new Symbol(LexicalUnit.ASSIGN, yyline, yycolumn);}
    ">"          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.GT); yybegin(YYINITIAL); return new Symbol(LexicalUnit.GT, yyline, yycolumn);}
    "="          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.EQ); yybegin(YYINITIAL); return new Symbol(LexicalUnit.EQ, yyline, yycolumn);}
    "/"          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.DIVIDE); yybegin(YYINITIAL); return new Symbol(LexicalUnit.DIVIDE, yyline, yycolumn);}
    "*"          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.TIMES); yybegin(YYINITIAL); return new Symbol(LexicalUnit.TIMES, yyline, yycolumn);}
    "+"          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.PLUS); yybegin(YYINITIAL); return new Symbol(LexicalUnit.PLUS, yyline, yycolumn);}
    "-"          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.MINUS); yybegin(YYINITIAL); return new Symbol(LexicalUnit.MINUS, yyline, yycolumn);}
    ","          {System.out.println("token: " + yytext() + "\t\tlexical unit: " + LexicalUnit.COMMA); yybegin(YYINITIAL); return new Symbol(LexicalUnit.COMMA, yyline, yycolumn);}
    {LineTerminator} {System.out.println("token: " + "\\n" + "\t\tlexical unit: " + LexicalUnit.ENDLINE); yybegin(YYINITIAL); return new Symbol(LexicalUnit.ENDLINE, yyline, yycolumn);}
    [^]          {}
}
