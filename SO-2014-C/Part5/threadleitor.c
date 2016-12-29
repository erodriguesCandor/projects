/* Projecto - Exercicio 5 */
/* Programa Thread-Leitor */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/file.h> 
#include <pthread.h>
#include <string.h>
#include <semaphore.h>
#include "vars.h"       // ficheiro com constantes

const size_t schar = sizeof(char)*STRINGSIZE;

const char *strings[10] = {"aaaaaaaaa\n", "bbbbbbbbb\n", "ccccccccc\n",
			"ddddddddd\n", "eeeeeeeee\n", "fffffffff\n",
			"ggggggggg\n", "hhhhhhhhh\n", "iiiiiiiii\n", 
			"jjjjjjjjj\n"};

const char *pathname[5] = {"SO2014-0.txt", "SO2014-1.txt", "SO2014-2.txt",
			"SO2014-3.txt", "SO2014-4.txt"};

extern sem_t write_allowed, read_allowed;
extern pthread_mutex_t read_mutex;
extern char* buffer[];
extern int write_ptr, read_ptr, stop;

int check (char *filename);

void reader() {
	char name[NAMELEN];
	while (stop) {
		/* obtem nomes de ficheiros do buffer partilhado e verifica a sua coerência */
		if (sem_wait(&read_allowed) == -1) {
		   /* espera pelo semáforo de leitura */		
			perror("Erro a esperar pelo semáforo");
			exit(-1);
		}
		
		if (!stop)
		   /* se for acordado porque o leitor vai terminar, sai do ciclo */
		   break;
		
		if (pthread_mutex_lock(&read_mutex) != 0){
		   /* espera pelo mutex */
			perror("Erro a esperar pelo mutex");
			exit(-1);
		}
		
		strncpy(name, buffer[read_ptr], NAMELEN+1);              // obtém o nome do ficheiro do buffer
		read_ptr = (read_ptr+1) % BUFSIZE;
		
		if (sem_post(&write_allowed) == -1) {		
		   /* assinala semáforo de escrita */
			perror("Erro a assinalar o semáforo");
			exit(-1);
		}
		
		if (pthread_mutex_unlock(&read_mutex) != 0) { 
		   /* assinala mutex */
			perror("Erro a assinalar o mutex");
			exit(-1);
		}
		
		printf("File is %scoherent\n", check(name) ? "not " : "");   // verifica coerência do ficheiro
	}

}

int check (char *filename) {
	int i, code, fd;
	char buffer1[STRINGSIZE];
	
	if ((fd=open(filename, O_RDONLY)) == -1) {
	   /* abre o ficheiro */
	   perror("Erro a abrir o ficheiro");
	   exit(-1);
	}
	
	if (flock(fd, LOCK_SH)==-1) {
      /* bloqueia o ficheiro com um shared lock */
		perror("Erro a bloquear o ficheiro");
		close(fd);
		exit (-1);
	}
	
	read(fd, buffer1, schar);                                   // copia a primeira linha do ficheiro
	code = buffer1[0]-'a';
	
	if (!(code>=0 && code<NSTRINGS) || strncmp(buffer1, strings[code], schar)) {
		/* verifica se a linha corresponde a uma das cadeias de caracteres predefinidas */
		
		if (flock(fd, LOCK_UN)==-1) {
			perror("Erro a desbloquear o ficheiro");
			exit (-1);
		}
		
		if (close(fd)==-1) {
         perror("Erro a fechar ficheiro");
         exit(-1);
      }
      
		return -1;
	}
	
	for(i=1; i<NLINES; i++) {
		read(fd, buffer1, schar);
		
		if (strncmp(strings[code], buffer1, schar)) {
			/* compara a primeira linha com as restantes */
			if (flock(fd, LOCK_UN)==-1) {
				perror("Erro a desbloquear o ficheiro");
				exit (-1);
			}
			
			if (close(fd)==-1) {
            perror("Erro a fechar ficheiro");
            exit(-1);
         }
         
			return -1;
		}
	}
	
	if (read(fd, buffer1, schar)) {
		/* verifica se chegou ao final do ficheiro */
		if (flock(fd, LOCK_UN)==-1) {
			perror("Erro a desbloquear o ficheiro");
			exit (-1);
		}
		
		if (close(fd)==-1) {
         perror("Erro a fechar ficheiro");
         exit(-1);
      }

		return -1;
	}
	
	if (flock(fd, LOCK_UN)==-1) {   
	   /* desbloqueia o ficheiro */
		perror("Erro a desbloquear o ficheiro");
		exit (-1);
	}
	
   if (close(fd)==-1) {
      /* fecha o ficheiro */
      perror("Erro a fechar ficheiro");
      exit(-1);
   }

	return 0;
}
