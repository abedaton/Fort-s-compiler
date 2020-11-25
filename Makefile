scannerPath = src/Scanner
parserPath = src/Parser
jarOut = dist/Part.jar
docOut = doc/javadoc

all:
	jflex $(scannerPath)/LexicalAnalyzer.flex
	javac -d bin -cp src src/Main.java
	jar cfe dist/Part2.jar Main -C bin .
	javadoc -private src/Main.java -d $(docOut)