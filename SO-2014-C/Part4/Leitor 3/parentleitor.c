/* Projecto - Exercicio 4 */
/*     Programa Leitor-Pai    */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h> 
#include <sys/types.h> 
#include <sys/wait.h>
#include <sys/time.h> 
#include <pthread.h>
#include "vars.h"       /* ficheiro com constantes, nomes dos ficheiros e das strings */

static void* wrapper(void *args);
int reader (int file, int line, int interval, char firstChar);
char firstChar(int file);

typedef struct {     
   /* estrutura que guarda os argumentos necessarios para a funcao reader */
   int file;
   int start;
   int interval;
   char c;
} argset;

int main(){
	struct timeval t_init, t_end;
	int i, status, interval = NLINES/NCHILDREN, choice = rand()%NFILES;
	char first = firstChar(choice);
	argset args[NCHILDREN];          /* cria um argset para cada thread */
	pthread_t tids[NCHILDREN];       /* vector com thread ids dos threads a criar */
	
	gettimeofday(&t_init, NULL);     /* regista o tempo inicial */
	
	for(i=0; i<NCHILDREN-1; i++){
	   args[i].file = choice;
	   args[i].c = first;
		args[i].start = interval*i;
		args[i].interval = interval;
		/* cria o numero definido de threads */
		if (pthread_create(&tids[i], NULL, wrapper, (void *)&args[i])) {
			perror("Erro a criar thread");
			exit(-1);
		}
	}
	/* definicao de args para a ultima thread */
	args[i].file = choice;
	args[i].c = first;
	args[i].start = interval*i;
	/* adiciona-se o numero de linhas restantes */
	args[i].interval = interval+ NLINES%NCHILDREN;
	if (pthread_create(&tids[i], NULL, wrapper, (void *)&args[i])) {
		perror("Erro a esperar pelo thread");
		exit(-1);
	}

	for(i=NCHILDREN;i--;) {
	   /* espera pelos processos filho todos */
		if ((pthread_join(tids[i], (void**)&status))<0) {
         perror("Erro a esperar pelo thread");
         exit(-1);
	   }  
		printf("O thread terminou com %d\n",status);
	}

	gettimeofday(&t_end,NULL);
	/* regista o tempo final */
	printf("Tempo de execucao: %.3f segundos\n", (t_end.tv_sec-t_init.tv_sec) + (double)(t_end.tv_usec-t_init.tv_usec)/SEGS);
	/* calcula e imprime o tempo total da execucao do programa */
	exit(0);
}


static void *wrapper(void *args){
   argset *argp = args;
   return (void*) reader(argp->file, argp->start, argp->interval, argp->c);
}

int reader (int file, int line, int interval, char firstChar){
	int i, fd, code = firstChar - 'a';
	size_t schar = sizeof(char)*STRINGSIZE;
	off_t start = line*schar;
	char buffer1[NSTRINGS];
	fd = open(pathname[file], O_RDONLY);
	if (fd==-1) {
		perror("Erro a abrir ficheiro");
		exit(-1);
	}
	if (lseek(fd, start, SEEK_SET)== (off_t)-1) {
	   perror("Erro a fazer lseek");
	   exit(-1);
	}
	read(file, buffer1, schar);
	/* copia a primeira linha do ficheiro */
	if(!(code>=0 && code<10) || strncmp(buffer1, strings[code], schar)) {
		/* verifica se a linha corresponde a uma das cadeias de caracteres predefinidas */
		close(fd);
		return -1;
	}
	for(i=1; i<interval; i++){
		read(file, buffer1, schar);
		if (strncmp(strings[code], buffer1,schar)){
			/* compara a primeira linha com as restantes */
			close(fd);
		   return -1;
		}
	}
	close(fd);
	return 0;
}

char firstChar(int file){
	int fd;
	char firstChar[1];
	fd=open(pathname[file], O_RDONLY);
	if(fd==-1){
		perror("Error a abrir ficheiro");
		exit(-1);
	}
	read(fd, firstChar, sizeof(char));
	if(close(fd)==-1){
		perror("Erro a fechar o ficheiro");
		exit(-1);
	}
	return firstChar[0];
}
