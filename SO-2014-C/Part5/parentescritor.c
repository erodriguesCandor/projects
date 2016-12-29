/* Projecto - Exercicio 5 */
/* Programa Escritor-Pai  */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>
#include "vars.h"       // ficheiro com constantes

int use_locks=TRUE, write_mode=TRUE, running=TRUE;	   // estados do escritor

void sig_handler(int signal);
extern void writer();

int main(){
	int i;
	pthread_t tids[NWRITERS];                          // vector com ids dos threads a criar
   struct sigaction handler_action;                   // estrutura de tratamento de sinais
   
   /* inicializa a estrutura de forma a tratar os sinais SIGUSR1, SIGUSR2 e SIGTSTP */
   handler_action.sa_handler = sig_handler;
   sigemptyset(&handler_action.sa_mask);
   handler_action.sa_flags = 0;
  
   sigaction(SIGUSR1, &handler_action, NULL);
   sigaction(SIGUSR2, &handler_action, NULL);
   sigaction(SIGTSTP, &handler_action, NULL); 
   
	for (i=0; i<NWRITERS; i++) {
	  /* cria o numero definido de threads */
	  if (pthread_create(&tids[i], NULL, (void *) writer, NULL)) {
	    perror("Erro a criar thread");
	    exit(-1);
	  }
	}
	
	for (i=NWRITERS; i--;) {
	  /* espera pelos threads todos */
	  if (pthread_join(tids[i], NULL) < 0) {
	    perror("Erro a esperar pelo thread");
	    exit(-1);
	  }    
	  printf("Writer thread terminated\n");
	}
	
	exit(0);
}

void sig_handler(int signal) {
   /* atualiza os estados de acordo com os sinais recebidos */
   if (signal == SIGUSR1) {
      use_locks = !(use_locks);
      printf("Lock usage %sactivated\n", (use_locks) ? "" : "de");
   }
   else if (signal == SIGUSR2) {
      write_mode = !(write_mode);
      printf("Error writing %sactivated\n", (write_mode) ? "de" : "");
   }
   else if (signal == SIGTSTP)
      running = !(running);
}
