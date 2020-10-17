/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Projet Compilation                                                      *
 * Defraene Pierre, Bedaton Antoine                                        *
 *                                                                         *
 *                                                                         *                                                                          *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


%%

%class LexicalAnalyzer
%function next_token

%type Symbol
%unicode

%column
%line

%eofclose

%state YYINITIAL, UNITSTATE, PROGNAMESTATE



%init{
  System.out.println("Starting parssing... ");
%init}

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Alpha = {AlphaLowerCase} | {AlphaUpperCase}
Numeric = [0-9]
AlphaNumeric = {Alpha} | {Numeric}

ProgramName     = [A-Z][a-zA-Z0-9]*[a-z0-9][a-zA-Z0-9]*
Variables      = "^"{AlphaLowerCase}{AlphaLowerCase}?{Numeric}"$"
Unit          = {AlphaUpperCase}*

%%

<YYINITIAL> {
    "BEGINPROG" {
            System.out.println("token: " + LexicalUnit.BEGINPROG + "\tlexical unit: " + LexicalUnit.BEGINPROG);
            yybegin(PROGNAMESTATE);
            return new Symbol(LexicalUnit.BEGINPROG, yyline, yycolumn);
      }

    .       {System.out.println("TEXT: " + yytext());return new Symbol(LexicalUnit.EOS, yyline, yycolumn);}
}

<PROGNAMESTATE> {
        {ProgramName} {
                System.out.println("token: " + LexicalUnit.PROGNAME + "\tlexical unit: " + LexicalUnit.PROGNAME);
                return new Symbol(LexicalUnit.PROGNAME, yyline, yycolumn);
        }

        . {System.out.println("TEXT: " + yytext());return new Symbol(LexicalUnit.EOS, yyline, yycolumn);}


}