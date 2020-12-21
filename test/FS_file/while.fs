BEGINPROG Factorial

/* Compute the factorial of a number
   If the input number is negative, print -1. */

  READ(anumber)              // Read a number from user input
 // anumber := 10
  result := 0

maxborn := anumber+10
WHILE (maxborn > anumber) DO
    result := result + 1
    anumber := anumber +  1   // decrease number
ENDWHILE
PRINT(result)
ENDPROG
