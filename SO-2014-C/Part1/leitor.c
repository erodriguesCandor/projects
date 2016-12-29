/* Projeto - Exercicio 1 */
/* Programa Leitor */

#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include "vars.h"	/* ficheiro com nomes dos ficheiros e das strings */

int main() {

	int file, i, code;
	size_t schar = sizeof(char)*10;
	char buffer1[10];
	file = open(pathname[rand()%5], O_RDONLY);
	/* abre um ficheiro aleatorio dos 5 possiveis */
	read(file, buffer1, schar);
	/* copia a primeira linha do ficheiro */
	code = buffer1[0]-97;
	if(!(0<=code<10) || strncmp(buffer1, strings[code], schar)){
		/* verifica se a linha corresponde a uma das cadeias de caracteres predefinidas */
		close(file);
		return -1;
	}
	for(i=1; i<1024; i++){
		read(file, buffer1, schar);
		if (strncmp(strings[code], buffer1,schar)){
			/* compara a primeira linha com as restantes 1023 */
			close(file);
			return -1;
			}
	}
	if(read(file, buffer1, schar)){
		/* verifica se chegou ao final do ficheiro */
		close(file);
		return -1;
	}
	close(file);
	return 0;
}
