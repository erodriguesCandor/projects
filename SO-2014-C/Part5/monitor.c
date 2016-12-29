/* Projecto - Exercicio 5 */
/*    Programa Monitor    */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>
#include "vars.h"       // ficheiro com constantes 

const char* comandos[3] = {"il", "ie", "sair"};
char cmd[NAMELEN+1];

int getCommand();

int main() {
	int status, key;
	int fds[2];
	pid_t pid_escritor, pid_leitor;

	pid_escritor = fork();		                     // cria processo filho para executar o escritor
	if (pid_escritor == -1) {
		   perror("Erro a criar o processo filho");
		   exit (-1);
	}
	else if(!pid_escritor)
		/* se estiver no processo filho executa o programa escritor */
		if (execl(EXECWRITER, EXECWRITER, NULL) == -1) {
			perror("Erro a executar o programa escritor");
			exit (-1);
		}

	pipe(fds);                                               // cria pipe para comunicar com o leitor
	pid_leitor = fork();		                                 // cria processo filho para executar o leitor
	if (pid_leitor == -1) {
		   perror("Erro a criar o processo filho");
		   exit (-1);
	}
	else if(!pid_leitor){
		/* se estiver no processo filho fecha o fd de escrita do pipe e executa o programa leitor */
		close(fds[1]);
		close(STDIN_FILENO);
		dup(fds[0]);
		if (execl(EXECREADER, EXECREADER, NULL) == -1) {
			perror("Erro a executar leitor");
			exit(-1);
		}
	}
	else {
		/* se estiver no processo pai fecha o fd de leitura do pipe */
		close(fds[0]);
	}

	while ((key = getCommand()) != STOP) {
      /* comunica com os processos filho de acordo com o comando recebido */
      switch ( key ) {
         case IL:
            kill(pid_escritor, SIGUSR1);
            break;
         case IE:
            kill(pid_escritor, SIGUSR2);
            break;
         case FILE:
            write(fds[1], cmd, sizeof(char)*(NAMELEN+1));	   // escreve nome do ficheiro no pipe
            break;
         default:
            printf("Unknown command\n");
      }
	}

	kill(pid_escritor, SIGTSTP);                             // termina o processo escritor
	close(fds[1]);                                           // fecha o pipe, provocando terminação do processo leitor
	
	/* espera pelos processos filho */

   if (waitpid(pid_escritor, &status, 0) == -1) {
      perror("Erro a esperar pelo escritor");
      exit(-1);
   }
   printf("Writer terminated; status: %d\n", status);
   
	if (waitpid(pid_leitor, &status, 0) == -1) {
	   perror("Erro a esperar pelo leitor");
	   exit(-1);
	}
	printf("Reader terminated; status: %d\n", status);
	
	exit(0);
}

int getCommand() {
   /* recebe um comando */	                              
	int i, nchars;
	if ((nchars=read(STDIN_FILENO, cmd, NAMELEN+1)) == 0)
	   return STOP;
	
	cmd[NAMELEN] = '\0';
	
	if (nchars==NAMELEN+1 && access(cmd, F_OK)==0)
	   return FILE;
	   
	for (i=0; i<=STOP; i++)
		if (!strncmp(cmd, comandos[i], strlen(comandos[i])))
			return i;
	
	return -1;
}
