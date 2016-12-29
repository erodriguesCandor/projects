JAVADIR=/usr/share/java
JARNAME=poof-textui

all:
	(cd src; javac -encoding UTF-8 -cp $(JAVADIR)/poof-core.jar:$(JAVADIR)/po-uilib.jar:$(JAVADIR)/poof-support.jar `find . -name \*.java`)
	(cd src; jar cvf ../$(JARNAME).jar `find . -name \*.class -o -name \*.java`)

clean:
	$(RM) $(JARNAME).jar `find . -name \*.class`

install:
	/bin/cp $(JARNAME).jar $(JAVADIR)
