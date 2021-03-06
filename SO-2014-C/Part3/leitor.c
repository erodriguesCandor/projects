/* Projeto - Exercicio 3 */
/*    Programa Leitor    */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <sys/file.h>
#include "vars.h"	/* ficheiro com nomes dos ficheiros e das strings */

int main() {

	int file, i, code;
	size_t schar = sizeof(char)*NSTRINGS;
	char buffer1[NSTRINGS];
	file = open(pathname[rand()%NFILES], O_RDONLY);
	/* bloqueia o ficheiro para qualquer processo diferente de leitor (shared lock) */
	if (flock(file, LOCK_SH)==-1){
			perror("Erro a bloquear o ficheiro");
			exit (-1);
	}
	/* abre um ficheiro aleatorio dos possiveis */
	read(file, buffer1, schar);
	/* copia a primeira linha do ficheiro */
	code = buffer1[0]-'a';
	if(!(0<=code<NSTRINGS) || strncmp(buffer1, strings[code], schar)){
		/* verifica se a linha corresponde a uma das cadeias de caracteres predefinidas */
		flock(file, LOCK_UN);            /* desbloqueia o ficheiro */
		perror("Ficheiro inconsistente");
		close(file);
		exit(-1);
	}
	for(i=1; i<NLINES; i++){
		read(file, buffer1, schar);
		if (strncmp(strings[code], buffer1,schar)){
			/* compara a primeira linha com as restantes */
			flock(file, LOCK_UN);
			perror("Ficheiro inconsistente");
			close(file);
			exit(-1);
			}
	}
	if(read(file, buffer1, schar)){
		/* verifica se chegou ao final do ficheiro */
		flock(file, LOCK_UN);
		perror("Ficheiro inconsistente");
		close(file);
		exit(-1);
	}
	
	if (flock(file, LOCK_UN)==-1){
			perror("Erro a desbloquear o ficheiro");
			exit (-1);
	}
	close(file);
	exit(0);
}
