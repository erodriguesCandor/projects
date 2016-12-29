/* Projeto - Exercicio 1 */
/* Programa Escritor */

#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include "vars.h"  /* ficheiro com nomes dos ficheiros e das strings */

int main() {

	int file, i, j, r;
	for(i=0; i<5120; i++){		/* repete processo de escrita 5120 vezes */
		file = open(pathname[rand()%5], O_WRONLY|O_CREAT, S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH);
		/* abre ou cria um ficheiro aleatorio dos 5 possiveis*/
		/* as flags permitem ao user ler e escrever no ficheiro, e ao grupo e a outros apenas ler o ficheiro */
		r = rand()%10;
		for (j=0; j<1024; j++)
			write(file, strings[r], sizeof(char)*10);
			/* escreve a string aleatoria 1024 vezes*/
		close(file);
	}
	return 0;
}
