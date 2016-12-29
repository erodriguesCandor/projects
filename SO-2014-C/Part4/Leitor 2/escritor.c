/* Projeto - Exercicio 3 */
/*   Programa Escritor   */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/file.h>
#include "vars.h"  /* ficheiro com nomes dos ficheiros e das strings */
			
int main() {
	int file, i, j, r;
	mode_t mode = S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH;
	/* as flags permitem ao user ler e escrever no ficheiro, e ao grupo e a outros apenas ler o ficheiro */

	for(i=0; i<RUNCYCLES; i++){		/* repete processo de escrita 512 vezes */
		file = open(pathname[rand()%NFILES], O_WRONLY|O_CREAT, mode);
		/* abre ou cria um ficheiro aleatorio dos 5 possiveis*/
		if (file==-1){
			perror("Erro a abrir o ficheiro");
			exit (-1);
		}
		/*Bloqueia o ficheiro para qualquer outro processo(Exclusive Lock)*/
		if (flock(file, LOCK_EX)==-1){ 
			perror("Erro a bloquear o ficheiro");
			exit (-1);
		}
		r = rand()%NSTRINGS;
		for (j=0; j<NLINES; j++)
			write(file, strings[r], sizeof(char)*STRINGSIZE);
			/* escreve a string aleatoria 1024 vezes*/
		if (flock(file, LOCK_UN)==-1){/*Desbloqueia o ficheiro*/
			perror("Erro a desbloquear o ficheiro");
			exit (-1);
		}
		close(file);
	}
	exit(0);
}
