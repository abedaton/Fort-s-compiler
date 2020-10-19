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

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Alpha = {AlphaLowerCase} | {AlphaUpperCase}
Numeric = [0-9]
AlphaNumeric = {Alpha} | {Numeric}

ProgramName     = [A-Z][a-zA-Z0-9]*[a-z0-9][a-zA-Z0-9]*
Variables      = "^"{AlphaLowerCase}{AlphaLowerCase}?{Numeric}"$"
Unit          = {AlphaUpperCase}*

%%

"BEGINPROG" {{System.out.println("token: " + yytext() + " lexical unit: " +LexicalUnit.BEGINPROG); return new Symbol(LexicalUnit.BEGINPROG,yyline, yycolumn);}}
"ENDPROG" {{System.out.println("token: " + yytext() + " lexical unit: " +LexicalUnit.ENDPROG); return new Symbol(LexicalUnit.ENDPROG,yyline, yycolumn);}}

"READ" {{System.out.println("token: " + yytext() + " lexical unit: " +LexicalUnit.READ); return new Symbol(LexicalUnit.READ,yyline, yycolumn);}}

.           {}
