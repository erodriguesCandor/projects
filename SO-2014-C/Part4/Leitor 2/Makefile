all: parentleitor escritor

parentleitor: parentleitor.c vars.h
	gcc -pthread -o parentleitor parentleitor.c vars.h

escritor: escritor.c vars.h
	gcc -o escritor escritor.c vars.h
	
clean:
	rm -f parentleitor escritor
