all: escritor leitor

escritor: escritor.o
	gcc -o escritor escritor.o

leitor: leitor.o
	gcc -o leitor leitor.o

escritor.o: escritor.c vars.h
	gcc -g -c escritor.c

leitor.o: leitor.c vars.h
	gcc -g -c leitor.c


