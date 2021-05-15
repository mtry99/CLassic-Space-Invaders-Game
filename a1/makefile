JFLAGS=-g
JC=javac
.SUFFIXES:.java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES=\
        Rename.java

MAIN=Rename

default: classes

build: $(CLASSES:.java=.class)

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
