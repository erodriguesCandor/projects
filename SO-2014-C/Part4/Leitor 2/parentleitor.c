/* Projecto - Exercicio 4 */
/*   Programa Leitor-Pai  */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h> 
#include <sys/types.h> 
#include <sys/wait.h>
#include <sys/time.h> 
#include <pthread.h>
#include <string.h>
#include "vars.h"       /* ficheiro com constantes, nomes dos ficheiros e das strings */

static void* wrapper(void *fd);
int reader (int file);

int main(){
	struct timeval t_init, t_end;
	int i, status, fd[NCHILDREN];
	pthread_t tids[NCHILDREN];   /* vector com thread ids dos threads a criar */ 	
	gettimeofday(&t_init, NULL); /* regista o tempo inicial */

	for(i=0; i<NCHILDREN; i++){
	  int choice = rand()%NFILES;
	  fd[i]=open(pathname[choice], O_RDONLY);
	  /* cria o numero definido de threads */
	  if (pthread_create(&tids[i], NULL, wrapper, (void *)fd[i])) {
       /* cria um thread */
	    perror("Erro a criar thread");
	    exit(-1);
	  }
	}

	for(i=NCHILDREN; i--;) {
	  /* espera pelos threads todos */
	  if ((pthread_join(tids[i], (void**)&status))<0) {
	    perror("Erro a esperar pelo thread");
	    exit(-1);
	  }    
	  printf("Thread terminou com %d\n", status);
	}
	
	gettimeofday(&t_end,NULL);
	/* regista o tempo final */
	printf("Tempo de execucao: %.3f segundos\n", (t_end.tv_sec-t_init.tv_sec) + (double)(t_end.tv_usec-t_init.tv_usec)/SEGS);
	/* calcula e imprime o tempo total da execucao do programa */
	exit(0);
}

static void *wrapper(void *fd){
	/* torna possivel a passagem da funçao reader como argumento ao pthread_create */
	return (void*) reader((int)fd);
}

int reader (int file){
	int i, code;
	size_t schar = sizeof(char)*NSTRINGS;
	char buffer1[NSTRINGS];
	read(file, buffer1, schar);
	/* copia a primeira linha do ficheiro */
	code = buffer1[0]-'a';
	if(!(code>=0 && code<STRINGSIZE) || strncmp(buffer1, strings[code], schar)){
		/* verifica se a linha corresponde a uma das cadeias de caracteres predefinidas */
		close(file);
		return -1;
	}
	for(i=1; i<NLINES; i++){
		read(file, buffer1, schar);
		if (strncmp(strings[code], buffer1, schar)){
			/* compara a primeira linha com as restantes */
			close(file);
			return -1;
			}
	}
	if(read(file, buffer1, schar)) {
		/* verifica se chegou ao final do ficheiro */
		close(file);
		return -1;
	}
	close(file);
	return 0;
}
