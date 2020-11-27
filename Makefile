scannerPath = src/scanner
parserPath = src/parser
jarOut = dist/Part2.jar
docOut = doc/javadoc

all:
	jflex $(scannerPath)/LexicalAnalyzer.flex
	javac -d bin -cp src src/Main.java
	jar cfe  Main -C bin .
	javadoc $(jarOut)-private -sourcepath ./src -d $(docOut) -subpackages . -Xdoclint:none