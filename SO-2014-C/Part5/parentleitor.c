/* Projecto - Exercicio 5 */
/*  Programa Leitor-Pai   */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>
#include <semaphore.h>
#include "vars.h"       // ficheiro com constantes

sem_t write_allowed, read_allowed;
pthread_mutex_t read_mutex;
char* buffer[BUFSIZE];
int write_ptr=0, read_ptr=0, stop=1;

extern void reader();

int main() {
	int i;
	char name[NAMELEN+1];	
	pthread_t tids[NREADERS];                          // vector com ids dos threads a criar
	
	for (i=0; i<BUFSIZE; i++)	 
	   /* reserva memória para o buffer partilhado */
		buffer[i] = malloc(sizeof(char)*(NAMELEN+1));
	
	if (sem_init(&write_allowed, NONSHARED, BUFSIZE) == -1) {
		/* inicializa o semaforo de escrita */
	   perror("Erro a inicializar o semáforo");
	   exit(-1);
	}
	
	if (sem_init(&read_allowed, NONSHARED, 0) == -1) {
		/* inicializa o semaforo de leitura */
	   perror("Erro a inicializar o semáforo");
	   exit(-1);
	}
	
	if (pthread_mutex_init(&read_mutex, NULL) != 0) {
		/* inicializa mutex */
	   perror("Erro a inicializar o mutex");
	   exit(-1);
	}

	for (i=0; i<NREADERS; i++) {
	  /* cria o numero definido de threads */
	  if (pthread_create(&tids[i], NULL, (void*)reader, NULL)) {
	    perror("Erro a criar thread");
	    exit(-1);
	  }
	}
	
	while (stop) {	
		/* copia nomes de ficheiros recebidos para o buffer partilhado */
		stop = read(STDIN_FILENO, name, NAMELEN+1);	         // lê nome de um ficheiro
		      
		if ((stop!=NAMELEN+1) && (stop!=0)) {
			perror("Erro a ler nome do ficheiro");
			exit(-1);
		}
		
		name[NAMELEN]='\0';

		if (sem_wait(&write_allowed) == -1) { 
			/* espera pelo semáforo de escrita */
		   perror("Erro a esperar pelo semáforo");
		   exit(-1);
		}
		
		strncpy(buffer[write_ptr], name, NAMELEN+1);    // insere nome do ficheiro no buffer
		write_ptr = (write_ptr+1) % BUFSIZE;
		
		if (sem_post(&read_allowed) == -1) {
			/* assinala semáforo de leitura */
		   perror("Erro a assinalar o semáforo");
		   exit(-1);
		}
	}
	
	for (i=BUFSIZE; i--;) {
		/* acorda os threads filho, ao assinalar o semáforo de leitura o número de vezes necessário */
	   if (sem_post(&read_allowed) == -1) {
		   perror("Erro a assinalar o semáforo");
		   exit(-1);
		}
	}
		
	for (i=NREADERS; i--;) {
      /* espera pelos threads todos */
      if (pthread_join(tids[i], NULL) < 0) {
         perror("Erro a esperar pelo thread");
         exit(-1);
      }      
      printf("Reader thread terminated\n"); 
	}
	
	/* liberta semáforos e mutex utilizados */
	if (sem_destroy(&read_allowed) == -1) {
	   perror("Erro a libertar o semáforo");
	   exit(-1);
	}
	
	if (sem_destroy(&write_allowed) == -1) {
	   perror("Erro a libertar o semáforo");
	   exit(-1);
	}
	
	if (pthread_mutex_destroy(&read_mutex) != 0) {
	   perror("Erro a libertar o mutex");
	   exit(-1);
	}
	
	for (i=0; i<BUFSIZE; i++)	 
	   /* liberta memória usada */
		free(buffer[i]);
		
	exit(0);
}

