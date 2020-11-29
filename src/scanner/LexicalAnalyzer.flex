/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Projet Compilation                                                      *
 * Defraene Pierre, Bedaton Antoine                                        *
 *                                                                         *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package scanner;
import java.util.regex.PatternSyntaxException;

%%// Options of the scanner

%class LexicalAnalyzer // Name
%unicode               // Use unicode
%line                  // Use line counter (yyline variable)
%column                // Use character counter by line (yycolumn variable)
%function nextToken
%type Symbol
%yylexthrow PatternSyntaxException

%eofval{
	return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
%eofval}


//Extended Regular Expressions

AlphaUpperCase    = [A-Z]
AlphaLowerCase    = [a-z]
Alpha             = {AlphaUpperCase}|{AlphaLowerCase}
Numeric           = [0-9]
AlphaNumeric      = {Alpha}|{Numeric}
LowerAlphaNumeric	= {AlphaLowerCase}|{Numeric}

BadInteger     = (0[0-9]+)
Integer        = ([1-9][0-9]*)|0
VarName        = ({AlphaLowerCase})({LowerAlphaNumeric})*
ProgName       = ({AlphaUpperCase})(({AlphaNumeric})*({AlphaLowerCase})({AlphaNumeric})* | {Numeric}*)
BadProgName    = ({AlphaUpperCase})+
LineFeed       = "\n"
CarriageReturn = "\r"
EndLine        = ({LineFeed}{CarriageReturn}?) | ({CarriageReturn}{LineFeed}?)
Space          = (\t | \f | " ")
Spaces         = {Space}+

//Declare exclusive states
%xstate YYINITIAL, SHORTCOMMENTS, LONGCOMMENTS

%%// Identification of tokens

<SHORTCOMMENTS> {
// End of comment
{EndLine}        {yybegin(YYINITIAL); return new Symbol(LexicalUnit.ENDLINE, yyline, yycolumn, "\\n");} // go back to analysis
.	   				     {} //ignore any character
}

<LONGCOMMENTS> {
// End of comment
	"*/"             {yybegin(YYINITIAL);} // go back to analysis
  <<EOF>>          {throw new PatternSyntaxException("A comment is never closed.",yytext(),yyline);}
	[^]					     {} //ignore any character
}

<YYINITIAL> {
// Comments
    "//"              {yybegin(SHORTCOMMENTS);} // go to ignore mode
    "/*"              {yybegin(LONGCOMMENTS);} // go to ignore mode
// Code delimiters
  "BEGINPROG"         {return new Symbol(LexicalUnit.BEGINPROG, yyline, yycolumn, yytext());}
  "ENDPROG"           {return new Symbol(LexicalUnit.ENDPROG, yyline, yycolumn, yytext());}
  ","                 {return new Symbol(LexicalUnit.COMMA, yyline, yycolumn, yytext());}
// Assignation
  ":="                {return new Symbol(LexicalUnit.ASSIGN, yyline, yycolumn, yytext());}
// Parenthesis
  "("                 {return new Symbol(LexicalUnit.LPAREN, yyline, yycolumn, yytext());}
  ")"                 {return new Symbol(LexicalUnit.RPAREN, yyline, yycolumn, yytext());}
// Arithmetic signs
  "+"                 {return new Symbol(LexicalUnit.PLUS, yyline, yycolumn, yytext());}
  "-"                 {return new Symbol(LexicalUnit.MINUS, yyline, yycolumn, yytext());}
  "*"                 {return new Symbol(LexicalUnit.TIMES, yyline, yycolumn, yytext());}
  "/"                 {return new Symbol(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());}
// Conditional keywords
  "IF"                {return new Symbol(LexicalUnit.IF, yyline, yycolumn, yytext());}
  "THEN"              {return new Symbol(LexicalUnit.THEN, yyline, yycolumn, yytext());}
  "ELSE"              {return new Symbol(LexicalUnit.ELSE, yyline, yycolumn, yytext());}
  "ENDIF"             {return new Symbol(LexicalUnit.ENDIF, yyline, yycolumn, yytext());}
// Loop keywords
  "WHILE"             {return new Symbol(LexicalUnit.WHILE, yyline, yycolumn, yytext());}
  "DO"                {return new Symbol(LexicalUnit.DO, yyline, yycolumn, yytext());}
  "ENDWHILE"          {return new Symbol(LexicalUnit.ENDWHILE, yyline, yycolumn, yytext());}
// Comparison operators
  "="                 {return new Symbol(LexicalUnit.EQ, yyline, yycolumn, yytext());}
  ">"                 {return new Symbol(LexicalUnit.GT, yyline, yycolumn, yytext());}
// IO keywords
  "PRINT"             {return new Symbol(LexicalUnit.PRINT, yyline, yycolumn, yytext());}
  "READ"              {return new Symbol(LexicalUnit.READ, yyline, yycolumn, yytext());}
// Numbers
  {BadInteger}        {System.err.println("Warning! Numbers with leading zeros are deprecated: " + yytext()); return new Symbol(LexicalUnit.NUMBER, yyline, yycolumn, Integer.valueOf(yytext()));}
  {Integer}           {return new Symbol(LexicalUnit.NUMBER, yyline, yycolumn, Integer.valueOf(yytext()));}
// Variable Names
  {VarName}           {return new Symbol(LexicalUnit.VARNAME, yyline, yycolumn, yytext());}
  {ProgName}          {return new Symbol(LexicalUnit.PROGNAME, yyline, yycolumn, yytext());}
  {BadProgName}       {return new Symbol(LexicalUnit.PROGNAME, yyline, yycolumn, yytext());}
  {EndLine}           {return new Symbol(LexicalUnit.ENDLINE, yyline, yycolumn, "\\n");}
  {Spaces}	          {} // ignore spaces
 "*/"                 {throw new PatternSyntaxException("A comment is closed, but never opened.",yytext(),yyline);}
  [^]                 {throw new PatternSyntaxException("Unmatched token, out of symbols",yytext(),yyline);} // unmatched token gives an error
}
