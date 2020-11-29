BEGINPROG Pgcd

/* Find the PGCD of 2 number */

READ(number1)
READ(number2)
remainder := 1

WHILE (remainder > 0) DO
	mult := 0
	WHILE (number1 > mult * number2) DO
		mult := mult +1
	ENDWHILE
	mult := mult - 1
	remainder := number1 - number2 * mult
	number1 := number2
	number2 := remainder
	ENDWHILE

PRINT(remainder)
ENDPROG
