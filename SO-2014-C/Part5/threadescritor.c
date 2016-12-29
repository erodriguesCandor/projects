/*  Projeto - Exercicio 5   */
/* Programa Thread-Escritor */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/file.h>
#include "vars.h"       // ficheiro com constantes

const char *strings[10] = {"aaaaaaaaa\n", "bbbbbbbbb\n", "ccccccccc\n",
			"ddddddddd\n", "eeeeeeeee\n", "fffffffff\n",
			"ggggggggg\n", "hhhhhhhhh\n", "iiiiiiiii\n", 
			"jjjjjjjjj\n"};
			
const char *wrong[10] = {"aaaaaaaaz\n", "bbbbbbbbz\n", "ccccccccz\n",
			"ddddddddz\n", "eeeeeeeez\n", "ffffffffz\n",
			"ggggggggz\n", "hhhhhhhhz\n", "iiiiiiiiz\n", 
			"jjjjjjjjz\n"};
			
const char *pathname[5] = {"SO2014-0.txt", "SO2014-1.txt", "SO2014-2.txt",
			"SO2014-3.txt", "SO2014-4.txt"};
			
extern int use_locks, write_mode, running;

void write_correct(int fd, int index);
void write_wrong(int fd, int index);

void writer() {
	int file, i, r, lock;
	mode_t mode = S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH;
	/* as flags permitem ao user ler e escrever no ficheiro, e ao grupo e a outros apenas ler o ficheiro */

	while (running) {		
	   /* escreve num ficheiro até ser parado */
		if ((file = open(pathname[rand()%NFILES], O_WRONLY|O_CREAT, mode)) == -1) {
		   /* abre ou cria um ficheiro aleatorio dos possiveis */
			perror("Erro a abrir o ficheiro");
			exit (-1);
		}

		
		if ( (lock=use_locks) && flock(file, LOCK_EX)==-1) {
		   /* bloqueia o ficheiro com um exclusive lock */ 
			perror("Erro a bloquear o ficheiro");
			exit (-1);
		}

		r = rand()%NSTRINGS;
		for (i=0; i<NLINES; i++)
		   /* escreve a string aleatoria o numero de vezes definido */
			(write_mode || i%2==0) ? write_correct(file, r) : write_wrong(file, r);
			

		if (lock && flock(file, LOCK_UN)==-1){
			/* desbloqueia o ficheiro */
			perror("Erro a desbloquear o ficheiro");
			exit (-1);
		}

		if (close(file)==-1){
		   /* fecha o ficheiro */
			perror("Erro a fechar o ficheiro");
			exit(-1);
		}
	}
	
}

void write_correct(int fd, int index) {
	/* escreve nos ficheiros sem erros */
	write(fd, strings[index], sizeof(char)*STRINGSIZE);
}

void write_wrong(int fd, int index) {
	/* escreve nos ficheiros com erros */
	write(fd, wrong[index], sizeof(char)*STRINGSIZE);
}
