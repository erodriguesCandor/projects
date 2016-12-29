all: parent escritor leitor

parent: parent.c vars.h
	gcc -o parent parent.c

escritor: escritor.c vars.h
	gcc -o escritor escritor.c

leitor: leitor.c vars.h
	gcc -o leitor leitor.c

clean: rm -f parent escritor leitor


