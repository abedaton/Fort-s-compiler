BEGINPROG Factorial

/* Compute the factorial of a number
   If the input number is negative, print -1. */

  READ(anumber)              // Read a number from user input
  result := 1

IF (anumber > -1) THEN
  WHILE (anumber > 0) DO
    result := result * anumber
    anumber := anumber - 1   // decrease number
  ENDWHILE
ELSE                      // The input number is negative
  result := -1
ENDIF
PRINT(result)
ENDPROG
