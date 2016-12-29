/* Projecto - Exercicio 4 */
/* Programa Parent-Leitor */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h> 
#include <sys/types.h> 
#include <sys/wait.h>
#include <sys/time.h> 
#include "vars.h"       /* ficheiro com constantes, nomes dos ficheiros e das strings */

int main(){
	struct timeval t_init, t_end;
	int i, status;
	char r[MAXDIGITS];
	pid_t current;
	
	gettimeofday(&t_init, NULL); /* regista o tempo inicial */
	
	for(i=0; i<NCHILDREN; i++){
		/* cria o numero definido de processos filho */
		current = fork();
		sprintf(r,"%d", rand()%NFILES);
		/* transforma o numero aleatorio obtido em string */
		if (current == -1) {
		   perror("Erro a criar o processo filho");
		   exit (-1);
		}
		if(!current) {
			/* se estiver no processo filho executa o programa leitor */
			if (execl(EXECNAME, EXECNAME, r, NULL) == -1) {
				perror("Erro a executar o programa leitor");
				exit (-1);
			}
	   }
	}
	
	for(i=NCHILDREN; i>0; i--) {
	   /* espera pelos processos filho todos */
		if (wait(&status)==-1) {
		   perror("Erro a esperar pelo processo filho");
		   exit (-1);
		}
		printf("O leitor %d terminou com %d\n", i, WEXITSTATUS(status));
	}
	
	gettimeofday(&t_end,NULL);
	/* regista o tempo final */
	printf("Tempo de execucao: %.3f segundos\n", (t_end.tv_sec-t_init.tv_sec) + (double)(t_end.tv_usec-t_init.tv_usec)/SEGS);
	/* calcula e imprime o tempo total da execucao do programa */
	exit(0);
}
