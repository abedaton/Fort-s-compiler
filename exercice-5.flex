%%// Options of the scanner

%class Lexer5	//Name
%unicode		//Use unicode
%line         	//Use line counter (yyline variable)
%column       	//Use character counter by line (yycolumn variable)
%type Symbol  //Says that the return type is Symbol
%standalone		//Standalone mode

// Return value of the program
%eofval{
	return new Symbol(LexicalUnit.END_OF_STREAM, yyline, yycolumn);
%eofval}

// Extended Regular Expressions

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Alpha          = {AlphaUpperCase}|{AlphaLowerCase}
Numeric        = [0-9]
AlphaNumeric   = {Alpha}|{Numeric}

Sign           = [+-]
Integer        = {Sign}?(([1-9][0-9]*)|0)
Decimal        = \.[0-9]*
Exponent       = [eE]{Integer}
Real           = {Integer}{Decimal}?{Exponent}?
Identifier     = {Alpha}{AlphaNumeric}*

%%// Identification of tokens

// Relational operators
"!"		        {System.out.println("NOT: " + yytext()); return new Symbol(LexicalUnit.NOT,yyline, yycolumn);}
"=="	        {System.out.println("EQUALS: " + yytext()); return new Symbol(LexicalUnit.EQUALS,yyline, yycolumn);}
"!="	        {System.out.println("NOTEQUALS: " + yytext()); return new Symbol(LexicalUnit.NOTEQUALS,yyline, yycolumn);}
">"		        {System.out.println("GREATER: " + yytext()); return new Symbol(LexicalUnit.GREATER,yyline, yycolumn);}
">="	        {System.out.println("EGREATER: " + yytext()); return new Symbol(LexicalUnit.EGREATER,yyline, yycolumn);}
"<"		        {System.out.println("LOWER: " + yytext()); return new Symbol(LexicalUnit.LOWER,yyline, yycolumn);}
"<="	        {System.out.println("ELOWER: " + yytext()); return new Symbol(LexicalUnit.ELOWER,yyline, yycolumn);}

// If/Else keywords
"if"	        {System.out.println("IF: " + yytext()); return new Symbol(LexicalUnit.IF,yyline, yycolumn);}
"then"        {System.out.println("THEN: " + yytext()); return new Symbol(LexicalUnit.THEN,yyline, yycolumn);}
"else"        {System.out.println("ELSE: " + yytext()); return new Symbol(LexicalUnit.ELSE,yyline, yycolumn);}

// Decimal number in scientific notation
{Real}			  {System.out.println("FLOAT: " + yytext()); return new Symbol(LexicalUnit.FLOAT,yyline, yycolumn, new Double(yytext()));}

// C99 variable identifier
{Identifier}  {System.out.println("C99VAR: " + yytext()); return new Symbol(LexicalUnit.C99VAR,yyline, yycolumn, yytext());}

// Ignore other characters
.             {}