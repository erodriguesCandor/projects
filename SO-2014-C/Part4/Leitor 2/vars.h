/* Projecto - Exercício 4 */
/*    Header File Vars    */

#ifndef __VARS_H__
#define __VARS_H__

#define RUNCYCLES 512
#define NLINES 1024
#define NCHILDREN 3
#define NSTRINGS 10
#define NFILES 5
#define STRINGSIZE 10
#define SEGS 1000000

const char *strings[10] = {"aaaaaaaaa\n", "bbbbbbbbb\n", "ccccccccc\n",
			"ddddddddd\n", "eeeeeeeee\n", "fffffffff\n",
			"ggggggggg\n", "hhhhhhhhh\n", "iiiiiiiii\n", 
			"jjjjjjjjj\n"};

const char *pathname[5] = {"SO2014-0.txt", "SO2014-1.txt", "SO2014-2.txt",
			"SO2014-3.txt", "SO2014-4.txt"};
			
#endif


