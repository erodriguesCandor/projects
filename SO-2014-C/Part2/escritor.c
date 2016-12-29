/* Projecto - Exercicio 2 */
/*    Programa Escritor   */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#include "vars.h"  /* ficheiro com constantes, nomes dos ficheiros e das strings */

int main() {
	int file, i, j, r;
	mode_t mode = S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH;

	for(i=0; i<RUNCYCLES; i++){		/* repete processo de escrita 512 vezes */
		file = open(pathname[rand()%NFILES], O_WRONLY|O_CREAT, mode);
		/* abre ou cria um ficheiro aleatorio dos 5 possiveis*/
                
		if (file==-1){
			perror("Erro a abrir o ficheiro");
			exit (-1);
		}
		/* as flags permitem ao user ler e escrever no ficheiro, e ao grupo e a outros apenas ler o ficheiro */
		r = rand()%NSTRINGS;
		for (j=0; j<NLINES; j++)
			if (write(file, strings[r], sizeof(char)*STRINGSIZE) == -1) {
			   perror("Erro a escrever no ficheiro");
			   exit (-1);
			}
			/* escreve a string aleatoria 1024 vezes*/
		close(file);
	}
	return 0;
}
