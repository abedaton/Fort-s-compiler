scannerPath = src/scanner
parserPath = src/parser
jarOut = dist/part2.jar
docOut = doc/javadoc

all:
	jflex $(scannerPath)/LexicalAnalyzer.flex
	javac -d bin -cp src src/Main.java
	jar cfe $(jarOut) Main -C bin .
	javadoc -private -sourcepath ./src -d $(docOut) -subpackages . -Xdoclint:none

clean:
	rm -rf bin/* doc/ *.class out/  $(jarOut)

purge: clean
	rm src/scanner/LexicalAnalyzer.java
	rm *.tex