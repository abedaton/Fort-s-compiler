all:
	jflex src/LexicalAnalyzer.flex
	javac -d bin -cp src/ src/Main.java
	jar cfe dist/Part1.jar Main -C bin .
	javadoc -private src/Main.java -d doc/javadoc

testing:
	for testFile in test/*.fs ; do \
		echo "\nTest file:" $$testFile ; \
		java -jar dist/Part1.jar $$testFile ; \
		echo "" ; \
	done
