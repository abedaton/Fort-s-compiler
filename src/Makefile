all:
ifeq (,$(wildcard ../src/Lexer.java))
	rm ../src/Lexer.java
endif

	jflex LexicalAnalyzer.flex
	javac Lexer.java -cp . Main.java

testing:
	java Lexer ../test/read.fs
