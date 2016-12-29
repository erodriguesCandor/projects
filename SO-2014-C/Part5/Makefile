CC = gcc
CFLAGS = -g -Wall
LDFLAGS = -pthread
EXECS = monitor parentescritor parentleitor

all: $(EXECS)

parentescritor: parentescritor.c threadescritor.c vars.h

parentleitor: parentleitor.c threadleitor.c vars.h

monitor: monitor.c vars.h

clean:
	rm -f $(EXECS)
