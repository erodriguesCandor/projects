all: parentleitor escritor leitor

parentleitor: parentleitor.c vars.h
	gcc -o parentleitor parentleitor.c vars.h

escritor: escritor.c vars.h
	gcc -o escritor escritor.c vars.h
	
leitor: leitor.c vars.h
	gcc -o leitor leitor.c vars.h
	
clean:
	rm -f parentleitor escritor leitor
